package GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements;

import GUI_Tests.Utilities.MyUtils;
import java.awt.*;

public class GridWidget implements VisualElement {
    private float scanLinePos = 0;

    @Override
    public void update() {
        // Move the "scanning" highlight line
        this.scanLinePos += 2.0f;
        if (this.scanLinePos > MyUtils.GAME_MIN_HEIGHT * 2) {
            this.scanLinePos = 0;
        }
    }

    @Override
    public void draw(Graphics2D g2, int w, int h) {
        int tileSize = MyUtils.GAME_TILE_SIZE * 2; // Larger grid for background

        // 1. Draw the base dark grid
        g2.setStroke(new BasicStroke(1.0f));
        for (int x = 0; x <= w; x += tileSize) {
            g2.setColor(new Color(0, 255, 255, 20)); // Very subtle cyan
            g2.drawLine(x, 0, x, h);
        }
        for (int y = 0; y <= h; y += tileSize) {
            // 2. Add the "Scan Line" highlight
            float distance = Math.abs(y - (this.scanLinePos % h));
            if (distance < 100) {
                int alpha = (int) (100 * (1.0 - (distance / 100)));
                g2.setColor(new Color(0, 255, 255, Math.max(0, alpha)));
                g2.setStroke(new BasicStroke(2.0f));
            } else {
                g2.setColor(new Color(0, 255, 255, 20));
                g2.setStroke(new BasicStroke(1.0f));
            }
            g2.drawLine(0, y, w, y);
        }
    }
}