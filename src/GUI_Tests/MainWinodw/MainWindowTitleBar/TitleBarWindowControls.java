package GUI_Tests.MainWinodw.MainWindowTitleBar;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TitleBarWindowControls extends JPanel {
    private final TitleBarWindowControlsButton minimizeButton =
            new TitleBarWindowControlsButton(0, "—", e ->
            {
                MyUtils.minimizeWindow(Objects.requireNonNull(MyUtils.getWindow(this.minimizeButton)));
            });
    private final TitleBarWindowControlsButton maximizeButton =
            new TitleBarWindowControlsButton(1, "\uD83D\uDDD6", e ->
            {
                MyUtils.toggleMaximize2(Objects.requireNonNull(MyUtils.getWindow(this.maximizeButton)));
            });
    private final TitleBarWindowControlsButton closeButton =
            new TitleBarWindowControlsButton(2, "X", e ->
            {
                MyUtils.closeWindow(Objects.requireNonNull(MyUtils.getWindow(this.closeButton)));
            });

    public TitleBarWindowControls() {
        this.initialize();
    }

    private void initialize() {
        this.setLayout(new FlowLayout());

        // TEST
        this.setBackground(MyUtils.COLOR_TRANSPARENT);
//        this.setBorder(MyUtils.BORDER_TEST_RED);
//        this.setForeground(MyUtils.COLOR_TEST_BLUE);

        this.add(minimizeButton);
        this.add(maximizeButton);
        this.add(closeButton);

        this.setVisible(true);
    }
}
