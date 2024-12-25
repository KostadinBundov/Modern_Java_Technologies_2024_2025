package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.composite;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompositeSimilarityCalculatorTest {
    @Mock
    private Book book;

    @Test
    void testCalculateSimilarityWithFirstArgumentNullThrowsException() {
        Book first = null;
        SimilarityCalculator calculator = new CompositeSimilarityCalculator(Map.of());

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(first, book),
            "Should throws exception when first argument is null.");
    }

    @Test
    void testCalculateSimilarityWithSecondArgumentNullThrowsException() {
        Book second = null;
        SimilarityCalculator calculator = new CompositeSimilarityCalculator(Map.of());

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(book, second),
            "Should throws exception when second argument is null.");
    }

    @Test
    void testCalculateSimilarity() {
        Book second = Mockito.mock(Book.class);

        SimilarityCalculator genresCalculator = Mockito.mock(GenresOverlapSimilarityCalculator.class);
        when(genresCalculator.calculateSimilarity(book, second)).thenReturn(0.7);

        SimilarityCalculator tfidfCalculator = Mockito.mock(SimilarityCalculator.class);
        when(tfidfCalculator.calculateSimilarity(book, second)).thenReturn(0.15);

        SimilarityCalculator calculator = new CompositeSimilarityCalculator(Map.of(
            genresCalculator, 0.2,
            tfidfCalculator, 0.6
        ));

        double similarity = calculator.calculateSimilarity(book, second);

        assertEquals(0.23, similarity, 0.01, "Similarity should be a weighted sum of the individual calculators.");
    }
}