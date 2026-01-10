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
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(">> Willkommen! Bitte einloggen mit: /login <user> <pass>");

            String line;

            while (!isAuthenticated) {
                line = in.readLine();
                if (line == null) return;

                if (line.startsWith("/login ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 3) {
                        String user = parts[1];
                        String pass = parts[2];

                        if (userManager.checkCredentials(user, pass)) {
                            this.username = user;
                            this.isAuthenticated = true;

                            server.getStatusManager().setStatus(username, "Online");

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

            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.startsWith("/quit")) {
                    break;
                }

                else if (line.startsWith("/status ")) {
                    String newStatus = line.substring(8).trim();
                    if (!newStatus.isEmpty()) {
                        server.getStatusManager().setStatus(username, newStatus);
                        server.broadcast(">> " + username + " ist jetzt: " + newStatus, this);
                    }
                }

                else {
                    String currentStatus = server.getStatusManager().getStatus(username);
                    String prefix = "";

                    if (!currentStatus.equalsIgnoreCase("Online")) {
                        prefix = "[" + currentStatus + "] ";
                    }


                    server.broadcast(prefix + username + ": " + line, this);
                }
            }

        } catch (IOException e) {
            System.out.println("Verbindung zu " + username + " unterbrochen.");
        } finally {
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
        server.removeClient(this);

        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
        }

        if (isAuthenticated) {
            server.broadcast(">> " + username + " hat den Chat verlassen.", this);
        }
    }
}