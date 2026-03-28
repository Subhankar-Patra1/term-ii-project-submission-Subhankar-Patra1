package chat.core;

import chat.model.Message;

/**
 * The Observer interface as part of the Observer Design Pattern.
 * Any class that needs to receive messages (like a Chat Client or User Handler)
 * will implement this interface.
 */
public interface Observer {
    /**
     * Called by the Subject to update the observer with a new message.
     * @param message The message broadcasted by the server.
     */
    void update(Message message);
}
