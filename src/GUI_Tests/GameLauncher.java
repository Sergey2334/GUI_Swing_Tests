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

    public GameLauncher() {
        this.initialize();

        // TEST
        int widthMiddle = MyUtils.WINDOW_MIN_WIDTH;
        int heightMiddle = MyUtils.WINDOW_MIN_HEIGHT;
        this.player1 = new Player(widthMiddle, heightMiddle - MyUtils.PLAYER_TILE_SIZE * 5, MyUtils.COLOR_TRON1, MyUtils.DIRECTION_DOWN, MyUtils.GAME_SPEED_MEDIUM);
        this.player2 = new Player(widthMiddle, heightMiddle + MyUtils.PLAYER_TILE_SIZE * 5, MyUtils.COLOR_TRON2, MyUtils.DIRECTION_UP, MyUtils.GAME_SPEED_MEDIUM);
    }

    private void initialize() {
        this.setMinimumSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT));
        this.setLayout(null);
        // TEST
//        this.setBorder(BorderFactory.createLineBorder(MyUtils.COLOR_TEST_BLUE, 5));
        this.panelWidth = this.getWidth();
        this.panelHeight = this.getHeight();
        this.gameGrid = new boolean[this.panelWidth][this.panelHeight];
        this.setBackground(MyUtils.COLOR_BLACK1);
        this.setOpaque(true);
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

        if (player1.getPlayerX() < 0 || player1.getPlayerX() > this.getWidth() - MyUtils.PLAYER_TILE_SIZE ||
                player1.getPlayerY() < 0 || player1.getPlayerY() > this.getHeight() - MyUtils.PLAYER_TILE_SIZE) {

            System.out.println("Player 1 hit the border!");
            System.out.println("Player 2 Wins! (P1 hit the wall)");
            return; // Stop processing further logic this frame
        }

        // 2. Check Player 2 Border Collision
        if (player2.getPlayerX() < 0 || player2.getPlayerX() > this.getWidth() - MyUtils.PLAYER_TILE_SIZE ||
                player2.getPlayerY() < 0 || player2.getPlayerY() > this.getHeight() - MyUtils.PLAYER_TILE_SIZE) {

            System.out.println("Player 2 hit the border!");
            System.out.println("Player 1 Wins! (P2 hit the wall)");
            return;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Better Options For Graphics :D , Google Said Always Do It
        // This makes the lines smooth and "high def"
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Google Suggestion :D


        // TEST - Draw Border
        MyUtils.drawGrid(g2, this);
        g2.setColor(MyUtils.COLOR_TEST_BLUE);
        g2.setStroke(new BasicStroke(5));
        // TEST - Draw Players
        g2.drawRect(0, 0, getWidth(), getHeight());
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