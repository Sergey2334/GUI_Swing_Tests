package GUI_Tests.Entities.PowerUps;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Utilities.MyUtils;

import java.awt.*;

public class InvinciblePowerUp implements PowerUp {
    private int x, y;
    private boolean collected = false;
    private final int size = 20;

    public InvinciblePowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!this.collected) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(this.x - 3, this.y - 3, this.size + 3, this.size + 3);
            g2.fillOval(this.x, this.y, this.size, this.size);
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