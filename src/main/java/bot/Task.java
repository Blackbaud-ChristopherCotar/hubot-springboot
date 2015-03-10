package bot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Testing scheduling in spring boot
 */
@Component
public class Task {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
    }
}
