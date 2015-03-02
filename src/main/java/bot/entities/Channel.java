package bot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Slack Channel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
