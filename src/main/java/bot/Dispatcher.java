package bot;

import bot.entities.SlackChatMessage;
import bot.entities.SlackRTMResponse;
import bot.handlers.Ping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Evaluates commands and calls the correct handler
 */
public class Dispatcher {

    private static final String SLACK_TYPE_MESSAGE = "message";

    private int idCounter = 0;
    private SlackRTMResponse roomState;

    public Dispatcher(SlackRTMResponse slackRTMResponse){
        this.roomState = slackRTMResponse;
    }

    public String processMessage(SlackChatMessage slackChatMessage) {
        if(isBotCommand(slackChatMessage)) {
            return respond(slackChatMessage);
        } else {
            return null;
        }
    }

    String respond(SlackChatMessage slackChatMessage) {
        System.out.println("responding...");

        String message = slackChatMessage.getText();
        String command = extractBotCommand(message);

        Ping ping = new Ping();
        if(ping.handlesCommand(command)) {
            String handlerMessage = ping.processCommand(slackChatMessage);
            return processValidChatResponse(slackChatMessage, handlerMessage);
        }

        // no handler processes this command, is null a could return value? meh
        return processValidChatResponse(slackChatMessage, "BLARG");
    }

    String processValidChatResponse(SlackChatMessage slackChatMessage, String handlerMessage) {
        ObjectMapper mapper = new ObjectMapper();

        SlackChatMessage response = new SlackChatMessage();
        response.setId(++idCounter);
        response.setChannel(slackChatMessage.getChannel());
        response.setText(handlerMessage);
        response.setType(SLACK_TYPE_MESSAGE);

        String responseText = "";
        try {
            responseText =  mapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return responseText;
    }

    String extractBotCommand(String text) {
        String botName = roomState.getSelf().getName();
        String botId = roomState.getSelf().getId();

        Pattern p = Pattern.compile("^\\s*(" + botName + "|<@" + botId +  ">:?)(.*)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if(m.matches()) {
            return m.group(2).trim();
        }
        return null;
    }

    boolean isBotCommand(SlackChatMessage chatMessage) {
        if(chatMessage.getType() != null
                && chatMessage.getType().equals(SLACK_TYPE_MESSAGE)
                && chatMessage.getText() != null
                && extractBotCommand(chatMessage.getText()) != null) {
            return true;
        }

        return false;
    }
}
