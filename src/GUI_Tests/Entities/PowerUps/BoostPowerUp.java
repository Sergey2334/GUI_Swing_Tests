package GUI_Tests.Entities.PowerUps;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Utilities.MyUtils;

import java.awt.*;

public class BoostPowerUp implements PowerUp {
    private int x, y;
    private boolean collected = false;
    private final int size = 20;
    private final int speedModifier = MyUtils.POWER_UP_SPEED_MODIFIER;
    private final int duration = MyUtils.POWER_UP_SPEED_DURATION * 1000;

    public BoostPowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!this.collected) {
            g2.setColor(Color.YELLOW);
            g2.fillOval(this.x, this.y, this.size, this.size);
            // Optional "Glow" effect
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(this.x - 2, this.y - 2, this.size + 4, this.size + 4);
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