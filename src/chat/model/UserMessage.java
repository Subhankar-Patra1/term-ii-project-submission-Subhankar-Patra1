package chat.model;

/**
 * Inherits from Message. Represents a standard chat message sent by a user.
 * Demonstrates Inheritance and Polymorphism.
 */
public class UserMessage extends Message {
    private static final long serialVersionUID = 1L;
    
    private String senderName;
    private String content;

    public UserMessage(String senderName, String content) {
        super();
        this.senderName = senderName;
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getFormattedMessage() {
        // Polymorphic behavior: User messages have the sender's name
        return "[" + timestamp + "] " + senderName + ": " + content;
    }
}
