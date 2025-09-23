package questions.QList.ElevatorSystem.second.request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestProcessor {
    BlockingQueue<Request> pendingRequest;
    boolean running;
    
    public RequestProcessor(){
        pendingRequest = new LinkedBlockingQueue<>(90);
        running = true;
        new Thread(new Runnable() {
            public void run(){
                processRequest();
            }
        }).start();
    }

   public void processRequest(){
      while (running) {
         Request request;
         try {
            request = pendingRequest.take();
            processNextReqeust(request);
         } catch (InterruptedException e) {
              e.printStackTrace();
             Thread.currentThread().interrupt();
         }
         
      }
   }

   public void processNextReqeust(Request request){
     System.out.println("processing next request..");
     //get instacene
     // instace = ElevatorController.getInstace();
     //instace.processRequest(request);

   }

   public void addRequest(Request request){
      try {
        System.out.println("adding request in the Queue");
        pendingRequest.put(request);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
   }

    public void processNextReqeust() throws InterruptedException{
        if(!pendingRequest.isEmpty()) {
            Request request = pendingRequest.poll();
            if(request != null){
              processNextReqeust(request);
            }
        }
    }
    public void stop(){
        running = false;
        Thread.currentThread().interrupt();
    }

}
