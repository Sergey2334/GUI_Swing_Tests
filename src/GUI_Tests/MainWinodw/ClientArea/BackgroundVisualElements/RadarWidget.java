package GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

public class RadarWidget implements VisualElement {
    private float angle = 0;
    private final int size = 125; // Slightly larger for better detail

    // Blips with varying positions
    private double[][] blips = {
            {0.5, (double) this.size / 4, 1.0}, // {angle, distance, brightness}
            {4.2, (double) this.size / 3, 0.8},
            {2.1, (double) this.size / 2, 0.5}
    };

    @Override
    public void update() {
        this.angle += 0.05f; // Constant sweep speed
    }

    @Override
    public void draw(Graphics2D g2, int panelWidth, int panelHeight) {
        int x = panelWidth - this.size - 40;
        int y = panelHeight - this.size - 40;
        int cx = x + this.size / 2;
        int cy = y + this.size / 2;

        // 1. Draw Range Rings & Crosshair
        this.drawBackground(g2, x, y, cx, cy);

        // 2. Draw The Sweeping Trail (Phosphor Effect)
        this.drawSweepTrail(g2, x, y);

        // 3. Draw Blips
        for (double[] blip : this.blips) {
            this.drawBlip(g2, cx, cy, blip);
        }

        // 4. Draw Outer Frame
        g2.setStroke(new BasicStroke(2));
        g2.setColor(new Color(0, 255, 255, 100));
        g2.drawOval(x, y, this.size, this.size);
    }

    private void drawBackground(Graphics2D g2, int x, int y, int cx, int cy) {
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(0, 255, 255, 30));

        // Draw 3 rings
        for (int i = 1; i <= 3; i++) {
            int r = (this.size / 3) * i;
            g2.drawOval(cx - r/2, cy - r/2, r, r);
        }

        // Draw Crosshair lines
        g2.drawLine(cx - this.size/2, cy, cx + this.size/2, cy);
        g2.drawLine(cx, cy - this.size/2, cx, cy + this.size/2);
    }

    private void drawSweepTrail(Graphics2D g2, int x, int y) {
        // We draw multiple arcs with decreasing alpha to create a "fade" effect
        for (int i = 0; i < 30; i++) {
            float alpha = (30 - i) / 450f; // Softening the fade
            g2.setColor(new Color(0, 255, 255, (int)(alpha * 255)));

            // Each arc is 1 degree behind the previous one
            double startAngle = -Math.toDegrees(this.angle) + i;
            Arc2D.Double trailPart = new Arc2D.Double(x, y, this.size, this.size, startAngle, 2, Arc2D.PIE);
            g2.fill(trailPart);
        }

        // The bright leading edge
        g2.setColor(new Color(0, 255, 255, 180));
        g2.setStroke(new BasicStroke(2));
        double lx = x + this.size/2 + (this.size/2) * Math.cos(this.angle);
        double ly = y + this.size/2 + (this.size/2) * Math.sin(this.angle);
        g2.drawLine(x + this.size/2, y + this.size/2, (int)lx, (int)ly);
    }

    private void drawBlip(Graphics2D g2, int cx, int cy, double[] blip) {
        double bAngle = blip[0];
        double dist = blip[1];

        double currentScan = this.angle % (Math.PI * 2);
        double targetAngle = bAngle % (Math.PI * 2);
        double diff = Math.abs(currentScan - targetAngle);

        // Blip lights up when the beam passes
        if (diff < 0.4) {
            float intensity = (float)(1.0 - (diff / 0.4)) * (float)blip[2];
            int alpha = (int) (255 * intensity);

            int bx = (int) (cx + dist * Math.cos(bAngle));
            int by = (int) (cy + dist * Math.sin(bAngle));

            // Inner core
            g2.setColor(new Color(0, 255, 0, Math.max(0, alpha)));
            g2.fillOval(bx - 2, by - 2, 4, 4);

            // Outer glow
            g2.setColor(new Color(0, 255, 0, alpha / 4));
            g2.fillOval(bx - 5, by - 5, 10, 10);
        }
    }
}