package GUI_Tests;

import javax.swing.*;

public class TitleBarTitleText extends JLabel {
    public TitleBarTitleText(String windowTitle) {
        this.initialize(windowTitle);
    }

    private void initialize(String windowTitle) {
        this.setText(windowTitle);

        MyUtils.setTitleBarTextIcon(this);


        //TEST
//        this.setBorder(MyUtils.BORDER_TEST_RED);
//        this.setBackground(MyUtils.COLOR_TRANSPARENT); // JLabel need setOpaque(true)
//        this.setOpaque(true);
        this.setForeground(MyUtils.COLOR_TRON1);


        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalAlignment(JLabel.CENTER);

        this.setHorizontalTextPosition(JLabel.RIGHT);
        this.setVerticalTextPosition(JLabel.CENTER);

        this.setFont(MyUtils.FONT_TRON1);

        // Optional
        // this.setIconTextGap(25);

        this.setVisible(true);
    }
}