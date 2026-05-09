package GUI_Tests.MainWinodw.TitleBar;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;

public class TitleText extends JLabel {
    public TitleText(String windowTitle) {
        this.initialize(windowTitle);
    }

    private void initialize(String windowTitle) {
        this.setText(windowTitle);
        MyUtils.setTitleBarTextIcon(this);

        this.setBorder(MyUtils.PADDING_STANDARD);

        this.setForeground(MyUtils.COLOR_TRON1);
        this.setHorizontalAlignment(JLabel.LEFT); // Usually looks better with left padding
        this.setVerticalAlignment(JLabel.CENTER);

        this.setHorizontalTextPosition(JLabel.RIGHT);
        this.setVerticalTextPosition(JLabel.CENTER);

        this.setFont(MyUtils.FONT_TRON1);

        this.setIconTextGap(5); // Spacing between the Icon and the Text
        this.setVisible(true);
    }
}