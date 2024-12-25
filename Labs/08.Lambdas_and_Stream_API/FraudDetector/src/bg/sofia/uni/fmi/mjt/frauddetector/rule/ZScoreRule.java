package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class ZScoreRule implements Rule  {
    private final double zScoreThreshold;
    private final double weight;

    public ZScoreRule(double zScoreThreshold, double weight) {
        this.zScoreThreshold = zScoreThreshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        double averageValue = transactions
            .stream()
            .mapToDouble(Transaction::transactionAmount)
            .average()
            .orElse(0);

        double dispersion = transactions
            .stream()
            .mapToDouble(t -> Math.pow(t.transactionAmount() - averageValue, 2))
            .average()
            .orElse(0);

        double standardDeviation = Math.sqrt(dispersion);

        if (standardDeviation == 0) {
            return false;
        }

        long transactionsCount = transactions
            .stream()
            .map(Transaction::transactionAmount)
            .filter(x -> Math.abs((x - averageValue) / standardDeviation) >= zScoreThreshold)
            .count();

        return transactionsCount > 0;
    }

    @Override
    public double weight() {
        return weight;
    }
}