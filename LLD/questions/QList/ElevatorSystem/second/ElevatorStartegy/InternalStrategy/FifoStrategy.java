package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.ArrayList;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

// Strategy 3: FIFO (First In First Out)
/**
 * First-In-First-Out (FIFO) Strategy - Simple and fair approach
 *
 * FEATURES:
 * - Serves floors in the order they were requested
 * - Simple to implement and understand
 * - Guarantees fairness - no starvation
 * - Not efficient for travel optimization
 * - Good for testing, debugging, and basic scenarios
 *
 * ALGORITHM:
 * 1. Serve floors in the exact order they were added to pending list
 * 2. No optimization based on distance or direction
 * 3. First come, first served principle
 *
 * NOTE: In real implementation, we would track request timestamps.
 * Here we use the list order as a simplification.
 */
public class FifoStrategy implements InternalRequestStrategy {

    @Override
    public List<Integer> planRoute(ElevatorCar elevator, List<Integer> pendingFloors) {
        System.out.println("🔄 FIFO Algorithm planning route for floors: " + pendingFloors);

        // Early return if no floors to serve
        if (pendingFloors.isEmpty()) {
            System.out.println("ℹ️ No pending floors to serve");
            return new ArrayList<>();
        }

        // FIFO ALGORITHM: Simple first-come-first-served
        // No optimization - just preserve the original order
        List<Integer> optimalRoute = new ArrayList<>(pendingFloors);

        System.out.println("📋 FIFO Algorithm Route (First-Come-First-Served): " + optimalRoute);
        System.out.println("✅ Fairness guaranteed - No optimization applied");

        return optimalRoute;
    }

    @Override
    public String getStrategyName() {
        return "First-In-First-Out (FIFO) - Fair Service";
    }
}