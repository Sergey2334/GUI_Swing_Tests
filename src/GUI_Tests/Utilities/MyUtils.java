package GUI_Tests.Utilities;

import GUI_Tests.MainWinodw.TitleBar.TitleBar;
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
    public static final int WINDOW_MIN_WIDTH = userScreenWidth / 2;
    public static final int WINDOW_MIN_HEIGHT = userScreenHeight / 2;


    public static final int ROUNDED_CORNERS_RADIUS = 11;
    public static final int ROUNDED_CORNERS_RADIUS_DIALOG_BOX = 35;


    public static final int DIALOG_BOX_NOTHING = -1;
    public static final int DIALOG_BOX_EXIT = 1;
    public static final int DIALOG_BOX_CANCEL = 0;

    public static final int GAME_FPS = 60;
    public static final int GAME_MIN_WIDTH = 1280;
    public static final int GAME_MIN_HEIGHT = 720;
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
    public static final int PLAYER_WIN_SCORE = 2;
    public static final Color[] TRON_COLORS = {
            new Color(0, 255, 255),   // Cyan
            new Color(255, 0, 255),   // Pink
            new Color(50, 255, 50),   // Green
            new Color(255, 150, 0),   // Orange
            new Color(255, 255, 255)  // White
    };

    // --- GAMEPLAY CONFIG ---
    public static final int COUNTDOWN_START_VAL = 3;
    public static final int SHAKE_INITIAL_MAGNITUDE = 15;
    public static final int SHAKE_REDUCTION_STEP = 2;

    // --- FONTS ---
    public static final Font FONT_SCORE = new Font("Agency FB", Font.BOLD, 100);
    public static final Font FONT_COUNTDOWN = new Font("Agency FB", Font.BOLD, 100);
    public static final Font FONT_PAUSE = new Font("Arial", Font.BOLD, 50);



    private MyUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // FUNCTIONS

    private static Color transparentColor(Color color, int alphaValue) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alphaValue);
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
                // 1. Check if the window is in "True Fullscreen" mode
                GraphicsDevice device = frame.getGraphicsConfiguration().getDevice();
                if (device.getFullScreenWindow() == frame) {
                    // If fullscreen, remove the shape (must be a rectangle)
                    frame.setShape(null);
                    return;
                }

                // 2. Only apply rounded corners if NOT maximized and NOT fullscreen
                boolean isMaximized = (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;
                if (!isMaximized) {
                    frame.setShape(new RoundRectangle2D.Double(
                            0, 0, frame.getWidth(), frame.getHeight(), radius, radius));
                } else {
                    // Remove rounded corners when maximized so it fits the screen edges perfectly
                    frame.setShape(null);
                }
            }
        });
    }


    public static void applyRoundedCornersDialogBox(Window window, int radius) {
        window.setShape(new java.awt.geom.RoundRectangle2D.Double(
                0, 0, window.getWidth(), window.getHeight(), radius, radius
        ));
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

    public static void toggleFullScreen(JFrame window) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // 1. Find the TitleBar component modularly
        TitleBar titleBar = null;
        for (Component comp : window.getContentPane().getComponents()) {
            if (comp instanceof TitleBar) {
                titleBar = (TitleBar) comp;
                break;
            }
        }

        if (device.getFullScreenWindow() == null) {
            // --- ENTERING FULLSCREEN ---
            // Save the state so we know if we were maximized or not
            int currentState = window.getExtendedState();

            if (titleBar != null) titleBar.setVisibleState(false);

            // Use a client property to remember the state across modes
            window.getRootPane().putClientProperty("preFullScreenState", currentState);

            device.setFullScreenWindow(window);
        } else {
            // --- EXITING FULLSCREEN ---
            device.setFullScreenWindow(null);

            // Recover the old state
            Object oldStateObj = window.getRootPane().getClientProperty("preFullScreenState");
            int oldState = (oldStateObj instanceof Integer) ? (int)oldStateObj : Frame.NORMAL;

            // Restore TitleBar
            if (titleBar != null) titleBar.setVisibleState(true);

            // CRITICAL FIX: Re-apply the state and force a layout update
            window.setExtendedState(oldState);

            if (oldState == Frame.NORMAL) {
                window.setLocationRelativeTo(null);
            }

            window.revalidate();
            window.repaint();
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
        MouseAdapter doubleClickAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Check for exactly 2 clicks
                if (e.getClickCount() == 2) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor((Component) e.getSource());
                    if (frame != null) {
                        MyUtils.toggleMaximize(frame);
                    }
                }
            }
        };

        // Apply the listener to the TitleBar itself
        titleBar.addMouseListener(doubleClickAdapter);

        // CRITICAL: Apply it to all children (like the TitleText) so clicking them also works!
        for (Component child : titleBar.getComponents()) {
            // Skip buttons (we don't want double-clicking the 'X' to maximize)
            if (!(child instanceof JButton)) {
                child.addMouseListener(doubleClickAdapter);
            }
        }
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

    //---------------- Client Area Visuals  ----------------
    public static void drawDeepGrid(Graphics2D g2, int w, int h) {
        g2.setColor(new Color(15, 15, 25));
        for (int i = 0; i < w; i += 50) g2.drawLine(i, 0, i, h);
        for (int i = 0; i < h; i += 50) g2.drawLine(0, i, w, i);
    }
    //---------------- Client Area Visuals  ----------------


    public static void drawGrid(Graphics2D g2, JPanel gameLauncher) {
        g2.setColor(MyUtils.COLOR_GRAY2_TRANSPARENT);
        g2.setStroke(new BasicStroke(1.0f));

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

    public static void drawMainMenu(Graphics2D g2, JPanel gameLauncher, int shake, float hue) {
        // Background overlay
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gameLauncher.getWidth(), gameLauncher.getHeight());

        // Create the rainbow color based on the hue
        Color rainbowColor = Color.getHSBColor(hue, 0.8f, 1.0f); // 0.8f is saturation (vibrancy)

        // --- LOGO ---
        int centerX = gameLauncher.getWidth() / 2;
        int centerY = gameLauncher.getHeight() / 3;
        g2.setFont(new Font("Agency FB", Font.BOLD, 120));

        // Draw Glow with the rainbow color
        g2.setColor(new Color(rainbowColor.getRed(), rainbowColor.getGreen(), rainbowColor.getBlue(), 100));
        g2.drawString("TRON", gameLauncher.getWidth() / 2 - 105, gameLauncher.getHeight() / 3 + 5);

        // Draw Core
        g2.setColor(Color.WHITE);
        g2.drawString("TRON", gameLauncher.getWidth() / 2 - 110, gameLauncher.getHeight() / 3);

        // --- INSTRUCTIONS ---
        g2.setFont(new Font("Monospaced", Font.PLAIN, 20));
        g2.setColor(Color.WHITE);
        g2.drawString("P1: WASD  |  P2: ARROWS", centerX - 130, gameLauncher.getHeight() / 2);

        // Pulsing "PRESS E"
        float alpha = (float) (Math.sin(System.currentTimeMillis() * 0.005) * 0.5 + 0.5);
        g2.setColor(new Color(1f, 1f, 1f, alpha));
        g2.drawString("PRESS E TO BEGIN", centerX - 100, gameLauncher.getHeight() / 2 + 100);
    }

    public static void drawSettingsScreen(Graphics2D g2, JPanel panel, int diff, int p1C, int p2C) {
        g2.setColor(new Color(0, 0, 0, 230));
        g2.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        g2.setFont(new Font("Agency FB", Font.BOLD, 60));
        g2.setColor(Color.WHITE);
        g2.drawString("MATCH CONFIGURATION", 50, 80);

        // Show Difficulty
        String[] diffNames = {"EASY", "MEDIUM", "HARD"};
        g2.setFont(new Font("Monospaced", Font.BOLD, 25));
        g2.setColor(Color.GRAY);
        g2.drawString("DIFFICULTY: [1-3] " + diffNames[diff], 50, 180);

        // Show Color Selection
        g2.setColor(MyUtils.TRON_COLORS[p1C]);
        g2.drawString("P1 COLOR: [Q] TO CYCLE", 50, 250);

        g2.setColor(MyUtils.TRON_COLORS[p2C]);
        g2.drawString("P2 COLOR: [E] TO CYCLE", 50, 320);

        // Start Instruction
        float pulse = (float)(Math.sin(System.currentTimeMillis()*0.01)*0.5+0.5);
        g2.setColor(new Color(1f,1f,1f, pulse));
        g2.drawString("PRESS SPACE TO INITIATE", 50, 450);
    }


    public static void drawWinScreen(Graphics2D g2, JPanel gameLauncher, String winnerName, Color winnerColor, float logoHue) {
        // 1. Dim the background
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gameLauncher.getWidth(), gameLauncher.getHeight());

        // 2. Pulsing Winner Text
        float pulse = (float) (Math.sin(System.currentTimeMillis() * 0.005) * 0.05 + 1);
        g2.setFont(new Font("Agency FB", Font.BOLD, (int)(100 * pulse)));

        // Rainbow winner color
        Color winRainbow = Color.getHSBColor(logoHue, 1.0f, 1.0f);
        g2.setColor(winRainbow);

        String mainText = winnerName + " VICTORIOUS";
        FontMetrics fmMain = g2.getFontMetrics();
        int xMain = (gameLauncher.getWidth() - fmMain.stringWidth(mainText)) / 2;
        int yMain = gameLauncher.getHeight() / 2;
        g2.drawString(mainText, xMain, yMain);

        // 3. Sub-instructions (Rematch/Menu)
        g2.setFont(new Font("Monospaced", Font.PLAIN, 18));
        g2.setColor(Color.WHITE);

        String subText = "PRESS ESC FOR MENU | PRESS E FOR REMATCH";
        FontMetrics fmSub = g2.getFontMetrics();
        int xSub = (gameLauncher.getWidth() - fmSub.stringWidth(subText)) / 2;
        // Position it below the main text
        g2.drawString(subText, xSub, yMain + 80);
    }

