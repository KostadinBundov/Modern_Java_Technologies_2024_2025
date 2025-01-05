package bg.sofia.uni.fmi.mjt.poll.server.repository;

import bg.sofia.uni.fmi.mjt.poll.server.model.Poll;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPollRepository implements PollRepository {
    private final Map<Integer, Poll> data;
    private int idCounter;

    public InMemoryPollRepository() {
        data = new HashMap<>();
        idCounter = 1;
    }

    @Override
    public int addPoll(Poll poll) {
        int currId = idCounter;
        data.put(idCounter, poll);
        idCounter++;
        return currId;
    }

    @Override
    public Poll getPoll(int pollId) {
        return data.get(pollId);
    }

    @Override
    public Map<Integer, Poll> getAllPolls() {
        return Map.copyOf(data);
    }

    @Override
    public void clearAllPolls() {
        data.clear();
    }
}