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
        // TEST
        this.setSize(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT);
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setLayout(new BorderLayout());
        this.setOpaque(true);

        // TEST

        GameLauncher gameLauncher = new GameLauncher();
        this.add(gameLauncher, BorderLayout.CENTER);
        gameLauncher.startGame();

        this.setVisible(true);
    }
}