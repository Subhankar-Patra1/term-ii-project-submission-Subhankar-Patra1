package chat.client;

import chat.model.Message;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * The Client application. Connects to the ChatServer.
 * Handles user input from the console and displays incoming messages.
 */
public class ChatClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8080;
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Welcome to the Real-Time Chat System ===");
        System.out.print("Enter your username: ");
        this.username = scanner.nextLine();

        try {
            // Establish connection
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            System.out.println("Connected to Chat Server!");

            // Initialize streams
            out = new ObjectOutputStream(socket.getOutputStream());
            // It's critical out is initialized and flushed first before in
            out.flush(); 
            in = new ObjectInputStream(socket.getInputStream());

            // 1. Send username to server as the first message
            out.writeObject(this.username);
            out.flush();

            // 2. Start a separate background thread to listen for incoming messages
            // This is required so reading incoming data doesn't block user input
            Thread listenerThread = new Thread(new ServerListener(in));
            listenerThread.start();

            // 3. Keep the main thread alive to read user input from the console
            System.out.println("Type your messages below. Type '/quit' to exit.");
            while (true) {
                String input = scanner.nextLine();
                if (input != null && !input.trim().isEmpty()) {
                    out.writeObject(input);
                    out.flush();
                    
                    if ("/quit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Exiting chat...");
                        break; // Break the typing loop
                    }
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        } finally {
            closeConnections();
            scanner.close();
            System.exit(0);
        }
    }

    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connections.");
        }
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.start();
    }

    /**
     * Inner class to handle incoming messages asynchronously.
     */
    private class ServerListener implements Runnable {
        private ObjectInputStream inputStream;

        public ServerListener(ObjectInputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                // Infinitely listen for incoming structured Messages from the Subject (Server)
                while (true) {
                    Object obj = inputStream.readObject();
                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        // Polymorphism in action: getFormattedMessage behaves differently based on Message subtype
                        System.out.println(msg.getFormattedMessage());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server connection closed.");
            }
        }
    }
}
