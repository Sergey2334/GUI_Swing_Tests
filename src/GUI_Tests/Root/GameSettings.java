package GUI_Tests.Root;

import GUI_Tests.Utilities.MyUtils;
import java.awt.Color;

public class GameSettings {
    private int speed = MyUtils.GAME_SPEED_MEDIUM;
    private long trailLifetime = MyUtils.PLAYER_TRAIL_LIFETIME_MEDIUM;
    private double shrinkSpeed = MyUtils.GAME_ARENA_INSET_SHRINK_SPEED_MEDIUM;
    private int winScore = MyUtils.PLAYER_WIN_SCORE_MEDIUM;
    private boolean vsAI = false; // The new AI flag

    private Color p1Color = MyUtils.TRON_COLORS[0];
    private Color p2Color = MyUtils.TRON_COLORS[2];

    public void setDifficulty(String type) {
        switch(type) {
            case "RELAXED" -> {
                this.speed = MyUtils.GAME_SPEED_SLOW;
                this.trailLifetime = MyUtils.PLAYER_TRAIL_LIFETIME_SHORT;
                this.shrinkSpeed = MyUtils.GAME_ARENA_INSET_SHRINK_SPEED_SLOW;
                this.winScore = MyUtils.PLAYER_WIN_SCORE_EASY;
            }
            case "HYPER" -> {
                this.speed = MyUtils.GAME_SPEED_FAST;
                this.trailLifetime = MyUtils.PLAYER_TRAIL_LIFETIME_LONG;
                this.shrinkSpeed = MyUtils.GAME_ARENA_INSET_SHRINK_SPEED_FAST;
                this.winScore = MyUtils.PLAYER_WIN_SCORE_HARD;
            }
            default -> {
                this.speed = MyUtils.GAME_SPEED_MEDIUM;
                this.trailLifetime = MyUtils.PLAYER_TRAIL_LIFETIME_MEDIUM;
                this.shrinkSpeed = MyUtils.GAME_ARENA_INSET_SHRINK_SPEED_MEDIUM;
                this.winScore = MyUtils.PLAYER_WIN_SCORE_MEDIUM;
            }
        }
    }

    // New AI Methods
    public boolean isVsAI() { return this.vsAI; }
    public void toggleAI() { this.vsAI = !this.vsAI; }

    // Getters and Setters
    public int getSpeed() { return this.speed; }
    public long getTrailLifetime() { return this.trailLifetime; }
    public double getShrinkSpeed() { return this.shrinkSpeed; }
    public int getWinScore() { return this.winScore; }
    public Color getP1Color() { return this.p1Color; }
    public void setP1Color(Color c) { this.p1Color = c; }
    public Color getP2Color() { return this.p2Color; }
    public void setP2Color(Color c) { this.p2Color = c; }
}