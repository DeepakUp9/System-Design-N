package questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

// ============= STRATEGY CONTEXT CLASSES =============
/**
 * Handles external request assignment using strategy pattern
 * Decides which elevator should serve hallway button presses
 */
public class ExternalRequestHandler {
    private ExternalRequestStrategy strategy;

    public ExternalRequestHandler(ExternalRequestStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ExternalRequestStrategy strategy) {
        this.strategy = strategy;
        System.out.println("🔄 External strategy changed to: " + strategy.getStrategyName());
    }

    /**
     * Handles external request assignment using current strategy
     * @return Assigned elevator or null if no elevator available
     */
    public ElevatorCar handleRequest(ExternalRequest request, List<ElevatorCar> elevators) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🌐 EXTERNAL REQUEST: " + request);
        System.out.println("=".repeat(50));

        ElevatorCar assigned = strategy.assignElevator(request, elevators);
        if (assigned != null) {
            System.out.println("✅ Assigned to Elevator " + assigned.getId());
        } else {
            System.out.println("❌ No elevator available");
        }

        return assigned;
    }
}
