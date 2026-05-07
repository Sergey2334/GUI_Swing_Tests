package GUI_Tests.Utilities;

import GUI_Tests.Player;
import GUI_Tests.Utilities.DialogBox.DialogBox;
import GUI_Tests.Utilities.DialogBox.DialogBoxOption;
import GUI_Tests.MainWinodw.MainWindowUtilities.WindowAnimations;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class MyUtils {
    public static final Color COLOR_TRANSPARENT = new Color(0, 0, 0, 0);
    public static final Color COLOR_TRON1 = new Color(0x18CAE6);
    public static final Color COLOR_TRON1_TRANSPARENT = transparentColor(MyUtils.COLOR_TRON1, 80);
    public static final Color COLOR_TRON2 = new Color(0x7DFDFE);
    public static final Color COLOR_TRON2_TRANSPARENT = transparentColor(MyUtils.COLOR_TRON2, 80);
    public static final Color COLOR_GRAY1 = new Color(100, 100, 100, 255);
    public static final Color COLOR_GRAY1_TRANSPARENT = new Color(100, 100, 100, 80);
    public static final Color COLOR_GRAY2 = new Color(70, 70, 70, 255);
    public static final Color COLOR_GRAY2_TRANSPARENT = new Color(70, 70, 70, 80);
    public static final Color COLOR_BLACK1 = new Color(25, 25, 25, 255);
    public static final Color COLOR_BLACK1_TRANSPARENT = new Color(25, 25, 25, 80);
    public static final Color COLOR_BLACK1_TRANSPARENT2 = new Color(25, 25, 25, 180);
    public static final Color COLOR_BLACK1_TRANSPARENT3 = new Color(25, 25, 25, 200);


    public static final Color COLOR_TEST_RED = new Color(0xFF0000);
    public static final Color COLOR_TEST_GREEN = new Color(0x00FF00);
    public static final Color COLOR_TEST_BLUE = new Color(0x0000FF);

    public static final String CONSOLE_RESET = "\u001B[0m";
    public static final String CONSOLE_RED = "\u001B[31m";
    public static final String CONSOLE_GREEN = "\u001B[32m";
    public static final String CONSOLE_YELLOW = "\u001B[33m";
    public static final String CONSOLE_BLUE = "\u001B[34m";
    public static final String CONSOLE_PURPLE = "\u001B[35m";
    public static final String CONSOLE_CYAN = "\u001B[36m";
    public static final String EMOJI_GREEN_CHECK = "✅";

    public static final Border BORDER_TEST_RED = BorderFactory.createLineBorder(COLOR_TEST_RED, 3);
    public static final Border BORDER_TEST_GREEN = BorderFactory.createLineBorder(COLOR_TEST_GREEN, 3);
    public static final Border BORDER_TEST_BLUE = BorderFactory.createLineBorder(COLOR_TEST_BLUE, 3);

    public static final Font FONT_TRON1 = new Font("Agency FB", Font.BOLD, 24);
    public static final Font FONT_TRON2 = new Font("Ariel", Font.BOLD, 24);

    public static final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int userScreenWidth = (int) screenSize.getWidth();
    public static int userScreenHeight = (int) screenSize.getHeight();
    public static final int WINDOW_MIN_WIDTH = (int) (userScreenWidth / 2);
    public static final int WINDOW_MIN_HEIGHT = (int) (userScreenHeight / 2);


    public static final int ROUNDED_CORNERS_RADIUS = 11;

    public static final int DIALOG_BOX_NOTHING = -1;
    public static final int DIALOG_BOX_EXIT = 1;
    public static final int DIALOG_BOX_CANCEL = 0;

    public static final int GAME_FPS = 60;
    public static final int GAME_MIN_WIDTH = 640 * 2;
    public static final int GAME_MIN_HEIGHT = 360 * 2;
    public static final int GAME_TILE_SIZE = 15;
    public static final int PLAYER_TILE_SIZE = GAME_TILE_SIZE * 2;
    public static final int PLAYER_TRAIL_SIZE = GAME_TILE_SIZE / 2;
    public static final long PLAYER_TRAIL_LIFETIME = 14 * 1000;
    public static final int PLAYER_SAFE_SEGMENT = PLAYER_TILE_SIZE;
    public static final int GAME_SPEED_FAST = 10;
    public static final int GAME_SPEED_MEDIUM = 5;
    public static final int GAME_SPEED_SLOW = 2;
    public static final int DIRECTION_UP = 90;
    public static final int DIRECTION_LEFT = 180;
    public static final int DIRECTION_DOWN = 270;
    public static final int DIRECTION_RIGHT = 0;
    public static final int PLAYER_TURN_DELAY = 100;
    public static final double GAME_ARENA_INSET = 0;
    public static final double GAME_ARENA_INSET_SHRINK_SPEED = 0.03;

    private MyUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // FUNCTIONS

    private static Color transparentColor(Color color, int alphaValue) {
        int hexColor = color.getRGB();
        int alpha = alphaValue;

        int r = (hexColor >> 16) & 0xFF;
        int g = (hexColor >> 8) & 0xFF;
        int b = (hexColor >> 0) & 0xFF;

        return new Color(r, g, b, alpha);
    }

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
                MyUtils.printSuccessfulInitialization("Title Bar Icon");
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
            MyUtils.printSuccessfulInitialization("Window Icon");
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

    public static void printWithColor(String text, String color) {
        System.out.println(color + text + MyUtils.CONSOLE_RESET);
    }

    public static void printSuccessfulInitialization(String componentName) {
        printWithColor(componentName + " - " + MyUtils.EMOJI_GREEN_CHECK, MyUtils.CONSOLE_GREEN);
    }


    //---------------- Window Buttons Functions ----------------
    public static void minimizeWindow(JFrame window) {
        WindowAnimations.minimizeWindow(window);
    }

    public static void toggleMaximize(JFrame window) {
        MyUtils.printWithColor("isMaximized - " + WindowAnimations.toggleMaximize(window), MyUtils.CONSOLE_BLUE);
    }

    public static void closeWindow(JFrame window) {
        int exitValue = MyUtils.getExitDialogBoxValue(window);
        if (exitValue == MyUtils.DIALOG_BOX_EXIT) {
            WindowAnimations.closeWindow(window);
        }
    }

    private static int getExitDialogBoxValue(JFrame window) {
        DialogBoxOption dialogBoxOption1 = new DialogBoxOption(" Yes ", MyUtils.DIALOG_BOX_EXIT);
        DialogBoxOption dialogBoxOption2 = new DialogBoxOption(" No ", MyUtils.DIALOG_BOX_CANCEL);
        DialogBoxOption[] dialogBoxOptionsArray = {dialogBoxOption1, dialogBoxOption2};
        DialogBox dialogBox = new DialogBox("Are You Sure You Want To Exit ?", dialogBoxOptionsArray, window);

        return dialogBox.getSelectedValue();
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
                        MyUtils.toggleMaximize(frame);
                    }
                }
            }
        });
    }

    public static void addButtonHoverColorChangeEffect(JButton button, Color colorHover, Color colorExit) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(colorHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(colorExit);
            }
        });
    }
    //---------------- Window Buttons Functions ----------------


    //---------------- Game Launcher Functions ----------------

    public static void drawGrid(Graphics2D g2, JPanel gameLauncher) {
        g2.setColor(MyUtils.COLOR_GRAY2_TRANSPARENT);

        // If we Include a Border
        Insets insets = gameLauncher.getInsets();
        int xStart = insets.left;
        int yStart = insets.top;

        int usableWidth = gameLauncher.getWidth() - insets.left - insets.right;
        int usableHeight = gameLauncher.getHeight() - insets.top - insets.bottom;

        int tileSize = MyUtils.GAME_TILE_SIZE;
        int cols = usableWidth / tileSize;
        int rows = usableHeight / tileSize;

        int offsetX = xStart + (usableWidth - (cols * tileSize)) / 2;
        int offsetY = yStart + (usableHeight - (rows * tileSize)) / 2;

        // Draw Vertical Lines
        for (int i = 0; i <= cols; i++) {
            int x = offsetX + (i * tileSize);
            g2.drawLine(x, offsetY, x, offsetY + (rows * tileSize));
        }

        // Draw Horizontal Lines
        for (int i = 0; i <= rows; i++) {
            int y = offsetY + (i * tileSize);
            g2.drawLine(offsetX, y, offsetX + (cols * tileSize), y);
        }
    }

    public static void drawCenterPoint(Graphics2D g2, JPanel gameLauncher) {
        g2.setColor(MyUtils.COLOR_TEST_RED);
        g2.fillOval(gameLauncher.getWidth() / 2, gameLauncher.getHeight() / 2, 10, 10);
    }

    public static boolean isOutOfBounds(Player p, int w, int h) {
        return p.getPlayerX() < 0 ||
                p.getPlayerX() > w - MyUtils.PLAYER_TILE_SIZE ||
                p.getPlayerY() < 0 ||
                p.getPlayerY() > h - MyUtils.PLAYER_TILE_SIZE;
    }

    //---------------- Game Launcher Functions ----------------
}