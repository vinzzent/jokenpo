package server;

import java.util.Random;

public class JokenpoLogic {
    private static final String[] validWords = {"pedra", "papel", "tesoura"};
    private static final String[] defeatedWords = {"tesoura", "pedra", "papel"};

    protected static String[] getValidWords() {
        return validWords;
    }

    private static String[] getDefeatedWords() {
        return defeatedWords;
    }

    protected static String checkResult(String word1, String word2) {
        String[] words = getValidWords();
        String[] defWords = getDefeatedWords();
        String result = null;
        if (word1.equals(word2)) {
            result = "0";
        } else {
            for (int i = 0; i < words.length; i++) {
                if (words[i].equals(word1)) {
                    if (defWords[i].equals(word2)) {
                        result = "1";
                        break;
                    }
                } else {
                    result = "2";
                }
            }
        }
        return result;
    }

    protected static String randomWeapon() {
        Random rand = new Random();
        int n = rand.nextInt(2);
        return getValidWords()[n];
    }
}