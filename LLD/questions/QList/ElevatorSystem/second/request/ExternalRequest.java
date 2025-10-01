package questions.QList.ElevatorSystem.second.request;

import questions.QList.ElevatorSystem.second.enums.Direction;

/**
 * External request from hallway buttons
 */
public class ExternalRequest extends Request {
    private Direction direction;

    public ExternalRequest(int floor, Direction direction) {
        super(floor);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return String.format("External[Floor:%d, Dir:%s]", floor, direction);
    }
}