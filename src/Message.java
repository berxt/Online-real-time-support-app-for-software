import java.io.File;
import java.time.LocalDateTime;

public class Message {
    private String sender;
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;

    public enum MessageType {
        TEXT, FILE
    }

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.type = MessageType.TEXT;
        this.timestamp = LocalDateTime.now();
    }

    public Message(String sender, File file) {
        this.sender = sender;
        this.content = file.getPath();
        this.type = MessageType.FILE;
        this.timestamp = LocalDateTime.now();
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
