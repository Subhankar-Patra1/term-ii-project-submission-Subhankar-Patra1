package chat.core;

/**
 * Abstract class demonstrating Abstraction in OOP.
 * Represents a generic participant in the chat.
 */
public abstract class User {
    protected String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Abstract method defining how a user handles an incoming message.
     * Implementing classes (like a specific ClientHandler) will provide the details.
     */
    public abstract void handleMessage(String messageContent);
}
