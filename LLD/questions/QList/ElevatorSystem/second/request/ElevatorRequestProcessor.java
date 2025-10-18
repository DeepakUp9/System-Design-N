package questions.QList.ElevatorSystem.second.request;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.InternalRequestStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.LookScanStrategy;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;

public class ElevatorRequestProcessor {
        private ElevatorCar elevator;
        private BlockingQueue<Request> requests;
        private Set<Integer> pendingFloors;
        private InternalRequestStrategy internalStrategy;
        private boolean running;

        public ElevatorRequestProcessor(ElevatorCar elevator) {
            this.elevator = elevator;
            this.requests = new LinkedBlockingQueue<>();
            this.pendingFloors = new HashSet<>();
            this.internalStrategy = new LookScanStrategy();
            this.running = true;

            new Thread(this::processRequests).start();
        }

        /**
         * Add any type of request to this elevator's queue
         */
        public void addRequest(Request request) {
            requests.add(request);
            System.out.println("📥 Elevator " + elevator.getId() + " received: " + request);
        }

        /**
         * Main processing loop - runs in separate thread
         */
        private void processRequests() {
            while (running) {
                try {
                    Request request = requests.take();
                    processSingleRequest(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        /**
         * Process a single request
         */
        private void processSingleRequest(Request request) {
            if (request instanceof InternalRequest) {
                processInternalRequest((InternalRequest) request);
            } else if (request instanceof ExternalRequest) {
                processExternalRequest((ExternalRequest) request);
            }

            // After processing request, check if we need to execute route
            if (!pendingFloors.isEmpty() && elevator.getState() == ElevatorState.IDLE) {
                executeOptimizedRoute();
            }
        }

        /**
         * Process internal request - just add to pending floors
         */
        private void processInternalRequest(InternalRequest request) {
            System.out.println("🔄 Processing internal request: " + request);
            pendingFloors.add(request.getFloor());
        }

        /**
         * Process external request - just add to pending floors
         */
        private void processExternalRequest(ExternalRequest request) {
            System.out.println("🌐 Processing external request: " + request);
            pendingFloors.add(request.getFloor());
        }

        /**
         * Execute optimized route using strategy pattern
         */
        private void executeOptimizedRoute() {
            if (pendingFloors.isEmpty()) {
                elevator.setIdle();
                return;
            }

            // Convert to list for strategy processing
            List<Integer> pendingList = new ArrayList<>(pendingFloors);

            // Get optimized route from strategy
            List<Integer> route = internalStrategy.planRoute(elevator, pendingList);

            System.out.println("🗺️ Elevator " + elevator.getId() + " executing route: " + route);

            // Execute the route
            for (Integer floor : route) {
                if (!running) break;

                // Move elevator physically
                elevator.moveToFloor(floor);

                // Stop at floor
                elevator.stopAtFloor(floor);

                // Remove from pending floors
                pendingFloors.remove(floor);

                // Small pause between floors
                try { Thread.sleep(500); } catch (InterruptedException e) { break; }
            }

            // Set to idle after completing route
            elevator.setIdle();
        }

        /**
         * Strategy management
         */
        public void setInternalStrategy(InternalRequestStrategy strategy) {
            this.internalStrategy = strategy;
            System.out.println("🔄 Elevator " + elevator.getId() + " strategy: " + strategy.getStrategyName());
        }

        public Set<Integer> getPendingFloors() {
            return new HashSet<>(pendingFloors);
        }

        public void stop() {
            running = false;
            elevator.stop();
        }

        public void clearPendingRequests() {
            synchronized (pendingFloors) {
                pendingFloors.clear();
            }
            System.out.println("🧹 Cleared all pending requests for Elevator " + elevator.getId());
        }
    }
