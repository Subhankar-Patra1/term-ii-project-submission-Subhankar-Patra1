# Application Architecture

## Overview
The real-time chat application follows a rigorous **Client-Server Architecture**. It employs concurrent programming (Multithreading) and the **Observer Design Pattern** to guarantee real-time message delivery without starvation or UI blocking.

## The Observer Design Pattern
This pattern dictates how messages are broadcasted across the network:
* **Subject (`ChatServer.java`)**: Maintains a thread-safe `Set` of active clients. When it receives a message, it iterates over all stored observers and triggers their `update()` method.
* **Observer (`ClientHandler.java` / `ChatClient.java`)**: Every connected user acts as an observer. When `update(Message message)` is called by the Server, the handler pipes the serialized `Message` object down the TCP socket to the designated client.

## Multithreading
To ensure that multiple users can connect simultaneously (and so no one user's typing blocks another user from receiving messages):
1. **Server-Side Concurrency**: 
    - The main thread loops continuously on `serverSocket.accept()`.
    - Upon a new connection, it spins up a new `Thread(new ClientHandler(clientSocket))`. This dedicated thread strictly listens to that specific user's incoming messages.
2. **Client-Side Concurrency**:
    - The client's main thread is dedicated to interacting with the user (reading from the Console or handling Swing UI events).
    - A secondary background thread (`ServerListener`) actively waits for incoming data streams from the server loop.

## Object-Oriented Principles Demonstrated
* **Abstraction**: `Observer`, `Subject` interfaces; abstract `User` and `Message` classes.
* **Inheritance**: `ClientHandler` extends `User`. `SystemMessage` and `UserMessage` extend `Message`.
* **Polymorphism**: The system strictly passes around the base `Message` class. When `getFormattedMessage()` is invoked, the JVM dynamically resolves whether it should render a system alert or a standard user dialog.
* **Exception Handling**: Utilization of `ChatConnectionException` and `InvalidMessageException` to gracefully recover from network drops and invalid payloads.