package bot;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import bot.entities.SlackChatResponse;
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

    public EventSocket() {
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
            SlackChatResponse chatResponse = mapper.readValue(msg, SlackChatResponse.class);
            System.out.println("got this text   " + chatResponse.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("Got msg: %s%n", msg);
    }
}