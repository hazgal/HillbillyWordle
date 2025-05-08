package game;

import io.SoundPlayer;
import io.StatsWriter;
import model.GameStats;

import java.io.IOException;
import java.util.*;

public class GameEngine {
    private final Scanner scanner;
    private final Set<Character> remainingLetters;
    private final String art;
    private final SoundPlayer player;
    private String previousWord = null;

    private static final Map<String, String> wordMeanings = Map.ofEntries(
            Map.entry("TATER", "The king o’ side dishes, best with gravy or fried up crispy."),
            Map.entry("GASSY", "What ya get after Ma's chili night."),
            Map.entry("BUBBA", "Your cousin, uncle, and neighbor — all rolled into one."),
            Map.entry("HOOCH", "Backwoods rocket fuel that’ll curl yer nose hairs."),
            Map.entry("CRICK", "A tiny river where you go fishin’ or skinny dippin’."),
            Map.entry("PLUMB", "As in 'plumb loco' or 'plumb wore out'."),
            Map.entry("SPITT", "What comes after a chaw of tobacky."),
            Map.entry("BRUNG", "What ya done brung to the potluck."),
            Map.entry("CLUCK", "Yer dinner, still runnin’ ‘round."),
            Map.entry("YODEL", "The noise ya make after sippin’ hooch."),
            Map.entry("DANDY", "That feller who wears shoes to the barn."),
            Map.entry("SKEER", "Fancy word for scared witless."),
            Map.entry("LUMPY", "Ma’s gravy ain't never smooth."),
            Map.entry("HUFFY", "When yer mule’s got an attitude."),
            Map.entry("SNORT", "How ya laugh or sip moonshine."),
            Map.entry("WRASS", "Half rassle, half wrangle."),
            Map.entry("OINKY", "The pig who thinks it’s a lapdog."),
            Map.entry("ZIPPY", "Faster than cousin Earl on a dare."),
            Map.entry("RUMPS", "Where ya land when ya fall off the porch."),
            Map.entry("JANKY", "Broken but still kickin’."),
            Map.entry("NAPPY", "A nap so good, you miss supper."),
            Map.entry("FUNKY", "That smell waftin’ from the barn."),
            Map.entry("DOOKY", "Ya really don’t wanna step in it."),
            Map.entry("LARDS", "Secret to all of Granny’s cookin’."),
            Map.entry("SLOSH", "Sound ya make after a 6-pack."),
            Map.entry("HANKS", "Not Tom — just a wad of rope."),
            Map.entry("MUNCH", "The sound of pig rinds disappearin’."),
            Map.entry("GONER", "What the outhouse is after the storm."),
            Map.entry("NANNY", "Yer goat or yer aunt — or both."),
            Map.entry("RETCH", "What happens after bad hooch and fish sticks.")
    );

    private static final List<String> guessPhrases = Arrays.asList(
            "Go on, take a wild guess at this here word, partner!",
            "Give it yer best shot, what's yer guess?",
            "Well, ain’t no time like the present – what’s yer guess?",
            "Alright, step up to the plate, what’s yer guess?",
            "Come on, put yer noggin to work, take a guess!",
            "Take a wild shot, don't be shy!",
            "I reckon it’s time for a guess, what ya got?",
            "Put yer thinking cap on, what’s yer guess?"
    );

public GameEngine(String secretWord) {
    this.scanner = new Scanner(System.in);
    this.remainingLetters = new TreeSet<>();
    for (char c = 'A'; c <= 'Z'; c++) remainingLetters.add(c);

this.art =
        "\033[32m" +
        "⠀⠀⠀                   ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀\n" +
        "⠀⠀⠀                   ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡀⠙⠿⠀⣀⠀\n" +
        "⠀⠀⠀                  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠋⣀⣀⣾⣿⣿⡄⠀\n" +
        "⠀⠀     ⠀⠀⠀⠀⠀⠀⠀              ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⣿⡟⠉⢠⣤⡀\n" +
        "⠀⠀⠀⠀		      ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠃⣀⠙⠛⠃⣠⡀⠙⠁⠀\n" +
        "⠀⠀	    	  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠋⣠⠞⠁⠀⠀⠀⠈⠁\n" +
        "┌┬┐┬ ┬┌─┐┌┬┐  ┌─┐┬  ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠋⣠⠞⠁\n" +
        " │ ├─┤├─┤ │   │ ││  ⠀⠀⠀⠀⢀⣠⣤⣶⣶⣦⣤⠀⠀⡴⠋⣠⠞⠁\n" +
        " ┴ ┴ ┴┴ ┴ ┴   └─┘┴─┘⠀⠀⢀⣴⣿⣿⣿⣿⣿⠟⣡⡾⠂⡀⠚⠁\n" +
        "╦ ╦┬ ┬┬─┐┬─┐┌┬┐┬    ⠀⢀⣾⣿⣿⣿⡿⠋⣡⡾⠋⣠⡾⢋⣤\n" +
        "║║║│ │├┬┘├┬┘ │││    ⠀⢸⣿⣿⣅⠉⢠⡾⠋⣠⡾⢋⣴⣿⣿⡆\n" +
        "╚╩╝└─┘┴└─┴└──┴┘┴─┘  ⠀⠸⣿⣿⣿⣷⣄⠀⠾⠋⣠⣾⣿⣿⣿⠃\n" +
        "┌─┐┌─┐┌┬┐┌─┐┬       ⠀⠀⢻⣿⣿⣿⣿⣷⣄⢘⣿⣿⣿⣿⠏\n" +
        "│ ┬├─┤│││├┤ │       ⠀⠀⠀⠙⠿⣿⣿⣿⣿⣿⣿⡿⠟⠁\n" +
        "└─┘┴ ┴┴ ┴└─┘o       ⠀⠀⠀⠀⠀⠀⠈⠉⠉⠉⠁ " +
        "\033[0m";

    this.player = new SoundPlayer();
    // Use the secretWord here if necessary for other initializations
}

