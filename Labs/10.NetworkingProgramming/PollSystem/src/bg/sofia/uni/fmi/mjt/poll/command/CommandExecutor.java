package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.command.commands.CommandAction;

public class CommandExecutor {

    private CommandExecutor() { }

    public static String execute(CommandAction command) {
        try {
            return command.execute();
        } catch (IllegalArgumentException e) {
            return "{\"status\":\"ERROR\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}