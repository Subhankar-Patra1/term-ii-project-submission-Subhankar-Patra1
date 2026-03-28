package chat.exceptions;

/**
 * Custom Exception demonstrating logical error handling when a user submits an invalid message.
 */
public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String message) {
        super(message);
    }
}
