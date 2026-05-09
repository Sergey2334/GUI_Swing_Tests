package GUI_Tests.Utilities.MainWindowUtilities;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public final class WindowAnimations {

    // TEST - Original
//    private static final int FPS_DELAY = 10;
//    private static final int TOTAL_FRAMES = 15;

    // TEST - Works Smooth
    private static final int FPS_DELAY = 8;
    private static final int TOTAL_FRAMES = 16;

    private static Rectangle lastNormalBounds = null;

    private WindowAnimations() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void minimizeWindow(JFrame window) {
        if (window == null) return;
        final float startOpacity = window.getOpacity();
        final int[] frame = {0};

        Timer timer = new Timer(FPS_DELAY, e -> {
            frame[0]++;
            float progress = (float) frame[0] / TOTAL_FRAMES;
            window.setOpacity(Math.max(0, startOpacity * (1 - progress)));

            if (frame[0] >= TOTAL_FRAMES) {
                ((Timer) e.getSource()).stop();
                window.setExtendedState(Frame.ICONIFIED);
                window.setOpacity(startOpacity); // Reset for when it's restored
            }
        });
        timer.start();
    }

    public static void closeWindow(JFrame window) {
        if (window == null) return;
        final int[] frame = {0};

        Timer timer = new Timer(FPS_DELAY, e -> {
            frame[0]++;
            float progress = (float) frame[0] / TOTAL_FRAMES;
            window.setOpacity(Math.max(0, 1.0f - progress));

            if (frame[0] >= TOTAL_FRAMES) {
                ((Timer) e.getSource()).stop();
                window.dispose();
                System.exit(0);
            }
        });
        timer.start();
    }

    public static boolean toggleMaximize(JFrame window) {
        if (window == null) return false;

        // 1. Get current usable screen area
        GraphicsConfiguration config = window.getGraphicsConfiguration();
        Insets screenInsets = window.getToolkit().getScreenInsets(config);
        Rectangle screenBounds = config.getBounds();
        Rectangle usableBounds = new Rectangle(
                screenBounds.x + screenInsets.left,
                screenBounds.y + screenInsets.top,
                screenBounds.width - (screenInsets.left + screenInsets.right),
                screenBounds.height - (screenInsets.top + screenInsets.bottom)
        );

        // 2. Check if we are currently maximized (OR if we are already filling the usable bounds)
        boolean isCurrentlyMaximized = (window.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;

        // Safety check: if the window is exactly the size of usableBounds, treat it as maximized
        if (window.getBounds().equals(usableBounds)) {
            isCurrentlyMaximized = true;
        }

        if (!isCurrentlyMaximized) {
            // GOING TO MAXIMIZED
            lastNormalBounds = window.getBounds();
            window.setMaximizedBounds(usableBounds);
            animateToggle(window, lastNormalBounds, usableBounds, true);
            return true;
        } else {
            // GOING TO NORMAL (Shrinking)
            // If we lost our "breadcrumb" during minimize, use a default 720p size centered
            if (lastNormalBounds == null || lastNormalBounds.width >= usableBounds.width) {
                lastNormalBounds = new Rectangle(
                        usableBounds.x + (usableBounds.width - 1280) / 2,
                        usableBounds.y + (usableBounds.height - 720) / 2,
                        1280, 720
                );
            }

            animateToggle(window, window.getBounds(), lastNormalBounds, false);
            return false;
        }
    }

    private static void animateToggle(JFrame window, Rectangle from, Rectangle to, boolean goMax) {
        // 1. Temporarily disable rounded corners for MAX SPEED
        window.setShape(null);
        window.setExtendedState(Frame.NORMAL);

        final int[] frame = {0};
        Timer timer = new Timer(FPS_DELAY, e -> {
            frame[0]++;
            float progress = (float) frame[0] / TOTAL_FRAMES;
            float ease = 1 - (float) Math.pow(1 - progress, 3);

            int x = (int) (from.x + (to.x - from.x) * ease);
            int y = (int) (from.y + (to.y - from.y) * ease);
            int w = (int) (from.width + (to.width - from.width) * ease);
            int h = (int) (from.height + (to.height - from.height) * ease);

            window.setBounds(x, y, w, h);

            if (frame[0] >= TOTAL_FRAMES) {
                ((Timer) e.getSource()).stop();

                if (goMax) {
                    window.setExtendedState(Frame.MAXIMIZED_BOTH);
                } else {
                    // 2. RE-ENABLE rounded corners only when the window is small and still
                    MyUtils.applyRoundedCorners(window, MyUtils.ROUNDED_CORNERS_RADIUS);
                }
                window.revalidate();
                window.repaint();
            }
        });
        timer.start();
    }
}