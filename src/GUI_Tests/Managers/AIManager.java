package GUI_Tests.Managers;

import GUI_Tests.Entities.Player.Player;
import GUI_Tests.Root.GameLauncher;
import GUI_Tests.Utilities.MyUtils;
import java.awt.geom.Rectangle2D;

public class AIManager {
    private final GameLauncher game;

    public AIManager(GameLauncher game) {
        this.game = game;
    }

    public void updateAI(Player bot) {
        if (!bot.isAI()) return;

        // 1. "Sense": Check if we are about to hit something
        if (this.isPathBlocked(bot, bot.getDirection())) {
            // 2. "Think": Find a safe turn
            this.chooseNewDirection(bot);
        }
    }

    private boolean isPathBlocked(Player p, int direction) {
        int nextX = (int) p.getPlayerX();
        int nextY = (int) p.getPlayerY();
        int step = MyUtils.PLAYER_TILE_SIZE + 5; // Look slightly ahead

        if (direction == MyUtils.DIRECTION_UP) nextY -= step;
        else if (direction == MyUtils.DIRECTION_DOWN) nextY += step;
        else if (direction == MyUtils.DIRECTION_LEFT) nextX -= step;
        else if (direction == MyUtils.DIRECTION_RIGHT) nextX += step;

        Rectangle2D.Double nextPos = new Rectangle2D.Double(nextX, nextY,
                MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);

        // Check against trails and walls (Reuse your existing collision logic)
        return this.game.isPositionDangerous(nextPos);
    }

    private void chooseNewDirection(Player bot) {
        int currentDir = bot.getDirection();
        int[] possibleTurns;

        // If moving vertically, can only turn horizontally
        if (currentDir == MyUtils.DIRECTION_UP || currentDir == MyUtils.DIRECTION_DOWN) {
            possibleTurns = new int[]{MyUtils.DIRECTION_LEFT, MyUtils.DIRECTION_RIGHT};
        } else {
            possibleTurns = new int[]{MyUtils.DIRECTION_UP, MyUtils.DIRECTION_DOWN};
        }

        for (int dir : possibleTurns) {
            if (!this.isPathBlocked(bot, dir)) {
                bot.setDirection(dir);
                return; // Act on the first safe turn found
            }
        }
    }
}