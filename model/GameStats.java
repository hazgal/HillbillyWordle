package model;

public class GameStats {
    private final String username;
    private final String secretWord;
    private final int attempts;
    private final boolean won;

    public GameStats(String username, String secretWord, int attempts, boolean won) {
        this.username = username;
        this.secretWord = secretWord;
        this.attempts = attempts;
        this.won = won;
    }

    public String toCSV() {
        return username + "," + secretWord.toUpperCase() + "," + attempts + "," + (won ? "win" : "loss");
    }
}
