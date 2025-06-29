package LLD.DesignPattern.behaviouralPattern.observer.thirdResource.realtimechatapp;

public interface Subject {
   void subscribe(ChatObserver o);
   void  unsubscribe(ChatObserver o);
   void notifychange(); 
}
