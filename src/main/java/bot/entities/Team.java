package bot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Represents the state of the Slack team
 * that this bot is connected to.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {

    private String id;
    private String name;
    private String email_domain;

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

    public String getEmail_domain() {
        return email_domain;
    }

    public void setEmail_domain(String email_domain) {
        this.email_domain = email_domain;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email_domain='" + email_domain + '\'' +
                '}';
    }
}
