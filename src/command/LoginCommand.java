package command;
import communication.NetworkService;

public class LoginCommand implements Command {
    private final NetworkService ns;

    public LoginCommand(NetworkService ns) { this.ns = ns; }

    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            // Sendet "/login user pass" an den Server
            ns.sendMessage("/login " + args[0] + " " + args[1]);
        } else {
            System.out.println(">> Nutzung: /login <user> <pass>");
        }
    }
}