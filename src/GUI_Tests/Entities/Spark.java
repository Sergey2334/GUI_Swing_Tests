package GUI_Tests.Entities;

import java.awt.*;
import java.util.Random;

public class Spark {
    private double x, y, dx, dy;
    private float life = 1.0f; // Start at 100% life
    private final Color color;
    private final Random random = new Random();

    public Spark(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        // Random "explosion" speed
        this.dx = (random.nextDouble() - 0.5) * 6;
        this.dy = (random.nextDouble() - 0.5) * 6;
    }

    public void update() {
        this.x += this.dx;
        this.y += this.dy;
        // Gravity effect
        this.dy += 0.1;
        // Fade out (Higher number = faster disappearance)
        this.life -= 0.02f;
    }

    public void draw(Graphics2D g2) {
        if (this.life <= 0) return;

        int alpha = (int) (this.life * 255);
        g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha));
        // Draw a tiny 3x3 square for the spark
        g2.fillRect((int)this.x, (int)this.y, 3, 3);
    }

    public boolean isDead() {
        return this.life <= 0;
    }
}