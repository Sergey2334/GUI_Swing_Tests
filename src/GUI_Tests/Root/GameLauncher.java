package GUI_Tests.Root;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Managers.EffectManager;
import GUI_Tests.Managers.ScoreManager;
import GUI_Tests.Managers.SetupManager;
import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class GameLauncher extends JPanel implements Runnable {
    // 1. Core Systems
    private Thread gameThread;
    private boolean isRunning;
    private boolean isPaused;
    private long gameTime = 0;
    private final int FPS = MyUtils.GAME_FPS;

    // 2. Modular Components
    private Player player1;
    private Player player2;
    private EffectManager effectManager;
    private final ScoreManager scoreManager = new ScoreManager();
    private GameSettings gameSettings = new GameSettings();
    private SetupManager setupManager = new SetupManager(this.gameSettings);

    // 3. Game State & Visuals
    public enum State {MENU, SETUP, PLAYING, WIN}

    private State currentState = State.MENU;
    private double arenaInset = MyUtils.GAME_ARENA_INSET;
    private int countdownVal = 0;
    private boolean isCountingDown = false;
    private float logoHue = 0.0f;
    private int shakeMagnitude = 0;
    private Timer shakeTimer;
    private boolean isDeathPause = false;
    private String statusMessage = ""; // Member variable

    public GameLauncher() {
        this.initializePanel();
        this.initializeSystems();
        this.initializePlayers();
    }

    private void initializePanel() {
        this.setPreferredSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT));
        this.setOpaque(true);
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.setupInput();
    }

    private void initializeSystems() {
        this.effectManager = new EffectManager();
    }

    private void initializePlayers() {
        int centerX = MyUtils.GAME_MIN_WIDTH / 2;
        int p1Y = MyUtils.GAME_MIN_HEIGHT / 4;
        int p2Y = (MyUtils.GAME_MIN_HEIGHT / 4) * 3;

        this.player1 = new Player(centerX, p1Y, MyUtils.COLOR_TRON1, MyUtils.DIRECTION_DOWN, MyUtils.GAME_SPEED_MEDIUM);
        this.player2 = new Player(centerX, p2Y, MyUtils.COLOR_TRON2, MyUtils.DIRECTION_UP, MyUtils.GAME_SPEED_MEDIUM);
    }

    // --- INPUT API (Called by InputHandler) ---
    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public void handleEnterKey() {
        if (this.currentState == State.MENU) {
            this.currentState = State.SETUP;
        } else if (this.currentState == State.SETUP) {
            if (this.setupManager.getCurrentRow() == 3) { // START button
                this.currentState = State.PLAYING;
                this.restartGame();
            }
        } else if (this.currentState == State.WIN) {
            this.currentState = State.PLAYING;
            this.restartGame();
        }
    }

    public void openSetup() {
        if (this.currentState == State.MENU) {
            this.currentState = State.SETUP;
        }
    }

    public State getCurrentState() {
        return this.currentState;
    }

    public SetupManager getSetupManager() {
        return this.setupManager;
    }


    public void backToMenu() {
        this.currentState = State.MENU;
    }

    public void togglePause() {
        // This flips the pause state (true to false, or false to true)
        if (this.isCountingDown)
        {
            return;
        }
        this.isPaused = !this.isPaused;
    }
    private void restartRound() {
        // 1. Initial State Setup
        this.isPaused = true;
        this.isCountingDown = true;
        this.countdownVal = MyUtils.COUNTDOWN_START_VAL;
        this.arenaInset = MyUtils.GAME_ARENA_INSET;

        // 2. APPLY CHOSEN SETTINGS (The OOP Injection)
        // Update Player 1
        this.player1.setColor(this.gameSettings.getP1Color());
        this.player1.setSpeed(this.gameSettings.getSpeed());
        // Update Player 2
        this.player2.setColor(this.gameSettings.getP2Color());
        this.player2.setSpeed(this.gameSettings.getSpeed());

        // 3. Reset Positions
        this.player1.respawn();
        this.player2.respawn();

        // 4. The Countdown Thread
        new Thread(() -> {
            try {
                while (this.countdownVal >= 0) {
                    this.repaint();
                    Thread.sleep(1000);
                    this.countdownVal--;
                }

                this.isPaused = false;
                this.isCountingDown = false;
                this.repaint();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    public void restartGame() {
        this.scoreManager.resetScores();

        this.scoreManager.setWinLimit(this.gameSettings.getWinScore());

        this.gameTime = 0;
        this.effectManager.clear();
        this.restartRound();
    }

    private void triggerDeathEffects() {
        this.shakeMagnitude = MyUtils.SHAKE_INITIAL_MAGNITUDE;
        if (this.shakeTimer != null) this.shakeTimer.stop();
        this.shakeTimer = new Timer(30, e -> {
            this.shakeMagnitude -= MyUtils.SHAKE_REDUCTION_STEP;
            if (this.shakeMagnitude <= 0) ((Timer) e.getSource()).stop();
        });
        this.shakeTimer.start();
    }

    private void drawGameLevel(Graphics2D g2) {
        MyUtils.drawGrid(g2, this);
        this.player1.draw(g2, this.gameTime);
        this.player2.draw(g2, this.gameTime);
        this.effectManager.draw(g2);

        MyUtils.drawVoidWalls(g2, this.getWidth(), this.getHeight(), this.arenaInset);

        // FIX: Pull scores from the Manager, not the Player objects
        MyUtils.drawScores(g2, this.getWidth(), this.getHeight(),
                this.scoreManager.getP1Score(), this.scoreManager.getP2Score(), player1.getTransparentColor(), player2.getTransparentColor());

        if (this.isCountingDown) {
            MyUtils.drawCountdown(g2, this.getWidth(), this.getHeight(), this.countdownVal);
        }

        if (this.currentState == State.WIN) {
            this.drawWinScreen(g2);
        }
    }


    private void drawWinScreen(Graphics2D g2) {
        // 1. Ask the manager for the winner's name
        String winner = this.scoreManager.getWinnerName();

        // 2. Determine color based on the winner
        Color winColor = (winner.equals(MyUtils.PLAYER1_NAME)) ? this.player1.getColor() : this.player2.getColor();

        // 3. Hand off the actual drawing to MyUtils
        MyUtils.drawWinScreen(g2, this, winner, winColor, this.logoHue);
    }


    // --- GAME LOOP ---
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / this.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (this.isRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                // 1. Always update logic, regardless of visibility
                this.update();

                // 2. Only request a repaint if the window is actually showing
                // This prevents the thread from "stacking up" paint requests while minimized
                if (this.isShowing()) {
                    SwingUtilities.invokeLater(this::repaint);
                }

                delta--;
            }
        }
    }

    private void update() {
        this.effectManager.update();
        this.logoHue = (this.logoHue + 0.005f) % 1.0f;

        if (this.currentState != State.PLAYING || this.isPaused || this.isDeathPause) return;

        this.arenaInset += this.gameSettings.getShrinkSpeed();

        this.gameTime += (1000 / this.FPS);
        this.player1.move(this.gameTime);
        this.player2.move(this.gameTime);
        this.checkAllCollisions();
    }


    private void checkAllCollisions() {
        int offset = MyUtils.PLAYER_TILE_SIZE / 2;

        // 1. Create movement lines (Head Paths) for both players
        java.awt.geom.Line2D.Double p1Path = new java.awt.geom.Line2D.Double(
                this.player1.getLastPointX() + offset, this.player1.getLastPointY() + offset,
                this.player1.getPlayerX() + offset, this.player1.getPlayerY() + offset
        );

        java.awt.geom.Line2D.Double p2Path = new java.awt.geom.Line2D.Double(
                this.player2.getLastPointX() + offset, this.player2.getLastPointY() + offset,
                this.player2.getPlayerX() + offset, this.player2.getPlayerY() + offset
        );

        // 2. Check Player 1 collisions
        boolean p1HitSelf = this.player1.getPlayerTrail().checkCollision(p1Path, true);
        boolean p1HitP2 = this.player2.getPlayerTrail().checkCollision(p1Path, false);
        boolean p1WallHit = this.isOutOfBounds(this.player1);

        // 3. Check Player 2 collisions
        boolean p2HitSelf = this.player2.getPlayerTrail().checkCollision(p2Path, true);
        boolean p2HitP1 = this.player1.getPlayerTrail().checkCollision(p2Path, false);
        boolean p2WallHit = this.isOutOfBounds(this.player2);

        // 4. Combine results
        boolean p1Died = p1HitSelf || p1HitP2 || p1WallHit;
        boolean p2Died = p2HitSelf || p2HitP1 || p2WallHit;

        if (p1Died || p2Died) {
            this.handleDeath(p1Died, p2Died);
        }
    }

    private void handleDeath(boolean p1Died, boolean p2Died) {
        this.triggerDeathEffects();
        this.isDeathPause = true;

        if (p1Died && p2Died) {
            // No one scores on mutual destruction
            this.effectManager.addText("MUTUAL DESTRUCTION", getWidth()/2.0, getHeight()/3.0, Color.RED, 2000);
            this.effectManager.createExplosion(this.player1.getPlayerX(), this.player1.getPlayerY(), this.player1.getColor());
            this.effectManager.createExplosion(this.player2.getPlayerX(), this.player2.getPlayerY(), this.player2.getColor());
        } else {
            if (p1Died) {
                this.scoreManager.addScore(2); // P2 Scores
                this.effectManager.addText("P2 SCORES", getWidth()/2.0, getHeight()/3.0, MyUtils.COLOR_TRON2, 2000);
                this.effectManager.createExplosion(this.player1.getPlayerX(), this.player1.getPlayerY(), this.player1.getColor());
            } else {
                this.scoreManager.addScore(1); // P1 Scores
                this.effectManager.addText("P1 SCORES", getWidth()/2.0, getHeight()/3.0, MyUtils.COLOR_TRON1, 2000);
                this.effectManager.createExplosion(this.player2.getPlayerX(), this.player2.getPlayerY(), this.player2.getColor());
            }
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                this.isDeathPause = false;

                // Check the manager for the win condition
                if (this.scoreManager.hasWinner()) {
                    this.currentState = State.WIN;
                } else {
                    this.restartRound();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    // --- RENDERING ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Handle Camera Shake
        if (this.shakeMagnitude > 0) {
            g2.translate((Math.random() * 2 - 1) * this.shakeMagnitude, (Math.random() * 2 - 1) * this.shakeMagnitude);
        }

        // 2. Render based on Current State
        switch (this.currentState) {
            case MENU ->
                    MyUtils.drawMainMenu(g2, this, this.shakeMagnitude, this.logoHue);

            case SETUP ->
                    this.setupManager.draw(g2, this.getWidth(), this.getHeight());

            case PLAYING, WIN -> {
                this.drawGameLevel(g2);
                // Draw Pause Overlay if the game is paused but NOT during the countdown
                if (this.isPaused && !this.isCountingDown) {
                    MyUtils.drawPauseOverlay(g2, this.getWidth(), this.getHeight());
                }
            }
        }

        // 3. Always draw global effects (floating text, explosions) on top
        this.effectManager.draw(g2);
    }

    // --- HELPER LOGIC ---

    private boolean isOutOfBounds(Player p) {
        return p.getPlayerX() < this.arenaInset ||
                p.getPlayerX() > this.getWidth() - this.arenaInset - MyUtils.PLAYER_TILE_SIZE ||
                p.getPlayerY() < this.arenaInset ||
                p.getPlayerY() > this.getHeight() - this.arenaInset - MyUtils.PLAYER_TILE_SIZE;
    }

    private void setupInput() {
        InputHandler handler = new InputHandler(this);
        handler.setupBindings();
    }

    public void startGame() {
        this.isRunning = true;
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }
}