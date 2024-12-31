import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.ParallelSentimentAnalyzer;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Set<String> stopWords = new HashSet<>();
        Map<String, SentimentScore> sentimentLexicon = new HashMap<>();

        try (BufferedReader stopWordsReader = Files.newBufferedReader(Path.of("stopwords.txt"))) {
            stopWords = stopWordsReader.lines().collect(Collectors.toSet());
        } catch (IOException e) {
            System.err.println("Error reading stopwords file: " + e.getMessage());
            return;
        }

        try (BufferedReader lexiconReader = Files.newBufferedReader(Path.of("AFINN-111.txt"))) {
            String line;
            while ((line = lexiconReader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String word = parts[0];
                    int score = Integer.parseInt(parts[1]);
                    sentimentLexicon.put(word, SentimentScore.fromScore(score));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading lexicon file: " + e.getMessage());
            return;
        }

        AnalyzerInput input1 = new AnalyzerInput("doc1", new StringReader("I love java"));
        AnalyzerInput input2 = new AnalyzerInput("doc2", new StringReader("I hate bugs"));

        ParallelSentimentAnalyzer analyzer = new ParallelSentimentAnalyzer(4, stopWords, sentimentLexicon);

        try {
            Map<String, SentimentScore> results = analyzer.analyze(input1, input2);

            results.forEach((docId, sentiment) ->
                System.out.println(docId + ": " + sentiment.getDescription())
            );
        } catch (Exception e) {
            System.err.println("Error during sentiment analysis: " + e.getMessage());
        }
    }
}
