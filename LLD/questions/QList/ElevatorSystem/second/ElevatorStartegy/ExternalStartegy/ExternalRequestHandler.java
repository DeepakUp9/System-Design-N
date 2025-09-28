package questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

// ============= STRATEGY CONTEXT CLASSES =============

public class ExternalRequestHandler {
    private ExternalRequestStrategy strategy;
    
    public ExternalRequestHandler(ExternalRequestStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(ExternalRequestStrategy strategy) {
        this.strategy = strategy;
        System.out.println("🔄 External strategy changed to: " + strategy.getStrategyName());
    }
    
    public ElevatorCar handleRequest(ExternalRequest request, List<ElevatorCar> elevators) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🌐 EXTERNAL REQUEST PROCESSING");
        System.out.println("=".repeat(60));
        
        ElevatorCar assigned = strategy.assignElevator(request, elevators);
        if (assigned != null) {
            assigned.addExternalRequest(request);
            System.out.println("✅ Request assigned to: " + assigned.getId());
        } else {
            System.out.println("❌ No elevator available for request");
        }
        
        return assigned;
    }
}