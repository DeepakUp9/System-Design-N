package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.chatApp;

import LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp.ChatObserver;

public class UIListener implements ChatObserver {
    @Override
    public void onMessage(String message) {
        System.out.println("[UI] Displaying message: " + message);
    }
}