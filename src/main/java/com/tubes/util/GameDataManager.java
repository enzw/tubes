package com.tubes.util;

import java.io.FileWriter;
import java.io.IOException;

public class GameDataManager {
    public static void saveScore(int score) {
        try (FileWriter writer = new FileWriter("score.txt")) {
            writer.write(String.valueOf(score));
        } catch (IOException e) {
            System.err.println("Gagal menyimpan skor: " + e.getMessage());
        }
    }
}