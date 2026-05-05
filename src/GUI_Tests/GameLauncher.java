package GUI_Tests;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameLauncher extends JPanel implements Runnable {
    private Thread gameThread;
    private boolean isRunning;
    private final int FPS = MyUtils.GAME_FPS;


    // TEST
    private boolean[][] gameGrid;
    private int panelWidth;
    private int panelHeight;
    private Player player1;
    private Player player2;
    private double arenaInset = MyUtils.GAME_ARENA_INSET;


    public GameLauncher() {
        this.initialize();

        // Spawn relative to the GAME BOARD size, not the window size
        int boardWidth = MyUtils.GAME_MIN_WIDTH;
        int boardHeight = MyUtils.GAME_MIN_HEIGHT;

        this.player1 = new Player(boardWidth / 2, boardHeight / 4, MyUtils.COLOR_TRON1, MyUtils.DIRECTION_DOWN, MyUtils.GAME_SPEED_MEDIUM);
        this.player2 = new Player(boardWidth / 2, (boardHeight / 4) * 3, MyUtils.COLOR_TRON2, MyUtils.DIRECTION_UP, MyUtils.GAME_SPEED_MEDIUM);
    }


    private void initialize() {
        this.setPreferredSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT));
        this.setLayout(null); // Keep null for absolute player movement
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setFocusable(true);
        this.setupInput();

        this.setVisible(true);
    }

    public void startGame() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / this.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (this.isRunning) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    int counter1 = 0;
    int counter2 = 0;
    private void update() {
        // TEST
        player1.move(this.getWidth(), this.getHeight());
        player2.move(this.getWidth(), this.getHeight());


        int w = this.getWidth();
        int h = this.getHeight();

        double leftWall = arenaInset;
        double rightWall = w - arenaInset - MyUtils.PLAYER_TILE_SIZE;
        double topWall = arenaInset;
        double bottomWall = h - arenaInset - MyUtils.PLAYER_TILE_SIZE;

        this.arenaInset += MyUtils.GAME_ARENA_INSET_SHRINK_SPEED;

        // Player 1 Hits Wall
        if (player1.getPlayerX() < leftWall || player1.getPlayerX() > rightWall ||
                player1.getPlayerY() < topWall || player1.getPlayerY() > bottomWall) {
            System.out.println("P1 Crushed by the Arena!");
        }

        // Player 2 Hits Wall
        if (player2.getPlayerX() < leftWall || player2.getPlayerX() > rightWall ||
                player2.getPlayerY() < topWall || player2.getPlayerY() > bottomWall) {
            System.out.println("P2 Crushed by the Arena!");
        }

        // Player 1 checks if they hit their own trail OR Player 2's trail
        if (player1.checkCollision(player2.getTrailSegments())) {
            counter1++;
            System.out.println("Player 1 Crashed! , #" + counter1 + " Times!");
            //stopGame();
        }

        // Player 2 checks if they hit their own trail OR Player 1's trail
        if (player2.checkCollision(player1.getTrailSegments())) {
            counter2++;
            System.out.println("Player 2 Crashed! , #" + counter2 + " Times!");
            //stopGame();
        }

//        System.out.println(player1 + "\t" + player2);
    }

    // TEST
    int opacity = 0;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Better Options For Graphics :D , Google Said Always Do It
        // This makes the lines smooth and "high def"
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Google Suggestion :D

        // TEST - Draw Grid
        MyUtils.drawGrid(g2, this);
        // TEST - Draw Border
        g2.setColor(new Color(255, 0, 0, Math.min(this.opacity++, 255))); // Semi-transparent red
        g2.setStroke(new BasicStroke(5));
        // Draw the rectangle representing the safe zone
        int safeX = (int) this.arenaInset;
        int safeY = (int) this.arenaInset;
        int safeW = (int) (this.getWidth() - (arenaInset * 2));
        int safeH = (int) (this.getHeight() - (arenaInset * 2));

        g2.drawRect(safeX, safeY, safeW, safeH);
        // TEST - Draw Players
        player1.draw(g2);
        player2.draw(g2);
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
    }
}