package command;

import communication.NetworkService;


public class DisconnectCommand implements Command {

    private final NetworkService networkService;

    public DisconnectCommand(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public void execute(String[] args) {
        networkService.disconnect();
    }
}