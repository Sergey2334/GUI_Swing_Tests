package GUI_Tests.Entities;

import java.awt.*;

public class FloatingText {
    private final String text;
    private final double x;
    private double y;
    private final Color color;
    private float life = 1.0f; // 1.0 = New, 0.0 = Dead
    private final float fadeSpeed;

    public FloatingText(String text, double x, double y, Color color, int durationMs) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = color;
        // Calculate how much life to lose per frame (assuming 60fps)
        this.fadeSpeed = 1.0f / (durationMs / 16.0f);
    }

    public void update() {
        this.life -= this.fadeSpeed;
        this.y -= 0.5;
    }

// Inside FloatingText.java -> draw(Graphics2D g2)

    public void draw(Graphics2D g2) {
        if (this.life <= 0) return;

        g2.setFont(new Font("Agency FB", Font.BOLD, 80)); // Big and bold
        FontMetrics fm = g2.getFontMetrics();

        // Calculate the centered X based on the text width
        int centeredX = (int) (this.x - (fm.stringWidth(this.text) / 2));
        int alpha = (int) (this.life * 255);

        // 1. Shadow/Glow (Off-center)
        g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha / 2));
        g2.drawString(this.text, centeredX + 3, (int)this.y + 3);

        // 2. Core Text
        g2.setColor(new Color(255, 255, 255, alpha));
        g2.drawString(this.text, centeredX, (int)this.y);
    }


    public boolean isDead() {
        return this.life <= 0;
    }
}