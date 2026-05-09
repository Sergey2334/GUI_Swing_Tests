package GUI_Tests.MainWinodw.ClientArea.BackgroundVisualElements;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TerminalTextWidget implements VisualElement {
    private final List<String> logEntries = new ArrayList<>();
    private final String[] possibleMessages = {
            "GRID_SYNC_ACTIVE", "LINK_STABLE", "BUFFER_CLEARED",
            "MEM_OPTIMIZED", "TRACE_INBOUND", "IO_PORT_OPEN"
    };
    private final Random random = new Random();
    private long lastLogTime = 0;

    public TerminalTextWidget() {
        // Start with some initial logs
        this.logEntries.add("SYSTEM_INITIALIZED...");
        this.logEntries.add("KERNEL_BOOT_SUCCESS");
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        // Add a new log entry every 2-4 seconds
        if (currentTime - this.lastLogTime > 2000 + this.random.nextInt(2000)) {
            String newLog = "[" + (currentTime % 10000) + "] " +
                    this.possibleMessages[this.random.nextInt(this.possibleMessages.length)];
            this.logEntries.add(newLog);

            // Keep only the last 5 entries to prevent clutter
            if (this.logEntries.size() > 5) {
                this.logEntries.remove(0);
            }
            this.lastLogTime = currentTime;
        }
    }

    @Override
    public void draw(Graphics2D g2, int w, int h) {
        g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        int x = 20;
        int y = h - 100; // Positioned at the bottom left

        for (int i = 0; i < this.logEntries.size(); i++) {
            // Fade older entries
            int alpha = (int) (255 * ((double) (i + 1) / this.logEntries.size()));
            g2.setColor(new Color(0, 255, 255, Math.max(0, alpha / 2)));

            // Draw each entry slightly higher than the last
            g2.drawString(this.logEntries.get(i), x, y + (i * 15));
        }
    }
}