package GUI_Tests;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameLauncher extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;
    private final int FPS = MyUtils.GAME_FPS;
    private boolean isPaused;


    // TEST
    private boolean[][] gameGrid;
    private int panelWidth;
    private int panelHeight;
    private Player player1;
    private Player player2;
    private int player1Score;
    private int player2Score;
    private double arenaInset = MyUtils.GAME_ARENA_INSET;
    // TEST
    private int opacity = 0;
    private int countdownVal = 0;
    private boolean isCountingDown = false;
    private long gameTime = 0;
    private int shakeMagnitude = 0;
    private Timer shakeTimer;


    public GameLauncher() {
        this.initialize();
        this.initializePlayers();
    }


    private void initialize() {
        this.setDoubleBuffered(true); // Should be on by default, but good to force
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT));
        this.setLayout(null); // Keep null for absolute player movement
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setFocusable(true);
        this.setupInput();
        this.isPaused = false;

        this.setVisible(true);
    }

    private void initializePlayers() {
        int centerX = MyUtils.GAME_MIN_WIDTH / 2;
        int p1Y = MyUtils.GAME_MIN_HEIGHT / 4;
        int p2Y = (MyUtils.GAME_MIN_HEIGHT / 4) * 3;

        this.player1 = new Player(centerX, p1Y, MyUtils.COLOR_TRON1, MyUtils.DIRECTION_DOWN, MyUtils.GAME_SPEED_MEDIUM);
        this.player2 = new Player(centerX, p2Y, MyUtils.COLOR_TRON2, MyUtils.DIRECTION_UP, MyUtils.GAME_SPEED_MEDIUM);
    }

    public void startGame() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void togglePause() {
        this.isPaused = !this.isPaused || this.isCountingDown;
    }

