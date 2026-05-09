package GUI_Tests.Utilities.DialogBox;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class DialogBox extends JDialog {
    private final String message;
    private final DialogBoxOption[] options;
    private int selectedValue = MyUtils.DIALOG_BOX_NOTHING;

    public DialogBox(String message, DialogBoxOption[] options, JFrame parent) {
        super(parent, true); // Set modal and parent in super
        this.message = message;
        this.options = options;
        this.initialize();
    }

    private void initialize() {
        this.setUndecorated(true);
        this.setBackground(MyUtils.COLOR_TRANSPARENT);
        this.setSize(MyUtils.WINDOW_MIN_WIDTH / 3, MyUtils.WINDOW_MIN_HEIGHT / 3);
        this.setLocationRelativeTo(this.getOwner());
        this.setLayout(new BorderLayout());

        // Apply visual style
        MyUtils.applyRoundedCornersDialogBox(this, MyUtils.ROUNDED_CORNERS_RADIUS_DIALOG_BOX);

        // Add Components using modular methods
        this.add(new DialogBoxMessage(this.message), BorderLayout.NORTH);
        this.add(this.createOptionsPanel(), BorderLayout.CENTER);

        this.setupKeyBindings();
        this.setVisible(true);
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(MyUtils.COLOR_BLACK1_TRANSPARENT3);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        for (DialogBoxOption option : this.options) {
            // The DialogBox handles the click logic, NOT the button!
            option.addActionListener(e -> {
                this.selectedValue = option.getOptionValue();
                this.dispose();
            });
            panel.add(option, gbc);
        }
        return panel;
    }

    private void setupKeyBindings() {
        InputMap im = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getRootPane().getActionMap();

        // Confirm (ENTER)
        im.put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        am.put("confirm", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                DialogBox.this.selectedValue = MyUtils.DIALOG_BOX_EXIT; // Note: You'll need to use DialogBox.this.selectedValue if inside an anonymous class
                dispose();
            }
        });

        // Cancel (ESCAPE) - Use the full word "ESCAPE"
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        am.put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                DialogBox.this.selectedValue = MyUtils.DIALOG_BOX_CANCEL;
                dispose();
            }
        });
    }

    public int getSelectedValue() {
        return this.selectedValue;
    }
}