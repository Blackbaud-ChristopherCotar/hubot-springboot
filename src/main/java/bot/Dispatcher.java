package bot;

import bot.entities.SlackChatMessage;
import bot.handlers.Ping;

/**
 * Evaluates commands and calls the correct handler
 */
public class Dispatcher {

    public Dispatcher(){}

    public String respond(SlackChatMessage slackChatMessage) {
        String message = slackChatMessage.getText();

        Ping ping = new Ping();
        if(ping.handlesCommand(message)) {
            return ping.processCommand(slackChatMessage);
        }

        // no handler processes this command
        return "I can't process your command!";
    }
}
