package bot.handlers;

import bot.entities.SlackChatMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hangman Game handler
 *
 * Creates a new hangman game for the entire room
 *
 * {botname} hangman -> 'new hangman game with random word'
 */
public class Hangman implements Handler {

    private Map<String, HangmanGame> channelToGameMap = new HashMap<>();
    private static final Pattern NEW_GAME_PATTERN = Pattern.compile("\\s*hangman\\s+start\\s*", Pattern.CASE_INSENSITIVE);
    private static final Pattern GUESS_PATTERN = Pattern.compile("\\s*hangman\\s+guess\\s+([a-z]+)\\s*", Pattern.CASE_INSENSITIVE);

    public Hangman() {}

    /**
     *
     * @param command command part of message text after {botname}
     * @return boolean if this handler will process the command
     */
    @Override
    public boolean handlesCommand(String command) {
        System.out.println(command);
        Pattern p = Pattern.compile("^.*hangman.*$");
        Matcher m = p.matcher(command);
        if(m.matches()) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param slackChatMessage the message from Slack
     * @return String the message to send back to Slack
     */
    @Override
    public String processCommand(SlackChatMessage slackChatMessage) {
        StringBuilder response = new StringBuilder();

        String command = slackChatMessage.getText();
        System.out.println("hangman command: " + command);
        String channel = slackChatMessage.getChannel();

        if (NEW_GAME_PATTERN.matcher(command).matches()) {
            if(isGameStartedForChannel(channel)) {
                response.append("A game already started for this channel!\n\n");
                HangmanGame game = channelToGameMap.get(channel);
                response.append("What's going down:\n");
                response.append(game.printBoard());
                response.append(game.printGuesses());
                response.append(game.printMissesLeft());
            } else {
                response.append("Starting new game of Hangman!\n\n");
                HangmanGame newGame = new HangmanGame();
                channelToGameMap.put(channel, newGame);
                newGame.getRandomWord();
                response.append(newGame.printBoard());
            }
        } else if (GUESS_PATTERN.matcher(command).matches()) {
            if(isGameStartedForChannel(channel)) {
                HangmanGame game = channelToGameMap.get(channel);
                Matcher matcher = GUESS_PATTERN.matcher(command);
                matcher.matches();
                String guess = matcher.group(1);
                if(guess.length() > 1 && game.guessWholeWord(guess)) {
                    response.append("You guessed the right word!  It was " + guess + "\n\n");
                    response.append(game.printBoard());
                    response.append(game.printGuesses());
                    response.append(game.printMissesLeft());
                    channelToGameMap.put(channel, null);
                } else {
                    if(game.guessedBefore(guess)){
                        response.append(guess + " has been guessed before.\n");
                        response.append(game.printGuesses());
                    } else if(game.guess(guess)) {
                        if(game.isGameWon()) {
                            response.append("YOU WIN!\n\n");
                            response.append(game.printBoard());
                            response.append(game.printGuesses());
                            response.append(game.printMissesLeft());
                            channelToGameMap.put(channel, null);
                        } else {
                            response.append("Good guess!\n\n");
                            response.append(game.printBoard());
                            response.append(game.printGuesses());
                            response.append(game.printMissesLeft());
                        }
                    } else {
                        response.append("Incorrect guess!\n");
                        response.append(game.printBoard());
                        response.append(game.printGuesses());
                        response.append(game.printMissesLeft());
                        if(game.isGameLost()) {
                            response.append("GAME OVER\n\nDeleting game!");
                            channelToGameMap.put(channel, null);
                        }
                    }
                }
            } else {
                response.append("A game has not been started for this channel.\nType\"hangman start\" to start a new game.");
            }
        }
        return response.toString();
    }

    boolean isGameStartedForChannel(String channelId) {
        if(channelToGameMap.containsKey(channelId)) {
            return true;
        } else {
            return false;
        }
    }

}
