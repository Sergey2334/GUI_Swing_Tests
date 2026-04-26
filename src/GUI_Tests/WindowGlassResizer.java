package GUI_Tests;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WindowGlassResizer extends JComponent {
    private final int BORDER = 10; // Trigger area
    private int direction = Cursor.DEFAULT_CURSOR;
    private Point startPos;
    private Rectangle startBounds;
    private final JFrame frame;

    public WindowGlassResizer(JFrame frame) {
        this.frame = frame;

        // This makes the GlassPane "transparent" to events we don't want
        AWTEventListener listener = e -> {
            if (e instanceof MouseEvent me) {
                if (me.getSource() == this) processMouseEvent(me);
            }
        };

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                updateCursor(e.getPoint());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (direction != Cursor.DEFAULT_CURSOR) {
                    startPos = e.getLocationOnScreen();
                    startBounds = frame.getBounds();
                    e.consume(); // Stop buttons from clicking while resizing
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startPos != null) {
                    handleResize(e.getLocationOnScreen());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                startPos = null;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (startPos == null) { // Only reset if we aren't currently dragging/resizing
                    frame.setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }

    private void updateCursor(Point p) {
        int x = p.x, y = p.y, w = getWidth(), h = getHeight();
        int cursor = Cursor.DEFAULT_CURSOR;

        if (y < BORDER) cursor = Cursor.N_RESIZE_CURSOR;
        else if (y > h - BORDER) cursor = Cursor.S_RESIZE_CURSOR;

        if (x < BORDER) {
            if (cursor == Cursor.N_RESIZE_CURSOR) cursor = Cursor.NW_RESIZE_CURSOR;
            else if (cursor == Cursor.S_RESIZE_CURSOR) cursor = Cursor.SW_RESIZE_CURSOR;
            else cursor = Cursor.W_RESIZE_CURSOR;
        } else if (x > w - BORDER) {
            if (cursor == Cursor.N_RESIZE_CURSOR) cursor = Cursor.NE_RESIZE_CURSOR;
            else if (cursor == Cursor.S_RESIZE_CURSOR) cursor = Cursor.SE_RESIZE_CURSOR;
            else cursor = Cursor.E_RESIZE_CURSOR;
        }

        direction = cursor;
        frame.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    private void handleResize(Point currentPos) {
        int dx = currentPos.x - startPos.x;
        int dy = currentPos.y - startPos.y;
        Rectangle b = new Rectangle(startBounds);

        if (direction == Cursor.E_RESIZE_CURSOR || direction == Cursor.NE_RESIZE_CURSOR || direction == Cursor.SE_RESIZE_CURSOR) b.width += dx;
        if (direction == Cursor.S_RESIZE_CURSOR || direction == Cursor.SW_RESIZE_CURSOR || direction == Cursor.SE_RESIZE_CURSOR) b.height += dy;
        if (direction == Cursor.W_RESIZE_CURSOR || direction == Cursor.NW_RESIZE_CURSOR || direction == Cursor.SW_RESIZE_CURSOR) { b.x += dx; b.width -= dx; }
        if (direction == Cursor.N_RESIZE_CURSOR || direction == Cursor.NW_RESIZE_CURSOR || direction == Cursor.NE_RESIZE_CURSOR) { b.y += dy; b.height -= dy; }

        if (b.width > 300 && b.height > 200) frame.setBounds(b);
    }

    // This is the secret sauce:
    // If the mouse is in the middle (not resizing), let the click pass through.
    @Override
    public boolean contains(int x, int y) {
        return (x < BORDER || x > getWidth() - BORDER || y < BORDER || y > getHeight() - BORDER || startPos != null);
    }
}
