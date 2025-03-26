# Online Real-Time Support App for Software

## Project Overview
This project is an online real-time support application designed to connect users with technical support personnel for software-related issues. The application is built using **Java** and implements **WebSocket** technology for bidirectional real-time communication.

## Purpose
The goal of the application is to allow users facing issues with specific software to connect with a support assistant who has expertise in that software. This real-time communication helps resolve issues efficiently by allowing both the user and the support assistant to exchange messages, send files, and track the progress of the support session.

## Key Components
### 1. User & Tech Assistant Interaction
- **Users**: Individuals facing issues with the software who require help.
- **Tech Assistants**: Employees with knowledge of specific software who provide support to the users.

### 2. WebSocket Communication
The application leverages WebSocket technology to maintain an open, bidirectional communication channel between the server, users, and tech assistants. This ensures that messages and file transfers happen in real-time without delays.

### 3. Server & Client Architecture
- **Server**: A WebSocket server that manages connections, ensuring users are matched with the correct assistant based on the software they are using. The server manages messages and file transfers between users and assistants.
- **Client (Tech Assistant & Customer)**: Both the tech assistant and the user have separate client applications that connect to the server, enabling them to send and receive messages and files.

### 4. User Interface
- **Users** provide basic information such as their username and the software they need help with. They must accept data privacy terms and describe the issue they are facing.
- **Tech assistants** log into the system by providing their name and the software they support, making them available to assist users.

### 5. Matching Process
- When a user connects, the system searches for a tech assistant who is available and qualified to assist with the specific software the user is using.
- If a match is found, the connection is made, and the user can communicate with the assistant.
- If no assistant is available, the system notifies the user and ends the session.

### 6. Message and File Handling
- Both users and assistants can exchange text messages and share files.
- A sample file (`text.txt`) is available for users to send for testing purposes.

### 7. System Flow
1. The user connects to the system, provides their problem description, and waits for a support assistant.
2. The system either connects the user to a suitable assistant or notifies them if no assistant is available.
3. Once connected, the user and assistant can communicate in real-time, sending messages and files.

## Technologies Used
- **Java**: For the development of the server and client-side applications.
- **WebSocket**: To enable real-time, full-duplex communication between clients and the server.
- **IntelliJ IDE**: For code development.

## Features
- **Real-time communication**: Users and assistants can exchange messages instantly.
- **File sharing**: Users can send files like screenshots or logs to help with troubleshooting.
- **Data privacy**: Users must accept terms for personal data processing before using the system.
- **Error Handling**: The system handles cases where no assistant is available, providing appropriate notifications to the user.
- **Easy-to-use interface**: The application is designed to be user-friendly, allowing both tech assistants and users to easily interact with the system.

## Application Flow
1. The tech assistant and the user connect to the server.
2. The system matches the user with an available tech assistant based on the software being used.
3. Once connected, both parties can exchange text messages or share files.
4. The session can end when either party chooses to exit the application.

## Class Structure
The following classes are used in the implementation:
- **User**: Stores user data.
- **TechAssistant**: Stores data of the support employee (inherits from `User`).
- **Customer**: Stores data of the user (inherits from `User`).
- **Software**: Stores data about the software.
- **HelpDeskSystem**: Matches the software used by the user with technical personnel who have knowledge of that specific software.
- **HelpDeskWebSocketServer**: Creates the server and manages message routing.
- **WebSocketTechAssistantClient**: Connects the tech assistant to the server.
- **WebSocketCustomerClient**: Connects the software user to the server.
- **Message**: Handles message sending.
- **MessageType (Enum)**: Differentiates between text messages and file transfers.

The class design and relationships are documented in `HelpDeskApp.drawio.png`.

![image](https://github.com/user-attachments/assets/6d58567f-9a0e-433e-8312-a3e7169e546a)

## Functionality Description
1. The user logs into the software (user details and software details are already known).
2. The server is running and waiting for a user (customer or assistant) to connect.
3. The assistant connects to the server by providing their name and software expertise.
4. The customer connects to the server. They must accept data processing terms and describe their issue.
5. The system searches for an available assistant. If a match is found, the connection is made; otherwise, a message is returned.
6. Once connected, both users can send messages, share files, or end the connection.

## Instructions for Running the Application
1. **Run the server**:
   ```sh
   java HelpDeskWebSocketServer
   ```
2. **Run the tech assistant client**:
   ```sh
   java WebSocketTechAssistantClient
   ```
   - Provide:
     - Assistant's name
     - Software they support
   - Note: The default user uses `AppA`. To test an unavailable assistant, use a different software name.
3. **Run the user client**:
   ```sh
   java WebSocketCustomerClient
   ```
   - Accept data storage terms.
   - Provide a brief description of the problem.
4. **System Matching Process**:
   - If a matching assistant is found, the user is connected.
   - If no assistant is available, the system returns: `There is no available employee for help at the moment. Please try again later.`
   - If connected, the assistant receives user and software details along with the problem description.
   - The user is notified that an assistant is available.

## Interaction Commands
- **Send a message**: Type the message and press `Enter`.
- **Send a file**: Type `file` and press `Enter`. A file selection prompt will appear.
- **Exit**: Type `exit` and press `Enter` to leave the application.

---

### Contributions
Feel free to fork this repository and submit pull requests to improve the application.

### License
This project is open-source and available under the MIT License.

---

Enjoy using the **Online Real-Time Support App for Software**! 🚀
