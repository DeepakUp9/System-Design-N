package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

// Strategy 1: LOOK/SCAN Algorithm
public class LookScanStrategy implements InternalRequestStrategy {
    
    @Override
    public String getStrategyName() {
        return "LOOK/SCAN Strategy";
    }
    
    @Override
    public List<Integer> planRoute(ElevatorCar elevator, InternalRequest request) {
        int currentFloor = elevator.getCurrentFloor();
        int targetFloor = request.getFloor();
        Direction currentDirection = elevator.getDirection();
        
        System.out.println("🔄 " + getStrategyName() + " planning route for: " + request);
        System.out.println("   Current: Floor " + currentFloor + ", Direction: " + currentDirection);
        
        // Get all pending floors including the new request
        Set<Integer> allFloors = new HashSet<>(elevator.getPendingFloors());
        allFloors.add(targetFloor);
        
        List<Integer> route = new ArrayList<>();
        
        // Separate floors by direction
        List<Integer> floorsAbove = allFloors.stream()
                .filter(floor -> floor > currentFloor)
                .sorted()
                .collect(Collectors.toList());
        
        List<Integer> floorsBelow = allFloors.stream()
                .filter(floor -> floor < currentFloor)
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
        
        // Apply LOOK algorithm based on current direction
        if (currentDirection == Direction.UP || currentDirection == Direction.IDLE) {
            // First serve floors above in ascending order
            route.addAll(floorsAbove);
            // Then serve floors below in descending order
            route.addAll(floorsBelow);
        } else { // Direction.DOWN
            // First serve floors below in descending order
            route.addAll(floorsBelow);
            // Then serve floors above in ascending order
            route.addAll(floorsAbove);
        }
        
        System.out.println("📋 LOOK Route planned: " + route);
        return route;
    }
}
