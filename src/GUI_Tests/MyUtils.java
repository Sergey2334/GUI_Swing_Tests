package GUI_Tests;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class MyUtils {
    public static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Color COLOR_TRON1 = new Color(0x18CAE6);
    public static final Color COLOR_TRON2 = new Color(0x7DFDFE);
    public static final Color COLOR_GRAY1 = new Color(100, 100, 100, 255);
    public static final Color COLOR_GRAY1_TRANSPARENT = new Color(100, 100, 100, 80);
    public static final Color COLOR_BLACK1 = new Color(25, 25, 25, 255);
    public static final Color COLOR_BLACK1_TRANSPARENT = new Color(25, 25, 25, 80);
    public static final Color COLOR_BLACK1_TRANSPARENT2 = new Color(25, 25, 25, 180);


    public static final Color COLOR_TEST_RED = new Color(0xFF0000);
    public static final Color COLOR_TEST_GREEN = new Color(0x00FF00);
    public static final Color COLOR_TEST_BLUE = new Color(0x0000FF);

    public static final Border BORDER_TEST_RED = BorderFactory.createLineBorder(COLOR_TEST_RED, 3);
    public static final Border BORDER_TEST_GREEN = BorderFactory.createLineBorder(COLOR_TEST_GREEN, 3);
    public static final Border BORDER_TEST_BLUE = BorderFactory.createLineBorder(COLOR_TEST_BLUE, 3);

    public static final Font FONT_TRON1 = new Font("Agency FB", Font.BOLD, 24);
    public static final Font FONT_TRON2 = new Font("Ariel", Font.BOLD, 24);

    public static final int WINDOW_MIN_WIDTH = 600;
    public static final int WINDOW_MIN_HEIGHT = 300;

    public static final int ROUNDED_CORNERS_RADIUS = 11;

    private MyUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // FUNCTIONS

    /* !!!-MAY CAUSE ERRORS IF SWITCHING PATHS-!!! */
    public static void setTitleBarTextIcon(JLabel titleBarTitleText) {
        /* !!!-MAY CAUSE ERRORS IF SWITCHING PATHS-!!! */
        String path = "/GUI_Tests/Assets/Images/TronGame.png";
        URL iconURL = MyUtils.class.getResource(path);

        if (iconURL != null) {
            try {
                BufferedImage originalImage = ImageIO.read(iconURL);
                Image scaledImage = originalImage.getScaledInstance(24, 24, Image.SCALE_SMOOTH); // 24px Looks the best
                titleBarTitleText.setIcon(new ImageIcon(scaledImage));
                System.out.println("Title Bar Icon - ✅");
            } catch (IOException e) {
                System.err.println("Error loading image: " + path);
                e.printStackTrace();
            }
        } else {
            System.err.println("WindowIcon NOT FOUND: " + path);
        }
    }

    /* !!!-MAY CAUSE ERRORS IF SWITCHING PATHS-!!! */
    public static void setWindowIcon(JFrame window) {
        /* !!!-MAY CAUSE ERRORS IF SWITCHING PATHS-!!! */
        String path = "/GUI_Tests/Assets/Images/TronGame.png";
        URL iconURL = MyUtils.class.getResource(path);

        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            window.setIconImage(icon.getImage());
            System.out.println("Window Icon - ✅");
        } else {
            System.err.println("WindowIcon NOT FOUND: " + path);
        }
    }

    public static void applyRoundedCorners(JFrame frame, int radius) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                frame.setShape(new RoundRectangle2D.Double(
                        0, 0, frame.getWidth(), frame.getHeight(), radius, radius));
            }
        });
    }

    //---------------- Window Buttons Functions ----------------
    public static void minimizeWindow(JFrame window) {
        int currentWindowState = window.getExtendedState();

        window.setExtendedState(currentWindowState | JFrame.ICONIFIED);
    }

//    public static boolean toggleMaximize(JFrame window) {
//        boolean isMaximized = (window.getExtendedState() & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH;
//        System.out.println("isMaximized: " + isMaximized);
//
//        if (isMaximized) {
//            window.setExtendedState(JFrame.NORMAL);
//            return false; // Now normal
//        } else {
//            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
//            return true; // Now maximized
//        }
//    }

    public static boolean toggleMaximize2(JFrame window) {
        // 1. Calculate the 'Safe Area' (Screen minus Taskbar)
        GraphicsConfiguration config = window.getGraphicsConfiguration();
        Insets screenInsets = window.getToolkit().getScreenInsets(config);
        Rectangle screenSize = config.getBounds();

        // Subtract taskbar space from all 4 sides
        Rectangle usableBounds = new Rectangle(
                screenSize.x + screenInsets.left,
                screenSize.y + screenInsets.top,
                screenSize.width - screenInsets.left - screenInsets.right,
                screenSize.height - screenInsets.top - screenInsets.bottom
        );

        // 2. Set these as the limits for the maximized state
        window.setMaximizedBounds(usableBounds);

        // 3. Perform the toggle using Bitwise check
        boolean isMaximized = (window.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
        System.out.println("isMaximized: " + isMaximized);

        if (isMaximized) {
            window.setExtendedState(JFrame.NORMAL);
            return false;
        } else {
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            return true;
        }
    }

    public static void closeWindow(JFrame window) {
        window.setVisible(false);
        window.dispose();
    }

    public static JFrame getWindow(Component component) {
        Window window = SwingUtilities.getWindowAncestor(component);

        if (window instanceof JFrame) {
            return (JFrame) window;
        }

        return null;
    }

    public static void addDoubleClickOnTitleBarForFullScreen(JPanel titleBar) {
        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Only trigger if it is exactly 2 clicks (a double-click)
                if (e.getClickCount() == 2) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(titleBar);

                    if (frame != null) {
                        MyUtils.toggleMaximize2(frame);
                    }
                }
            }
        });
    }
    //---------------- Window Buttons Functions ----------------
}