// --- MODULAR DRAWING HELPERS ---
    public static void drawVoidWalls(Graphics2D g2, int w, int h, double arenaInset) {
        float wallThickness = (float) arenaInset;
        if (wallThickness > 0) {
            // Draw the dark outer "Void"
            g2.setStroke(new BasicStroke(wallThickness * 2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            g2.setColor(COLOR_BLACK1.brighter());
            g2.drawRect(0, 0, w, h);

            // Draw the neon red "Laser Fence"
            g2.setStroke(new BasicStroke(5));
            g2.setColor(Color.RED);
            int inset = (int) arenaInset;
            g2.drawRect(inset, inset, w - (inset * 2), h - (inset * 2));
        }
    }

    public static void drawScores(Graphics2D g2, int w, int h, int s1, int s2) {
        g2.setFont(FONT_SCORE);

        // Player 1
        g2.setColor(COLOR_TRON1_TRANSPARENT);
        g2.drawString(String.valueOf(s1), 50, h / 2);

        // Player 2
        g2.setColor(COLOR_TRON2_TRANSPARENT);
        String p2Text = String.valueOf(s2);
        int p2X = w - g2.getFontMetrics().stringWidth(p2Text) - 50;
        g2.drawString(p2Text, p2X, h / 2);
    }

    public static void drawCountdown(Graphics2D g2, int w, int h, int value) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, w, h);

        g2.setColor(COLOR_TRON1);
        g2.setFont(FONT_COUNTDOWN);
        String text = (value > 0) ? String.valueOf(value) : "GO!";
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = (h / 2) + (fm.getAscent() / 4);
        g2.drawString(text, x, y);
    }

    public static void drawPauseOverlay(Graphics2D g2, int w, int h) {
        // 1. Darken the entire screen
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, w, h);

        // 2. Draw "PAUSED" text
        g2.setColor(COLOR_TRON1);
        g2.setFont(FONT_PAUSE);
        String text = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();
        int x = (w - fm.stringWidth(text)) / 2;
        int y = h / 2;
        g2.drawString(text, x, y);
    }

    //---------------- Game Launcher Functions ----------------
}