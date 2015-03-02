package bot;

import bot.entities.SlackRTMResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        String destUri = "https://slack.com/api/rtm.start?token={token}";
        String token = System.getenv("SLACK_TOKEN");
        Map<String, String> params = new HashMap<>();
        params.put("token", token);

        RestTemplate restTemplate = new RestTemplate();
        SlackRTMResponse slackResponse = null;
        boolean ok = false;
        while(!ok) {
            slackResponse = restTemplate.getForObject(destUri, SlackRTMResponse.class, params);
            ok = slackResponse.getOk();
            if(ok) System.out.println("slack says hi!");
        }
        String webSocketUri = slackResponse.getUrl();

        System.out.println(slackResponse);

        WebSocketClient client = new WebSocketClient(new SslContextFactory());
        try {
            client.start();
            // The socket that receives events
            EventSocket socket = new EventSocket();
            // Attempt Connect
            client.connect(socket, new URI(webSocketUri), new ClientUpgradeRequest());
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

}