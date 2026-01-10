import cli.ConsoleUI;
import communication.NetworkService;
import communication.TcpNetworkService;
import command.*; // Importiert alle Befehle, auch LoginCommand

public class Main {

    public static void main(String[] args) {
        NetworkService networkService = new TcpNetworkService();

        CommandInterpreter interpreter = new CommandInterpreter();

        ConsoleUI cli = new ConsoleUI(interpreter, networkService);

        interpreter.register("listen", new ListenCommand(networkService));
        interpreter.register("connect", new ConnectCommand(networkService));
        interpreter.register("disconnect", new DisconnectCommand(networkService));
        interpreter.register("quit", new QuitCommand());

        interpreter.register("login", new LoginCommand(networkService));

        cli.run();
    }
}