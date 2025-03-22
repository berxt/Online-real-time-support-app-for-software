import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class HelpDeskWebSocketServer extends WebSocketServer {

    private List<WebSocket> clients = new ArrayList<>();
    private HelpDeskSystem helpDeskSystem = new HelpDeskSystem(); // Centralized system

    public HelpDeskWebSocketServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);

        if (message.startsWith("REGISTER_ASSISTANT:")) {
            String assistantData = message.substring(19);
            String[] parts = assistantData.split(",");
            if (parts.length >= 3) {
                String id = parts[0];
                String name = parts[1];
                List<String> expertise = Arrays.asList(parts[2].split(";"));

                TechAssistant assistant = new TechAssistant(id, name, expertise);
                helpDeskSystem.registerTechAssistant(assistant);
                System.out.println("Assistant registered: " + name);
            }
        } else if (message.startsWith("REQUEST_ASSISTANTS:")) {
            String softwareName = message.substring(19);
            List<TechAssistant> availableAssistants = helpDeskSystem.getAvailableAssistants(new Software("", softwareName, ""));

            if (availableAssistants.isEmpty()) {
                conn.send("ASSISTANT_LIST:None");
            } else {
                StringBuilder response = new StringBuilder("ASSISTANT_LIST:");
                for (TechAssistant assistant : availableAssistants) {
                    response.append(assistant.getName()).append(",");
                }
                conn.send(response.toString());
            }
        } else {
            for (WebSocket client : clients) {
                if (client != conn) {
                    client.send(message);
                }
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket Server started successfully.");
    }

    public static void main(String[] args) {
        int port = 8887;
        HelpDeskWebSocketServer server = new HelpDeskWebSocketServer(port);
        server.start();
        System.out.println("WebSocket server started on port " + port);
    }
}
