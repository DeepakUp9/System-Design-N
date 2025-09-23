package questions.QList.ElevatorSystem.first;

// RequestProcessor.java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class RequestProcessor {
    private BlockingQueue<Request> pendingRequests;
    private boolean running;
    
    public RequestProcessor() {
        this.pendingRequests = new LinkedBlockingQueue<>();
        this.running = true;
        new Thread(this::processRequests).start(); // Automatic processing
    }
    
    // Method 1: Add request to queue (used by external callers)
    public void addRequest(Request request) {
        pendingRequests.add(request);
        System.out.println("Request added to queue: " + request);
    }
    
    // Method 2: Continuous automatic processing (runs in background thread)
    private void processRequests() {
        while (running) {
            try {
                Request request = pendingRequests.take(); // Blocks until request available
                processNextRequest(request); // Process one request
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    // Method 3: Process a single request (missing implementation!)
    private void processNextRequest(Request request) {
        System.out.println("Processing request: " + request);
        ElevatorController controller = ElevatorController.getInstance();
        controller.processRequest(request); // Delegate to controller
    }
    
    // Method 4: Manual single request processing (for testing/direct control)
    public void processNextRequest() {
        if (!pendingRequests.isEmpty()) {
            Request request = pendingRequests.poll(); // Non-blocking retrieval
            if (request != null) {
                processNextRequest(request); // Process the request
            }
        }
    }
    
    public void stop() {
        running = false;
        Thread.currentThread().interrupt();
    }
}
