package questions.QList.ElevatorSystem.second.request;

public class InternalRequest extends Request{

    private int elevatorId;
    public InternalRequest(int floor, int elevatorId) {
        super(floor);
        this.elevatorId = elevatorId;
    }

    public int getElevatorId() {return this.elevatorId;}

    @Override
    public String toString() {
        return String.format("Internal[Floor:%d, ElevatorId:%d]", floor, elevatorId);
    }
    
}
