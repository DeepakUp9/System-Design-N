package questions.QList.ElevatorSystem.first.ElevatorStrategy;

import java.util.List;

import questions.QList.ElevatorSystem.first.ElevatorCar;
import questions.QList.ElevatorSystem.first.Request;

public interface ElevatorStrategy {
    ElevatorCar selectElevator(Request request, List<ElevatorCar> elevators);
}

