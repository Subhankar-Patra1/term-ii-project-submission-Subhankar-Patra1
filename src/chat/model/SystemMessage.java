package chat.model;

/**
 * Inherits from Message. Represents a system-generated alert (e.g. joins, leaves).
 * Demonstrates Inheritance and Polymorphism.
 */
public class SystemMessage extends Message {
    private static final long serialVersionUID = 1L;
    
    private String alertText;

    public SystemMessage(String alertText) {
        super();
        this.alertText = alertText;
    }

    @Override
    public String getFormattedMessage() {
        // Polymorphic behavior: System messages are distinguished by a special tag
        return "[" + timestamp + "] [SYSTEM] " + alertText;
    }
}
