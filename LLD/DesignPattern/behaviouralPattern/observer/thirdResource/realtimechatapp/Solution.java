package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.chatApp.LoggerListener;
import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.chatApp.UIListener;

public class Solution {
    public static void main(String[] args) throws InterruptedException {
        AsyncMessagePublisher publisher = new AsyncMessagePublisher();

        publisher.register(new UIListener());
        publisher.register(new LoggerListener());
       //publisher.register(new AnalyticsListener());

        // Simulate multiple threads sending messages
        Runnable messageTask = () -> {
            String[] messages = {"Hello 👋", "How are you?", "New update available!", "User joined group 🎉"};
            for (String msg : messages) {
                publisher.publishMessage(msg);
                try {
                    Thread.sleep(500); // simulate time between messages
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread t1 = new Thread(messageTask, "Sender-1");
        Thread t2 = new Thread(messageTask, "Sender-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        publisher.shutdown();
    }

}

