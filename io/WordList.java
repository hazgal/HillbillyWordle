package io;

import java.util.List;
import java.util.Arrays;

public class WordList {
    private final List<String> words;

    public WordList() {
        // Hardcoded list of words
        words = Arrays.asList(
            "TATER", "GASSY", "BUBBA", "HOOCH", "CRICK", "PLUMB", "SPITT", 
            "BRUNG", "CLUCK", "YODEL", "DANDY", "SKEER", "LUMPY", "HUFFY", 
            "SNORT", "WRASS", "OINKY", "ZIPPY", "RUMPS", "JANKY", "NAPPY", 
            "FUNKY", "DOOKY", "LARDS", "SLOSH", "HANKS", "MUNCH", "GONER", 
            "NANNY", "RETCH", "BARNY", "MUDDY", "SASSY", "CATTY", "RAGGY", 
            "WOBBY", "SCRAT", "POKEY", "SPURT", "BOOGY", "GUMBY", "SWANK", 
            "TWANG", "FLUFF", "NINNY", "FIZZY", "JOLLY", "WIMPY", "YANKY", 
            "BRITZ", "BILLY", "LOOPY", "SCOOT", "LOOTY", "SHADY", "GAPPY", "SCREE"
        );
    }

    public String getWord(int index) throws Exception {
        if (index < 0 || index >= words.size()) {
            throw new Exception("That there index ain't valid. It oughta be from 0 to " + (words.size() - 1));
        }
        return words.get(index).toLowerCase();
    }

    public int size() {
        return words.size();
    }
}
