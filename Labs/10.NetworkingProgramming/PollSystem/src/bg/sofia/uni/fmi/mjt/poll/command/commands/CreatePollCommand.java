package bg.sofia.uni.fmi.mjt.poll.command.commands;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;
import bg.sofia.uni.fmi.mjt.poll.server.repository.PollRepository;

import java.util.HashMap;
import java.util.Map;

public class CreatePollCommand implements CommandAction {
    private final PollRepository repository;
    private final String question;
    private final String[] answers;

    public CreatePollCommand(PollRepository pollRepository, String question, String[] options) {
        this.repository = pollRepository;
        this.question = question;
        this.answers = options;
    }

    @Override
    public String execute() {
        Poll poll = new Poll(question, createOptionsMap(answers));
        int id = repository.addPoll(poll);
        return String.format("{\"status\":\"OK\",\"message\":\"Poll %d created successfully.\"}", id);
    }

    private static Map<String, Integer> createOptionsMap(String[] optionsArray) {
        Map<String, Integer> options = new HashMap<>();
        for (String option : optionsArray) {
            options.put(option, 0);
        }

        return options;
    }
}