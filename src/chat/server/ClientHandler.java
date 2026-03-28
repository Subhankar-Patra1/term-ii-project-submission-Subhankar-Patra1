package chat.server;

import chat.core.Observer;
import chat.core.User;
import chat.model.Message;
import chat.model.UserMessage;

import chat.exceptions.InvalidMessageException;

import java.io.*;
import java.net.Socket;

/**
 * Handles individual client connections on separate threads.
 * Extends the abstract User class to provide concrete behavior for handleMessage.
 * Implements Runnable for Multithreading and Observer to receive broadcasted messages.
 */
public class ClientHandler extends User implements Runnable, Observer {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ChatServer server;
    private boolean isRunning;

    public ClientHandler(Socket socket, ChatServer server) {
        super("Unknown"); // Will be updated when client sends their name
        this.socket = socket;
        this.server = server;
        this.isRunning = true;
    }

    @Override
    public void run() {
        try {
            // Initialize streams. Output stream must be initialized first to avoid deadlock!
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            // The first string sent by the client is their username
            this.username = (String) in.readObject();
            
            // Register this client with the server (Observer Pattern)
            server.registerObserver(this);
            
            // Infinite loop to listen for incoming messages from this client
            while (isRunning) {
                Object obj = in.readObject();
                if (obj instanceof String) {
                    try {
                        handleMessage((String) obj);
                    } catch (InvalidMessageException ime) {
                        System.err.println("Warning: " + ime.getMessage() + " from " + username);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // Expected exception when client disconnects abruptly
        } finally {
            server.removeObserver(this);
            closeConnections();
        }
    }

    @Override
    public void handleMessage(String messageContent) {
        if (messageContent == null || messageContent.trim().isEmpty()) {
            throw new InvalidMessageException("Message cannot be empty.");
        }

        if ("/quit".equalsIgnoreCase(messageContent.trim())) {
            isRunning = false; // Terminates the thread loop
        } else {
            // Polymorphic structure: Wrap the string in a UserMessage object
            Message userMsg = new UserMessage(this.username, messageContent);
            // Subject acts on the new state
            server.notifyObservers(userMsg);
        }
    }

    @Override
    public void update(Message message) {
        try {
            // Send the broadcasted message down the network stream to the actual client
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Failed to send message to " + username);
            isRunning = false;
        }
    }
    
    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
