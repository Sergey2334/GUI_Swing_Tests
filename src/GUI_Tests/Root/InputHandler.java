package GUI_Tests.Root;

import GUI_Tests.Utilities.MyUtils;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class InputHandler {
    private final GameLauncher game;

    public InputHandler(GameLauncher game) {
        this.game = game;
    }

    public void setupBindings() {
        InputMap im = this.game.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.game.getActionMap();

        // --- NAVIGATION & SYSTEM ---

        // E Key - Standard entry or Setup Trigger
        this.bind(im, am, "E", () -> {
            if (this.game.getCurrentState() == GameLauncher.State.MENU) {
                this.game.openSetup();
            } else {
                this.game.handleEnterKey();
            }
        });

        // ENTER Key - General confirmation
        this.bind(im, am, "ENTER", this.game::handleEnterKey);

        // ESCAPE - Back to Menu
        this.bind(im, am, "ESCAPE", this.game::backToMenu);

        // P - Toggle Pause
        this.bind(im, am, "P", this.game::togglePause);

        // F11 - Fullscreen
        this.bind(im, am, "F11", () -> MyUtils.toggleFullScreen(MyUtils.getWindow(this.game)));


        // --- DIRECTIONAL LOGIC (Context Sensitive) ---

        // UP
        this.bind(im, am, "UP", () -> {
            if (this.game.getCurrentState() == GameLauncher.State.SETUP) {
                this.game.getSetupManager().moveUp();
            } else {
                this.game.getPlayer2().setDirection(MyUtils.DIRECTION_UP);
            }
        });

        // DOWN
        this.bind(im, am, "DOWN", () -> {
            if (this.game.getCurrentState() == GameLauncher.State.SETUP) {
                this.game.getSetupManager().moveDown();
            } else {
                this.game.getPlayer2().setDirection(MyUtils.DIRECTION_DOWN);
            }
        });

        // LEFT (Cycle Settings Left)
        this.bind(im, am, "LEFT", () -> {
            if (this.game.getCurrentState() == GameLauncher.State.SETUP) {
                this.game.getSetupManager().cycle(-1);
            } else {
                this.game.getPlayer2().setDirection(MyUtils.DIRECTION_LEFT);
            }
        });

        // RIGHT (Cycle Settings Right)
        this.bind(im, am, "RIGHT", () -> {
            if (this.game.getCurrentState() == GameLauncher.State.SETUP) {
                this.game.getSetupManager().cycle(1);
            } else {
                this.game.getPlayer2().setDirection(MyUtils.DIRECTION_RIGHT);
            }
        });

        // --- PLAYER 1 (WASD) ---
        this.bind(im, am, "W", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_UP));
        this.bind(im, am, "A", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_LEFT));
        this.bind(im, am, "S", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_DOWN));
        this.bind(im, am, "D", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_RIGHT));
    }

    private void bind(InputMap im, ActionMap am, String key, Runnable action) {
        im.put(KeyStroke.getKeyStroke(key), key);
        am.put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }
}