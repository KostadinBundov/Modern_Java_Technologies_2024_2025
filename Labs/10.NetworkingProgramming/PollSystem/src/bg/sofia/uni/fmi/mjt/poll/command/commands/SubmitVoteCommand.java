package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.Map;

public class SubmitVoteCommand implements CommandAction {
    private final PollRepository repository;
    private final int pollId;
    private final String answer;

    public SubmitVoteCommand(PollRepository pollRepository, int pollId, String option) {
        this.repository = pollRepository;
        this.pollId = pollId;
        this.answer = option;
    }

    @Override
    public String execute() {
        Poll poll = repository.getPoll(pollId);

        if (poll == null) {
            return "{\"status\":\"ERROR\",\"message\":\"Poll with ID " + pollId + " does not exist.\"}";
        }

        Map<String, Integer> options = poll.options();
        if (!options.containsKey(answer)) {
            return "{\"status\":\"ERROR\",\"message\":\"Invalid option. Option " + answer + " does not exist.\"}";
        }

        options.put(answer, options.get(answer) + 1);

        return "{\"status\":\"OK\",\"message\":\"Vote submitted successfully for option: " + answer + "\"}";
    }
}