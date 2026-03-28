package chat.server;

import chat.core.Observer;
import chat.core.Subject;
import chat.model.Message;
import chat.model.SystemMessage;

import chat.exceptions.ChatConnectionException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The core server engine running the chat.
 * Acts as the Subject in the Observer Design Pattern.
 */
public class ChatServer implements Subject {
    private static final int PORT = 8080;
    
    // Thread-safe Collection to store our observers (connected clients)
    private Set<Observer> activeClients = Collections.synchronizedSet(new HashSet<>());

    public void startServer() throws ChatConnectionException {
        System.out.println("Starting Chat Server on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is waiting for connections...");
            
            while (true) {
                // Blocks until a client connects
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());
                
                // Multithreading: Create a new handler for the client and start it on a new thread
                ClientHandler handler = new ClientHandler(clientSocket, this);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Server Exception: " + e.getMessage());
            throw new ChatConnectionException("Failed to bind to port " + PORT, e);
        }
    }

    @Override
    public synchronized void registerObserver(Observer o) {
        activeClients.add(o);
        if (o instanceof ClientHandler) {
            ClientHandler handler = (ClientHandler) o;
            // Polymorphism: Generate a SystemMessage
            Message sysMsg = new SystemMessage(handler.getUsername() + " has joined the chat.");
            System.out.println(sysMsg.getFormattedMessage()); // Log on server
            notifyObservers(sysMsg); // Broadcast to all
        }
    }

    @Override
    public synchronized void removeObserver(Observer o) {
        if (activeClients.remove(o)) {
            if (o instanceof ClientHandler) {
                ClientHandler handler = (ClientHandler) o;
                // Polymorphism: Generate a SystemMessage
                Message sysMsg = new SystemMessage(handler.getUsername() + " has left the chat.");
                System.out.println(sysMsg.getFormattedMessage()); // Log on server
                notifyObservers(sysMsg); // Broadcast to all
            }
        }
    }

    @Override
    public synchronized void notifyObservers(Message message) {
        // Broadcast the message to all registered observers
        for (Observer client : activeClients) {
            client.update(message);
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        try {
            server.startServer();
        } catch (ChatConnectionException e) {
            System.err.println("Fatal Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
