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

/**
 * LOOK/SCAN Algorithm Strategy
 * - First serves all floors in current direction, then reverses
 * - Most efficient for minimizing wait time and energy
 * - Used in real-world elevators
 */
public class LookScanStrategy implements InternalRequestStrategy {
    
    @Override
    public List<Integer> planRoute(ElevatorCar elevator, List<Integer> pendingFloors) {
        // Get current elevator status
        int currentFloor = elevator.getCurrentFloor();
        Direction currentDirection = elevator.getDirection();
        
        System.out.println("🔄 LOOK Algorithm planning route from floor " + currentFloor + 
                         ", Direction: " + currentDirection + ", Pending: " + pendingFloors);
        
        // If no pending floors, return empty route
        if (pendingFloors.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Separate floors into two lists based on current position
        List<Integer> floorsAbove = new ArrayList<>();
        List<Integer> floorsBelow = new ArrayList<>();
        
        for (Integer floor : pendingFloors) {
            if (floor > currentFloor) {
                floorsAbove.add(floor);    // Floors above current position
            } else if (floor < currentFloor) {
                floorsBelow.add(floor);    // Floors below current position
            }
            // Note: current floor is handled separately if it's in pending
        }
        
        // Sort floors in appropriate order
        Collections.sort(floorsAbove);     // Ascending order for floors above
        Collections.sort(floorsBelow, Collections.reverseOrder()); // Descending for floors below
        
        List<Integer> optimalRoute = new ArrayList<>();
        
        // LOOK Algorithm Logic:
        if (currentDirection == Direction.UP || currentDirection == Direction.IDLE) {
            // Moving UP or starting from idle: Serve floors above first, then below
            
            // 1. Serve all floors above current floor in ascending order
            optimalRoute.addAll(floorsAbove);
            
            // 2. Then serve all floors below current floor in descending order
            optimalRoute.addAll(floorsBelow);
            
        } else if (currentDirection == Direction.DOWN) {
            // Moving DOWN: Serve floors below first, then above
            
            // 1. Serve all floors below current floor in descending order
            optimalRoute.addAll(floorsBelow);
            
            // 2. Then serve all floors above current floor in ascending order
            optimalRoute.addAll(floorsAbove);
        }
        
        System.out.println("📋 LOOK Algorithm Route: " + optimalRoute);
        return optimalRoute;
    }
    
    @Override
    public String getStrategyName() {
        return "LOOK/SCAN Algorithm";
    }
}
