package cli;

import command.Command;

public class SetNameCommand implements Command {

    private final ConsoleUI ui;

    public SetNameCommand(ConsoleUI ui) {
        this.ui = ui;
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 1 || args[0].trim().isEmpty()) {
            System.out.println(">> Nutzung: /setName <username>");
            return;
        }

        String newName = String.join(" ", args);
        String oldName = ui.getUsername();
        ui.setUsername(newName);

        System.out.println(">> Benutzername geändert von '" + oldName + "' zu '" + ui.getUsername() + "'.");
    }
}