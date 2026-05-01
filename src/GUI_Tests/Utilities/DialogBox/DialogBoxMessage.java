package GUI_Tests.Utilities.DialogBox;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;

public class DialogBoxMessage extends JPanel {
    private String dialogMessage;

    public DialogBoxMessage(String dialogMessage) {
        this.dialogMessage = dialogMessage;
        this.initialize();
    }

    private void initialize() {
        this.setBackground(MyUtils.COLOR_BLACK1_TRANSPARENT3);
        JLabel titleLabel = new JLabel(dialogMessage);
        titleLabel.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setVerticalTextPosition(javax.swing.SwingConstants.CENTER);
        titleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        titleLabel.setFont(MyUtils.FONT_TRON1);
        titleLabel.setForeground(MyUtils.COLOR_TRON1);

        titleLabel.setVisible(true);

        this.add(titleLabel);
        this.setVisible(true);
    }
}