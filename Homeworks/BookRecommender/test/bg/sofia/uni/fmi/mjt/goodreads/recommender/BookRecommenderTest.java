package bg.sofia.uni.fmi.mjt.goodreads.recommender;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BookRecommenderTest {
    @Test
    void testRecommendBooksWithInvalidBookThrowsException() {
        Book book = null;
        SimilarityCalculator calculator = Mockito.mock(SimilarityCalculator.class);

        BookRecommender recommender = new BookRecommender(Set.of(), calculator);
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book, 3));
    }

    @Test
    void testRecommendBooksWithNegativeLimitThrowsException() {
        Book book = Mockito.mock(Book.class);
        SimilarityCalculator calculator = Mockito.mock(SimilarityCalculator.class);

        BookRecommender recommender = new BookRecommender(Set.of(), calculator);
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(book, -1));
    }

    @Test
    void testRecommendBooksWithGreaterThanSizeLimitThrowsException() {
        Book first = Mockito.mock(Book.class);
        Book second = Mockito.mock(Book.class);
        SimilarityCalculator calculator = Mockito.mock(SimilarityCalculator.class);

        BookRecommender recommender = new BookRecommender(Set.of(first, second), calculator);
        assertThrows(IllegalArgumentException.class, () -> recommender.recommendBooks(first, 3));
    }

    @Test
    void testRecommendBooks() {
        Book first = Mockito.mock(Book.class);
        Book second = Mockito.mock(Book.class);
        Book third = Mockito.mock(Book.class);

        when(first.ID()).thenReturn("First Book");
        when(second.ID()).thenReturn("Second Book");
        when(third.ID()).thenReturn("Third Book");

        SimilarityCalculator calculator = Mockito.mock(SimilarityCalculator.class);

        when(calculator.calculateSimilarity(first, second)).thenReturn(1.0);
        when(calculator.calculateSimilarity(first, third)).thenReturn(2.0);

        BookRecommender recommender = new BookRecommender(Set.of(first, second, third), calculator);

        Map<Book, Double> result = recommender.recommendBooks(first, 1);
        Book mostSimilarBook = result.entrySet().iterator().next().getKey();

        assertEquals(mostSimilarBook, third,
            "The most similar book should match the expected result.");
    }
}