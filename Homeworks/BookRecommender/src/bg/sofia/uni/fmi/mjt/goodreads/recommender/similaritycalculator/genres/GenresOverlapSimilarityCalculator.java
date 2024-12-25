package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;

import java.util.HashSet;
import java.util.Set;

public class GenresOverlapSimilarityCalculator implements SimilarityCalculator {
    @Override
    public double calculateSimilarity(Book first, Book second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Book cannot be null!");
        }

        Set<String> commonGenres = new HashSet<>(first.genres());
        commonGenres.retainAll(second.genres());

        double commonGenresCount = commonGenres.size();
        int minGenresCount = Math.min(first.genres().size(), second.genres().size());

        if (minGenresCount == 0) {
            return 0.0;
        }

        return commonGenresCount / minGenresCount;
    }
}