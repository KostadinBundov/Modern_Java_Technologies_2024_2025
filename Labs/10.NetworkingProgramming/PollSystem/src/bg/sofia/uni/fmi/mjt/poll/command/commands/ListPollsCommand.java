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

        StringBuilder response = new StringBuilder("Available Polls:\n");
        for (var entry : polls.entrySet()) {
            int id = entry.getKey();
            Poll poll = entry.getValue();
            response.append("Poll ID: ").append(id).append("\n")
                .append("Question: ").append(poll.question()).append("\n")
                .append("Options:\n");

            for (var option : poll.options().entrySet()) {
                response.append("  - ").append(option.getKey())
                    .append(": ").append(option.getValue()).append(" votes\n");
            }

            response.append("\n");
        }

        return response.toString();
    }
}