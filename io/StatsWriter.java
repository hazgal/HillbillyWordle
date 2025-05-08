package io;

import model.GameStats;

import java.io.*;
import java.util.Scanner;

public class StatsWriter {
    private static final String FILE_NAME = "stats.csv";

    public static void writeStats(GameStats stats) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            fw.write(stats.toCSV() + "\n");
        } catch (IOException e) {
            System.out.println("Failed to write stats: " + e.getMessage());
        }
    }

    public static void printStats() {
        try (Scanner scanner = new Scanner(new File(FILE_NAME))) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("No stats file found.");
        }
    }
}