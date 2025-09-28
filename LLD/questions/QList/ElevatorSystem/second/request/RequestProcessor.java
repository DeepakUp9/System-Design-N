package questions.QList.ElevatorSystem.second.request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;

public class RequestProcessor {
  private BlockingQueue<Request> pendingRequests;
  private boolean running;

  public RequestProcessor() {
    this.pendingRequests = new LinkedBlockingQueue<>();
    this.running = true;
    new Thread(this::processRequests).start();
  }

  public void addRequest(Request request) {
    pendingRequests.add(request);
    System.out.println("Request added to queue: " + request);
  }

  private void processRequests() {
    while (running) {
        try {
            Request request = pendingRequests.take();
            processNextRequest(request);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
        }
    }
  }

  private void processNextRequest(Request request) {
    System.out.println("Processing request: " + request);
    ElevatorController controller = ElevatorController.getInstance();

    if (request instanceof ExternalRequest) {
        controller.submitExternalRequest((ExternalRequest) request);
    } else if (request instanceof InternalRequest) {
        controller.submitInternalRequest((InternalRequest) request);
    }
  }

  public void processNextRequest() {
    if (!pendingRequests.isEmpty()) {
        Request request = pendingRequests.poll();
        if (request != null) {
            processNextRequest(request);
        }
    }
  }

  public void stop() {
    running = false;
    Thread.currentThread().interrupt();
  }
}
