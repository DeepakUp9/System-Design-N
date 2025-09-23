package questions.QList.ElevatorSystem.first;

// Request.java
public class Request {
    private int sourceFloor;
    private int destinationFloor;
    private Direction direction;
    //DateTime requestTime; // will do it later 
     
    public Request(int sourceFloor, Direction direction) {
        this.sourceFloor = sourceFloor;
        this.direction = direction;
        this.destinationFloor = -1; // Not specified yet
    }
    
    public Request(int sourceFloor, int destinationFloor, Direction direction) {
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
        this.direction = direction;
    }
    
    // Getters
    public int getSourceFloor() { return sourceFloor; }
    public int getDestinationFloor() { return destinationFloor; }
    public Direction getDirection() { return direction; }
    
    public void setDestinationFloor(int floor) { this.destinationFloor = floor; }
    
    @Override
    public String toString() {
        return "Request{from=" + sourceFloor + ", to=" + destinationFloor + 
               ", dir=" + direction + "}";
    }
}