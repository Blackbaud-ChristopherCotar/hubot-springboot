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

    private static final String SLACK_TYPE_MESSAGE = "message";

    @SuppressWarnings("unused")
    private Session session;
    private Dispatcher dispatcher;

    @Autowired
    public EventSocket() {
        this.closeLatch = new CountDownLatch(1);
    }

    public void setState(SlackRTMResponse roomState) {
        this.dispatcher = new Dispatcher(roomState);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Got connection: %s%n", session);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String msg) {
        SlackChatMessage chatMessage = parseMsg(msg);
        String chatResponse = dispatcher.processMessage(chatMessage);
        if(chatResponse != null) {
            try {
                session.getRemote().sendString(chatResponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Got msg: %s%n", msg);
    }

    SlackChatMessage parseMsg(String message) {
        ObjectMapper mapper = new ObjectMapper();
        SlackChatMessage slackChatMessage = null;
        try {
            slackChatMessage = mapper.readValue(message, SlackChatMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return slackChatMessage;
    }

}