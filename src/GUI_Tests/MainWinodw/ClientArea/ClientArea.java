package GUI_Tests.MainWinodw.ClientArea;

import GUI_Tests.GameLauncher;
import GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements.*;
import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClientArea extends JPanel {
    private final String name = "Client Area";

    // The collection of all modular background widgets
    private final List<VisualElement> backgroundElements = new ArrayList<>();
    private final GameLauncher gameLauncher;

    public ClientArea() {
        this.gameLauncher = new GameLauncher();
        this.initialize();
        this.setupBackgroundWidgets();
        this.startAnimationHeartbeat();

        MyUtils.printSuccessfulInitialization(this.name);
    }

    private void initialize() {
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setMinimumSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT));
        this.setLayout(new GridBagLayout());
        this.setDoubleBuffered(true);
        this.setOpaque(true);

        // Ensure the game launcher is solid
        this.gameLauncher.setOpaque(true);
        // Add the game on top of the background visuals
//        this.gameLauncher.setOpaque(false);

        this.add(this.gameLauncher, new GridBagConstraints());

        this.gameLauncher.startGame();
    }

    private void setupBackgroundWidgets() {
         this.backgroundElements.add(new GridWidget());
         this.backgroundElements.add(new RadarWidget());
         this.backgroundElements.add(new SystemStatusWidget());
         this.backgroundElements.add(new TerminalTextWidget());
         this.backgroundElements.add(new EasterEggWidget()); // :D
    }


    private void startAnimationHeartbeat() {
        // This timer handles the 'update' and 'repaint' for all widgets
        Timer timer = new Timer(30, e -> {
            for (VisualElement element : this.backgroundElements) {
                element.update();
            }
            this.repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 2. Draw all modular background elements (Radar, Blips, etc.)
        for (VisualElement element : this.backgroundElements) {
            element.draw(g2, this.getWidth(), this.getHeight());
        }

        g2.dispose();
    }
}