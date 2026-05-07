package GUI_Tests.MainWinodw.MainWindowClientArea;

import GUI_Tests.GameLauncher;
import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.Random;

public class MainWindowClientArea extends JPanel {
    private final String name = "Main Window Client Area";

    public MainWindowClientArea() {
        this.initialize();
        MyUtils.printSuccessfulInitialization(this.name);
    }

    private void initialize() {
        this.setMinimumSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT + 40));
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setLayout(new GridBagLayout()); // Use GridBag for perfect centering
        this.setOpaque(false);

        GameLauncher gameLauncher = new GameLauncher();
        // GridBagConstraints() with no arguments defaults to Center
        gameLauncher.setOpaque(false);
        this.add(gameLauncher, new GridBagConstraints());

        gameLauncher.startGame();
        this.setVisible(true);
    }

    // TEST - MainWindowContent Background
    private float radarAngle = 1;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. STATIC DEEP GRID
        g2.setColor(new Color(15, 15, 25));
        for (int i = 0; i < getWidth(); i += 50) g2.drawLine(i, 0, i, getHeight());
        for (int i = 0; i < getHeight(); i += 50) g2.drawLine(0, i, getWidth(), i);

        // 2. CORNER PULSE LIGHTS
        float pulse = (float) (Math.sin(System.currentTimeMillis() * 0.002) * 0.5 + 0.5);

        this.blip1Angle += 0.01f;
        this.blip2Angle += 0.01f;

        g2.setColor(new Color(0, 255, 255, (int)(pulse * 100)));
        g2.fillOval(20, 20, 12, 12); // Status LED
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        g2.drawString("SECURE_LINK", 40, 30);

        // 3. RADAR SWEEP (Bottom Right)
        drawRadar(g2);

        // 4. DECORATIVE TERMINAL TEXT
        g2.setColor(new Color(0, 255, 255, 80));
        g2.drawString("CPU_LOAD: " + (int)(pulse * 20 + 10) + "%", 20, getHeight() - 20);
        g2.drawString("GRID_SYNC: ACTIVE", 20, getHeight() - 40);

        g2.dispose();
    }

    // Coordinates relative to the radar center
    private double blip1Angle = 0.5; // Radians
    private double blip1Dist = 35;  // Pixels from center
    private double blip2Angle = 4.2;
    private double blip2Dist = 45;

    private void drawRadar(Graphics2D g2) {
        int size = 150;
        int x = getWidth() - size - 30;
        int y = getHeight() - size - 30;
        int cx = x + size/2;
        int cy = y + size/2;

        g2.setColor(new Color(0, 80, 80, 50));
        g2.drawOval(x, y, size, size);
        g2.drawOval(x+25, y+25, 50, 50);

        radarAngle += 0.04f;
        Arc2D.Double beam = new Arc2D.Double(x, y, size, size, -Math.toDegrees(radarAngle), 45, Arc2D.PIE);
        g2.setPaint(new Color(0, 255, 255, 60));
        g2.fill(beam);

        g2.setColor(new Color(0, 255, 255, 150));
        g2.drawLine(cx, cy, (int)(cx + 50 * Math.cos(radarAngle)), (int)(cy + 50 * Math.sin(radarAngle)));
        drawBlip(g2, cx, cy, blip1Angle, blip1Dist);
        drawBlip(g2, cx, cy, blip2Angle, blip2Dist);
    }

    private void drawBlip(Graphics2D g2, int cx, int cy, double angle, double dist) {
        // Normalize angles to 0 - 2PI for comparison
        double currentScan = radarAngle % (Math.PI * 2);
        double targetAngle = angle % (Math.PI * 2);

        // Calculate the distance between the beam and the blip
        double diff = Math.abs(currentScan - targetAngle);

        // If the beam is close, make the blip bright. Otherwise, let it fade.
        if (diff < 0.3) {
            int alpha = (int) (255 * (1.0 - (diff / 0.3)));
            g2.setColor(new Color(0, 255, 0, Math.max(0, alpha))); // Neon Green Blip

            int bx = (int) (cx + dist * Math.cos(angle));
            int by = (int) (cy + dist * Math.sin(angle));

            // Draw the dot
            g2.fillOval(bx - 2, by - 2, 5, 5);

            // Add a tiny glow
            g2.setColor(new Color(0, 255, 0, alpha / 4));
            g2.drawOval(bx - 4, by - 4, 9, 9);
        }
    }
}