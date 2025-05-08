package game;

public class FeedbackGenerator {
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String WHITE = "\u001B[37m";
    public static final String RESET = "\u001B[0m";

    public static String getFeedback(String secret, String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            char g = guess.charAt(i);
            if (g == secret.charAt(i)) {
                feedback.append(GREEN).append(g).append(RESET);
            } else if (secret.contains(String.valueOf(g))) {
                feedback.append(YELLOW).append(g).append(RESET);
            } else {
                feedback.append(WHITE).append(g).append(RESET);
            }
        }
        return feedback.toString();
    }
}
