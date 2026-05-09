package GUI_Tests.MainWinodw.TitleBar;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class TitleBar extends JPanel {
    private final String name = "Title Bar";
    private WindowControls windowControls;

    public TitleBar(String windowTitle) {
        this.initialize(windowTitle);
        MyUtils.printSuccessfulInitialization(this.name);
    }

    private void initialize(String windowTitle) {
        this.setLayout(new BorderLayout());
        this.setOpaque(false);

        // Add the Label directly! No need for the TitleBarWindowTitle wrapper.
        this.add(new TitleText(windowTitle), BorderLayout.WEST);

        // Add your controls
        this.add(new WindowControls(), BorderLayout.EAST);

        // Add maximize on double-click
        MyUtils.addDoubleClickOnTitleBarForFullScreen(this);
    }

    public void setVisibleState(boolean visible) {
        this.setVisible(visible);
        // This tells the parent to recalculate the layout because a component vanished/appeared
        if (this.getParent() != null) {
            this.getParent().revalidate();
        }
    }

}