package command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandInterpreter {

    private final Map<String, Command> commands = new HashMap<>();

    public void register(String commandName, Command command) {
        commands.put(commandName.toLowerCase(), command);
    }

    public void process(String rawInput) {
        if (!rawInput.startsWith("/")) {
            return;
        }

        String[] parts = rawInput.substring(1).split("\\s+");

        if (parts.length == 0 || parts[0].isEmpty()) {
            return;
        }

        String commandName = parts[0].toLowerCase();
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(args);
        } else {

            System.out.println(">> Unbekannter Befehl: " + commandName);
        }
    }
}