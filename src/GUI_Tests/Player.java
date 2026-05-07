package GUI_Tests;

import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player extends JPanel {
    private int gridX, gridY;
    private double playerX, playerY;
    private double playerStartX, playerStartY;
    private double lastPointX, lastPointY;
    private Color color;
    private int direction;
    private int startDirection;
    private long lastTurnTime;
    private int moveSpeed;
    private int playerNumber;
    private int playerScore;
    private boolean isAlive;

    private static int playerCount;

    private List<TrailSegment> trailSegments = new CopyOnWriteArrayList<>();
    private List<Breadcrumb> trailPoints = new CopyOnWriteArrayList<>();
    private static final long TRAIL_LIFETIME = MyUtils.PLAYER_TRAIL_LIFETIME;
    private List<Spark> sparks = new ArrayList<>();

    public Player(int startX, int startY, Color color, int startDirection, int moveSpeed) {
        this.playerStartX = startX - ((double) MyUtils.PLAYER_TILE_SIZE / 2);
        this.playerStartY = startY - ((double) MyUtils.PLAYER_TILE_SIZE / 2);
        this.gridX = startX;
        this.gridY = startY;
        this.playerX = this.playerStartX;
        this.playerY = this.playerStartY;
        this.lastPointX = startX;
        this.lastPointY = startY;
        this.color = color;
        this.direction = startDirection;
        this.startDirection = startDirection;
        this.lastTurnTime = System.currentTimeMillis();
        this.moveSpeed = moveSpeed;
        this.playerScore = 0;
        this.isAlive = true;
        playerCount++;
        this.playerNumber = playerCount;
    }

    public String toString() {
        return "Player #" + this.playerNumber + " Score: " + this.playerScore;
    }

    public double getPlayerStartX() {
        return this.playerStartX;
    }

    public double getPlayerStartY() {
        return this.playerStartY;
    }

    public int getStartDirection() {
        return this.startDirection;
    }

    public double getPlayerX() {
        return this.playerX;
    }

    public double getPlayerY() {
        return this.playerY;
    }

    public int getPlayerScore() {
        return this.playerScore;
    }

    public void addScore() {
        this.playerScore += 1;
    }

    public void subScore() {
        this.playerScore = Math.max(0, this.playerScore - 1);
    }

    public void killPlayer() {
        this.triggerExplosion();
        this.isAlive = false;
    }

    public void resetPlayerScore() {
        this.playerScore = 0;
    }

    public void triggerFirework(int x, int y, Color c) {
        // Don't clear sparks here, we want them to overlap!
        for (int i = 0; i < 30; i++) {
            this.sparks.add(new Spark(x, y, c));
        }
    }

//    public void draw(Graphics2D g2, long currenGameTime) {
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        long currentTime = currenGameTime;
//
//        List<Breadcrumb> pointsCopy;
//        synchronized (this.trailPoints) {
//            pointsCopy = new ArrayList<>(this.trailPoints);
//        }
//
//        Breadcrumb lastCrumb = null;
//        for (Breadcrumb currentCrumb : pointsCopy) {
//            long age = currentTime - currentCrumb.creationTime;
//
//            if (age > TRAIL_LIFETIME) continue;
//
////            if (lastCrumb != null) {
////                float alphaFactor = 1.0f - ((float) age / TRAIL_LIFETIME);
////                int alpha = (int) (alphaFactor * 255);
////                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alpha)));
////                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE));
////                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));
////            }
//
//            if (lastCrumb != null) {
//                // TEST - Making a Glow Effect on Player Trail
//                float alphaFactor = 1.0f - ((float) age / TRAIL_LIFETIME);
//                int alpha = (int) (alphaFactor * 255);
//
//                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alpha / 4)));
//                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));
//
//                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, alpha / 2)));
//                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));
//
//                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//                g2.setColor(new Color(255, 255, 255, Math.max(0, alpha))); // White or very bright version of color
//                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));
//            }
//
//            lastCrumb = currentCrumb;
//        }
//
//        g2.setColor(this.color);
//        g2.fillRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
//    }

    public void draw(Graphics2D g2, long currentGameTime) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Calculate Pulse Factor
        float pulse = (float) (Math.sin(currentGameTime * 0.001) * 0.1 + 1.0);

        List<Breadcrumb> pointsCopy;
        synchronized (this.trailPoints) {
            pointsCopy = new ArrayList<>(this.trailPoints);
        }

        Breadcrumb lastCrumb = null;
        for (Breadcrumb currentCrumb : pointsCopy) {
            long age = currentGameTime - currentCrumb.creationTime;
            if (age > TRAIL_LIFETIME || age < 0) continue;

            if (lastCrumb != null) {
                float alphaFactor = 1.0f - ((float) age / TRAIL_LIFETIME);
                int alpha = (int) (alphaFactor * 255);

                // --- DRAW NEON LAYERS ---
                // OUTER GLOW (Pulsing)
                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 1.5f * pulse, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.max(0, alpha / 6)));
                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));

                // INNER GLOW
                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), Math.max(0, alpha / 3)));
                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));

                // WHITE CORE
                g2.setStroke(new BasicStroke(MyUtils.PLAYER_TRAIL_SIZE * 0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(new Color(255, 255, 255, Math.max(0, alpha)));
                g2.draw(new Line2D.Double(lastCrumb.x, lastCrumb.y, currentCrumb.x, currentCrumb.y));
            }
            lastCrumb = currentCrumb;
        }

        // 2. DRAW PLAYER BIKE GLOW
        int glowSize = (int) (MyUtils.PLAYER_TILE_SIZE * 1.1 * pulse);
        int glowOffset = (glowSize - MyUtils.PLAYER_TILE_SIZE) / 2;
        g2.setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), 100));
        g2.fillOval((int)this.playerX - glowOffset, (int)this.playerY - glowOffset, glowSize, glowSize);

        // 3. DRAW PLAYER BIKE BODY
        g2.setColor(Color.WHITE); // White center for the bike
        g2.fillRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);
        g2.setColor(this.color); // Colored border
        g2.setStroke(new BasicStroke(2));
        g2.drawRect((int) this.playerX, (int) this.playerY, MyUtils.PLAYER_TILE_SIZE, MyUtils.PLAYER_TILE_SIZE);

        // TEST - Collision Explosion
        for (Spark s : sparks) {
            s.update();
            s.draw(g2);
        }
    }


    public void move(int mapWidth, int mapHeight, long currentGameTime) {
        double prevX = this.playerX;
        double prevY = this.playerY;
        double playerVelocity = MyUtils.GAME_TILE_SIZE * this.moveSpeed * (1.0 / MyUtils.GAME_FPS);

        switch (this.direction) {
            case MyUtils.DIRECTION_LEFT:
                this.playerX -= playerVelocity;
                break;
            case MyUtils.DIRECTION_RIGHT:
                this.playerX += playerVelocity;
                break;
            case MyUtils.DIRECTION_UP:
                this.playerY -= playerVelocity;
                break;
            case MyUtils.DIRECTION_DOWN:
                this.playerY += playerVelocity;
                break;
        }

        int offset = MyUtils.PLAYER_TILE_SIZE / 2;
        long currentTime = currentGameTime;

        synchronized (this) {
//            this.trailPoints.add(new Breadcrumb(this.playerX + offset, this.playerY + offset));
            this.trailPoints.add(new Breadcrumb(this.playerX + offset, this.playerY + offset, currentGameTime));

            synchronized (trailSegments) {
                this.trailSegments.add(new TrailSegment(
                        prevX + offset, prevY + offset,
                        this.playerX + offset, this.playerY + offset,
                        currentTime
                ));
            }

            this.trailSegments.removeIf(ts -> ts.isExpired(currentTime));
            this.trailPoints.removeIf(p -> (currentTime - p.creationTime) > TRAIL_LIFETIME);
        }

        getGridPosition();
    }

    public void setDirection(int newDirection) {
        // Add Delay To Turn
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - this.lastTurnTime < MyUtils.PLAYER_TURN_DELAY) {
//            return; // Too fast! Ignore this key press.
//        }

        // Dont allow 180 turns
        if (this.direction == newDirection) return;
        if (this.direction == MyUtils.DIRECTION_UP && newDirection == MyUtils.DIRECTION_DOWN) return;
        if (this.direction == MyUtils.DIRECTION_DOWN && newDirection == MyUtils.DIRECTION_UP) return;
        if (this.direction == MyUtils.DIRECTION_LEFT && newDirection == MyUtils.DIRECTION_RIGHT) return;
        if (this.direction == MyUtils.DIRECTION_RIGHT && newDirection == MyUtils.DIRECTION_LEFT) return;


        // Makes Current Direction Last Direction Before Turning
        this.lastPointX = this.playerX;
        this.lastPointY = this.playerY;

        this.lastTurnTime = System.currentTimeMillis();
        this.direction = newDirection;
    }


    // TEST - Cool Trick, may Implement Later :D
