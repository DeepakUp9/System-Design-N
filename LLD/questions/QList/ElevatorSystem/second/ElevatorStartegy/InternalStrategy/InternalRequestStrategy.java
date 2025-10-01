package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
/**
 * Strategy interface for internal request processing algorithms
 * Different implementations provide different optimization strategies
 * for serving floor requests inside the elevator
 */
public interface InternalRequestStrategy {

    /**
     * Plans optimal route for serving pending floors based on specific algorithm
     * @param elevator The elevator for which route is being planned (contains current state)
     * @param pendingFloors List of floors that need to be served (all pending destinations)
     * @return Ordered list of floors in optimal serving sequence according to the strategy
     */
    List<Integer> planRoute(ElevatorCar elevator, List<Integer> pendingFloors);

    /**
     * Returns descriptive name of the strategy for display and logging purposes
     * @return Strategy name as String
     */
    String getStrategyName();
}
