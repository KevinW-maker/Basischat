package server.communication;

import server.usermanagement.UserManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    // HIER WAR DER FEHLER: Wir schreiben <ClientHandler> nun explizit in die zweite Klammer.
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<ClientHandler>();

    private final UserManager userManager;

    public ChatServer() {
        this.userManager = new UserManager();
    }

    public void start(int port) {
        System.out.println("Server startet auf Port " + port + "...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                // Warten auf neue Verbindung
                Socket clientSocket = serverSocket.accept();
                System.out.println("Neue Verbindung von: " + clientSocket.getInetAddress());

                // Handler erstellen
                ClientHandler handler = new ClientHandler(clientSocket, this, userManager);

                // Zur Liste hinzufügen
                clients.add(handler);

                // Handler in neuem Thread starten
                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("Server-Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sendet eine Nachricht an alle verbundenen Clients, außer an den Absender.
     */
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Entfernt einen Client aus der Liste.
     */
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client entfernt. Aktuelle User: " + clients.size());
    }
}