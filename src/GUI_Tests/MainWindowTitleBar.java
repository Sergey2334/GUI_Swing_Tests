package GUI_Tests;

import javax.swing.*;
import java.awt.*;

public class MainWindowTitleBar extends JPanel {

    private TitleBarWindowTitle titleBarWindowTitle;
    private TitleBarWindowControls titleBarWindowControls;


    public MainWindowTitleBar(String windowTitle) {
        this.initialize(windowTitle);
    }

    private void initialize(String windowTitle) {
        this.setLayout(new BorderLayout());
        MyUtils.addDoubleClickOnTitleBarForFullScreen(this);

        // TEST
//        this.setBorder(MyUtils.BORDER_TEST_BLUE);
        this.setBackground(MyUtils.COLOR_BLACK1);

        this.titleBarWindowTitle = new TitleBarWindowTitle(windowTitle);
        this.titleBarWindowControls = new TitleBarWindowControls();
        this.add(titleBarWindowTitle, BorderLayout.WEST);
        this.add(titleBarWindowControls, BorderLayout.EAST);

        this.setOpaque(false);
        this.setVisible(true);
    }
}
