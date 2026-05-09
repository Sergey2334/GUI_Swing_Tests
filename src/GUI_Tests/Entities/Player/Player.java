package GUI_Tests.Entities.Player;

import GUI_Tests.Utilities.MyUtils;
import java.awt.*;

public class Player {
    // 1. State and Position
    private double playerX, playerY;
    private double playerStartX, playerStartY;
    private double lastPointX, lastPointY;
    private int direction;
    private int startDirection;
    private int moveSpeed;
    private boolean isAlive;
    private Color color;

    // 2. Modular Components
    private final PlayerTrail playerTrail;

    public Player(int startX, int startY, Color color, int startDirection, int moveSpeed) {
        // Center the bike on the starting tile
        this.playerStartX = startX - ((double) MyUtils.PLAYER_TILE_SIZE / 2);
        this.playerStartY = startY - ((double) MyUtils.PLAYER_TILE_SIZE / 2);

        this.playerX = this.playerStartX;
        this.playerY = this.playerStartY;
        this.lastPointX = this.playerX;
        this.lastPointY = this.playerY;

        this.color = color;
        this.direction = startDirection;
        this.startDirection = startDirection;
        this.moveSpeed = moveSpeed;
        this.isAlive = true;

        // Initialize the modular Trail
        this.playerTrail = new PlayerTrail(this.color);
    }

    // --- LOGIC ---

    public void move(long currentGameTime) {
        if (!this.isAlive) return;

        // Store previous position before moving
        this.lastPointX = this.playerX;
        this.lastPointY = this.playerY;

        double playerVelocity = MyUtils.GAME_TILE_SIZE * this.moveSpeed * (1.0 / MyUtils.GAME_FPS);

        switch (this.direction) {
            case MyUtils.DIRECTION_LEFT -> this.playerX -= playerVelocity;
            case MyUtils.DIRECTION_RIGHT -> this.playerX += playerVelocity;
            case MyUtils.DIRECTION_UP -> this.playerY -= playerVelocity;
            case MyUtils.DIRECTION_DOWN -> this.playerY += playerVelocity;
        }

        // Delegate trail management to the PlayerTrail object
        this.playerTrail.update(this.playerX, this.playerY, this.lastPointX, this.lastPointY, currentGameTime);
    }

    public void draw(Graphics2D g2, long currentGameTime) {
        // 1. Calculate Pulse Factor for visuals
        float pulse = (float) (Math.sin(currentGameTime * 0.001) * 0.1 + 1.0);

        // 2. Draw the Trail first (so it's behind the bike)
        this.playerTrail.draw(g2, currentGameTime, pulse);

        // 3. Draw the Bike Glow
        int glowSize = (int) (MyUtils.PLAYER_TILE_SIZE * 1.1 * pulse);
        int glowOffset = (glowSize - MyUtils.PLAYER_TILE_SIZE) / 2;
        g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 100));
        g2.fillOval((int)this.playerX - glowOffset, (int)this.playerY - glowOffset, glowSize, glowSize);

        // 4. Draw the Bike Body
        g2.setColor(Color.WHITE);
        g2.fillRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
        g2.setColor(this.color);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
    }

    public void setDirection(int newDirection) {
        // Don't allow 180-degree turns (cannot go back into your own tail)
        if (this.direction == newDirection) return;
        if (this.direction == MyUtils.DIRECTION_UP && newDirection == MyUtils.DIRECTION_DOWN) return;
        if (this.direction == MyUtils.DIRECTION_DOWN && newDirection == MyUtils.DIRECTION_UP) return;
        if (this.direction == MyUtils.DIRECTION_LEFT && newDirection == MyUtils.DIRECTION_RIGHT) return;
        if (this.direction == MyUtils.DIRECTION_RIGHT && newDirection == MyUtils.DIRECTION_LEFT) return;

        this.direction = newDirection;
    }

    public void respawn() {
        this.playerX = this.playerStartX;
        this.playerY = this.playerStartY;
        this.direction = this.startDirection;
        this.isAlive = true;
        this.playerTrail.clear(); // Important: Reset the trail visually and logically
    }

    public void killPlayer() {
        this.isAlive = false;
    }

    public Color getTransparentColor() { return MyUtils.transparentColor(this.color, 150); }

    // --- GETTERS & SETTERS ---

    public double getPlayerX() { return this.playerX; }
    public double getPlayerY() { return this.playerY; }
    public Color getColor() { return this.color; }
    public double getLastPointX() { return this.lastPointX; }
    public double getLastPointY() { return this.lastPointY; }
    public PlayerTrail getPlayerTrail() { return this.playerTrail; }
    public void setSpeed(int newSpeed) { this.moveSpeed = newSpeed; }
    public void setColor(Color color) { this.color = color; }
}