package chat.core;

import chat.model.Message;

/**
 * The Subject interface as part of the Observer Design Pattern.
 * The Server implements this to allow users (Observers) to register and receive broadcasts.
 */
public interface Subject {
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(Message message);
}
