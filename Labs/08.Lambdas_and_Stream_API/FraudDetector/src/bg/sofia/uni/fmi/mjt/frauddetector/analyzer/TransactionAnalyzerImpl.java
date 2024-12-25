package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {
    private static final double PRECISION = 0.000000001;

    private final List<Rule> rules;
    private final List<Transaction> transactions;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        validateWeight(rules);

        this.rules = rules.stream().distinct().toList();
        this.transactions = new ArrayList<>();
        deserializeFile(reader);
    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions
            .stream()
            .map(Transaction::accountID)
            .distinct()
            .toList();
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions
            .stream()
            .map(Transaction::channel)
            .collect(
                Collectors.toMap(
                    channel -> channel,
                    channel -> 1,
                    Integer::sum
                ));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        validateString(accountID);

        return transactions
            .stream()
            .filter(t -> t.accountID().equals(accountID))
            .mapToDouble(Transaction::transactionAmount)
            .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        validateString(accountId);

        return transactions
            .stream()
            .filter(t -> t.accountID().equals(accountId))
            .toList();
    }

    @Override
    public double accountRating(String accountId) {
        List<Transaction> userTransactions = allTransactionsByUser(accountId);

        return rules
            .stream()
            .filter(r -> r.applicable(userTransactions))
            .mapToDouble(Rule::weight)
            .sum();
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        Map<String, List<Transaction>> transactionsByAccount = transactions
            .stream()
            .collect(Collectors.groupingBy(Transaction::accountID));

        return transactionsByAccount
            .entrySet()
            .stream()
            .sorted((entry1, entry2) -> Double.compare(
                accountRating(entry2.getKey()),
                accountRating(entry1.getKey())
            ))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                x -> rules.stream()
                    .filter(rule -> rule.applicable(x.getValue()))
                    .mapToDouble(Rule::weight)
                    .sum(),
                (e1, e2) -> e1,
                TreeMap::new
            ));
    }

    private void deserializeFile(Reader reader) {
        try (BufferedReader bfReader = new BufferedReader(reader)) {
            String line = bfReader.readLine();

            while ((line = bfReader.readLine()) != null) {
                transactions.add(Transaction.of(line));
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error");
        }
    }

    private void validateString(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("accountID cannot be null or empty!");
        }
    }

    private void validateWeight(List<Rule> rules) {
        double weightsSum = rules
            .stream()
            .mapToDouble(Rule::weight)
            .sum();

        if (Math.abs(1.0 - weightsSum) > PRECISION) {
            throw new IllegalArgumentException("The total weight of all rules should be 1!");
        }
    }
}