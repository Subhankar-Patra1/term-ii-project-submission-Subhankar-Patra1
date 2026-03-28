package chat.client;

import chat.model.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

/**
 * A Graphical User Interface (GUI) Client for the Chat System using Java Swing.
 * Connects to the same ChatServer as the console client.
 */
public class ChatGUIClient {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 8080;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String username;

    // GUI Components
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ChatGUIClient() {
        // 1. Prompt for Username
        username = JOptionPane.showInputDialog(
            null, 
            "Enter your username:", 
            "Login to Chat", 
            JOptionPane.PLAIN_MESSAGE
        );

        if (username == null || username.trim().isEmpty()) {
            System.exit(0); // Exit if canceled or empty
        }

        // 2. Setup the GUI Window
        setupGUI();

        // 3. Connect to Server
        connectToServer();
    }

    private void setupGUI() {
        // Set system Look and Feel for a more modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback to default if system look fails
        }

        frame = new JFrame("Real-Time Chat - " + username);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null); // Center on screen
        
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        chatArea.setBackground(new Color(245, 245, 245)); // Light gray background
        chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Live Chat Room"));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        inputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        // Allow pressing 'Enter' to send
        inputField.addActionListener(e -> sendMessage());

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sendButton.setBackground(new Color(60, 130, 240)); // Blue button
        sendButton.setForeground(Color.WHITE); // White text
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.addActionListener(e -> sendMessage());

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Outer padding
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.setLayout(new BorderLayout(10, 10)); // Gap between components
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        
        // Add a clean title label at the top
        JLabel headerLabel = new JLabel("Logged in as: " + username, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        frame.add(headerLabel, BorderLayout.NORTH);

        // Handle window close smoothly
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disconnect();
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            // Send username to server
            out.writeObject(username);
            out.flush();

            // Start background thread to listen for server messages
            new Thread(new ServerListener()).start();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, 
                "Could not connect to server: " + e.getMessage(), 
                "Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            try {
                if ("/quit".equalsIgnoreCase(text)) {
                    disconnect();
                    System.exit(0);
                } else {
                    out.writeObject(text);
                    out.flush();
                    inputField.setText("");
                }
            } catch (IOException e) {
                appendMessage("Error sending message.");
            }
        }
    }

    private void appendMessage(String message) {
        // Thread-safe UI update
        SwingUtilities.invokeLater(() -> {
            chatArea.append(message + "\n");
            // Auto-scroll to the bottom
            chatArea.setCaretPosition(chatArea.getDocument().getLength());
        });
    }

    private void disconnect() {
        try {
            if (out != null) {
                out.writeObject("/quit");
                out.flush();
            }
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Clean disconnect failed.");
        }
    }

    // Background Listener Thread
    private class ServerListener implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof Message) {
                        Message msg = (Message) obj;
                        appendMessage(msg.getFormattedMessage());
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                appendMessage("Disconnected from server.");
            }
        }
    }

    public static void main(String[] args) {
        // Best practice to start Swing apps inside the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ChatGUIClient());
    }
}
