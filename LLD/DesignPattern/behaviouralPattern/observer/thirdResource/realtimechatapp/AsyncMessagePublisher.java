package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncMessagePublisher {
    private final List<ChatObserver> observers = new CopyOnWriteArrayList<>();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public void register(ChatObserver observer) {
        observers.add(observer);
    }

    public void unregister(ChatObserver observer) {
        observers.remove(observer);
    }

    public void publishMessage(String message) {
        for (ChatObserver observer : observers) {
            executor.submit(() -> observer.onMessage(message));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