    public void run() {
        // Start music immediately
        player.playSoundLoop("words.wav");

        clearConsole();
        System.out.println(art);
	System.out.println("");
	System.out.println("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->");
        System.out.println("Well kiss my grits and call me granny -- we got ourselves company!\nScoot yer boots on over here and I'll jaw your ear off 'bout hillbilly shenanigans.\n");
        System.out.println("The word \"hillbilly\" got its start a long time ago, way back in the 18th century.");
        System.out.println("It all began with them Scots-Irish folks who moved into the mountains, settlin' in the hollers of Appalachia.");
        System.out.println("They came from the highlands of Scotland and Ireland, bringin' along their old ways, music, and talk.");
        System.out.println("Over time, folks started callin' 'em \"hillbillies\" 'cause they lived up high in them hills, away from the rest of the world.");
        System.out.println("Some folks used it to make fun of 'em, sayin' they was backward and uneducated.");
        System.out.println("But them hillbillies weren't about to take that lightly.");
        System.out.println("They kept their culture strong, with their own kind of music, stories, and ways of doin' things.");
	System.out.println("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->");
        System.out.println("\nSmack that Enter key, partner, an' let me teach ya some of them hillbilly words!\n");
        scanner.nextLine();

        System.out.println("Now hold yer horses 'fore we learn us some hillbilly lingo...\nY’all got a name, or what?");
        System.out.print("I sure as shootin' do! It's called... ");
        String username = scanner.nextLine();

        boolean keepPlaying = true;

        while (keepPlaying) {
            clearConsole();

            // Randomize a secret word that’s not the same as the last one
            String secretWord;
            do {
                List<String> keys = new ArrayList<>(wordMeanings.keySet());
                secretWord = keys.get(new Random().nextInt(keys.size()));
            } while (secretWord.equalsIgnoreCase(previousWord));
            previousWord = secretWord;

            int attempts = 0;
            boolean won = false;
            remainingLetters.clear();
            for (char c = 'A'; c <= 'Z'; c++) remainingLetters.add(c);

            while (attempts < 6) {
                String randomPhrase = guessPhrases.get(new Random().nextInt(guessPhrases.size()));
		System.out.println("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->");
                System.out.println("\n" + randomPhrase + " " + username + "!\n");
                System.out.println("How many more kicks ‘fore the mule drops: " + (6 - attempts));
                System.out.println("Them letters ya still got kickin’: " + getRemainingLetters());
                System.out.print("\nTake a stab at some o’ that backwoods babblin: ");
                String guess = scanner.nextLine().toUpperCase();
		
                if (guess.length() != 5) {
                    System.out.println("Whoa there, slick — gimme five letters, not whatever that mess was.");
                    continue;
                }

                attempts++;
                String feedback = FeedbackGenerator.getFeedback(secretWord.toUpperCase(), guess);
                System.out.println(feedback);

                for (char c : guess.toCharArray()) {
                    remainingLetters.remove(c);
                }

                if (guess.equals(secretWord.toUpperCase())) {
                    System.out.println("\nWell butter my backside and call me a biscuit — ya done guessed it!");
                    won = true;
                    break;
                }
            }

            if (!won) {
                System.out.println("\nAww shoot, you done lost! That there word was: " + secretWord.toUpperCase());
            }

            String explanation = wordMeanings.getOrDefault(secretWord.toUpperCase(), "Ain’t got no idea what that means, but it sure sounds hillbilly enough.");
            System.out.println("\nWord meaning: " + explanation);

            GameStats stats = new GameStats(username, secretWord, attempts, won);
            StatsWriter.writeStats(stats);

            String again;
            do {
                System.out.print("\nWanna give it another whirl? (y/n): ");
                again = scanner.nextLine().trim().toLowerCase();
                if (!(again.equals("y") || again.equals("yes") || again.equals("n") || again.equals("no"))) {
                    System.out.println("Invalid input, please type 'y' for yes or 'n' for no.");
                }
            } while (!(again.equals("y") || again.equals("yes") || again.equals("n") || again.equals("no")));

            if (!again.equals("y") && !again.equals("yes")) {
                String showStats;
                do {
                    System.out.print("Wanna see how ya did, numbers-wise? (y/n): ");
                    showStats = scanner.nextLine().trim().toLowerCase();
                    if (!(showStats.equals("y") || showStats.equals("yes") || showStats.equals("n") || showStats.equals("no"))) {
                        System.out.println("That don’t make no sense, partner! Hit 'y' if ya reckon yes, or 'n' if ya think no.");
                    }
                } while (!(showStats.equals("y") || showStats.equals("yes") || showStats.equals("n") || showStats.equals("no")));

                if (showStats.equals("y") || showStats.equals("yes")) {
                    StatsWriter.printStats();
                } else {
		    System.out.println("->->->->->->->->->->->->->->->->->->->->->->->->->->->->->->");
                    System.out.println("Well, partner, I reckon it’s time to mosey on down the trail. You take care now, y’hear?\nDon’t let the door hit ya where the good Lord split ya.\nTil we meet again, keep yer boots clean and yer chin up!");
                }
                keepPlaying = false;
            }
        }
    }

    private String getRemainingLetters() {
        StringBuilder sb = new StringBuilder();
        for (char c : remainingLetters) {
            sb.append(c).append(" ");
        }
        return sb.toString().trim();
    }

// from the notes group task
    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: Failed to clear the console.");
        }
    }
}
