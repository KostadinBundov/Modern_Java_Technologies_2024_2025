package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FrequencyRule implements Rule {
    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;
    private final double weight;

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        List<Transaction> copy = new LinkedList<>(transactions);
        copy.sort((t1, t2) -> t1.transactionDate().compareTo(t2.transactionDate()));
        Queue<LocalDateTime> window = new LinkedList<>();

        for (int i = 0; i < copy.size(); i++) {
            LocalDateTime transactionDate = copy.get(i).transactionDate();
            window.add(transactionDate);

            while (!window.isEmpty() && window.peek().plus(timeWindow).isBefore(transactionDate)) {
                window.remove();
            }

            if (window.size() >= transactionCountThreshold) {
                return true;
            }
        }

        return false;
    }

    @Override
    public double weight() {
        return weight;
    }
}