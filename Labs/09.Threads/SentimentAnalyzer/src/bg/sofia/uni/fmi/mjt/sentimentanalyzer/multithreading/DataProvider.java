package bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.AnalyzerInput;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DataProvider implements Runnable {
    private final AnalyzerInput analyzerInput;
    private final Set<String> stopWords;
    private final BlockingQueue queue;

    public DataProvider(Set<String> stopWords,
                        AnalyzerInput analyzerInput,
                        BlockingQueue queue) {
        this.analyzerInput = analyzerInput;
        this.stopWords = stopWords;
        this.queue = queue;
    }

    @Override
    public void run() {
        String text = processText();
        try {
            queue.add(new Task(analyzerInput.inputID(), text));
        } catch (InterruptedException e) {
            throw new SentimentAnalysisException("Failed to add task to queue", e);
        }
    }

    private String processText() {
        String noPunctuationString = extractTextFromReader()
                .replaceAll("\\p{Punct}", "")
                .trim();

        return Arrays.stream(noPunctuationString
                .split("\\s+"))
                .map(String::toLowerCase)
                .filter(w -> !stopWords.contains(w) && !w.isEmpty())
                .collect(Collectors.joining(" "));
    }

    private String extractTextFromReader() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(analyzerInput.inputReader())) {
            String line;

            while (true) {
                line = reader.readLine();

                if (line == null) {
                    break;
                }

                sb.append(line).append(" ");
            }
        } catch (IOException ex) {
            return "";
        }

        return sb.toString();
    }
}
