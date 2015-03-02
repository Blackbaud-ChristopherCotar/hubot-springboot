package bot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Details about the Slack authenticated user.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Self {

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
        return "Self{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
