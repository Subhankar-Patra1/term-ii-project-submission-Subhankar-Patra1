package chat.exceptions;

/**
 * Custom Exception demonstrating specific network or server handling errors.
 */
public class ChatConnectionException extends Exception {
    public ChatConnectionException(String message) {
        super(message);
    }

    public ChatConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
