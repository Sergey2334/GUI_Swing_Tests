package GUI_Tests.Entities.PowerUps;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Managers.TextureManager;
import GUI_Tests.Utilities.MyUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class InvinciblePowerUp implements PowerUp {
    private int x, y;
    private boolean collected = false;
    private final int size = 35;

    public InvinciblePowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Inside BoostPowerUp.java
    @Override
    public void draw(Graphics2D g2) {
        if (!this.collected) {
            // Now this line will work!
            BufferedImage icon = TextureManager.getInstance().getTexture("powerup_invincible");

            if (icon != null) {
                g2.drawImage(icon, this.x, this.y, this.size, this.size, null);
            } else {
                // Fallback so the game doesn't look broken if image is missing
                g2.setColor(Color.WHITE);
                g2.fillOval(this.x, this.y, this.size, this.size);
            }
        }
    }

    @Override
    public void applyEffect(Player player) {
        this.collected = true;
        player.setInvincible(true);

        new Thread(() -> {
            try {
                Thread.sleep(MyUtils.POWER_UP_INVINCIBLE_DURATION * 1000);
                player.setInvincible(false);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override public boolean isCollected() { return this.collected; }
    @Override public int getX() { return this.x; }
    @Override public int getY() { return this.y; }
    @Override public String getName() { return "INVINCIBLE!"; }
}