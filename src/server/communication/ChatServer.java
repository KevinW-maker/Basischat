package server.communication;

import server.usermanagement.UserManager;
import server.features.status.StatusManager;
import server.features.logging.IChatLogger;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {

    private final List<ClientHandler> clients = new CopyOnWriteArrayList<ClientHandler>();

    private final UserManager userManager;
    private final StatusManager statusManager; // Feature 1

    private IChatLogger logger; // Feature 2

    public ChatServer() {
        this.userManager = new UserManager();
        this.statusManager = new StatusManager();

        try {
            System.out.println("Lade Logging-Modul...");

            Class<?> clazz = Class.forName("server.features.logging.FileLogger");

            Constructor<?> ctor = clazz.getConstructor();
            this.logger = (IChatLogger) ctor.newInstance();

            System.out.println("✅ PRO-VERSION: Logging-Modul erfolgreich geladen!");
        } catch (Exception e) {
            System.out.println("ℹ️ NORMAL-VERSION: Kein Logging-Modul gefunden. Server läuft ohne Logs.");
            this.logger = null;
        }
    }

    public void start(int port) {
        System.out.println("Server startet auf Port " + port + "...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Neue Verbindung von: " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, this, userManager);

                clients.add(handler);

                new Thread(handler).start();
            }

        } catch (IOException e) {
            System.err.println("Server-Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void broadcast(String message, ClientHandler sender) {

        if (logger != null) {
            logger.log(message);
        }

        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }


    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client entfernt. Aktuelle User: " + clients.size());
    }


    public StatusManager getStatusManager() {
        return statusManager;
    }
}