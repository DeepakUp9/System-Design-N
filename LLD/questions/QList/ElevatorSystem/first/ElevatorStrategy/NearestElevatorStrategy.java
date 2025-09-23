package questions.QList.ElevatorSystem.first.ElevatorStrategy;

import java.util.List;

import questions.QList.ElevatorSystem.first.Direction;
import questions.QList.ElevatorSystem.first.ElevatorCar;
import questions.QList.ElevatorSystem.first.Request;

public class NearestElevatorStrategy implements ElevatorStrategy {
    
    @Override
    public ElevatorCar selectElevator(Request request, List<ElevatorCar> elevators) {
        ElevatorCar bestElevator = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (ElevatorCar elevator : elevators) {
            // Check if elevator is going in same direction or is idle
            boolean isSuitable = elevator.getDirection() == Direction.IDLE ||
                               elevator.getDirection() == request.getDirection();
            
            if (isSuitable) {
                int distance = Math.abs(elevator.getCurrentFloor() - request.getSourceFloor());
                if (distance < minDistance) {
                    minDistance = distance;
                    bestElevator = elevator;
                }
            }
        }
        
        System.out.println("Selected elevator " + (bestElevator != null ? bestElevator.getId() : "none"));
        return bestElevator;
    }

}
   