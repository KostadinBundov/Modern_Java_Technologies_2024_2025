package bg.sofia.uni.fmi.mjt.sentimentanalyzer.multithreading;

import java.util.LinkedList;
import java.util.Queue;

public class BlockingQueue {
    private final Queue<Task> data;
    private final int capacity;

    public BlockingQueue(int capacity) {
        data = new LinkedList<>();
        this.capacity = capacity;
    }

    public synchronized void add(Task task) throws InterruptedException {
        while (data.size() == capacity) {
            wait();
        }

        data.add(task);
        notifyAll();
    }

    public synchronized Task remove() throws InterruptedException {
        while (data.isEmpty()) {
            wait();
        }

        Task task = data.peek();
        data.remove();
        notifyAll();
        return task;
    }

    public int size() {
        return data.size();
    }
}