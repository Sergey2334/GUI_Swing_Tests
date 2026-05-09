package GUI_Tests.Managers;

import GUI_Tests.Utilities.MyUtils;

public class ScoreManager {
    private int player1Score = 0;
    private int player2Score = 0;
    private final int winScore = MyUtils.PLAYER_WIN_SCORE;
    private final String player1Name = MyUtils.PLAYER1_NAME;
    private final String player2Name = MyUtils.PLAYER2_NAME;

    public void addScore(int playerNumber) {
        if (playerNumber == 1) {
            this.player1Score++;
        } else if (playerNumber == 2) {
            this.player2Score++;
        }
    }

    public void resetScores() {
        this.player1Score = 0;
        this.player2Score = 0;
    }

    public boolean hasWinner() {
        return this.player1Score >= this.winScore || this.player2Score >= this.winScore;
    }

    public String getWinnerName() {
        if (this.player1Score >= this.winScore) return this.player1Name;
        if (this.player2Score >= this.winScore) return this.player2Name;
        return "NONE";
    }

    // Getters for rendering
    public int getP1Score() { return this.player1Score; }
    public int getP2Score() { return this.player2Score; }
}