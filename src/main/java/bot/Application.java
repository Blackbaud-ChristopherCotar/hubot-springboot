package bot;

import bot.entities.SlackRTMResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class Application {

    private static final String SLACK_TOKEN = "SLACK_TOKEN";
    private static final int MAX_NUM_AUTH_ATTEMPTS = 5;
    private static final long BACKOFF_INITIAL_MILLIS = 250;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        String destUri = "https://slack.com/api/rtm.start?token={token}";
        String token = "";
        try{
            token = getSlackToken();
        } catch(IllegalStateException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        RestTemplate restTemplate = new RestTemplate();
        SlackRTMResponse slackResponse = null;
        boolean ok = false;
        long backOff = BACKOFF_INITIAL_MILLIS; // backoff time in milli
        int numAuthAttemps = 0;
        while(!ok) {
            slackResponse = restTemplate.getForObject(destUri, SlackRTMResponse.class, params);
            ok = slackResponse.getOk();
            System.out.println("BACKOFF = " + backOff);
            if(ok) {
                System.out.println(slackResponse);
            } else {
                try {
                    Thread.sleep(backOff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(++numAuthAttemps >= MAX_NUM_AUTH_ATTEMPTS) {

                }
                backOff = backOff * 2;
            }
        }
        String webSocketUri = slackResponse.getUrl();

        WebSocketClient client = new WebSocketClient(new SslContextFactory());
        try {
            client.start();
            // The socket that receives events
            EventSocket socket = new EventSocket();
            socket.setState(slackResponse);
            // Attempt Connect
            client.connect(socket, new URI(webSocketUri), new ClientUpgradeRequest());
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    static String getSlackToken() {
        String token = System.getenv(SLACK_TOKEN);
        if(token == null || token.isEmpty()) {
            throw new IllegalStateException(SLACK_TOKEN + " environment variable is not set or is empty. Brobot will not start without a token.");
        }
        return token;
    }

}