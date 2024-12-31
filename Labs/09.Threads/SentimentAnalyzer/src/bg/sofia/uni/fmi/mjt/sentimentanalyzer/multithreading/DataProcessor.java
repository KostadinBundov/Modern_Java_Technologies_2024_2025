package bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.SentimentScore;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;

import java.util.Arrays;
import java.util.Map;

public class DataProcessor implements Runnable {
    private final Map<String, SentimentScore> sentimentLexicon;
    private final Map<String, SentimentScore> results;
    private final BlockingQueue queue;

    public DataProcessor(BlockingQueue queue, Map<String, SentimentScore> sentimentLexicon,
                         Map<String, SentimentScore> results) {
        this.queue = queue;
        this.sentimentLexicon = sentimentLexicon;
        this.results = results;
    }

    @Override
    public void run() {
        while (true) {
            Task task;

            try {
                task = queue.remove();
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Failed to remove task from queue", e);
            }

            if ("POISON_PILL".equals(task.id())) {
                return;
            }

            int totalScore = evaluateWordsInText(task.text());
            SentimentScore sentiment = SentimentScore.fromScore(totalScore);

            synchronized (results) {
                results.put(task.id(), sentiment);
            }
        }
    }

    private int evaluateWordsInText(String text) {
        return Arrays.stream(text.split("\\s+"))
            .filter(sentimentLexicon::containsKey)
            .mapToInt(word -> sentimentLexicon.get(word).getScore())
            .sum();
    }
}
