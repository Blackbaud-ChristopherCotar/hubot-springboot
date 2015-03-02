package bot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackChatResponse {

    private String type;
    private String ts;
    private String user;
    private String text;
    private SlackChatError error;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SlackChatError getError() {
        return error;
    }

    public void setError(SlackChatError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "SlackChatResponse{" +
                "type='" + type + '\'' +
                ", ts='" + ts + '\'' +
                ", user='" + user + '\'' +
                ", text='" + text + '\'' +
                ", error=" + error +
                '}';
    }
}
