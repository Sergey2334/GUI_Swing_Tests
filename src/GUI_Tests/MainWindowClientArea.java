package GUI_Tests;

import javax.swing.*;

public class MainWindowClientArea extends JPanel {
    public MainWindowClientArea() {
        this.initialize();
    }

    private void initialize() {
        // TEST
//        this.setBorder(MyUtils.BORDER_TEST_RED);
//        this.setForeground(MyUtils.COLOR_TEST_BLUE);

        this.setBackground(MyUtils.COLOR_TRANSPARENT);

        this.setSize(MyUtils.WINDOW_MIN_WIDTH, MyUtils.WINDOW_MIN_HEIGHT);

        JButton b = new JButton("Client Area Button");
        this.add(b);

        this.setOpaque(false);
        this.setVisible(true);
    }
}