package questions.QList.ElevatorSystem.second.ElevatorStartegy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.Request;

public interface ElevatorStartegy {
    ElevatorCar selectElevatorCar(Request request, List<ElevatorCar> elevators);
}
