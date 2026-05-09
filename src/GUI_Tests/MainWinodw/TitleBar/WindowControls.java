package GUI_Tests.MainWinodw.TitleBar;

import GUI_Tests.Utilities.MyUtils;
import javax.swing.*;
import java.awt.*;

public class WindowControls extends JPanel {
    // Ensure "TitleBarWindowControlsButton" matches the filename TitleBarWindowControlsButton.java
    private final ControlButton minimizeButton =
            new ControlButton(0, "—", e -> this.handleAction("min"));

    private final ControlButton maximizeButton =
            new ControlButton(1, "\uD83D\uDDD6", e -> this.handleAction("max"));

    private final ControlButton closeButton =
            new ControlButton(2, "X", e -> this.handleAction("close"));

    public WindowControls() {
        this.initialize();
    }

    private void initialize() {
        this.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        this.setOpaque(false);

        this.add(this.minimizeButton);
        this.add(this.maximizeButton);
        this.add(this.closeButton);
    }

    private void handleAction(String type) {
        JFrame parent = MyUtils.getWindow(this);
        if (parent == null) return;

        switch (type) {
            case "min"   -> MyUtils.minimizeWindow(parent);
            case "max"   -> MyUtils.toggleMaximize(parent);
            case "close" -> MyUtils.closeWindow(parent);
        }
    }
}