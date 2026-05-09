package GUI_Tests.Entities.Player;

import GUI_Tests.Utilities.MyUtils;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerTrail {
    // 1. Properties
    private final List<Breadcrumb> points = new CopyOnWriteArrayList<>();
    private final List<TrailSegment> segments = new CopyOnWriteArrayList<>();
    private Color color;
    private static final long LIFETIME = MyUtils.PLAYER_TRAIL_LIFETIME_MEDIUM;

    public PlayerTrail(Color color) {
        this.color = color;
    }

    // 2. Logic: Adding points and cleaning up old ones
    public void update(double x, double y, double prevX, double prevY, long currentTime) {
        int offset = MyUtils.PLAYER_TILE_SIZE / 2;

        // Add visual point
        this.points.add(new Breadcrumb(x + offset, y + offset, currentTime));

        // Add collision line
        this.segments.add(new TrailSegment(
                prevX + offset, prevY + offset,
                x + offset, y + offset,
                currentTime
        ));

        // Automatic Cleanup
        this.points.removeIf(p -> (currentTime - p.creationTime) > LIFETIME);
        this.segments.removeIf(s -> (currentTime - s.creationTime) > LIFETIME);
    }

    // 3. Rendering: The neon glow layers
    public void draw(Graphics2D g2, long currentTime, float pulse) {
        g2.setColor(this.color);
        List<Breadcrumb> pointsCopy = new ArrayList<>(this.points);
        Breadcrumb lastCrumb = null;

        for (Breadcrumb currentCrumb : pointsCopy) {
            long age = currentTime - currentCrumb.creationTime;
            if (age > LIFETIME || age < 0) continue;

            if (lastCrumb != null) {
                float alphaFactor = 1.0f - ((float) age / LIFETIME);
                int alpha = (int) (alphaFactor * 255);
                this.drawNeonLayers(g2, lastCrumb, currentCrumb, alpha, pulse);
            }
            lastCrumb = currentCrumb;
        }
    }

    /**
     * Checks if a head path line intersects any segments in this trail.
     * @param headPath The line segment from the player's last position to current.
     * @param isSelfCheck If true, we skip the most recent segments to prevent self-collision.
     */
    public boolean checkCollision(java.awt.geom.Line2D.Double headPath, boolean isSelfCheck) {
        // We skip the last few segments so the player doesn't hit their own "neck"
        int safetyGap = isSelfCheck ? MyUtils.PLAYER_SAFE_SEGMENT : 0;

        List<TrailSegment> segmentsCopy = new ArrayList<>(this.segments);

        for (int i = 0; i < segmentsCopy.size() - safetyGap; i++) {
            if (headPath.intersectsLine(segmentsCopy.get(i).line)) {
                return true;
            }
        }
        return false;
    }


    private void drawNeonLayers(Graphics2D g2, Breadcrumb start, Breadcrumb end, int alpha, float pulse) {
        Line2D.Double line = new Line2D.Double(start.x, start.y, end.x, end.y);

        // Outer Glow (Pulsing)
        g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 1.5f * pulse, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.max(0, alpha / 6)));
        g2.draw(line);

        // Inner Glow
        g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.max(0, alpha / 3)));
        g2.draw(line);

        // White Core
        g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(255, 255, 255, Math.max(0, alpha)));
        g2.draw(line);
    }

    // 4. Helpers
    public void clear() {
        this.points.clear();
        this.segments.clear();
    }

    public void setTrailColor(Color color) {
        this.color = color;
    }

    public List<TrailSegment> getSegments() {
        return this.segments;
    }

    // Internal data structures
    private static class Breadcrumb {
        double x, y;
        long creationTime;
        Breadcrumb(double x, double y, long time) { this.x = x; this.y = y; this.creationTime = time; }
    }

    public static class TrailSegment {
        public Line2D.Double line;
        public long creationTime;
        public TrailSegment(double x1, double y1, double x2, double y2, long time) {
            this.line = new Line2D.Double(x1, y1, x2, y2);
            this.creationTime = time;
        }
    }
}