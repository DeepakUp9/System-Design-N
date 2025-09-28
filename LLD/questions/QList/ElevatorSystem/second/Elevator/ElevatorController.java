package questions.QList.ElevatorSystem.second.Elevator;

import java.util.List;

import questions.QList.ElevatorSystem.second.Building;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.ExternalRequestHandler;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.ExternalRequestStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.SmartAssignmentStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.InternalRequestHandler;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.InternalRequestStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.LookScanStrategy;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;
import questions.QList.ElevatorSystem.second.request.InternalRequest;
import questions.QList.ElevatorSystem.second.request.RequestProcessor;

public class ElevatorController {
     private static ElevatorController instance;
        private List<ElevatorCar> elevators;
        private RequestProcessor requestProcessor;
        private ExternalRequestHandler externalRequestHandler;
        private InternalRequestHandler internalRequestHandler;
        private Building building;

        private ElevatorController(Building building) {
            this.building = building;
            this.elevators = building.getElevators();
            this.requestProcessor = new RequestProcessor();
            this.externalRequestHandler = new ExternalRequestHandler(new SmartAssignmentStrategy());
            this.internalRequestHandler = new InternalRequestHandler(new LookScanStrategy());
        }

        public static synchronized ElevatorController getInstance() {
            if (instance == null) {
                throw new IllegalStateException("ElevatorController not initialized");
            }
            return instance;
        }

        public static synchronized void initialize(Building building) {
            if (instance == null) {
                instance = new ElevatorController(building);
            }
        }

        public void submitExternalRequest(ExternalRequest request) {
            ElevatorCar assignedElevator = externalRequestHandler.handleRequest(request, elevators);
            if (assignedElevator != null) {
                assignedElevator.addRequest(request);
            }
        }

        public void submitInternalRequest(InternalRequest request) {
            ElevatorCar elevator = findElevatorById(request.getElevatorId());
            if (elevator != null) {
                List<Integer> route = internalRequestHandler.handleRequest(elevator, request);
                // Execute the planned route
                executeRoute(elevator, route);
            }
        }

        private ElevatorCar findElevatorById(int elevatorId) {
            return elevators.stream()
                    .filter(e -> e.getId() == elevatorId)
                    .findFirst()
                    .orElse(null);
        }

        private void executeRoute(ElevatorCar elevator, List<Integer> route) {
            new Thread(() -> {
                for (Integer floor : route) {
                    elevator.moveTo(floor);
                    try { Thread.sleep(1000); } catch (InterruptedException e) {}
                    elevator.clearFloor(floor);
                }
                elevator.setIdle();
            }).start();
        }

        public void setExternalStrategy(ExternalRequestStrategy strategy) {
            externalRequestHandler.setStrategy(strategy);
        }

        public void setInternalStrategy(InternalRequestStrategy strategy) {
            internalRequestHandler.setStrategy(strategy);
        }

        public void stopSystem() {
            requestProcessor.stop();
            for (ElevatorCar elevator : elevators) {
                elevator.stop();
            }
        }
    }