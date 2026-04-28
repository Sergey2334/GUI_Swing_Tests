package GUI_Tests.MainWinodw.MainWindowUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowDragger extends MouseAdapter {
    private Point mouseDownCompCoords = null;

    public WindowDragger(JFrame frame) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Component source = (Component) e.getSource();
        Window frame = SwingUtilities.getWindowAncestor(source);

        if (frame instanceof JFrame jf) {
            if ((jf.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) {
                // 1. Restore the window
                jf.setExtendedState(JFrame.NORMAL);

                // 2. Adjust coords so mouse is centered on the now-smaller bar
                mouseDownCompCoords = new Point(jf.getWidth() / 2, e.getPoint().y);
                return;
            }
        }

        // Standard dragging logic...
        Point currCoords = e.getLocationOnScreen();
        frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }

    // Static helper to apply it easily
    public static void makeDraggable(JFrame frame, JPanel panel) {
        WindowDragger dragger = new WindowDragger(frame);
        panel.addMouseListener(dragger);
        panel.addMouseMotionListener(dragger);
    }
}