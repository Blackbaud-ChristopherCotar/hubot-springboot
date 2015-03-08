package bot.handlers;

import bot.entities.SlackChatMessage;

/**
 * Created by chris on 3/7/15.
 */
public interface Handler {
    boolean handlesCommand(String command);

    String processCommand(SlackChatMessage slackChatMessage);
}
