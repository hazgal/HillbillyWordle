import java.util.Scanner;
import java.util.Random;

public class WordleGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            io.WordList wordList = new io.WordList(); // No file path needed now
            Random random = new Random();
            int wordIndex = random.nextInt(wordList.size());
            String secretWord = wordList.getWord(wordIndex);
            game.GameEngine engine = new game.GameEngine(secretWord);
            engine.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
