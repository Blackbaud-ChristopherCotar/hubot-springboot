package bot.handlers;

import bot.entities.SlackChatMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ping handler
 *
 * {botname} ping -> pong
 */
public class Ping {

    public Ping() {}

    /**
     *
     * @param command command part of message text after {botname}
     * @return boolean if this handler will process the command
     */
    public boolean handlesCommand(String command) {
        System.out.println(command);
        Pattern p = Pattern.compile("^.*ping.*$");
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
    public String processCommand(SlackChatMessage slackChatMessage) {
        return "pong @" + slackChatMessage.getUser();
    }


}
