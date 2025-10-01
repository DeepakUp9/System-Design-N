package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;

// Strategy 2: Shortest Seek Time First (SSTF)
/**
 * Shortest Seek Time First (SSTF) Strategy - Distance-based optimization
 *
 * FEATURES:
 * - Always serves the closest floor next (minimum travel distance)
 * - Minimizes travel time between consecutive stops
 * - Can cause starvation for faraway floors
 * - Good for systems prioritizing quick service over fairness
 *
 * ALGORITHM:
 * 1. Start from current floor position
 * 2. Always find and serve the closest pending floor
 * 3. Repeat until all floors are served
 *
 * WARNING: May cause starvation where distant floors wait indefinitely
 */
public class ShortestSeekTimeStrategy implements InternalRequestStrategy {

    @Override
    public List<Integer> planRoute(ElevatorCar elevator, List<Integer> pendingFloors) {
        int currentFloor = elevator.getCurrentFloor();

        System.out.println("🔄 SSTF Algorithm planning route from floor " + currentFloor +
                ", Pending floors: " + pendingFloors);

        // Early return if no floors to serve
        if (pendingFloors.isEmpty()) {
            System.out.println("ℹ️ No pending floors to serve");
            return new ArrayList<>();
        }

        List<Integer> optimalRoute = new ArrayList<>();
        List<Integer> remainingFloors = new ArrayList<>(pendingFloors); // Working copy
        int currentPosition = currentFloor; // Track current position during planning

        System.out.println("🎯 Starting SSTF optimization from floor " + currentPosition);

        // SSTF ALGORITHM: Greedy approach - always pick closest floor
        while (!remainingFloors.isEmpty()) {
            // Find the floor with minimum seek time (absolute distance)
            int finalCurrentPos = currentPosition; // Required for lambda expression
            Integer closestFloor = remainingFloors.stream()
                    .min(Comparator.comparingInt(floor -> Math.abs(floor - finalCurrentPos)))
                    .orElse(null);

            if (closestFloor != null) {
                // Add the closest floor to our optimal route
                optimalRoute.add(closestFloor);

                // Calculate and log the distance traveled
                int distance = Math.abs(closestFloor - currentPosition);
                System.out.println("   ➡️ Serving floor " + closestFloor + " (distance: " + distance + " floors)");

                // Remove served floor from remaining list
                remainingFloors.remove(closestFloor);

                // Update current position for next iteration
                currentPosition = closestFloor;
            }
        }

        System.out.println("📋 SSTF Algorithm Optimal Route: " + optimalRoute);
        System.out.println("⚠️ Note: SSTF may cause starvation for distant floors");
        return optimalRoute;
    }

    @Override
    public String getStrategyName() {
        return "Shortest Seek Time First (SSTF) - Distance Optimized";
    }
}