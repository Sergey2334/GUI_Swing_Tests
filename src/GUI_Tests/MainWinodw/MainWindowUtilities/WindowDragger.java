package GUI_Tests.MainWinodw.MainWindowUtilities;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowDragger extends MouseAdapter {
    private Point mouseDownCompCoords = null;

    // Static helper to apply it easily to any panel
    public static void makeDraggable(JPanel panel) {
        WindowDragger dragger = new WindowDragger();
        panel.addMouseListener(dragger);
        panel.addMouseMotionListener(dragger);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mouseDownCompCoords = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mouseDownCompCoords = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Component source = (Component) e.getSource();
        Window window = SwingUtilities.getWindowAncestor(source);

        if (window instanceof JFrame jf) {
            // --- THE SNAP-BACK LOGIC ---
            if ((jf.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {

                // 1. Calculate where the mouse is relative to the screen
                Point mouseOnScreen = e.getLocationOnScreen();

                // 2. FORCE the window to Normal state and give it its small size back
                jf.setExtendedState(JFrame.NORMAL);

                // 3. Immediately set the size to your "Normal" dimensions (e.g., from MyUtils)
                jf.setSize(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT);

                // 4. RE-CENTER the anchor: Put the mouse in the middle of the new smaller title bar
                this.mouseDownCompCoords = new Point(jf.getWidth() / 2, e.getY());

                // 5. Instantly move the window so it's under the mouse
                jf.setLocation(mouseOnScreen.x - this.mouseDownCompCoords.x,
                        mouseOnScreen.y - this.mouseDownCompCoords.y);

                // 6. Re-apply rounded corners for the windowed mode
                MyUtils.applyRoundedCorners(jf, MyUtils.ROUNDED_CORNERS_RADIUS);

                return;
            }

            // --- STANDARD DRAGGING ---
            if (this.mouseDownCompCoords != null) {
                Point currCoords = e.getLocationOnScreen();
                jf.setLocation(currCoords.x - this.mouseDownCompCoords.x,
                        currCoords.y - this.mouseDownCompCoords.y);
            }
        }
    }

}