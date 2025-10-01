package questions.QList.ElevatorSystem.second.request;

/**
 * Internal request from inside elevator
 */
public class InternalRequest extends Request {
    private int elevatorId;

    public InternalRequest(int floor, int elevatorId) {
        super(floor);
        this.elevatorId = elevatorId;
    }

    public int getElevatorId() {
        return elevatorId;
    }

    @Override
    public String toString() {
        return String.format("Internal[Floor:%d, Elevator:%d]", floor, elevatorId);
    }
}
