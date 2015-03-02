package bot.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackChatMessage {

    private String type;
    private String ts;
    private String user;
    private String text;
    private SlackChatError error;
    private boolean ok;
    private int id;
    private String channel;
    private int reply_to;

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

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getReply_to() {
        return reply_to;
    }

    public void setReply_to(int reply_to) {
        this.reply_to = reply_to;
    }

    @Override
    public String toString() {
        return "SlackChatMessage{" +
                "type='" + type + '\'' +
                ", ts='" + ts + '\'' +
                ", user='" + user + '\'' +
                ", text='" + text + '\'' +
                ", error=" + error +
                '}';
    }
}
