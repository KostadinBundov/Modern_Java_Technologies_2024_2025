package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class BookRecommender implements BookRecommenderAPI {
    private final SimilarityCalculator calculator;
    private final List<Book> books;

    public BookRecommender(Set<Book> initialBooks, SimilarityCalculator calculator) {
        this.calculator = calculator;
        books = new ArrayList<>(initialBooks);
    }

    @Override
    public SortedMap<Book, Double> recommendBooks(Book origin, int maxN) {
        validateBook(origin);
        validateLimitNumber(maxN);

        Map<Book, Double> booksSimilarityValues = books
            .stream()
            .filter(b -> !b.equals(origin))
            .collect(Collectors.toMap(
                b -> b,
                b -> calculator.calculateSimilarity(origin, b)
            ));

        return  booksSimilarityValues
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(maxN)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (first, second) -> first,
                () -> new TreeMap<>(
                    Comparator.<Book, Double>comparing(booksSimilarityValues::get, Comparator.reverseOrder())
                        .thenComparing(Book::ID)
                )));
    }

    private void validateLimitNumber(int maxN) {
        if (maxN <= 0) {
            throw new IllegalArgumentException("MaxN must be greater than 0!");
        }

        if (maxN > books.size()) {
            throw new IllegalArgumentException("MaxN cannot be greater than the total books count!");
        }
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Origin book cannot be null!");
        }
    }
}