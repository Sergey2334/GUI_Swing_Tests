package GUI_Tests.Utilities.DialogBox;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DialogBox extends JDialog {
    private String dialogBoxMessage;
    private DialogBoxOption[] dialogBoxOptions;

    private int selectedValue = MyUtils.DIALOG_BOX_NOTHING;

    public DialogBox(String dialogBoxMessage, DialogBoxOption[] dialogBoxOptions, JFrame parentFrame) {
        this.dialogBoxMessage = dialogBoxMessage;
        this.dialogBoxOptions = dialogBoxOptions;

        this.initialize(parentFrame);
    }

    private void initialize(JFrame parentFrame) {
        this.setAlwaysOnTop(true);
        this.setModal(true);
        this.setUndecorated(true);
        this.setBackground(MyUtils.COLOR_TRANSPARENT);
//        MyUtils.applyRoundedCorners(this, MyUtils.ROUNDED_CORNERS_RADIUS);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.setSize(MyUtils.WINDOW_MIN_WIDTH / 3, MyUtils.WINDOW_MIN_HEIGHT / 3);
        this.setLocationRelativeTo(parentFrame);
        MyUtils.applyRoundedCornersDialogBox(this, MyUtils.ROUNDED_CORNERS_RADIUS_DIALOG_BOX);

        DialogBoxMessage dialogBoxMessage = new DialogBoxMessage(this.dialogBoxMessage);
        this.add(dialogBoxMessage, BorderLayout.NORTH);

        if (dialogBoxOptions != null) {
            JPanel dialogOptionsPanel = new JPanel();
            dialogOptionsPanel.setOpaque(true);
            dialogOptionsPanel.setBackground(MyUtils.COLOR_BLACK1_TRANSPARENT3);

            // TEST GridBagLayout :D
            dialogOptionsPanel.setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(10, 10, 10, 10); // Margin

            for (int i = 0; i < this.dialogBoxOptions.length; i++) {
                int finalI = i;
                dialogBoxOptions[i].addActionListener(e -> {
                    this.selectedValue = dialogBoxOptions[finalI].getOptionValue();

                    this.dispose();
                });

                this.dialogBoxOptions[i].setVisible(true);
                dialogOptionsPanel.add(this.dialogBoxOptions[i], c); // Has Margin for GridBagLayout
            }

            dialogOptionsPanel.setVisible(true);
            this.add(dialogOptionsPanel, BorderLayout.CENTER);
        }

        // TEST - Add "Enter" and "ESC" For Quick Option Select
        JRootPane rootPane = this.getRootPane();
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("ENTER"), "confirmYes"
        );
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("ESCAPE"), "cancelNo"
        );
        rootPane.getActionMap().put("confirmYes", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (DialogBoxOption option : dialogBoxOptions) {
                    if (option.getOptionValue() == MyUtils.DIALOG_BOX_EXIT) {
                        selectedValue = MyUtils.DIALOG_BOX_EXIT;
                        dispose();
                        break;
                    }
                }
            }
        });
        rootPane.getActionMap().put("cancelNo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedValue = MyUtils.DIALOG_BOX_CANCEL;
                dispose();
            }
        });

        this.setVisible(true);
    }

    public int getSelectedValue() {
        return this.selectedValue;
    }
}
