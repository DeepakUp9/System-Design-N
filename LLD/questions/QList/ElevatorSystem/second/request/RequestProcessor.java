package questions.QList.ElevatorSystem.second.request;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
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
            System.out.println("🌍 Global Request added: " + request);
        }

        private void processRequests() {
            while (running) {
                try {
                    Request request = pendingRequests.take();
                    routeRequest(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        /**
         * Route request to appropriate handler
         */
        private void routeRequest(Request request) {
            ElevatorController controller = ElevatorController.getInstance();

            if (request instanceof ExternalRequest) {
                // External requests go through assignment strategy
                controller.submitExternalRequest((ExternalRequest) request);
            } else if (request instanceof InternalRequest) {
                // Internal requests go directly to specific elevator
                InternalRequest internalRequest = (InternalRequest) request;
                ElevatorCar elevator = controller.findElevatorById(internalRequest.getElevatorId());
                if (elevator != null) {
                    ElevatorRequestProcessor processor = controller.getProcessorForElevator(elevator.getId());
                    processor.addRequest(internalRequest);
                }
            }
        }

        public void stop() {
            running = false;
            Thread.currentThread().interrupt();
        }
    }
