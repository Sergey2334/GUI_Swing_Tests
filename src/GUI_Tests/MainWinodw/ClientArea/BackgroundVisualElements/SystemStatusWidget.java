package GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements;

import java.awt.*;
import java.util.Random;

public class SystemStatusWidget implements VisualElement {
    private float pulse = 0;
    private final Random random = new Random();
    private String currentBitstream = "01011010";

    @Override
    public void update() {
        // Pulse for the "LED" glow
        this.pulse = (float) (Math.sin(System.currentTimeMillis() * 0.003) * 0.5 + 0.5);

        // Randomly flicker the bitstream text
        if (this.random.nextInt(10) > 8) {
            this.currentBitstream = Integer.toBinaryString(this.random.nextInt(255));
        }
    }

    @Override
    public void draw(Graphics2D g2, int w, int h) {
        int x = 30;
        int y = 40;

        // 1. Draw the "LED" Indicator
        Color ledColor = new Color(0, 255, 255, (int) (this.pulse * 150) + 100);
        g2.setColor(ledColor);
        g2.fillOval(x, y - 10, 10, 10);

        // Add a glow ring around the LED
        g2.setStroke(new BasicStroke(1));
        g2.setColor(new Color(0, 255, 255, (int) (this.pulse * 50)));
        g2.drawOval(x - 3, y - 13, 16, 16);

        // 2. Draw Main Text
        g2.setFont(new Font("Monospaced", Font.BOLD, 14));
        g2.setColor(new Color(0, 255, 255, 200));
        g2.drawString("SYSTEM_LINK: ESTABLISHED", x + 25, y);

        // 3. Draw Sub-Data (The "Flickering" bits)
        g2.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g2.setColor(new Color(0, 255, 255, 100));
        g2.drawString("BITSTREAM_SYNC: " + this.currentBitstream, x + 25, y + 20);

        // 4. Draw a small decorative bar
        g2.setColor(new Color(0, 255, 255, 40));
        g2.fillRect(x + 25, y + 30, 150, 4);
        g2.setColor(new Color(0, 255, 255, 180));
        g2.fillRect(x + 25, y + 30, (int)(150 * this.pulse), 4); // Pulsing progress bar
    }
}