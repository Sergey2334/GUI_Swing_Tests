package GUI_Tests.Root;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Entities.PowerUps.PowerUp;
import GUI_Tests.Managers.*;
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
    private AIManager aiManager = new AIManager(this);
    private final java.util.List<PowerUp> activePowerUps = new java.util.concurrent.CopyOnWriteArrayList<>();

    // NEW: Accessing our Singleton SoundManager
    private final SoundManager soundManager = SoundManager.getInstance();

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
    private String statusMessage = "";


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

    // --- INPUT API ---
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
            if (this.setupManager.getCurrentRow() == MyUtils.SETUP_START) {
                this.currentState = State.PLAYING;
                this.restartGame();
            }
        } else if (this.currentState == State.WIN) {
            this.soundManager.stopAll();
            this.currentState = State.SETUP;
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

    public java.util.List<PowerUp> getActivePowerUps() {
        return this.activePowerUps;
    }

    public void backToMenu() {
        if (this.currentState != State.MENU) {
            this.soundManager.stopAll();
        }
        this.soundManager.loop("bg_music");

        this.currentState = State.MENU;
        this.isPaused = false;
        this.isCountingDown = false;
        this.isDeathPause = false;
    }

    public void togglePause() {
        if (this.isCountingDown) return;
        this.isPaused = !this.isPaused;
        // Pause/Resume background music logic could go here if desired
    }

    private void restartRound() {
        this.isPaused = true;
        this.isCountingDown = true;
        this.countdownVal = MyUtils.COUNTDOWN_START_VAL;
        this.arenaInset = MyUtils.GAME_ARENA_INSET;
        this.activePowerUps.clear();

        this.player1.setColor(this.gameSettings.getP1Color());
        this.player1.setSpeed(this.gameSettings.getSpeed());
        this.player1.updateTexture();
        this.player2.setColor(this.gameSettings.getP2Color());
        this.player2.setSpeed(this.gameSettings.getSpeed());
        this.player2.updateTexture();
        this.player2.setAI(this.gameSettings.isVsAI());

        this.player1.respawn();
        this.player2.respawn();

        new Thread(() -> {
            if (this.currentState == State.PLAYING)
            {
                try {
                    this.soundManager.stopAll();
                    this.soundManager.play("countdown");

                    while (this.countdownVal >= 0) {
                        this.repaint();
                        Thread.sleep(1000);
                        this.countdownVal--;
                    }

                    // Start music when round actually begins
                    this.soundManager.loop("game_music");

                    this.isPaused = false;
                    this.isCountingDown = false;
                    this.repaint();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    public void restartGame() {
        this.scoreManager.resetScores();
        this.scoreManager.setWinLimit(this.gameSettings.getWinScore());
        this.gameTime = 0;
        this.effectManager.clear();
        this.soundManager.stopAll();
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

    private void handleDeath(boolean p1Died, boolean p2Died) {
        this.triggerDeathEffects();
        this.isDeathPause = true;

        this.soundManager.stopAll();

        if (p1Died && p2Died) {
            this.soundManager.play("crush_mutual");
            this.effectManager.addText("MUTUAL DESTRUCTION", getWidth() / 2.0, getHeight() / 3.0, Color.RED, 2000);
            this.effectManager.createExplosion(this.player1.getPlayerX(), this.player1.getPlayerY(), this.player1.getColor());
            this.effectManager.createExplosion(this.player2.getPlayerX(), this.player2.getPlayerY(), this.player2.getColor());
        } else {
            this.soundManager.play("crush");
            if (p1Died) {
                this.scoreManager.addScore(2);
                this.effectManager.addText("P2 SCORES", getWidth() / 2.0, getHeight() / 3.0, this.player2.getColor(), 2000);
                this.effectManager.createExplosion(this.player1.getPlayerX(), this.player1.getPlayerY(), this.player1.getColor());
            } else {
                this.scoreManager.addScore(1);
                this.effectManager.addText("P1 SCORES", getWidth() / 2.0, getHeight() / 3.0, this.player1.getColor(), 2000);
                this.effectManager.createExplosion(this.player2.getPlayerX(), this.player2.getPlayerY(), this.player2.getColor());
            }
        }

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                this.isDeathPause = false;
                if (this.scoreManager.hasWinner()) {
                    this.soundManager.loop("app_start");
                    this.currentState = State.WIN;
                } else {
                    this.restartRound();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void checkPowerUpCollisions() {
        for (PowerUp p : this.activePowerUps) {
            if (!p.isCollected()) {
                Rectangle player1Rect = new Rectangle((int) this.player1.getPlayerX(), (int) this.player1.getPlayerY(), 20, 20);
                Rectangle player2Rect = new Rectangle((int) this.player2.getPlayerX(), (int) this.player2.getPlayerY(), 20, 20);
                Rectangle powerUpRect = new Rectangle(p.getX(), p.getY(), 20, 20);

                if (player1Rect.intersects(powerUpRect)) {
                    p.applyEffect(this.player1);
                    this.effectManager.addText(p.getName(), p.getX(), p.getY(), this.player1.getColor(), 1000);
                } else if (player2Rect.intersects(powerUpRect)) {
                    p.applyEffect(this.player2);
                    this.effectManager.addText(p.getName(), p.getX(), p.getY(), this.player2.getColor(), 1000);
                }
            }
        }
        this.activePowerUps.removeIf(PowerUp::isCollected);
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
                this.update();
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
        this.aiManager.updateAI(this.player2);
        this.gameTime += (1000 / this.FPS);
        this.player1.move(this.gameTime);
        this.player2.move(this.gameTime);
        this.checkAllCollisions();
    }

    private void checkAllCollisions() {
        int offset = MyUtils.PLAYER_TILE_SIZE / 2;
        java.awt.geom.Line2D.Double p1Path = new java.awt.geom.Line2D.Double(
                this.player1.getLastPointX() + offset, this.player1.getLastPointY() + offset,
                this.player1.getPlayerX() + offset, this.player1.getPlayerY() + offset
        );
        java.awt.geom.Line2D.Double p2Path = new java.awt.geom.Line2D.Double(
                this.player2.getLastPointX() + offset, this.player2.getLastPointY() + offset,
                this.player2.getPlayerX() + offset, this.player2.getPlayerY() + offset
        );

        boolean p1Died = (this.player1.getPlayerTrail().checkCollision(p1Path, true) ||
                this.player2.getPlayerTrail().checkCollision(p1Path, false) ||
                this.isOutOfBounds(this.player1)) && !this.player1.isInvincible();

        boolean p2Died = (this.player2.getPlayerTrail().checkCollision(p2Path, true) ||
                this.player1.getPlayerTrail().checkCollision(p2Path, false) ||
                this.isOutOfBounds(this.player2)) && !this.player2.isInvincible();

        if (p1Died || p2Died) {
            this.handleDeath(p1Died, p2Died);
        }
        this.checkPowerUpCollisions();
    }

    public boolean isPositionDangerous(java.awt.geom.Rectangle2D.Double bounds) {
        if (bounds.x < this.arenaInset ||
                bounds.x > this.getWidth() - this.arenaInset - bounds.width ||
                bounds.y < this.arenaInset ||
                bounds.y > this.getHeight() - this.arenaInset - bounds.height) {
            return true;
        }
        java.awt.geom.Line2D.Double scanPath = new java.awt.geom.Line2D.Double(
                bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height
        );
        return this.player1.getPlayerTrail().checkCollision(scanPath, false) ||
                this.player2.getPlayerTrail().checkCollision(scanPath, false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (this.shakeMagnitude > 0) {
            g2.translate((Math.random() * 2 - 1) * this.shakeMagnitude, (Math.random() * 2 - 1) * this.shakeMagnitude);
        }
        switch (this.currentState) {
            case MENU -> MyUtils.drawMainMenu(g2, this, this.shakeMagnitude, this.logoHue);
            case SETUP -> this.setupManager.draw(g2, this.getWidth(), this.getHeight());
            case PLAYING, WIN -> {
                this.drawGameLevel(g2);
                if (this.isPaused && !this.isCountingDown) {
                    MyUtils.drawPauseOverlay(g2, this.getWidth(), this.getHeight());
                }
            }
        }
        this.effectManager.draw(g2);
    }

    private void drawGameLevel(Graphics2D g2) {
        MyUtils.drawGrid(g2, this);
        for (PowerUp p : this.activePowerUps) p.draw(g2);
        this.player1.draw(g2, this.gameTime);
        this.player2.draw(g2, this.gameTime);
        this.effectManager.draw(g2);
        MyUtils.drawVoidWalls(g2, this.getWidth(), this.getHeight(), this.arenaInset);
        MyUtils.drawScores(g2, this.getWidth(), this.getHeight(),
                this.scoreManager.getP1Score(), this.scoreManager.getP2Score(),
                this.player1.getTransparentColor(), this.player2.getTransparentColor());
        if (this.isCountingDown) MyUtils.drawCountdown(g2, this.getWidth(), this.getHeight(), this.countdownVal);
        if (this.currentState == State.WIN) this.drawWinScreen(g2);
    }

    private void drawWinScreen(Graphics2D g2) {
        String winner = this.scoreManager.getWinnerName();
        Color winColor = (winner.equals(MyUtils.PLAYER1_NAME)) ? this.player1.getColor() : this.player2.getColor();
        MyUtils.drawWinScreen(g2, this, winner, winColor, this.logoHue);
    }

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
        new Thread(new PowerUpSpawner(this)).start();
        this.gameThread.start();
        this.soundManager.loop("app_start");
    }
}