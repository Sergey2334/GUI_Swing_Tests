package GUI_Tests.Managers;

import GUI_Tests.Root.GameSettings;
import GUI_Tests.Utilities.MyUtils;
import java.awt.*;

/**
 * Handles the selection logic and rendering for the Pre-Game Setup Screen.
 * This class is decoupled from the main game loop to ensure maximum modularity.
 */
public class SetupManager {
    // Core settings data to be modified
    private final GameSettings settings;

    // UI State tracking
    private int currentRow = 0; // 0: Difficulty, 1: P1 Color, 2: P2 Color, 3: START
    private int diffIndex = 1;  // Default to "STANDARD" (Index 1)
    private int p1ColIndex = 0; // Default to TRON_COLORS[0]
    private int p2ColIndex = 3; // Default to TRON_COLORS[3]

    // Difficulty labels mapped to MyUtils parameters
    private final String[] difficultyLabels = {"RELAXED", "STANDARD", "HYPER"};

    public SetupManager(GameSettings settings) {
        this.settings = settings;
    }

    /**
     * Navigates the selection cursor downward.
     */
    public void moveDown() {
        this.currentRow = (this.currentRow + 1) % 4;
    }

    /**
     * Navigates the selection cursor upward.
     */
    public void moveUp() {
        this.currentRow = (this.currentRow - 1 + 4) % 4;
    }

    /**
     * Cycles through values in the currently selected row (Left/Right).
     * @param direction -1 for Left, 1 for Right.
     */
    public void cycle(int direction) {
        if (this.currentRow == 0) {
            // Cycle Difficulty
            this.diffIndex = (this.diffIndex + direction + this.difficultyLabels.length) % this.difficultyLabels.length;
            this.settings.setDifficulty(this.difficultyLabels[this.diffIndex]);
        }
        else if (this.currentRow == 1) {
            // Cycle Player 1 Color
            this.p1ColIndex = (this.p1ColIndex + direction + MyUtils.TRON_COLORS.length) % MyUtils.TRON_COLORS.length;
            this.settings.setP1Color(MyUtils.TRON_COLORS[this.p1ColIndex]);
        }
        else if (this.currentRow == 2) {
            // Cycle Player 2 Color
            this.p2ColIndex = (this.p2ColIndex + direction + MyUtils.TRON_COLORS.length) % MyUtils.TRON_COLORS.length;
            this.settings.setP2Color(MyUtils.TRON_COLORS[this.p2ColIndex]);
        }
    }

    /**
     * Draws the Setup terminal overlay.
     */
    public void draw(Graphics2D g2, int width, int height) {
        // 1. Semi-transparent dark background for the setup terminal
        g2.setColor(new Color(0, 5, 10, 230));
        g2.fillRect(0, 0, width, height);

        // 2. Title Section
        g2.setFont(new Font("Monospaced", Font.BOLD, 35));
        g2.setColor(MyUtils.COLOR_TRON1);
        g2.drawString("> GRID_PROTOCOL_INIT", 80, 100);

        // 3. Menu Options List
        String[] menuItems = {
                "DIFFICULTY: < " + this.difficultyLabels[this.diffIndex] + " >",
                "P1_COLOR_SCAN: ",
                "P2_COLOR_SCAN: ",
                "EXECUTE_PROGRAM"
        };

        g2.setFont(new Font("Monospaced", Font.PLAIN, 24));
        for (int i = 0; i < menuItems.length; i++) {
            boolean isHighlighted = (i == this.currentRow);

            // Set color based on selection
            g2.setColor(isHighlighted ? Color.WHITE : Color.DARK_GRAY);
            String cursor = isHighlighted ? ">> " : "   ";
            g2.drawString(cursor + menuItems[i], 110, 220 + (i * 65));

            // Draw color preview boxes for player color rows
            if (i == 1) this.drawPreviewBox(g2, this.settings.getP1Color(), 480, 220 + (1 * 65) - 28);
            if (i == 2) this.drawPreviewBox(g2, this.settings.getP2Color(), 480, 220 + (2 * 65) - 28);
        }

        // 4. Instructional footer
        g2.setFont(new Font("Monospaced", Font.ITALIC, 20));
        g2.setColor(Color.GRAY);
        g2.drawString("ARROWS: NAVIGATE | LEFT/RIGHT: CHANGE | ENTER: CONFIRM | ESC: BACK", 80, height - 60);
    }

    /**
     * Helper to draw a square preview of the currently selected color.
     */
    private void drawPreviewBox(Graphics2D g2, Color color, int x, int y) {
        g2.setColor(color);
        g2.fillRect(x, y, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
    }

    /**
     * @return The currently hovered row index.
     */
    public int getCurrentRow() {
        return this.currentRow;
    }
}