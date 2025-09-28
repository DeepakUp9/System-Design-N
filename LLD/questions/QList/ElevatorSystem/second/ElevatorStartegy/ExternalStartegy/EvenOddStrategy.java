package questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

// Strategy 2: Pure Even-Odd Strategy
public class EvenOddStrategy implements ExternalRequestStrategy {
    
    @Override
    public String getStrategyName() {
        return "Even-Odd Strategy";
    }
    
    @Override
    public ElevatorCar assignElevator(ExternalRequest request, List<ElevatorCar> ElevatorCars) {
        int targetFloor = request.getFloor();
        boolean isEvenFloor = (targetFloor % 2 == 0);
        
        System.out.println("🎯 " + getStrategyName() + " processing: " + request);
        System.out.println("   Floor " + targetFloor + " is " + (isEvenFloor ? "EVEN" : "ODD"));
        
        // Find matching ElevatorCars (even ElevatorCars for even floors, odd for odd)
        List<ElevatorCar> matchingElevatorCars = ElevatorCars.stream()
                .filter(e -> e.getState() != ElevatorState.MAINTENANCE)
                .filter(e -> (e.getId() % 2 == 0) == isEvenFloor)
                .collect(Collectors.toList());
        
        if (matchingElevatorCars.isEmpty()) {
            // No matching ElevatorCars, use any available
            System.out.println("⚠️ No matching even-odd ElevatorCars, using any available");
            return ElevatorCars.stream()
                    .filter(e -> e.getState() != ElevatorState.MAINTENANCE)
                    .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - targetFloor)))
                    .orElse(null);
        }
        
        // Among matching ElevatorCars, choose the closest
        ElevatorCar selected = matchingElevatorCars.stream()
                .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - targetFloor)))
                .orElse(null);
        
        if (selected != null) {
            System.out.println("✅ Selected ElevatorCar " + selected.getId() + 
                             " (" + (selected.getId() % 2 == 0 ? "even" : "odd") + 
                             ") for " + (isEvenFloor ? "even" : "odd") + " floor");
        }
        
        return selected;
    }
}
