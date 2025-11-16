package command;

import communication.NetworkService;
import java.io.IOException;

/**
 * Konkreter Befehl zum Lauschen auf einem Port.
 */
public class ListenCommand implements Command {

    private final NetworkService networkService;

    public ListenCommand(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 1) {
            System.out.println(">> Nutzung: /listen <port>");
            return;
        }
        try {
            int port = Integer.parseInt(args[0]);
            networkService.startListening(port);
        } catch (NumberFormatException e) {
            System.out.println(">> Ungültiger Port: " + args[0]);
        } catch (IOException e) {
            System.out.println(">> Fehler beim Starten des Listeners: " + e.getMessage());
        }
    }
}