package questions.QList.ElevatorSystem.second.request;

import questions.QList.ElevatorSystem.second.enums.Direction;

public class ExternalRequest extends Request {
    Direction direction;

    public ExternalRequest(int floor, Direction direction) {
        super(floor);
        this.direction = direction;
    }


    public Direction getDirection(){return this.direction;}

    public String toString() {
        return String.format("External[Floor:%d, Dir:%s]", floor, direction);
    }
}
