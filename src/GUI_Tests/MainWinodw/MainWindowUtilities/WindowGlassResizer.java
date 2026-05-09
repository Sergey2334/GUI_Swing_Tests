package GUI_Tests.MainWinodw.MainWindowUtilities;

import GUI_Tests.Utilities.MyUtils;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WindowGlassResizer extends JComponent {
    private final int BORDER = 10;
    private int direction = Cursor.DEFAULT_CURSOR;
    private Point startPos;
    private Rectangle startBounds;
    private final JFrame frame;

    public WindowGlassResizer(JFrame frame) {
        this.frame = frame;

        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                WindowGlassResizer.this.updateCursor(e.getPoint());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (WindowGlassResizer.this.direction != Cursor.DEFAULT_CURSOR) {
                    WindowGlassResizer.this.startPos = e.getLocationOnScreen();
                    WindowGlassResizer.this.startBounds = WindowGlassResizer.this.frame.getBounds();
                    e.consume();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (WindowGlassResizer.this.startPos != null) {
                    WindowGlassResizer.this.handleResize(e.getLocationOnScreen());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                WindowGlassResizer.this.startPos = null;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (WindowGlassResizer.this.startPos == null) {
                    WindowGlassResizer.this.frame.setCursor(Cursor.getDefaultCursor());
                }
            }
        };

        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);
    }

    private void updateCursor(Point p) {
        int x = p.x, y = p.y, w = this.getWidth(), h = this.getHeight();
        int cursor = Cursor.DEFAULT_CURSOR;

        if (y < this.BORDER) cursor = Cursor.N_RESIZE_CURSOR;
        else if (y > h - this.BORDER) cursor = Cursor.S_RESIZE_CURSOR;

        if (x < this.BORDER) {
            if (cursor == Cursor.N_RESIZE_CURSOR) cursor = Cursor.NW_RESIZE_CURSOR;
            else if (cursor == Cursor.S_RESIZE_CURSOR) cursor = Cursor.SW_RESIZE_CURSOR;
            else cursor = Cursor.W_RESIZE_CURSOR;
        } else if (x > w - this.BORDER) {
            if (cursor == Cursor.N_RESIZE_CURSOR) cursor = Cursor.NE_RESIZE_CURSOR;
            else if (cursor == Cursor.S_RESIZE_CURSOR) cursor = Cursor.SE_RESIZE_CURSOR;
            else cursor = Cursor.E_RESIZE_CURSOR;
        }

        this.direction = cursor;
        this.frame.setCursor(Cursor.getPredefinedCursor(cursor));
    }

    private void handleResize(Point currentPos) {
        int dx = currentPos.x - this.startPos.x;
        int dy = currentPos.y - this.startPos.y;
        Rectangle b = new Rectangle(this.startBounds);

        // Logic for which side to stretch
        if ((this.direction & 1) != 0 || this.direction == Cursor.E_RESIZE_CURSOR || this.direction == Cursor.NE_RESIZE_CURSOR || this.direction == Cursor.SE_RESIZE_CURSOR) b.width += dx;
        if (this.direction == Cursor.S_RESIZE_CURSOR || this.direction == Cursor.SW_RESIZE_CURSOR || this.direction == Cursor.SE_RESIZE_CURSOR) b.height += dy;
        if (this.direction == Cursor.W_RESIZE_CURSOR || this.direction == Cursor.NW_RESIZE_CURSOR || this.direction == Cursor.SW_RESIZE_CURSOR) { b.x += dx; b.width -= dx; }
        if (this.direction == Cursor.N_RESIZE_CURSOR || this.direction == Cursor.NW_RESIZE_CURSOR || this.direction == Cursor.NE_RESIZE_CURSOR) { b.y += dy; b.height -= dy; }

        if (b.width > MyUtils.WINDOW_MIN_WIDTH && b.height > MyUtils.WINDOW_MIN_HEIGHT) {
            this.frame.setBounds(b);
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return (x < this.BORDER || x > this.getWidth() - this.BORDER || y < this.BORDER || y > this.getHeight() - this.BORDER || this.startPos != null);
    }
}