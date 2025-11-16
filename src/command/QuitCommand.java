package command;


public class QuitCommand implements Command {

    @Override
    public void execute(String[] args) {
        System.out.println(">> Beende Chat...");
        System.exit(0);
    }
}