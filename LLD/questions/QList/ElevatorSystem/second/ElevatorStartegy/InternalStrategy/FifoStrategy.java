package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.ArrayList;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

// Strategy 3: FIFO (First In First Out)
class FifoStrategy implements InternalRequestStrategy {
    
    @Override
    public String getStrategyName() {
        return "First In First Out Strategy";
    }
    
    @Override
    public List<Integer> planRoute(ElevatorCar elevator, InternalRequest request) {
        System.out.println("🔄 " + getStrategyName() + " planning route for: " + request);
        
        // Simple FIFO - serve floors in the order they were requested
        List<Integer> route = new ArrayList<>(elevator.getPendingFloors());
        route.add(request.getFloor());
        
        System.out.println("📋 FIFO Route planned: " + route);
        return route;
    }
}
