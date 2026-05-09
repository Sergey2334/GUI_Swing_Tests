package GUI_Tests.Entities.PowerUps;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Managers.TextureManager;
import GUI_Tests.Utilities.MyUtils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BoostPowerUp implements PowerUp {
    private int x, y;
    private boolean collected = false;
    private final int size = 35;
    private final int speedModifier = MyUtils.POWER_UP_SPEED_MODIFIER;
    private final int duration = MyUtils.POWER_UP_SPEED_DURATION * 1000;

    public BoostPowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Inside BoostPowerUp.java
    @Override
    public void draw(Graphics2D g2) {
        if (!this.collected) {
            // Now this line will work!
            BufferedImage icon = TextureManager.getInstance().getTexture("powerup_boost");

            if (icon != null) {
                g2.drawImage(icon, this.x, this.y, this.size, this.size, null);
            } else {
                // Fallback so the game doesn't look broken if image is missing
                g2.setColor(Color.YELLOW);
                g2.fillOval(this.x, this.y, this.size, this.size);
            }
        }
    }


    @Override
    public void applyEffect(Player player) {
        this.collected = true;
        // Temporary boost logic (requires a timer or thread)
        int originalSpeed = player.getSpeed();
        player.setSpeed(originalSpeed * this.speedModifier);

        new Thread(() -> {
            try {
                Thread.sleep(duration);
                player.setSpeed(originalSpeed);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override public boolean isCollected() { return this.collected; }
    @Override public int getX() { return this.x; }
    @Override public int getY() { return this.y; }
    @Override public String getName() { return "BOOST!"; }
}