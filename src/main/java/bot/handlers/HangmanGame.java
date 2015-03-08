package bot.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An instance of a game for the Hangman handler
 */
public class HangmanGame {

    private String word = "";
    private int maxMisses = 9;
    private int leftToGeuss = 0;
    private Map<String, Integer> wordMap;
    private ArrayList<Integer> wordToCorrectGuessMap;
    private Map<String, Integer> guesses = new HashMap<>();

    public HangmanGame() {}

    public boolean isGameWon() {
        return leftToGeuss <= 0;
    }

    public boolean isGameLost() {
        return maxMisses <= 0;
    }

    public void getRandomWord() {
        // TODO use an API or a static list of words!
        word = "a";
        leftToGeuss = word.length();
        initializeWordMap();
    }

    void initializeWordMap() {
        wordToCorrectGuessMap = new ArrayList<>();
        wordMap = new HashMap<>();
        for(int i = 0; i < word.length(); i++) {
            Character c = word.charAt(i);
            Integer charCount = wordMap.get(c.toString());
            if(charCount != null) {
                wordMap.put(c.toString(), ++charCount);
            } else {
                wordMap.put(c.toString(), 0);
            }
            wordToCorrectGuessMap.add(0);
        }
    }

    public boolean guessWholeWord(String guess) {
        if(guess.equalsIgnoreCase(word)) {
            leftToGeuss = 0;
            return true;
        }
        return false;
    }

    boolean guessedBefore(String guess) {
        return guesses.containsKey(guess);
    }

    public boolean guess(String guess) {
        if(wordMap.containsKey(guess)) {
            leftToGeuss = wordMap.get(guess);
            for(int i = 0; i < word.length(); i++) {
                if (guess.equalsIgnoreCase(((Character)word.charAt(i)).toString())) {
                    wordToCorrectGuessMap.set(i, 1);
                }
            }
            return true;
        } else {
            guesses.put(guess, 1);
            maxMisses--;
            return false;
        }
    }

    public String printGuesses() {
        StringBuilder builder = new StringBuilder("Guesses so far:\n----------\n");
        int i = 1;
        for(String guess : guesses.keySet()) {
            builder.append(guess + " ");
            if(i++ % 5 == 0) builder.append("\n");
        }
        return builder.append("\n").toString();
    }

    public String printMissesLeft() {
        return "Everyone has " + getMaxMisses() + " misses left!\n\n";
    }

    public String printBoard() {
        StringBuilder board = new StringBuilder();
        for(int i = 0; i < word.length(); i++) {
            if(wordToCorrectGuessMap.get(i) > 0) {
                board.append(word.charAt(i) + " ");
            } else {
                board.append("__ ");
            }
        }
        return board.append("\n").toString();
    }

    public int getMaxMisses() {
        return maxMisses;
    }

    public void setMaxMisses(int maxMisses) {
        this.maxMisses = maxMisses;
    }
}
