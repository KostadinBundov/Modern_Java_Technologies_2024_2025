package bg.sofia.uni.fmi.mjt.poll.command;

import bg.sofia.uni.fmi.mjt.poll.command.commands.CommandAction;
import bg.sofia.uni.fmi.mjt.poll.command.commands.CreatePollCommand;
import bg.sofia.uni.fmi.mjt.poll.command.commands.ListPollsCommand;
import bg.sofia.uni.fmi.mjt.poll.command.commands.SubmitVoteCommand;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Arrays;

public class CommandCreator {
    private CommandCreator() { }

    public static CommandAction createCommand(String input, PollRepository repo) {
        String[] args = input.split(" ");
        String command = args[0];

        return switch (command) {
            case "create-poll" -> new CreatePollCommand(repo, args[1], Arrays.copyOfRange(args, 2, args.length));
            case "list-polls" -> new ListPollsCommand(repo);
            case "submit-vote" -> new SubmitVoteCommand(repo, Integer.parseInt(args[1]), args[2]);
            default -> throw new IllegalArgumentException("Unsupported command:" + command);
        };
    }
}