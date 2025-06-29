package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.chatApp;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.ChatObserver;

public class AnalyticsListener implements ChatObserver {
    @Override
    public void onMessage(String message) {
        System.out.println("[Analytics] Processing analytics for message: " + message);
    }
}