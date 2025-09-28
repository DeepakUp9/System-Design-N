package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

// Strategy 2: Shortest Seek Time First (SSTF)
class ShortestSeekTimeStrategy implements InternalRequestStrategy {
    
    @Override
    public String getStrategyName() {
        return "Shortest Seek Time First Strategy";
    }
    
    @Override
    public List<Integer> planRoute(ElevatorCar elevator, InternalRequest request) {
        int currentFloor = elevator.getCurrentFloor();
        int targetFloor = request.getFloor();
        
        System.out.println("🔄 " + getStrategyName() + " planning route for: " + request);
        
        Set<Integer> allFloors = new HashSet<>(elevator.getPendingFloors());
        allFloors.add(targetFloor);
        
        List<Integer> route = new ArrayList<>();
        List<Integer> remainingFloors = new ArrayList<>(allFloors);
        int current = currentFloor;
        
        // Always pick the closest floor next
        while (!remainingFloors.isEmpty()) {
            final int currentPos = current;
            Integer closest = remainingFloors.stream()
                    .min(Comparator.comparingInt(floor -> Math.abs(floor - currentPos)))
                    .orElse(null);
            
            if (closest != null) {
                route.add(closest);
                remainingFloors.remove(closest);
                current = closest;
            }
        }
        
        System.out.println("📋 SSTF Route planned: " + route);
        return route;
    }
}