//    public void setDirection(int newDirection) {
//        // If the difference is 2, they are opposites (e.g., |0 - 2| = 2 or |1 - 3| = 2)
//        if (Math.abs(this.direction - newDirection) == 2) {
//            return;
//        }
//        this.direction = newDirection;
//    }

    /**
     * Checks if the player has hit their own trail or another player's trail.
     */
    public boolean checkCollision(List<TrailSegment> otherPlayerSegments) {
        // 1. Initial Safety: Don't die in the first half-second of the game
        if (this.trailSegments.size() < MyUtils.PLAYER_SAFE_SEGMENT) return false;

        int offset = MyUtils.PLAYER_TILE_SIZE / 2;

        TrailSegment lastSeg = this.trailSegments.get(this.trailSegments.size() - 1);
        Line2D.Double headPath = new Line2D.Double(
                lastSeg.line.x2, lastSeg.line.y2,
                this.playerX + offset, this.playerY + offset
        );

        int safetyGap = MyUtils.PLAYER_SAFE_SEGMENT;

        if (this.trailSegments.size() > safetyGap) {
            for (int i = 0; i < this.trailSegments.size() - safetyGap; i++) {
                if (headPath.intersectsLine(this.trailSegments.get(i).line)) {
                    return true;
                }
            }
        }

        if (otherPlayerSegments != null) {
            for (int i = 0; i < otherPlayerSegments.size(); i++) {
                if (headPath.intersectsLine(otherPlayerSegments.get(i).line)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void respawn(int startX, int startY, int startDir) {
        this.playerX = startX;
        this.playerY = startY;
        this.direction = startDir;
        this.trailSegments.clear();
        this.trailPoints.clear();
        this.lastPointX = startX;
        this.lastPointY = startY;
        this.isAlive = true;
    }

    public void triggerExplosion() {
        this.sparks.clear(); // Clear old ones just in case
        for (int i = 0; i < 40; i++) {
            this.sparks.add(new Spark(this.playerX, this.playerY, this.color));
        }
    }


    private void getGridPosition() {
        this.gridX = (int) (this.playerX / MyUtils.GAME_TILE_SIZE);
        this.gridY = (int) (this.playerY / MyUtils.GAME_TILE_SIZE);
    }

    public List<TrailSegment> getTrailSegments() {
        return this.trailSegments;
    }


    public static class TrailSegment {
        public Line2D.Double line;
        public long creationTime;

        public TrailSegment(double x1, double y1, double x2, double y2, long creationTime) {
            this.line = new Line2D.Double(x1, y1, x2, y2);
            this.creationTime = creationTime;
        }

        // Helper to check if this segment should still exist
        public boolean isExpired(long currentGameTime) {
            return currentGameTime - creationTime > MyUtils.PLAYER_TRAIL_LIFETIME;
        }
    }

    // Trial Point "Leaving Breadcrumbs" :D
    private static class Breadcrumb {
        double x, y;
        long creationTime;

        public Breadcrumb(double x, double y, long gameTime) {
            this.x = x;
            this.y = y;
            this.creationTime = gameTime;
        }
    }

    private static class Spark {
        double x, y, vx, vy;
        Color color;
        float life = 1.0f; // 1.0 is full, 0.0 is gone

        public Spark(double x, double y, Color c) {
            this.x = x;
            this.y = y;
            // Random direction and speed
            this.vx = (Math.random() - 0.5) * 15;
            this.vy = (Math.random() - 0.5) * 15;
            this.color = c;
        }

        public void update() {
            x += vx;
            y += vy;

            vy += 0.15; // NEW: Gravity pulls sparks down slightly
            vx *= 0.98; // Friction
            vy *= 0.98;

            life -= 0.015f; // Fade a bit slower for fireworks
        }

        public void draw(Graphics2D g2) {
            if (life <= 0) return;
            int alpha = (int) (life * 255);
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            // Draw as a small flickering square
            g2.fillRect((int)x, (int)y, 5, 5);
        }
    }
}