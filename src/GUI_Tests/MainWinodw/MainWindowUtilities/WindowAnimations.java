package GUI_Tests.MainWinodw.MainWindowUtilities;

import javax.swing.*;
import java.awt.*;

public final class WindowAnimations {

    private static final int FPS_DELAY = 10;
    private static Rectangle lastNormalBounds = null;

    public static void minimizeWindow(JFrame window) {
        final int originalState = window.getExtendedState();
        final float startOpacity = window.getOpacity();
        final int[] frame = {0};

        Timer timer = new Timer(10, e -> {
            frame[0]++;
            float progress = (float) frame[0] / 15;
            window.setOpacity(Math.max(0, startOpacity * (1 - progress)));

            if (frame[0] >= 15) {
                ((Timer) e.getSource()).stop();
                window.setExtendedState(originalState | Frame.ICONIFIED);
                window.setOpacity(startOpacity);
            }
        });
        timer.start();
    }

    public static void closeWindow(JFrame window) {
        final float startOpacity = window.getOpacity();
        final int[] frame = {0};

        Timer timer = new Timer(FPS_DELAY, null);
        timer.addActionListener(e -> {
            frame[0]++;
            if (frame[0] >= 15) {
                ((Timer) e.getSource()).stop();
                window.dispose();
                System.exit(0);
            } else {
                float progress = (float) frame[0] / 15;
                window.setOpacity(Math.max(0, startOpacity * (1 - progress)));
            }
        });
        timer.start();
    }

    public static boolean toggleMaximize(JFrame window) {
        if ((window.getExtendedState() & Frame.ICONIFIED) != 0) {
            window.setExtendedState(Frame.NORMAL);
        }

        GraphicsConfiguration config = window.getGraphicsConfiguration();
        Insets screenInsets = window.getToolkit().getScreenInsets(config);
        Rectangle usableBounds = new Rectangle(
                config.getBounds().x + screenInsets.left,
                config.getBounds().y + screenInsets.top,
                config.getBounds().width - (screenInsets.left + screenInsets.right),
                config.getBounds().height - (screenInsets.top + screenInsets.bottom)
        );

        boolean isMaximized = (window.getExtendedState() & Frame.MAXIMIZED_BOTH) != 0;

        if (!isMaximized) {
            lastNormalBounds = window.getBounds();
            window.setMaximizedBounds(usableBounds);
            animateToggle(window, lastNormalBounds, usableBounds, true);
            return true;
        } else {
            if (lastNormalBounds == null) {
                lastNormalBounds = new Rectangle(usableBounds.x + 100, usableBounds.y + 100, 800, 600);
            }
            animateToggle(window, window.getBounds(), lastNormalBounds, false);
            return false;
        }
    }

    private static void animateToggle(JFrame window, Rectangle from, Rectangle to, boolean goMax) {
        window.setExtendedState(Frame.NORMAL);

        final int[] frame = {0};
        final int totalFrames = 15;

        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            frame[0]++;
            float progress = (float) frame[0] / totalFrames;
            float ease = 1 - (float) Math.pow(1 - progress, 3);

            int x = (int) (from.x + (to.x - from.x) * ease);
            int y = (int) (from.y + (to.y - from.y) * ease);
            int w = (int) (from.width + (to.width - from.width) * ease);
            int h = (int) (from.height + (to.height - from.height) * ease);

            window.setBounds(x, y, w, h);

            if (frame[0] >= totalFrames) {
                ((Timer) e.getSource()).stop();

                if (goMax) {
                    window.setExtendedState(Frame.MAXIMIZED_BOTH);
                } else {
                    window.setExtendedState(Frame.NORMAL);
                    window.setBounds(to);
                }
            }
        });
        timer.start();
    }
}