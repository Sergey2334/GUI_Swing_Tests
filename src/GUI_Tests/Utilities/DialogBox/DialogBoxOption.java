package GUI_Tests.Utilities.DialogBox;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class DialogBoxOption extends JButton {
    private String optionTitle;
    private int optionValue;

    public DialogBoxOption(String optionTitle, int optionValue) {
        this.optionTitle = optionTitle;
        this.optionValue = optionValue;

        this.initialize();
    }

    public void initialize() {
        this.setFont(MyUtils.FONT_TRON1);
        this.setText(optionTitle);
        this.setBorder(null);
        this.setBackground(MyUtils.COLOR_BLACK1_TRANSPARENT2);
        this.setForeground(MyUtils.COLOR_TRON1);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        this.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        this.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        this.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        this.setFocusable(true);
        this.setOpaque(true);

        MyUtils.addButtonHoverColorChangeEffect(this, MyUtils.COLOR_TRON2, MyUtils.COLOR_TRON1);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }
        });

        this.setVisible(true);
    }

    public int getOptionValue() {
        return this.optionValue;
    }
}