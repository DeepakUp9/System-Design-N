package questions.QList.ElevatorSystem.first.ElevatorStrategy;

import java.util.List;

import questions.QList.ElevatorSystem.first.Direction;
import questions.QList.ElevatorSystem.first.ElevatorCar;
import questions.QList.ElevatorSystem.first.Request;

public class EvenOddStrategy implements ElevatorStrategy {
    @Override
    public ElevatorCar selectElevator(Request request, List<ElevatorCar> elevators) {
        int sourceFloor = request.getSourceFloor();
        boolean isEvenFloor = sourceFloor % 2 == 0;
        
        for (ElevatorCar elevator : elevators) {
            boolean isEvenElevator = elevator.getId() % 2 == 0;
            
            if ((isEvenFloor && isEvenElevator) || (!isEvenFloor && !isEvenElevator)) {
                if (elevator.getDirection() == Direction.IDLE || 
                    elevator.getDirection() == request.getDirection()) {
                    System.out.println("Selected elevator " + elevator.getId() + " for " + 
                                     (isEvenFloor ? "even" : "odd") + " floor");
                    return elevator;
                }
            }
        }
        
        return null; // Fallback to nearest if no suitable elevator found
    }

   
}
