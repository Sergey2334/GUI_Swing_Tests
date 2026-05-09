package GUI_Tests.Managers;

import GUI_Tests.Entities.PowerUps.BoostPowerUp;
import GUI_Tests.Entities.PowerUps.InvinciblePowerUp;
import GUI_Tests.Entities.PowerUps.PowerUp;
import GUI_Tests.Root.GameLauncher;
import GUI_Tests.Utilities.MyUtils;
import java.util.Random;

public class PowerUpSpawner implements Runnable {
    private final GameLauncher game;
    private final Random random = new Random();
    private boolean running = true;

    public PowerUpSpawner(GameLauncher game) {
        this.game = game;
    }

    @Override
    public void run() {
        while (this.running) {
            try {
                // Wait 10 seconds between spawns
                Thread.sleep(MyUtils.POWER_UP_SPAWN_DELAY * 1000);

                if (this.game.getCurrentState() == GameLauncher.State.PLAYING) {
                    this.spawnRandom();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void spawnRandom() {
        int x = this.random.nextInt(50, MyUtils.GAME_MIN_WIDTH - 100) + 50;
        int y = this.random.nextInt(50, MyUtils.GAME_MIN_HEIGHT - 100) + 50;

        PowerUp p;
        if (this.random.nextBoolean()) {
            p = new BoostPowerUp(x, y);
        } else {
            p = new InvinciblePowerUp(x, y);
        }

        this.game.getActivePowerUps().add(p);
    }

    public void stop() { this.running = false; }
}