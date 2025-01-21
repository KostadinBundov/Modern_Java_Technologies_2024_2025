package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Map;

public class ListPollsCommand implements CommandAction {
    private final PollRepository repository;

    public ListPollsCommand(PollRepository repository) {
        this.repository = repository;
    }

    @Override
    public String execute() {
        Map<Integer, Poll> polls = repository.getAllPolls();
        if (polls.isEmpty()) {
            return "{\"status\":\"ERROR\",\"message\":\"No active polls available.\"}";
        }

        StringBuilder response = new StringBuilder("{\"status\":\"OK\",\"polls\":{");

        for (var entry : polls.entrySet()) {
            response.append(formatPoll(entry.getKey(), entry.getValue())).append(",");
        }

        if (!polls.isEmpty()) {
            response.deleteCharAt(response.length() - 1);
        }

        response.append("}}");

        return response.toString();
    }

    private String formatPoll(int pollId, Poll poll) {
        return "\"" + pollId + "\":{" +
            "\"question\":\"" + poll.question() + "\"," +
            "\"options\":{" +
            formatOptions(poll.options()) +
            "}}";
    }

    private String formatOptions(Map<String, Integer> options) {
        StringBuilder optionsJson = new StringBuilder();
        for (var option : options.entrySet()) {
            optionsJson.append("\"").append(option.getKey()).append("\":")
                .append(option.getValue()).append(",");
        }

        if (!options.isEmpty()) {
            optionsJson.deleteCharAt(optionsJson.length() - 1);
        }

        return optionsJson.toString();
    }
}