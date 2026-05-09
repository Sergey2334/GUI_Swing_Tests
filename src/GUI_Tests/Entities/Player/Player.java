package GUI_Tests.Entities.Player;

import GUI_Tests.Managers.TextureManager;
import GUI_Tests.Utilities.MyUtils;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Player {
    // 1. State and Position
    private double playerX, playerY;
    private double playerStartX, playerStartY;
    private double lastPointX, lastPointY;
    private int direction;
    private int startDirection;
    private int moveSpeed;
    private boolean isAlive;
    private boolean isInvincible;
    private Color color;

    private boolean isAI = false;

    // 2. Modular Components
    private final PlayerTrail playerTrail;
    private BufferedImage playerImage;

    public static java.util.Map<java.awt.Color, String> TEXTURE_MAP = new java.util.HashMap<>();
    static {
        // We map your MyUtils colors to the String keys used in TextureManager
        for (int i = 0; i < MyUtils.TRON_COLORS.length; i++)
        {
            TEXTURE_MAP.put(MyUtils.TRON_COLORS[i], MyUtils.TRON_COLORS_ARRAY[i]);
        }
    }

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
        this.isInvincible = false;

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
        // 1. SAVE the current rendering state
        java.awt.Composite oldComposite = g2.getComposite();

        // 2. Handle Invincibility Transparency (The Ghost Effect)
        if (this.isInvincible) {
            float alpha = (float) (0.45 + 0.25 * Math.sin(currentGameTime * 0.01));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        }

        // 3. Calculate Pulse Factor
        float pulse = (float) (Math.sin(currentGameTime * 0.001) * 0.1 + 1.0);

        // 4. DRAW NEON GLOW (Multiple Layers for Softness)
        // We draw 3 layers of increasing size and decreasing opacity
        int baseGlowSize = (int) (MyUtils.PLAYER_TILE_SIZE * pulse);
        for (int i = 3; i > 0; i--) {
            int currentGlowSize = baseGlowSize + (i * 10);
            int offset = (currentGlowSize - MyUtils.PLAYER_TILE_SIZE) / 2;

            // Max OOP: Calculate fading alpha for each layer
            int alpha = 40 / i; // Top layer is faintest, bottom is strongest
            g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha));

            g2.fillOval((int)this.playerX - offset, (int)this.playerY - offset, currentGlowSize, currentGlowSize);
        }

        // 5. Draw the Trail (Behind the bike but on top of the glow)
        this.playerTrail.draw(g2, currentGameTime, pulse);

        // 6. Draw the Bike Image (Scaled & Centered)
        if (this.playerImage != null) {
            java.awt.geom.AffineTransform tx = new java.awt.geom.AffineTransform();

            int padding = 2;
            double targetSize = MyUtils.PLAYER_TILE_SIZE - (padding * 2);
            double scale = Math.min(targetSize / this.playerImage.getWidth(), targetSize / this.playerImage.getHeight());

            double drawX = this.playerX + padding + (targetSize - (this.playerImage.getWidth() * scale)) / 2.0;
            double drawY = this.playerY + padding + (targetSize - (this.playerImage.getHeight() * scale)) / 2.0;

            tx.translate(drawX, drawY);
            tx.scale(scale, scale);

            double angle = 0;
            if (this.direction == MyUtils.DIRECTION_DOWN) angle = Math.PI;
            else if (this.direction == MyUtils.DIRECTION_LEFT) angle = -Math.PI / 2.0;
            else if (this.direction == MyUtils.DIRECTION_RIGHT) angle = Math.PI / 2.0;

            tx.rotate(angle, this.playerImage.getWidth() / 2.0, this.playerImage.getHeight() / 2.0);

            g2.drawImage(this.playerImage, tx, null);
        }

        // 7. RESTORE the original rendering state
        g2.setComposite(oldComposite);
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

    public void updateTexture() {
        // This looks up the String key from the map using the current color
        String key = TEXTURE_MAP.getOrDefault(this.color, "player_cyan");

        // This grabs the actual image from your TextureManager
        this.playerImage = GUI_Tests.Managers.TextureManager.getInstance().getTexture(key);
    }

    // --- GETTERS & SETTERS ---

    public double getPlayerX() { return this.playerX; }
    public double getPlayerY() { return this.playerY; }
    public Color getColor() { return this.color; }
    public double getLastPointX() { return this.lastPointX; }
    public double getLastPointY() { return this.lastPointY; }
    public PlayerTrail getPlayerTrail() { return this.playerTrail; }
    public int getSpeed() { return this.moveSpeed; }
    public void setSpeed(int newSpeed) { this.moveSpeed = newSpeed; }
    public boolean isInvincible() { return this.isInvincible; }
    public void setInvincible(boolean invincible) { this.isInvincible = invincible; }
    public void setColor(Color color) {
        this.color = color;
        // Max OOP: The player is responsible for updating its sub-components
        if (this.playerTrail != null) {
            this.playerTrail.setTrailColor(color);
        }
    }
    public void setAI(boolean ai) { this.isAI = ai; }
    public boolean isAI() { return this.isAI; }
    public int getDirection() { return this.direction; }
    public Color getTransparentColor() { return MyUtils.transparentColor(this.color, 150); }
}