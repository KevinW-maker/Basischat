package command;

import communication.NetworkService;
import java.io.IOException;

public class ConnectCommand implements Command {

    private final NetworkService networkService;

    public ConnectCommand(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public void execute(String[] args) {
        if (args.length != 2) {
            System.out.println(">> Nutzung: /connect <ip> <port>");
            return;
        }
        try {
            String ip = args[0];
            int port = Integer.parseInt(args[1]);
            networkService.connect(ip, port);
        } catch (NumberFormatException e) {
            System.out.println(">> Ungültiger Port: " + args[1]);
        } catch (IOException e) {
            System.out.println(">> Fehler beim Verbinden: " + e.getMessage());
        }
    }
}