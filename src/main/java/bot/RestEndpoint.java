package bot;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class RestEndpoint {

    @RequestMapping("/rest/greetings")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}