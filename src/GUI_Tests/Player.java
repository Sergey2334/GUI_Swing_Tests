package GUI_Tests;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;

public class Player extends JPanel {
    private int gridX, gridY;
    private double playerX, playerY;
    private Color color;
    private int direction;
    private int moveSpeed;
    private int playerNumber;
    private static int playerCount;

    public Player(int startX, int startY, Color color, int startDirection, int moveSpeed) {
        this.gridX = startX;
        this.gridY = startY;
        this.playerX = startX;
        this.playerY = startY;
        this.color = color;
        this.direction = startDirection;
        this.moveSpeed = moveSpeed;
        playerCount++;
        this.playerNumber = playerCount;
    }

    public String toString() {
        return "Player #" + this.playerNumber + " X: " + (int) this.playerX + ", Y: " + (int) this.playerY + ", gridX: " + this.gridX + ", gridY: " + this.gridY;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(this.color);

        g2.fillRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
    }

    public void move() {
        double playerVelocity = MyUtils.GAME_TILE_SIZE * this.moveSpeed * (1.0 / MyUtils.GAME_FPS);

        switch (this.direction) {
            case MyUtils.DIRECTION_LEFT:
                this.playerX -= playerVelocity;
                break;
            case MyUtils.DIRECTION_RIGHT:
                this.playerX += playerVelocity;
                break;
            case MyUtils.DIRECTION_UP:
                this.playerY -= playerVelocity;
                break;
            case MyUtils.DIRECTION_DOWN:
                this.playerY += playerVelocity;
                break;
            default:
                break;
        }

        getGridPosition();
    }

    public void setDirection(int newDirection) {
        // Dont allow 180 turns
        if (this.direction == MyUtils.DIRECTION_UP && newDirection == MyUtils.DIRECTION_DOWN) return;
        if (this.direction == MyUtils.DIRECTION_DOWN && newDirection == MyUtils.DIRECTION_UP) return;
        if (this.direction == MyUtils.DIRECTION_LEFT && newDirection == MyUtils.DIRECTION_RIGHT) return;
        if (this.direction == MyUtils.DIRECTION_RIGHT && newDirection == MyUtils.DIRECTION_LEFT) return;

        this.direction = newDirection;
    }


    // TEST - Cool Trick, may Implement Later :D
//    public void setDirection(int newDirection) {
//        // If the difference is 2, they are opposites (e.g., |0 - 2| = 2 or |1 - 3| = 2)
//        if (Math.abs(this.direction - newDirection) == 2) {
//            return;
//        }
//        this.direction = newDirection;
//    }

    private void getGridPosition() {
        this.gridX = (int) (this.playerX / MyUtils.GAME_TILE_SIZE);
        this.gridY = (int) (this.playerY / MyUtils.GAME_TILE_SIZE);
    }

    public void leaveTrail(GameLauncher gameLauncher) {
        JLabel trailLabel = new JLabel();
        trailLabel.setOpaque(true);
        trailLabel.setSize(MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
        trailLabel.setLocation((int) this.playerX, (int) this.playerY);
        trailLabel.setBackground(this.color);
        new Thread(() -> {
            try {
                for (int alpha = 255; alpha >= 0; alpha -= 50) {
                    int finalAlpha = alpha;
                    SwingUtilities.invokeLater(() -> {
                        Color current = trailLabel.getBackground();
                        trailLabel.setBackground(new Color(current.getRed(), current.getGreen(), current.getBlue(), finalAlpha));
                    });

                    Thread.sleep(2 * 1000);
                }
                // Once invisible, remove it to save memory
                SwingUtilities.invokeLater(() -> gameLauncher.remove(trailLabel));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        trailLabel.setVisible(true);
        gameLauncher.add(trailLabel);
    }
}