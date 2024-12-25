package bg.sofia.uni.fmi.mjt.goodreads.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TextTokenizer {
    private static final String PUNCTUATION_REGEX = "\\p{Punct}";
    private static final String SPLIT_REGEX = "\\s+";
    private final Set<String> stopwords;

    public TextTokenizer(Reader stopwordsReader) {
        try (var br = new BufferedReader(stopwordsReader)) {
            stopwords = br.lines().collect(Collectors.toSet());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not load dataset", ex);
        }
    }

    public List<String> tokenize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("String input for tokenization cannot be null!");
        }

        String noPunctuationString = input
            .replaceAll(PUNCTUATION_REGEX, "")
            .trim();

        return Arrays.stream(noPunctuationString
            .split(SPLIT_REGEX))
            .map(String::toLowerCase)
            .filter(w -> !stopwords.contains(w) && !w.isEmpty())
            .toList();
    }

    public Set<String> stopwords() {
        return Set.copyOf(stopwords);
    }
}