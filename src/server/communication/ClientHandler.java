package server.communication;

import server.usermanagement.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final ChatServer server;
    private final UserManager userManager;
    private PrintWriter out;
    private BufferedReader in;
    private String username = "Unbekannt";
    private boolean isAuthenticated = false;

    public ClientHandler(Socket socket, ChatServer server, UserManager userManager) {
        this.socket = socket;
        this.server = server;
        this.userManager = userManager;
    }

    @Override
    public void run() {
        try {
            // Streams erstellen
            out = new PrintWriter(socket.getOutputStream(), true); // true = autoFlush wichtig!
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(">> Willkommen! Bitte einloggen mit: /login <user> <pass>");

            String line;

            // --- PHASE 1: LOGIN ---
            while (!isAuthenticated) {
                line = in.readLine();
                if (line == null) return; // Client hat Verbindung getrennt

                if (line.startsWith("/login ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 3) {
                        String user = parts[1];
                        String pass = parts[2];

                        if (userManager.checkCredentials(user, pass)) {
                            this.username = user;
                            this.isAuthenticated = true;
                            out.println(">> Login erfolgreich! Willkommen, " + username);
                            server.broadcast(">> " + username + " ist dem Chat beigetreten.", this);
                        } else {
                            out.println(">> Login fehlgeschlagen! Falscher User oder Passwort.");
                        }
                    } else {
                        out.println(">> Format falsch. Nutzung: /login <user> <pass>");
                    }
                } else if (line.startsWith("/quit")) {
                    return; // Beenden
                } else {
                    out.println(">> Du bist noch nicht eingeloggt. Nutze /login");
                }
            }

            // --- PHASE 2: CHATTEN ---
            while ((line = in.readLine()) != null) {
                // Ignore empty lines
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("/quit")) {
                    break; // Schleife verlassen -> finally block wird ausgeführt
                }

                // Nachricht an alle verteilen
                server.broadcast(username + ": " + line, this);
            }

        } catch (IOException e) {
            System.out.println("Verbindung zu " + username + " unterbrochen.");
        } finally {
            // AUFRÄUMEN: Wird IMMER ausgeführt, egal ob Fehler oder normales Ende
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void closeConnection() {
        System.out.println("Schließe Verbindung für: " + username);
        server.removeClient(this); // WICHTIG: Aus der Liste austragen
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            // Ignorieren beim Schließen
        }

        // Nur Broadcasten, wenn er auch wirklich eingeloggt war
        if (isAuthenticated) {
            server.broadcast(">> " + username + " hat den Chat verlassen.", this);
        }
    }
}