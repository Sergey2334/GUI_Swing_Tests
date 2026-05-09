package GUI_Tests.Entities.PowerUps;

import GUI_Tests.Entities.Player.Player;
import java.awt.Graphics2D;

public interface PowerUp {
    void draw(Graphics2D g2);
    void applyEffect(Player player);
    boolean isCollected();
    int getX();
    int getY();
    String getName();
}