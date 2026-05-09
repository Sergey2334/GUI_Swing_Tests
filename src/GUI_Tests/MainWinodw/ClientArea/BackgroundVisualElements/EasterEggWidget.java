package GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements;

import java.awt.*;

public class EasterEggWidget implements VisualElement {
    private float pulse = 0;

    @Override
    public void update() {
        // Slow "breathing" pulse for the text
        this.pulse = (float) (Math.sin(System.currentTimeMillis() * 0.002) * 0.5 + 0.5);
    }

    @Override
    public void draw(Graphics2D g2, int w, int h) {
        g2.setFont(new Font("Monospaced", Font.BOLD, 30));

        // A sneaky dark gray so it's hidden until the GameLauncher moves
        g2.setColor(new Color(150, 150, 150, (int)(this.pulse * 100 + 50)));

        String text = "YOU WEREN'T SUPPOSED TO SEE THIS... O_O";
        FontMetrics fm = g2.getFontMetrics();

        int x = (w - fm.stringWidth(text)) / 2;
        int y = h / 2;

        g2.drawString(text, x, y);

        // Add a tiny stick figure or a "404" below it
        g2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        g2.drawString("(Go back to playing!)", x + 50, y + 30);
    }
}