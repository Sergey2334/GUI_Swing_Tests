package GUI_Tests;

import javax.swing.*;
import java.awt.*;

public class TitleBarWindowTitle extends JPanel {
    private TitleBarTitleText titleBarTitleText;

    public TitleBarWindowTitle(String windowTitle) {
        this.initialize(windowTitle);
    }

    private void initialize(String windowTitle) {
        this.setLayout(new FlowLayout());

        // TEST
//        this.setBorder(MyUtils.BORDER_TEST_RED);
        this.setBackground(MyUtils.COLOR_TRANSPARENT);

        this.titleBarTitleText = new TitleBarTitleText(windowTitle);

        this.add(titleBarTitleText);
        this.setVisible(true);
    }
}
