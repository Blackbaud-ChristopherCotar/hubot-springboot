package bot;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bot.entities.SlackChatMessage;
import bot.entities.SlackRTMResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class EventSocket {

    private final CountDownLatch closeLatch;

    @SuppressWarnings("unused")
    private Session session;
    private SlackRTMResponse state;

    public EventSocket(SlackRTMResponse slackRTMResponse) {
        this.state = slackRTMResponse;
        this.closeLatch = new CountDownLatch(1);
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
        try {
            SlackChatMessage chatMessage = mapper.readValue(msg, SlackChatMessage.class);
            System.out.println(chatMessage);
            if(chatMessage.getText() != null && chatMessage.getText().equals(state.getSelf().getName() + " ping")) {
                SlackChatMessage response = new SlackChatMessage();
                response.setId(1);
                response.setChannel(chatMessage.getChannel());
                response.setText("pong");
                response.setType("message");
                session.getRemote().sendString(mapper.writeValueAsString(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Got msg: %s%n", msg);
    }
}