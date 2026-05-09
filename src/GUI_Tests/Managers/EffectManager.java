package GUI_Tests.Managers;

import GUI_Tests.Entities.FloatingText;
import GUI_Tests.Entities.Spark;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EffectManager {
    // 1. Change to standard ArrayList for high-speed updates
    private final List<Spark> sparks = new ArrayList<>();
    private final List<FloatingText> texts = new ArrayList<>();

    public void createExplosion(double x, double y, Color color) {
        for (int i = 0; i < 50; i++) {
            this.sparks.add(new Spark(x, y, color));
        }
    }

    public void addText(String text, double x, double y, Color color, int durationMs) {
        this.texts.add(new FloatingText(text, x, y, color, durationMs));
    }

    public void update() {
        // 2. Safely remove dead items using removeIf
        this.sparks.removeIf(s -> {
            s.update(); // Update the spark logic first
            return s.isDead(); // Then check if it should be removed
        });

        this.texts.removeIf(t -> {
            t.update();
            return t.isDead();
        });
    }

    public void draw(Graphics2D g2) {
        // 3. Use a standard for-loop for drawing (Very fast)
        for (int i = 0; i < this.sparks.size(); i++) {
            this.sparks.get(i).draw(g2);
        }
        for (int i = 0; i < this.texts.size(); i++) {
            this.texts.get(i).draw(g2);
        }
    }

    public void clear() {
        this.sparks.clear();
        this.texts.clear();
    }
}