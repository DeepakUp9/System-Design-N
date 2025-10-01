package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.ArrayList;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

/**
 * Handler for internal requests (buttons pressed inside elevator)
 * Uses InternalRequestStrategy to plan optimal routes for serving floors
 *
 * RESPONSIBILITIES:
 * - Manage internal request processing pipeline
 * - Coordinate between elevator and strategy
 * - Handle strategy switching at runtime
 * - Plan optimal routes for all pending floors
 */
public class InternalRequestHandler {
    private InternalRequestStrategy strategy;

    /**
     * Constructor - Initializes with a specific internal request strategy
     * @param strategy The internal request strategy to use (LOOK, SSTF, FIFO, etc.)
     */
    public InternalRequestHandler(InternalRequestStrategy strategy) {
        this.strategy = strategy;
        System.out.println("✅ InternalRequestHandler initialized with: " + strategy.getStrategyName());
    }

    /**
     * Changes the internal request strategy at runtime
     * Allows dynamic algorithm switching without restarting system
     * @param strategy New strategy to use for route planning
     */
    public void setStrategy(InternalRequestStrategy strategy) {
        this.strategy = strategy;
        System.out.println("🔄 Internal Request Strategy changed to: " + strategy.getStrategyName());
    }

    /**
     * Handles internal request and returns optimized route using current strategy
     * PROCESS:
     * 1. Add new floor to elevator's pending list
     * 2. Get ALL pending floors (including existing ones)
     * 3. Use strategy to plan optimal route for ALL pending floors
     * 4. Return the optimized serving sequence
     *
     * @param elevator The elevator where request was made (for state and pending floors)
     * @param request The internal request containing target floor and elevator ID
     * @return List of floors in optimal serving order according to current strategy
     */
    // public List<Integer> handleRequest(ElevatorCar elevator, InternalRequest request) {
    //     System.out.println("\n" + "=".repeat(60));
    //     System.out.println("🏢 INTERNAL REQUEST PROCESSING");
    //     System.out.println("   Strategy: " + strategy.getStrategyName());
    //     System.out.println("   Request: " + request);
    //     System.out.println("=".repeat(60));

    //     // STEP 1: Add the newly requested floor to elevator's pending list
    //     elevator.addPendingFloor(request.getFloor());
    //     System.out.println("📥 Added floor " + request.getFloor() + " to pending list");

    //     // STEP 2: Get ALL pending floors (including previous requests and new one)
    //     List<Integer> allPendingFloors = new ArrayList<>(elevator.getPendingFloors());
    //     System.out.println("📋 All pending floors: " + allPendingFloors);

    //     // STEP 3: Use current strategy to plan optimal route for ALL pending floors
    //     System.out.println("🎯 Planning optimal route using " + strategy.getStrategyName());
    //     List<Integer> route = strategy.planRoute(elevator, allPendingFloors);

    //     // STEP 4: Return the optimized route for execution
    //     System.out.println("✅ Route planned for Elevator " + elevator.getId() + ": " + route);
    //     return route;
    // }

    /**
     * Gets the currently active strategy (for monitoring and debugging)
     * @return Current InternalRequestStrategy instance
     */
    public InternalRequestStrategy getCurrentStrategy() {
        return strategy;
    }
}