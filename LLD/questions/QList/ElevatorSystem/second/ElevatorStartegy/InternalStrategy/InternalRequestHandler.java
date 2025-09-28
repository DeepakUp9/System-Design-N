package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

public class InternalRequestHandler {
    private InternalRequestStrategy strategy;
    
    public InternalRequestHandler(InternalRequestStrategy strategy) {
        this.strategy = strategy;
    }
    
    public void setStrategy(InternalRequestStrategy strategy) {
        this.strategy = strategy;
        System.out.println("🔄 Internal strategy changed to: " + strategy.getStrategyName());
    }
    
    public List<Integer> handleRequest(ElevatorCar elevator, InternalRequest request) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🏢 INTERNAL REQUEST PROCESSING");
        System.out.println("=".repeat(60));
        
        List<Integer> route = strategy.planRoute(elevator, request);
        elevator.addPendingFloor(request.getFloor());
        
        System.out.println("✅ Route planned for Elevator " + elevator.getId());
        return route;
    }
}
