package GUI_Tests;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player extends JPanel {
    private int gridX, gridY;
    private double playerX, playerY;
    private double lastPointX, lastPointY;
    private Color color;
    private int direction;
    private long lastTurnTime;
    private int moveSpeed;
    private int playerNumber;
    private static int playerCount;

    private List<TrailSegment> trailSegments = new CopyOnWriteArrayList<>();
    private List<Breadcrumb> trailPoints = new CopyOnWriteArrayList<>();
    private static final long TRAIL_LIFETIME = MyUtils.PLAYER_TRAIL_LIFETIME;

    public Player(int startX, int startY, Color color, int startDirection, int moveSpeed) {
        this.gridX = startX;
        this.gridY = startY;
        this.playerX = startX;
        this.playerY = startY;
        this.lastPointX = startX;
        this.lastPointY = startY;
        this.color = color;
        this.direction = startDirection;
        this.lastTurnTime = System.currentTimeMillis();
        this.moveSpeed = moveSpeed;
        playerCount++;
        this.playerNumber = playerCount;
    }

    public String toString() {
        return "Player #" + this.playerNumber + " X: " + (int) this.playerX + ", Y: " + (int) this.playerY + ", gridX: " + this.gridX + ", gridY: " + this.gridY;
    }

    public double getPlayerX()
    {
        return this.playerX;
    }

    public double getPlayerY()
    {
        return this.playerY;
    }

    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        long currentTime = System.currentTimeMillis();

        List<Breadcrumb> pointsCopy;
        synchronized (this.trailPoints) {
            pointsCopy = new ArrayList<>(this.trailPoints);
        }

        Breadcrumb lastCrumb = null;
        for (Breadcrumb currentCrumb : pointsCopy) {
            long age = currentTime - currentCrumb.creationTime;

            if (age > TRAIL_LIFETIME) continue;

            if (lastCrumb != null) {
                float alphaFactor = 1.0f - ((float) age / TRAIL_LIFETIME);
                int alpha = (int) (alphaFactor * 255);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alpha)));
                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE));
                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));
            }
            lastCrumb = currentCrumb;
        }

        g2.setColor(this.color);
        g2.fillRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
    }


    public void move(int mapWidth, int mapHeight) {
        double prevX = this.playerX;
        double prevY = this.playerY;
        double playerVelocity = MyUtils.GAME_TILE_SIZE * this.moveSpeed * (1.0 / MyUtils.GAME_FPS);

        switch (this.direction) {
            case MyUtils.DIRECTION_LEFT:
                this.playerX -= playerVelocity;
                break;
            case MyUtils.DIRECTION_RIGHT:
                this.playerX += playerVelocity;
                break;
            case MyUtils.DIRECTION_UP:
                this.playerY -= playerVelocity;
                break;
            case MyUtils.DIRECTION_DOWN:
                this.playerY += playerVelocity;
                break;
        }

        int offset = MyUtils.PLAYER_TILE_SIZE / 2;
        long currentTime = System.currentTimeMillis();

        synchronized (this) {
            this.trailPoints.add(new Breadcrumb(this.playerX + offset, this.playerY + offset));

            synchronized (trailSegments) {
                this.trailSegments.add(new TrailSegment(
                        prevX + offset, prevY + offset,
                        this.playerX + offset, this.playerY + offset
                ));
            }

            this.trailSegments.removeIf(ts -> ts.isExpired());
            this.trailPoints.removeIf(p -> (currentTime - p.creationTime) > TRAIL_LIFETIME);
        }

        getGridPosition();
    }

    public void setDirection(int newDirection) {
        // Add Delay To Turn
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - this.lastTurnTime < MyUtils.PLAYER_TURN_DELAY) {
//            return; // Too fast! Ignore this key press.
//        }

        // Dont allow 180 turns
        if (this.direction == newDirection) return;
        if (this.direction == MyUtils.DIRECTION_UP && newDirection == MyUtils.DIRECTION_DOWN) return;
        if (this.direction == MyUtils.DIRECTION_DOWN && newDirection == MyUtils.DIRECTION_UP) return;
        if (this.direction == MyUtils.DIRECTION_LEFT && newDirection == MyUtils.DIRECTION_RIGHT) return;
        if (this.direction == MyUtils.DIRECTION_RIGHT && newDirection == MyUtils.DIRECTION_LEFT) return;

//        this.trailSegments.add(new TrailSegment(this.lastPointX, this.lastPointY, this.playerX, this.playerY));

        // Makes Current Direction Last Direction Before Turning
        this.lastPointX = this.playerX;
        this.lastPointY = this.playerY;

        this.lastTurnTime = System.currentTimeMillis();
        this.direction = newDirection;
    }


    // TEST - Cool Trick, may Implement Later :D
//    public void setDirection(int newDirection) {
//        // If the difference is 2, they are opposites (e.g., |0 - 2| = 2 or |1 - 3| = 2)
//        if (Math.abs(this.direction - newDirection) == 2) {
//            return;
//        }
//        this.direction = newDirection;
//    }

    /**
     * Checks if the player has hit their own trail or another player's trail.
     */
    public boolean checkCollision(List<TrailSegment> otherPlayerSegments) {
        // 1. Initial Safety: Don't die in the first half-second of the game
        if (this.trailSegments.size() < MyUtils.PLAYER_SAFE_SEGMENT) return false;

        int offset = MyUtils.PLAYER_TILE_SIZE / 2;

        TrailSegment lastSeg = this.trailSegments.get(this.trailSegments.size() - 1);
        Line2D.Double headPath = new Line2D.Double(
                lastSeg.line.x2, lastSeg.line.y2,
                this.playerX + offset, this.playerY + offset
        );

        int safetyGap = MyUtils.PLAYER_SAFE_SEGMENT;

        if (this.trailSegments.size() > safetyGap) {
            for (int i = 0; i < this.trailSegments.size() - safetyGap; i++) {
                if (headPath.intersectsLine(this.trailSegments.get(i).line)) {
                    return true;
                }
            }
        }

        if (otherPlayerSegments != null) {
            for (int i = 0; i < otherPlayerSegments.size(); i++) {
                if (headPath.intersectsLine(otherPlayerSegments.get(i).line)) {
                    return true;
                }
            }
        }
        return false;
    }


    private void getGridPosition() {
        this.gridX = (int) (this.playerX / MyUtils.GAME_TILE_SIZE);
        this.gridY = (int) (this.playerY / MyUtils.GAME_TILE_SIZE);
    }

    public List<TrailSegment> getTrailSegments() {
        return this.trailSegments;
    }


    public static class TrailSegment {
        public Line2D.Double line;
        public long creationTime;

        public TrailSegment(double x1, double y1, double x2, double y2) {
            this.line = new Line2D.Double(x1, y1, x2, y2);
            this.creationTime = System.currentTimeMillis();
        }

        // Helper to check if this segment should still exist
        public boolean isExpired() {
            return System.currentTimeMillis() - creationTime > MyUtils.PLAYER_TRAIL_LIFETIME;
        }
    }

    // Trial Point "Leaving Breadcrumbs" :D
    private static class Breadcrumb {
        double x, y;
        long creationTime;

        public Breadcrumb(double x, double y) {
            this.x = x;
            this.y = y;
            this.creationTime = System.currentTimeMillis();
        }
    }
}
