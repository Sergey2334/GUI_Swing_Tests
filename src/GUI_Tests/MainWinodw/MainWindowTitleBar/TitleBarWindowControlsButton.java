package GUI_Tests.MainWinodw.MainWindowTitleBar;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class TitleBarWindowControlsButton extends JButton {
    private int buttonId;

    public TitleBarWindowControlsButton(int buttonId, String text, ActionListener actionListener) {
        this.initialize(buttonId, text, actionListener);
    }

    private void initialize(int buttonId, String text, ActionListener actionListener) {
        this.buttonId = buttonId;
        this.addActionListener(actionListener);

        this.setText(text);
        this.setFocusable(true);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // TEST
        this.setBackground(MyUtils.COLOR_TRANSPARENT);
        this.setForeground(MyUtils.COLOR_TRON1);
//        this.setBorder(MyUtils.BORDER_TEST_GREEN);
        this.setFont(MyUtils.FONT_TRON2);
        this.setVisible(true);
    }
}
