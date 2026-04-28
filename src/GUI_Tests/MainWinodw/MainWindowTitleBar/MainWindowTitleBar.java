package GUI_Tests.MainWinodw.MainWindowTitleBar;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class MainWindowTitleBar extends JPanel {
    private final String name = "Main Window Title Bar";
    private TitleBarWindowTitle titleBarWindowTitle;
    private TitleBarWindowControls titleBarWindowControls;


    public MainWindowTitleBar(String windowTitle) {
        this.initialize(windowTitle);
        MyUtils.printSuccessfulInitialization(this.name);
    }

    private void initialize(String windowTitle) {
        this.setLayout(new BorderLayout());
        MyUtils.addDoubleClickOnTitleBarForFullScreen(this);

        this.titleBarWindowTitle = new TitleBarWindowTitle(windowTitle);
        this.titleBarWindowControls = new TitleBarWindowControls();
        this.add(titleBarWindowTitle, BorderLayout.WEST);
        this.add(titleBarWindowControls, BorderLayout.EAST);

        // TEST
        this.setOpaque(true);
        this.setBackground(MyUtils.COLOR_BLACK1_TRANSPARENT2);
//        this.setBorder(MyUtils.BORDER_TEST_BLUE);


        this.setVisible(true);
    }
}
