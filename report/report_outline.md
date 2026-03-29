# Final Project Report (Template)

**Course**: PCCCS495 – Term II Project
**Name**: Subhankar Patra
**Date**: March 2026

## 1. Introduction
*Briefly describe the purpose of creating a multithreaded chat system. Mention that it aims to solve the problem of synchronous blocking in basic socket programming.*

## 2. Objective & Scope
*Highlight the implemented features: real-time broadcasting, concurrent connections, an intuitive GUI via Java Swing, and robust OOP foundations.*

## 3. System Design & Architecture
*Refer to the ideas established in `docs/architecture.md`. Detail the Client-Server model and how the Observer pattern effectively tracks who should receive packets.*

## 4. Implementation Details
*Explain your concrete usage of Java concepts:*
- *Multithreading (`Thread` & `Runnable`)*
- *Networking (`java.net.Socket`, `ServerSocket`)*
- *Serialization (`ObjectInputStream`, `ObjectOutputStream`)*
- *OOP Concepts (`Inheritance`, `Exception Handling`)*

## 5. Challenges Faced
*Discuss any issues with Deadlocks, Stream Initialization (such as flushing ObjectOutputStream before creating ObjectInputStream), and how custom Exceptions mitigated crashes.*

## 6. Conclusion
*Summarize the success of the project and potential future improvements (like Database integration).*
