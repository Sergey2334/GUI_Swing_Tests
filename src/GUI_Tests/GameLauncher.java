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
    private int[][] gameGrid;
    private int panelWidth;
    private int panelHeight;
    private Player player1;
    private Player player2;

    public GameLauncher() {
        this.initialize();

        // TEST
        int widthMiddle = this.panelWidth / 2;
        int heightMiddle = this.panelHeight / 2;
        this.player1 = new Player(0, 0, MyUtils.COLOR_TRON1, MyUtils.DIRECTION_RIGHT, MyUtils.GAME_SPEED_MEDIUM);
        this.player2 = new Player(0, 0, MyUtils.COLOR_TRON2, MyUtils.DIRECTION_DOWN, MyUtils.GAME_SPEED_MEDIUM);
    }

    private void initialize() {
        this.setMinimumSize(new Dimension(MyUtils.GAME_MIN_WIDTH, MyUtils.GAME_MIN_HEIGHT));
        this.setLayout(null);
        // TEST
//        this.setBorder(BorderFactory.createLineBorder(MyUtils.COLOR_TRON1, 5));
        this.panelWidth = this.getWidth();
        this.panelHeight = this.getHeight();
        this.gameGrid = new int[this.panelWidth][this.panelHeight];
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

    private void update() {
        // TEST
        player1.move();
        player2.move();
        player1.leaveTrail(this);
        player2.leaveTrail(this);

        System.out.println(player1 + "\t" + player2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Better Options For Graphics :D , Google Said Always Do It


        // TEST - Draw Players
        MyUtils.drawGrid(g2, this);
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