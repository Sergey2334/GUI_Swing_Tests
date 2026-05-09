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

        // P1
        this.bind(im, am, "W", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_UP));
        this.bind(im, am, "A", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_LEFT));
        this.bind(im, am, "S", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_DOWN));
        this.bind(im, am, "D", () -> this.game.getPlayer1().setDirection(MyUtils.DIRECTION_RIGHT));

        // P2
        this.bind(im, am, "UP",    () -> this.game.getPlayer2().setDirection(MyUtils.DIRECTION_UP));
        this.bind(im, am, "LEFT",  () -> this.game.getPlayer2().setDirection(MyUtils.DIRECTION_LEFT));
        this.bind(im, am, "DOWN",  () -> this.game.getPlayer2().setDirection(MyUtils.DIRECTION_DOWN));
        this.bind(im, am, "RIGHT", () -> this.game.getPlayer2().setDirection(MyUtils.DIRECTION_RIGHT));

        // System
        this.bind(im, am, "P", this.game::togglePause);
        this.bind(im, am, "E", this.game::handleEnterKey);
        this.bind(im, am, "ESCAPE", this.game::backToMenu);
        this.bind(im, am, "F11", () -> MyUtils.toggleFullScreen(MyUtils.getWindow(this.game)));

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