[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/pG3gvzt-)
# PCCCS495 - Term II Project

## Project Title
Real-Time Chat Application using Multithreading and Observer Design Pattern

---

## Problem Statement (max 150 words)
Communication through messaging applications has become an essential part of daily life. Many basic chat systems fail to handle multiple users efficiently at the same time, causing delays or message loss. This project aims to develop a real-time console-based chat application that allows multiple users to communicate simultaneously using multithreading. Each user can send and receive messages without blocking other users in the system. The Observer Design Pattern will be implemented so that when a user sends a message, all subscribed users are instantly notified and receive the update. This approach improves scalability and responsiveness in the system. The project demonstrates how object-oriented programming concepts and concurrent programming techniques can be combined to create an efficient and structured communication system.

---

## Target User
Students, developers, or small teams who need a simple real-time messaging platform for communication and learning about concurrent programming concepts.

---

## Core Features

- Multiple users can join the chat system simultaneously via console.
- Real-time message broadcasting to all connected users.
- Multithreading to handle multiple clients concurrently without blocking.
- Observer pattern for message notification to subscribers.
- User join/leave notifications.
- Basic error handling for connection issues.

---

## OOP Concepts Used

- **Abstraction**: Abstract classes or interfaces will define common chat functionalities such as sending and receiving messages.
- **Inheritance**: Base communication classes will be extended by specific User/Client handlers, and base Message classes will be inherited by explicit subtypes like SystemMessage and UserMessage.
- **Polymorphism**: Different types of message handling (broadcast, private message, system message) can use polymorphic methods, allowing the system to process any message subtype uniformly.
- **Exception Handling**: Used to handle errors such as connection failures, invalid messages, or unexpected disconnections safely.
- **Collections / Threads**: Collections like ArrayList or HashMap will store connected users. Multithreading will allow multiple clients to interact with the server simultaneously without starvation.

---

## Proposed Architecture Description
The system follows a client-server architecture with a pure console-based interface. The server will manage all connected clients and maintain a list of observers (active users). Each client connection will run on a separate dedicated thread to allow simultaneous communication and high responsiveness. When a user sends a message, the server acts as the Subject in the Observer Pattern and notifies all registered clients (Observers). This ensures real-time message delivery while maintaining scalability. (Future enhancements, such as database persistence for messages or rigorous authentication steps, are intentionally left as out-of-scope extensions to keep the core concurrency operations clean).

---

## How to Run
(To be updated once the implementation is complete)

---

## Git Discipline Notes
Minimum 10 meaningful commits required.
