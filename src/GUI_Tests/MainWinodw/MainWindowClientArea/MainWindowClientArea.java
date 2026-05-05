package GUI_Tests.MainWinodw.MainWindowClientArea;

import GUI_Tests.GameLauncher;
import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class MainWindowClientArea extends JPanel {
    private final String name = "Main Window Client Area";

    public MainWindowClientArea() {
        this.initialize();
        MyUtils.printSuccessfulInitialization(this.name);
    }

    private void initialize() {
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setLayout(new GridBagLayout()); // Use GridBag for perfect centering
        this.setOpaque(true);

        GameLauncher gameLauncher = new GameLauncher();
        // GridBagConstraints() with no arguments defaults to Center
        this.add(gameLauncher, new GridBagConstraints());

        gameLauncher.startGame();
        this.setVisible(true);
    }
}