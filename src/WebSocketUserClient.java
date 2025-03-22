import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;

public class WebSocketUserClient extends WebSocketClient {

    private Scanner scanner;
    private User user;
    private boolean running = true; // Flag to control the input loop

    private static String problem = "";

    // ANSI escape code for green text
    private static String green = "\033[0;32m";
    private static String reset = "\033[0m"; // Reset to default color
    public WebSocketUserClient(URI serverUri, Scanner scanner) {
        super(serverUri);
        this.scanner = scanner; // Use the scanner passed from main
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(green + "Connected to the server!" + reset);
        createUserInstance();

        System.out.println(green + "User connected: " + user.getName() + " | Contact: " + user.getContact()
                + " | Software: " + user.getSoftware().getName() + reset);

        // Send user info to the other edge
        send("\n" + "USER_INFO:" + "\n" + "Name:" + user.getName() + "\n" + "Contact:" + user.getContact() + "\n"
                + "Software:" + user.getSoftware().getName() + "," + "Version:" + user.getSoftware().getVersion() + "\n" + "Problem Descriptiom:" + problem);

        // Request available assistants for the software
        send("REQUEST_ASSISTANTS:" + user.getSoftware().getName());
    }

    @Override
    public void onMessage(String message) {
        if (message.startsWith("ASSISTANT_LIST:")) {
            String assistants = message.substring(15);
            if (assistants.equals("None")) {
                System.out.println(green + "BNo available assistants for" + user.getSoftware().getName() +reset);
                this.close();
            } else {
                System.out.println(green + "Available Assistants: " + assistants + reset);
                // Start the input thread
                new Thread(this::promptUserForMessage).start();
            }
        } else if (message.startsWith("FILE:")) {
            String[] parts = message.split(":", 3);
            String sender = parts[1];
            String fileData = parts[2];

            try {
                byte[] fileBytes = Base64.getDecoder().decode(fileData);
                Files.write(Paths.get("received_file_from_" + sender + ".dat"), fileBytes);
                System.out.println(green + sender + "sent a file. Saved as: received_file_from_" + sender + ".dat" + reset);
            } catch (Exception e) {
                System.out.println(green + "Error saving file: " + e.getMessage()+reset);
            }
        } else {
            System.out.println("Received message from Assistant: " + message);
        }
        // Do not prompt for message here to avoid blocking
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        running = false; // Stop the input loop
        System.out.println(green + "Connection closed! Code: " + code + " Reason: " + reason + reset);
        System.exit(0); // Exit the application
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    private void createUserInstance() {
        String name = "UserA";
        String contact = "UserA@mail.com";
        Software appA = new Software("01AP22", "AppA", "v2.3.5");

        this.user = new User("U" + System.currentTimeMillis(), name, contact, appA);
    }

    private void promptUserForMessage() {
        while (running) {
            System.out.print(green + user.getName() + "(type 'file' to send a file, 'exit' to quit): " + reset);
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("file")) {
                System.out.print(green + "Enter file path: " + reset);
                String filePath = scanner.nextLine();
                sendFile(filePath);
            } else if (input.equalsIgnoreCase("exit")) {
                System.out.println(green + "Exiting chat..." + reset);
                closeConnection();
                break;
            } else {
                send(input);
            }
        }
    }

    private void sendFile(String filePath) {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
            String encodedFile = Base64.getEncoder().encodeToString(fileBytes);
            send("FILE:" + user.getName() + ":" + encodedFile);
            System.out.println(green + "File sent successfully." + reset);
        } catch (Exception e) {
            System.out.println(green + "Error sending file: " + e.getMessage() + reset);
        }
    }

    private void closeConnection() {
        try {
            this.close();
        } catch (Exception e) {
            System.out.println(green + "Error closing connection: " + e.getMessage() + reset);
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Added notification and consent prompt
            System.out.println(green + "Please note that your conversation data and customer data will be saved for safety reasons."+reset);
            System.out.print(green + "Do you agree to continue? (yes/no): "+reset);
            String consent = scanner.nextLine();
            System.out.println(green + "Please describe your problem" + reset);
            problem = scanner.nextLine();
            if (consent.equalsIgnoreCase( "yes") || consent.equalsIgnoreCase("y")) {
                URI serverUri = new URI("ws://localhost:8887");
                WebSocketUserClient client = new WebSocketUserClient(serverUri, scanner);
                client.connect();
            } else {
                System.out.println(green + "You did not agree to the terms. Exiting the program." + reset);
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
