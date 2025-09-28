package questions.QList.ElevatorSystem.second.ElevatorStartegy.ExternalStartegy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

public interface ExternalRequestStrategy {
    ElevatorCar assignElevator(ExternalRequest externalRequest, List<ElevatorCar>elevatorCars);
    String getStrategyName();
}
