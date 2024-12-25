package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextTokenizerTest {
    @Test
    void testTokenizeWithNullInputThrowsError() {
        String text = null;
        String stopWords = "";
        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopWords));

        assertThrows(IllegalArgumentException.class, () -> tokenizer.tokenize(text),
            "Exception should be thrown when trying to tokenize null text.");
    }

    @Test
    void testTokenizeWithStopWords() {
        String text = " Text    to be tokenized. This is a demo for you!  ";
        String stopWords = "is\nto\nbe\na\nfor\nyou";

        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopWords));
        List<String> tokenizedText = tokenizer.tokenize(text);
        List<String> expectedOutput = List.of("text", "tokenized", "this", "demo");
        assertEquals(expectedOutput, tokenizedText, "Words after tokenization should match the expected one.");
    }

    @Test
    void testTokenizeWithoutStopWords() {
        String text = " Text    to be tokenized. This is a demo for you!  ";
        String stopWords = "";

        TextTokenizer tokenizer = new TextTokenizer(new StringReader(stopWords));
        List<String> tokenizedText = tokenizer.tokenize(text);
        List<String> expectedOutput = List.of("text","to", "be", "tokenized", "this", "is", "a", "demo", "for", "you");
        assertEquals(expectedOutput, tokenizedText, "Words after tokenization should match the expected one.");
    }
}