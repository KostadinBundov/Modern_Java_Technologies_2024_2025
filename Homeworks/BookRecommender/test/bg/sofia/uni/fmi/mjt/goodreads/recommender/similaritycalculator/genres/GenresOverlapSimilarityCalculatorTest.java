package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class GenresOverlapSimilarityCalculatorTest {
    private Book first;
    private Book second;

    @Test
    void testCalculateSimilarityWithFirstArgumentNullThrowsException() {
        first = null;
        second = Mockito.mock(Book.class);

        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(first, second),
            "Should throws exception when first argument is null.");
    }

    @Test
    void testCalculateSimilarityWithSecondArgumentNullThrowsException() {
        first = Mockito.mock(Book.class);
        second = null;

        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(first, second),
            "Should throws exception when second argument is null.");
    }

    @Test
    void testCalculateSimilarityWithoutCommonGenres() {
        first = setUpBook(List.of("GenreA", "GenreB", "GenreC"));
        second = setUpBook(List.of("GenreD", "GenreE"));

        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        double similarity = calculator.calculateSimilarity(first, second);

        assertEquals(0, similarity, "Similarity between books without any common genres should be 0.");
    }

    @Test
    void testCalculateSimilarityWithCommonGenres() {
        first = setUpBook(List.of("GenreA", "GenreB", "GenreC"));
        second = setUpBook(List.of("GenreA", "GenreD", "GenreE"));

        SimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();
        double similarity = calculator.calculateSimilarity(first, second);

        assertEquals(0.33, similarity, 0.01, "Similarity between books without any common genres should be 0.");
    }

    private Book setUpBook(List<String> genres) {
        Book book = Mockito.mock(Book.class);
        when(book.genres()).thenReturn(genres);

        return book;
    }
}