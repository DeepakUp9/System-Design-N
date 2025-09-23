package questions.QList.ElevatorSystem.second.ElevatorStartegy;

import static java.util.function.Predicate.isEqual;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.Request;

public class EvenOddElevatorStartegy implements ElevatorStartegy{

    @Override
    public ElevatorCar selectElevatorCar(Request request, List<ElevatorCar> elevators) {
       if(request.getcurrentFloor() % 2 == 0 ){
          // i'll choose even elevator 
          for(ElevatorCar el : elevators){
               if(el.getId() % 2 == 0){
                 return el;
               }
          }
       }
    }
    
}
