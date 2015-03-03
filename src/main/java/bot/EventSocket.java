package bot;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bot.entities.SlackChatMessage;
import bot.entities.SlackRTMResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class EventSocket {

    private final CountDownLatch closeLatch;
    private int idCounter = 0;
    private String botName = "";
    private String botUserId = "";

    private static final String SLACK_TYPE_MESSAGE = "message";

    @SuppressWarnings("unused")
    private Session session;
    private SlackRTMResponse state;

    @Autowired
    public EventSocket() {
        this.closeLatch = new CountDownLatch(1);
    }

    public void setState(SlackRTMResponse state) {
        this.state = state;
        this.botName = state.getSelf().getName();
        this.botUserId = state.getSelf().getId();
    }

    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Got connect: %s%n", session);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        ObjectMapper mapper = new ObjectMapper();
        Dispatcher dispatcher = new Dispatcher();

        try {
            SlackChatMessage chatMessage = mapper.readValue(msg, SlackChatMessage.class);
            if(isBotCommand(chatMessage)) {
                String command = extractBotCommand(chatMessage.getText());
                chatMessage.setText(command);
                SlackChatMessage response = new SlackChatMessage();
                response.setId(++idCounter);
                response.setChannel(chatMessage.getChannel());
                response.setText(dispatcher.respond(chatMessage));
                response.setType(SLACK_TYPE_MESSAGE);
                session.getRemote().sendString(mapper.writeValueAsString(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Got msg: %s%n", msg);
    }

    public boolean isBotCommand(SlackChatMessage chatMessage) {
        if(chatMessage.getType() != null
                && chatMessage.getType().equals(SLACK_TYPE_MESSAGE)
                && chatMessage.getText() != null
                && extractBotCommand(chatMessage.getText()) != null) {
            return true;
        }

        return false;
    }

    public String extractBotCommand(String text) {
        Pattern p = Pattern.compile("^\\s*(" + botName + "|<@" + botUserId +  ">:?)(.*)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        if(m.matches()) {
            return m.group(2).trim();
        }
        return null;
    }
}