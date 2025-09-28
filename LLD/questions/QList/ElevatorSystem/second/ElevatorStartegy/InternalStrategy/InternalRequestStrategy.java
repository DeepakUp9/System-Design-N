package questions.QList.ElevatorSystem.second.ElevatorStartegy.InternalStrategy;

import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

public interface InternalRequestStrategy {
    List<Integer> planRoute(ElevatorCar elevator, InternalRequest request);
    String getStrategyName();
}
