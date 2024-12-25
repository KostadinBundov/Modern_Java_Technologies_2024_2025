package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TFIDFSimilarityCalculator implements SimilarityCalculator {
    private final TextTokenizer tokenizer;
    private final Map<String, Double> calculatedIDFValues;

    public TFIDFSimilarityCalculator(Set<Book> books, TextTokenizer tokenizer) {
        this.tokenizer = tokenizer;
        calculatedIDFValues = calculateIDFValues(books);
    }

    /*
     * Do not modify!
     */
    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Book cannot be null!");
        }

        Map<String, Double> tfIdfScoresFirst = computeTFIDF(first);
        Map<String, Double> tfIdfScoresSecond = computeTFIDF(second);

        if (tfIdfScoresFirst.isEmpty() || tfIdfScoresSecond.isEmpty()) {
            return 0.0;
        }

        return cosineSimilarity(tfIdfScoresFirst, tfIdfScoresSecond);
    }

    public Map<String, Double> computeTFIDF(Book book) {
        Map<String, Double> tfMap = computeTF(book);
        Map<String, Double> tdfMap = computeIDF(book);

        return tfMap
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue() * tdfMap.get(e.getKey())
                )
            );
    }

    public Map<String, Double> computeTF(Book book) {
        List<String> words = tokenizer.tokenize(book.description());
        Map<String, Integer> wordsOccurrences = countWordsOccurrencesInText(words);

        return wordsOccurrences
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue() / (double) words.size()
            ));
    }

    public Map<String, Double> computeIDF(Book book) {
        Set<String> words = new HashSet<>(tokenizer.tokenize(book.description()));

        return words
            .stream()
            .collect(Collectors.toMap(
                w -> w,
                w -> calculatedIDFValues.getOrDefault(w, 0.0)
            ));
    }

    private double cosineSimilarity(Map<String, Double> first, Map<String, Double> second) {
        double magnitudeFirst = magnitude(first.values());
        double magnitudeSecond = magnitude(second.values());

        return dotProduct(first, second) / (magnitudeFirst * magnitudeSecond);
    }

    private double dotProduct(Map<String, Double> first, Map<String, Double> second) {
        Set<String> commonKeys = new HashSet<>(first.keySet());
        commonKeys.retainAll(second.keySet());

        return commonKeys.stream()
            .mapToDouble(word -> first.get(word) * second.get(word))
            .sum();
    }

    private double magnitude(Collection<Double> input) {
        double squaredMagnitude = input.stream()
            .map(v -> v * v)
            .reduce(0.0, Double::sum);

        return Math.sqrt(squaredMagnitude);
    }

    private Map<String, Integer> countWordsOccurrencesInText(List<String> words) {
        return words
            .stream()
            .collect(Collectors.toMap(
                w -> w,
                w -> 1,
                Integer::sum));
    }

    private Map<String, Double> calculateIDFValues(Set<Book> books) {
        Map<String, Integer> wordsOccurrencesInAllBooks = countWordsOccurrencesInAllBooks(books);

        return wordsOccurrencesInAllBooks
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> Math.log10((double) books.size() / e.getValue())
            ));
    }

    private Map<String, Integer> countWordsOccurrencesInAllBooks(Set<Book> books) {
        return books
            .stream()
            .map(b -> new HashSet<>(tokenizer.tokenize(b.description())))
            .flatMap(Set::stream)
            .collect(Collectors.toMap(
                w -> w,
                w -> 1,
                Integer::sum
            ));
    }
}