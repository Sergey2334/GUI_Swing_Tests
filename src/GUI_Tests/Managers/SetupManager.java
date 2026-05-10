package GUI_Tests.Managers;

import GUI_Tests.Root.GameSettings;
import GUI_Tests.Utilities.MyUtils;
import java.awt.*;

public class SetupManager {
    private final GameSettings settings;
    private int currentRow = 0;
    private int diffIndex = 1;
    private int p1ColIndex = 0;
    private int p2ColIndex = 2;

    private final String[] difficultyLabels = {"RELAXED", "STANDARD", "HYPER"};

    private final SoundManager soundManager = SoundManager.getInstance();

    public SetupManager(GameSettings settings) {
        this.settings = settings;
    }

    public void moveDown() {
        this.soundManager.play("menu_turn");
        this.currentRow = (this.currentRow + 1) % 5;
    }

    public void moveUp() {
        this.soundManager.play("menu_turn");
        this.currentRow = (this.currentRow - 1 + 5) % 5;
    }

    public void cycle(int direction) {
        this.soundManager.play("menu_option");
        switch (this.currentRow) {
            case 0 -> {
                // Difficulty Cycling
                this.diffIndex = (this.diffIndex + direction + 3) % 3;
                this.settings.setDifficulty(this.difficultyLabels[this.diffIndex]);
            }
            case 1 -> {
                // Match Type Toggle (PvP vs PvAI)
                this.settings.toggleAI();
            }
            case 2 -> {
                // Player 1 Color Cycling
                this.p1ColIndex = (this.p1ColIndex + direction + MyUtils.TRON_COLORS.length) % MyUtils.TRON_COLORS.length;
                this.settings.setP1Color(MyUtils.TRON_COLORS[this.p1ColIndex]);
            }
            case 3 -> {
                // Player 2 Color Cycling
                this.p2ColIndex = (this.p2ColIndex + direction + MyUtils.TRON_COLORS.length) % MyUtils.TRON_COLORS.length;
                this.settings.setP2Color(MyUtils.TRON_COLORS[this.p2ColIndex]);
            }
            case 4 -> {
                // Row 4 is "START", no cycling needed here!
            }
        }
    }

    public void draw(Graphics2D g2, int width, int height) {
        // 1. Semi-transparent terminal background
        g2.setColor(new Color(0, 5, 10, 235));
        g2.fillRect(0, 0, width, height);

        // 2. Title
        g2.setFont(new Font("Monospaced", Font.BOLD, 35));
        g2.setColor(MyUtils.COLOR_TRON1);
        g2.drawString("> SYSTEM_CONFIGURATION", 80, 80);

        // 3. Dynamic Menu Items
        String[] menuItems = {
                "DIFFICULTY: < " + this.difficultyLabels[this.diffIndex] + " >",
                "MATCH_TYPE: [ " + (this.settings.isVsAI() ? "PLAYER vs AI" : "PLAYER vs PLAYER") + " ]",
                "P1_COLOR_SCAN: ",
                "P2_COLOR_SCAN: ",
                "EXECUTE_MATCH_START"
        };

        g2.setFont(new Font("Monospaced", Font.PLAIN, 22));
        for (int i = 0; i < menuItems.length; i++) {
            boolean isSelected = (i == this.currentRow);

            // Highlight selected row with White, others with Dark Gray
            g2.setColor(isSelected ? Color.WHITE : Color.DARK_GRAY);

            String cursor = (isSelected) ? ">> " : "   ";
            g2.drawString(cursor + menuItems[i], 110, 180 + (i * 55));

            // Draw the color preview boxes next to the text
            if (i == 2) {
                this.drawPreview(g2, this.settings.getP1Color(), 400, 180 + (2 * 55) - 25);
            }
            if (i == 3) {
                this.drawPreview(g2, this.settings.getP2Color(), 400, 180 + (3 * 55) - 25);
            }
        }

        // 4. Footer Help Text
        g2.setFont(new Font("Monospaced", Font.ITALIC, 18));
        g2.setColor(Color.GRAY);
        g2.drawString("ARROWS: NAVIGATE | LEFT/RIGHT: CHANGE | ENTER: START | ESC: BACK", 80, height - 40);
    }

    private void drawPreview(Graphics2D g2, Color c, int x, int y) {
        // Inner Color
        g2.setColor(c);
        g2.fillRect(x, y, 30, 30);
        // White Border
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, 30, 30);
    }

    public int getCurrentRow() {
        return this.currentRow;
    }
}