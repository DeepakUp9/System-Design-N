package questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy;

import java.util.Comparator;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

// Strategy 3: Closest Available Strategy
/**
 * Closest Available Strategy: Always picks the nearest elevator
 */
public class ClosestAvailableStrategy implements ExternalRequestStrategy {

    @Override
    public String getStrategyName() {
        return "Closest Available Strategy";
    }

    @Override
    public ElevatorCar assignElevator(ExternalRequest request, List<ElevatorCar> elevators) {
        int targetFloor = request.getFloor();

        System.out.println("🎯 " + getStrategyName() + " processing: " + request);

        ElevatorCar closest = elevators.stream()
                .filter(e -> e.getState() != ElevatorState.MAINTENANCE)
                .min(Comparator.comparingInt(e -> Math.abs(e.getCurrentFloor() - targetFloor)))
                .orElse(null);

        if (closest != null) {
            int distance = Math.abs(closest.getCurrentFloor() - targetFloor);
            System.out.println("✅ Closest Elevator " + closest.getId() + " (distance: " + distance + " floors)");
        }

        return closest;
    }
}