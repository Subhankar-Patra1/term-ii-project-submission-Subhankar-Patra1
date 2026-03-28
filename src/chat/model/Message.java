package chat.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Base abstract class representing a generic Message in the chat system.
 * Implements Serializable to allow sending objects over network streams.
 */
public abstract class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected String timestamp;

    public Message() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.timestamp = dtf.format(LocalDateTime.now());
    }

    /**
     * Polymorphic formatting method. Each message type will dictate how it looks.
     * @return Formatted message string.
     */
    public abstract String getFormattedMessage();
}