//    public long getGameTime() {
//        return this.gameTime;
//    }

    private void restartGame() {
        this.isPaused = true;
        this.isCountingDown = true;
        this.countdownVal = 3;
        this.opacity = 0;

        // Reset Player positions and clear trails immediately
        this.player1.respawn((int) this.player1.getPlayerStartX(), (int) this.player1.getPlayerStartY(), this.player1.getStartDirection());
        this.player2.respawn((int) this.player2.getPlayerStartX(), (int) this.player2.getPlayerStartY(), this.player2.getStartDirection());
        this.arenaInset = MyUtils.GAME_ARENA_INSET;

        // Create a timer that ticks every 1 second
        Timer timer = new Timer(1000, null);
        timer.addActionListener(e -> {
            this.countdownVal--;
            if (this.countdownVal <= 0) {
                this.isPaused = false;
                this.isCountingDown = false;
                timer.stop();
            }
            repaint(); // Force a redraw to show the new number
        });
        timer.start();
    }

    private void triggerDeathEffects() {
        this.shakeMagnitude = 15; // Starting shake strength

        if (this.shakeTimer != null && this.shakeTimer.isRunning()) this.shakeTimer.stop();

        this.shakeTimer = new Timer(30, e -> {
            this.shakeMagnitude -= 2; // Gradually reduce shake
            if (this.shakeMagnitude <= 0) {
                this.shakeMagnitude = 0;
                ((Timer)e.getSource()).stop();
            }
            repaint();
        });
        this.shakeTimer.start();
    }



    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / this.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        // TEST - If Resizing Window While isRunning
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Recalculate grid or center players if needed
                repaint();
            }
        });

        while (this.isRunning) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            // TEST - Repaint, Window Flickers
            if (delta >= 1) {
                update();
                // Ensure the UI update happens on the correct thread
                SwingUtilities.invokeLater(() -> repaint());
                delta--;
            }
        }
    }

    private void update() {
        if (this.isPaused) return;
        this.gameTime += (1000 / this.FPS);
        System.out.println("GameTime: " + this.gameTime);

        int w = this.getWidth();
        int h = this.getHeight();

        // 1. Move both players first
        this.player1.move(w, h, this.gameTime);
        this.player2.move(w, h, this.gameTime);

        // 2. Update the shrinking arena
        this.arenaInset += MyUtils.GAME_ARENA_INSET_SHRINK_SPEED;

        // 3. Define boundaries
        double leftWall = this.arenaInset;
        double rightWall = w - this.arenaInset - MyUtils.PLAYER_TILE_SIZE;
        double topWall = this.arenaInset;
        double bottomWall = h - this.arenaInset - MyUtils.PLAYER_TILE_SIZE;

        // 4. Determine collision flags (Check everything BEFORE restarting)
        boolean p1WallHit = this.player1.getPlayerX() < leftWall || this.player1.getPlayerX() > rightWall ||
                this.player1.getPlayerY() < topWall || this.player1.getPlayerY() > bottomWall;

        boolean p2WallHit = this.player2.getPlayerX() < leftWall || this.player2.getPlayerX() > rightWall ||
                this.player2.getPlayerY() < topWall || this.player2.getPlayerY() > bottomWall;

        boolean p1TrailHit = this.player1.checkCollision(this.player2.getTrailSegments());
        boolean p2TrailHit = this.player2.checkCollision(this.player1.getTrailSegments());

        boolean p1Died = p1WallHit || p1TrailHit;
        boolean p2Died = p2WallHit || p2TrailHit;

        // 5. Handle the outcome
        if (p1Died || p2Died) {
            // TEST - Death Effect
            this.triggerDeathEffects();

            if (p1Died && p2Died) {
                // It's a draw!
                player1.killPlayer();
                player2.killPlayer();
                System.out.println("Mutual Destruction!");
            } else if (p1Died) {
                if (p1WallHit) {
                    this.player1.subScore();
                }
                this.player1.killPlayer();
                this.player2.addScore();
            } else {
                if (p2WallHit) {
                    this.player2.subScore();
                }
                this.player2.killPlayer();
                this.player1.addScore();
            }

            this.restartGame();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Better Options For Graphics :D , Google Said Always Do It
        // This makes the lines smooth and "high def"
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Google Suggestion :D

        // TEST - APPLY SHAKE
        if (shakeMagnitude > 0) {
            int offsetX = (int) (Math.random() * shakeMagnitude * 2 - shakeMagnitude);
            int offsetY = (int) (Math.random() * shakeMagnitude * 2 - shakeMagnitude);
            g2.translate(offsetX, offsetY); // Shifts everything drawn after this!
        }

        // TEST - Draw Grid
        MyUtils.drawGrid(g2, this);
        // TEST - Draw Center Point
        MyUtils.drawCenterPoint(g2, this);
        // TEST - Draw Border
        g2.setColor(new Color(255, 0, 0, Math.min(this.opacity++ / 10, 255))); // Semi-transparent red
        g2.setStroke(new BasicStroke(5));
        int safeX = (int) this.arenaInset;
        int safeY = (int) this.arenaInset;
        int safeW = (int) (this.getWidth() - (arenaInset * 2));
        int safeH = (int) (this.getHeight() - (arenaInset * 2));
        g2.drawRect(safeX, safeY, safeW, safeH);

        // TEST - Draw Players
        player1.draw(g2, this.gameTime);
        player2.draw(g2, this.gameTime);

        // TEST - Draw Pause
        if (this.isPaused) {
            g2.setColor(new Color(0, 0, 0, 150)); // Darken the screen
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());

            if (!this.isCountingDown) {
                g2.setColor(MyUtils.COLOR_TRON1);
                g2.setFont(new Font("Arial", Font.BOLD, 50));
                g2.drawString("PAUSED", getWidth() / 2 - 100, getHeight() / 2);
            }
        }

        // TEST - Draw Score
        g2.setFont(new Font("Agency FB", Font.BOLD, 100));
        g2.setColor(MyUtils.COLOR_TRON1_TRANSPARENT);
        g2.drawString(player1.getPlayerScore() + "", 50, this.getHeight() / 2);

        g2.setColor(MyUtils.COLOR_TRON2_TRANSPARENT);
        String p2ScoreText = player2.getPlayerScore() + "";
        int p2TextWidth = g2.getFontMetrics().stringWidth(p2ScoreText);
        g2.drawString(p2ScoreText, this.getWidth() - p2TextWidth - 50, this.getHeight() / 2);

        // TEST - Draw CountDown
        if (this.isCountingDown) {
            g2.setColor(MyUtils.COLOR_TRON1);
            g2.setFont(new Font("Agency FB", Font.BOLD, 100));

            String text = (this.countdownVal > 0) ? String.valueOf(this.countdownVal) : "GO!";

            // Center the text
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = (getHeight() / 2) + (fm.getAscent() / 4);

            g2.drawString(text, x, y);
        }
    }

    private void setupInput() {
        InputMap im = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        im.put(KeyStroke.getKeyStroke("W"), "p1Up");
        im.put(KeyStroke.getKeyStroke("A"), "p1Left");
        im.put(KeyStroke.getKeyStroke("S"), "p1Down");
        im.put(KeyStroke.getKeyStroke("D"), "p1Right");

        im.put(KeyStroke.getKeyStroke("UP"), "p2Up");
        im.put(KeyStroke.getKeyStroke("LEFT"), "p2Left");
        im.put(KeyStroke.getKeyStroke("DOWN"), "p2Down");
        im.put(KeyStroke.getKeyStroke("RIGHT"), "p2Right");

        im.put(KeyStroke.getKeyStroke("P"), "pauseGame");


        // Player 1
        am.put("p1Up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection(MyUtils.DIRECTION_UP);
            }
        });

        am.put("p1Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection(MyUtils.DIRECTION_LEFT);
            }
        });

        am.put("p1Down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection(MyUtils.DIRECTION_DOWN);
            }
        });

        am.put("p1Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player1.setDirection(MyUtils.DIRECTION_RIGHT);
            }
        });


        // Player 2
        am.put("p2Up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.setDirection(MyUtils.DIRECTION_UP);
            }
        });

        am.put("p2Left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.setDirection(MyUtils.DIRECTION_LEFT);
            }
        });

        am.put("p2Down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.setDirection(MyUtils.DIRECTION_DOWN);
            }
        });

        am.put("p2Right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player2.setDirection(MyUtils.DIRECTION_RIGHT);
            }
        });


        // Pause Game
        am.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
    }
}