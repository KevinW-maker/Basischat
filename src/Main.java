import cli.ConsoleUI;
import communication.NetworkService;
import communication.TcpNetworkService;
import command.*; // Importiert alle Befehle, auch LoginCommand

public class Main {

    public static void main(String[] args) {
        // 1. Netzwerkkomponente erstellen
        NetworkService networkService = new TcpNetworkService();

        // 2. Befehlsinterpreter erstellen
        CommandInterpreter interpreter = new CommandInterpreter();

        // 3. UI-Komponente erstellen
        ConsoleUI cli = new ConsoleUI(interpreter, networkService);

        // 4. Befehle registrieren
        interpreter.register("listen", new ListenCommand(networkService));
        interpreter.register("connect", new ConnectCommand(networkService));
        interpreter.register("disconnect", new DisconnectCommand(networkService));
        interpreter.register("quit", new QuitCommand());

        // --- HIER FEHLTE DIESE ZEILE: ---
        interpreter.register("login", new LoginCommand(networkService));
        // --------------------------------

        // 5. Anwendung starten
        cli.run();
    }
}