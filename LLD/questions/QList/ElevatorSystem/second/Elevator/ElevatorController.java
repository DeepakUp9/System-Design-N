package questions.QList.ElevatorSystem.second.Elevator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import questions.QList.ElevatorSystem.second.Building;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.ExternalRequestHandler;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.ExternalRequestStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy.SmartAssignmentStrategy;
import questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy.InternalRequestStrategy;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;
import questions.QList.ElevatorSystem.second.request.ElevatorRequestProcessor;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;
import questions.QList.ElevatorSystem.second.request.InternalRequest;
import questions.QList.ElevatorSystem.second.request.RequestProcessor;

/**
* Central controller managing all elevators and request distribution
* Implements Singleton pattern for global access
*/
public class ElevatorController {
    private static ElevatorController instance;
    private List<ElevatorCar> elevators;
    private ExternalRequestHandler externalRequestHandler;
    private Map<Integer, ElevatorRequestProcessor> elevatorProcessors;
    private RequestProcessor globalRequestProcessor;

    private Building building;

    private ElevatorController(Building building) {
        this.building = building;
        this.elevators = building.getElevators();
        this.elevatorProcessors = new HashMap<>();
        this.globalRequestProcessor = new RequestProcessor();
        // Use SmartAssignment as default for external requests
        this.externalRequestHandler = new ExternalRequestHandler(new SmartAssignmentStrategy());

        // Initialize processor for each elevator
        for (ElevatorCar elevator : elevators) {
            elevatorProcessors.put(elevator.getId(), new ElevatorRequestProcessor(elevator));
        }
    }

    public static synchronized ElevatorController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("❌ ElevatorController not initialized. Call initialize() first.");
        }
        return instance;
    }

    public static synchronized void initialize(Building building) {
        if (instance == null) {
            instance = new ElevatorController(building);
            System.out.println("✅ ElevatorController initialized with " + building.getElevators().size() + " elevators");
        }
    }

    /**
     * Handles external requests from hallway buttons
     * Uses strategy to assign to best elevator
     */
    public void submitExternalRequest(ExternalRequest request) {
        System.out.println("🎯 Processing external request: " + request);

        // Use strategy to select best elevator
        ElevatorCar assignedElevator = externalRequestHandler.handleRequest(request, elevators);

        if (assignedElevator != null) {
            // Send to specific elevator's processor
            ElevatorRequestProcessor processor = elevatorProcessors.get(assignedElevator.getId());
            processor.addRequest(request);
        } else {
            System.out.println("❌ Could not assign elevator for: " + request);
        }
    }

    public void clearAllPendingRequests() {
        for (ElevatorRequestProcessor processor : elevatorProcessors.values()) {
            processor.clearPendingRequests();
        }
        System.out.println("🧹 Cleared all pending requests system-wide");
    }

    /**
     * Handles internal requests from inside elevators
     * Directly sends to specified elevator
     */
    //will to later if needed 
    public void submitInternalRequest(InternalRequest request) {
        ElevatorCar elevator = findElevatorById(request.getElevatorId());
        if (elevator != null) {
            //elevator.addRequest(request);
        } else {
            System.out.println("❌ Invalid elevator ID in internal request: " + request.getElevatorId());
        }
    }

    /**
     * Finds elevator by ID
     */
    public ElevatorCar findElevatorById(int elevatorId) {
        return elevators.stream()
                .filter(e -> e.getId() == elevatorId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Changes external request assignment strategy at runtime
     */
    public void setExternalStrategy(ExternalRequestStrategy strategy) {
        externalRequestHandler.setStrategy(strategy);
    }

    /**
     * Changes internal request strategy for all elevators
     */
    public void setInternalStrategy(InternalRequestStrategy strategy) {
        for (ElevatorCar elevator : elevators) {
            ElevatorRequestProcessor elevatorRequestProcessor = elevatorProcessors.get(elevator.getId());
            elevatorRequestProcessor.setInternalStrategy(strategy);
        }
    }

    /**
     * Stops all elevators and system components
     */
    public void stopSystem() {
        System.out.println("🛑 Stopping elevator system...");
        for (ElevatorCar elevator : elevators) {
            elevator.stop();
        }
    }

    public ElevatorRequestProcessor getProcessorForElevator(int elevatorId) {
        return elevatorProcessors.get(elevatorId);
    }
    public RequestProcessor getRequestProcessor() {
        return globalRequestProcessor;
    }

}
