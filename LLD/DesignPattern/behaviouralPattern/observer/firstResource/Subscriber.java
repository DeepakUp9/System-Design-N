package LLD.DesignPattern.behaviouralPattern.observer.firstResource;

public class Subscriber implements Observer{
   String name;

   public Subscriber(String name){
    this.name = name;
   }

   @Override
   public void notified(String titile){
      System.out.println("Hello"+ this.name + "New Video uploded: Notification"+ titile);
   }
}