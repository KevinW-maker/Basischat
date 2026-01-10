package cli;

import communication.MessageListener;
import communication.NetworkService;
import command.CommandInterpreter;
import java.util.Scanner;

public class ConsoleUI implements MessageListener {

    private final CommandInterpreter interpreter;
    private final NetworkService networkService;
    private String username = "User";

    public ConsoleUI(CommandInterpreter interpreter, NetworkService networkService) {
        this.interpreter = interpreter;
        this.networkService = networkService;

        this.networkService.addMessageListener(this);

        this.interpreter.register("setName", new SetNameCommand(this));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public void run() {
        System.out.println("Willkommen beim BasicChat!");
        System.out.println("Verfügbare Befehle: /listen <port>, /connect <ip> <port>, /disconnect, /setName <username>, /quit");

        Scanner scanner = new Scanner(System.in);

        while (true) {

            String input = scanner.nextLine();

            if (input == null || input.trim().isEmpty()) {
                continue;
            }

            if (input.startsWith("/")) {
                interpreter.process(input);
            } else {
                networkService.sendMessage(username + ": " + input);
            }
        }
    }


    @Override
    public void onMessageReceived(String message) {
        System.out.println(message);
    }

    @Override
    public void onStatusUpdate(String status) {
        System.out.println(status);
    }
}