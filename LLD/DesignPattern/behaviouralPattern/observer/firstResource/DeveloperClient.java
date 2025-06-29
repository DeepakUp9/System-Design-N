package LLD.DesignPattern.behaviouralPattern.observer.firstResource;



public class DeveloperClient{

   public static void main(String args[]){
      Subject channel = new YoutubeChannel();
     
      Observer aman = new Subscriber("Aman");
      channel.subscribe(aman);

      Observer amakhann = new Subscriber("Bob");
      channel.subscribe(amakhann);

      channel.notifyChanges("new video uploded");
   }
}