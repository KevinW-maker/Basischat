package server;
import server.communication.ChatServer;

public class ServerMain {
    public static void main(String[] args) {
        new ChatServer().start(5555);
    }
}