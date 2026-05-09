package GUI_Tests.MainWinodw.TitleBar;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ControlButton extends JButton {
    private int buttonId;

    public ControlButton(int buttonId, String text, ActionListener actionListener) {
        this.initialize(buttonId, text, actionListener);
    }

    private void initialize(int buttonId, String text, ActionListener actionListener) {
        this.buttonId = buttonId;
        this.addActionListener(actionListener);

        this.setText(text);
        this.setFont(MyUtils.FONT_TRON2);
        this.setForeground(MyUtils.COLOR_TRON1);

        // Standard button "cleanup"
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setOpaque(false); // Keeps the title bar background visible
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Let the button manage its own hover colors based on MyUtils
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                ControlButton.this.setForeground(MyUtils.COLOR_TRON2);
                // Optional: add a slight background highlight
                ControlButton.this.setContentAreaFilled(true);
                ControlButton.this.setBackground(new Color(255, 255, 255, 30));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                ControlButton.this.setForeground(MyUtils.COLOR_TRON1);
                ControlButton.this.setContentAreaFilled(false);
            }
        });

        this.setVisible(true);
    }
}
