package questions.QList.ElevatorSystem.first;

// ElevatorController.java
import java.util.ArrayList;
import java.util.List;

import questions.QList.ElevatorSystem.first.ElevatorStrategy.ElevatorStrategy;
import questions.QList.ElevatorSystem.first.ElevatorStrategy.NearestElevatorStrategy;

public class ElevatorController {
    private static ElevatorController instance;
    private List<ElevatorCar> elevators;
    private RequestProcessor requestProcessor;
    private ElevatorStrategy strategy;
    private Building building;
    
    private ElevatorController(Building building) {
        this.building = building;
        this.elevators = building.getElevators();
        this.requestProcessor = new RequestProcessor();
        this.strategy = new NearestElevatorStrategy(); // Default strategy
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
    
    public void submitRequest(Request request) {
        requestProcessor.addRequest(request);
    }
    
    public void processRequest(Request request) {
        ElevatorCar selectedElevator = strategy.selectElevator(request, elevators);
        
        if (selectedElevator != null) {
            // If it's an external request (no destination), set destination to source first
            if (request.getDestinationFloor() == -1) {
                request.setDestinationFloor(request.getSourceFloor());
            }
            selectedElevator.addRequest(request);
        } else {
            System.out.println("No suitable elevator found for request: " + request);
        }
    }
    
    public void setStrategy(ElevatorStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void stopSystem() {
        requestProcessor.stop();
        for (ElevatorCar elevator : elevators) {
            elevator.stop();
        }
    }
}
