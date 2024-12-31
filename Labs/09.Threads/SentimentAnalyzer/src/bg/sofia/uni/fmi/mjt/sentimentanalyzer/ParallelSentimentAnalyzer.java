package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import bg.sofia.uni.fmi.mjt.sentimentanalyzer.exceptions.SentimentAnalysisException;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading.BlockingQueue;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading.DataProcessor;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading.DataProvider;
import bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ParallelSentimentAnalyzer implements SentimentAnalyzerAPI {
    private static final int MAX_COUNT_TASKS_IN_QUEUE = 10;
    private final int workersCount;
    private final Set<String> stopWords;
    private final Map<String, SentimentScore> sentimentLexicon;

    public ParallelSentimentAnalyzer(int workersCount, Set<String> stopWords,
                                     Map<String, SentimentScore> sentimentLexicon) {
        this.workersCount = workersCount;
        this.stopWords = stopWords;
        this.sentimentLexicon = sentimentLexicon;
    }

    @Override
    public Map<String, SentimentScore> analyze(AnalyzerInput... input) throws SentimentAnalysisException {
        BlockingQueue queue = new BlockingQueue(MAX_COUNT_TASKS_IN_QUEUE);
        Map<String, SentimentScore> results = new HashMap<>();

        List<Thread> producers = startProducers(input, queue);
        List<Thread> consumers = startConsumers(queue, results);

        waitForThreads(producers);
        waitForThreads(producers);

        // Добавяме толкова "Poison Pills", колкото са consumer-ите
        for (int i = 0; i < workersCount; i++) {
            try {
                queue.add(new Task("POISON_PILL", ""));
            } catch (InterruptedException e) {
                throw new SentimentAnalysisException("Failed to add poison pill", e);
            }
        }

        waitForThreads(consumers);

        return results;
    }

    private List<Thread> startProducers(AnalyzerInput[] input, BlockingQueue queue) {
        List<Thread> producers = new ArrayList<>();

        for (AnalyzerInput analyzerInput : input) {
            Thread producer = new Thread(new DataProvider(stopWords, analyzerInput, queue));
            producers.add(producer);
            producer.start();
        }

        return producers;
    }

    private List<Thread> startConsumers(BlockingQueue queue, Map<String, SentimentScore> results) {
        List<Thread> consumers = new ArrayList<>();
        for (int i = 0; i < workersCount; i++) {
            Thread consumer = new Thread(new DataProcessor(queue, sentimentLexicon, results));
            consumers.add(consumer);
            consumer.start();
        }
        return consumers;
    }

    private void waitForThreads(List<Thread> threads) throws SentimentAnalysisException {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SentimentAnalysisException("Thread interrupted", e);
            }
        }
    }
}