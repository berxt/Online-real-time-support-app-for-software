import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class WebSocketTechAssistantClient extends WebSocketClient {

    private Scanner scanner;
    private TechAssistant assistant;
    private boolean running = true; // Flag to control the input loop

    private static String green = "\033[0;32m";
    private static String reset = "\033[0m"; // Reset to default color

    public WebSocketTechAssistantClient(URI serverUri, TechAssistant assistant) {
        super(serverUri);
        this.scanner = new Scanner(System.in);
        this.assistant = assistant;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println(green + "Connected to the WebSocket server!" + reset);

        // Send assistant details to the server for registration
        send("REGISTER_ASSISTANT:" + assistant.getId() + "," + assistant.getName() + ","
                + String.join(";", assistant.getExpertise()));

        // Start the input thread
        new Thread(this::promptAssistantForReply).start();
    }

    @Override
    public void onMessage(String message) {
        if (message.startsWith("FILE:")) {
            String[] parts = message.split(":", 3);
            String sender = parts[1];
            String fileData = parts[2];

            try {
                byte[] fileBytes = Base64.getDecoder().decode(fileData);
                Files.write(Paths.get("received_file_from_" + sender + ".dat"), fileBytes);
                System.out.println(sender + " sent a file. Saved as: received_file_from_" + sender + ".dat");
            } catch (Exception e) {
                System.out.println("Error saving file: " + e.getMessage());
            }
        } else {
            System.out.println("User: " + message);
        }
        // Do not prompt for reply here to avoid blocking
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        running = false; // Stop the input loop
        System.out.println(green + "Connection closed! Code: " + code + " Reason: " + reason +reset);
        System.exit(0); // Exit the application
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    private void promptAssistantForReply() {
        while (running) {
            System.out.print(green + assistant.getName() + " (type 'file' to send a file, 'exit' to quit): " + reset);
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
            send("FILE:" + assistant.getName() + ":" + encodedFile);
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
        Scanner scanner = new Scanner(System.in);

        System.out.print(green + "Enter your name (Assistant): " + reset);
        String assistantName = scanner.nextLine();

        System.out.print(green + "Enter the software you can assist with (comma separated if multiple): " + reset);
        String softwareInput = scanner.nextLine();
        List<String> expertise = Arrays.asList(softwareInput.split(","));

        TechAssistant assistant = new TechAssistant("A1", assistantName, expertise);

        try {
            URI serverUri = new URI("ws://localhost:8887");
            WebSocketTechAssistantClient client = new WebSocketTechAssistantClient(serverUri, assistant);
            client.connect();

            System.out.println(green + "Assistant connected: " + assistant.getName() + reset);
            System.out.println(green + "Expertise: " + String.join(", ", assistant.getExpertise()) + reset);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
