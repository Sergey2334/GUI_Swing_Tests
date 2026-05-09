package GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements;

import java.awt.Graphics2D;

/**
 * The blueprint for any decorative background asset.
 */
public interface VisualElement {
    void update();
    void draw(Graphics2D g2, int width, int height);
}