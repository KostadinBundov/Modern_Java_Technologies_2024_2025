package bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.descriptions;

import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TFIDFSimilarityCalculatorTest {
    private Book first;
    private Book second;
    private Book third;

    @Mock
    private TextTokenizer textTokenizer;

    @Test
    void testCalculateSimilarityWithFirstArgumentNullThrowsException() {
        first = null;
        second = Mockito.mock(Book.class);

        SimilarityCalculator calculator = new TFIDFSimilarityCalculator(Set.of(), textTokenizer);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(first, second),
            "Should throws exception when first argument is null.");
    }

    @Test
    void testCalculateSimilarityWithSecondArgumentNullThrowsException() {
        first = Mockito.mock(Book.class);
        second = null;
        SimilarityCalculator calculator = new TFIDFSimilarityCalculator(Set.of(), textTokenizer);

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateSimilarity(first, second),
            "Should throws exception when second argument is null.");
    }

    @Test
    void testComputeTF() {
        first = setUpBookDescription("academy superhero club superhero");
        when(textTokenizer.tokenize(first.description())).thenReturn(List.of("academy", "superhero", "club", "superhero"));

        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(Set.of(first), textTokenizer);

        Map<String, Double> result = calculator.computeTF(first);
        Map<String, Double> expectedResult = Map.of(
            "academy", 0.25,
            "club", 0.25,
            "superhero", 0.5
        );

        expectedResult.forEach((k, v) ->
            assertEquals(result.get(k), v, 0.01, "TF values should match expected values.")
        );
    }

    @Test
    void testComputeIDF() {
        setUpBooksAndTokenizer();

        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(Set.of(first, second, third), textTokenizer);

        Map<String, Double> result = calculator.computeIDF(first);
        Map<String, Double> expectedResult = Map.of(
            "academy", 0.47,
            "club", 0.0,
            "superhero", 0.17
        );

        expectedResult.forEach((k, v) ->
            assertEquals(result.get(k), v, 0.01, "IDF values should match expected values.")
        );
    }

    @Test
    void testCalculateTFIDF() {
        setUpBooksAndTokenizer();

        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(Set.of(first, second, third), textTokenizer);

        Map<String, Double> result = calculator.computeTFIDF(first);
        Map<String, Double> expectedResult = Map.of(
            "academy", 0.1193,
            "club", 0.0,
            "superhero", 0.088
        );

        expectedResult.forEach((k, v) ->
            assertEquals(result.get(k), v, 0.001, "IDF values should match expected values.")
        );
    }

    @Test
    void testCalculateSimilarity() {
        setUpBooksAndTokenizer();

        TFIDFSimilarityCalculator calculator = new TFIDFSimilarityCalculator(Set.of(first, second, third), textTokenizer);

        double result = calculator.calculateSimilarity(first, second);

        assertEquals(0.15, result, 0.01,
            "Similarity between Book X and Book Y should be approximately 0.16.");
    }

    private Book setUpBookDescription(String text) {
        Book book = Mockito.mock(Book.class);
        when(book.description()).thenReturn(text);

        return book;
    }

    private void setUpBooksAndTokenizer() {
        first = setUpBookDescription("academy superhero club superhero");
        second = setUpBookDescription("superhero mission save club");
        third = setUpBookDescription("crime murder mystery club");

        when(textTokenizer.tokenize(first.description())).thenReturn(List.of("academy", "superhero", "club", "superhero"));
        when(textTokenizer.tokenize(second.description())).thenReturn(List.of("superhero", "mission", "save", "club"));
        when(textTokenizer.tokenize(third.description())).thenReturn(List.of("crime", "murder", "mystery", "club"));

    }
}