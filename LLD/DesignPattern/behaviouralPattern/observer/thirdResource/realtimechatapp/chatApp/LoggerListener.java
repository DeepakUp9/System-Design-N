package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.chatApp;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.ChatObserver;

public class LoggerListener implements ChatObserver {
    @Override
    public void onMessage(String message) {
        System.out.println("[Logger] Writing to log file: " + message);
    }
}