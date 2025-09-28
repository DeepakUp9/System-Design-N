package questions.QList.ElevatorSystem.second.request;

public abstract class Request {
    protected int floor;
    protected long timestamp;
    
    public Request(int floor) {
        this.floor = floor;
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getFloor() { return floor; }
    public long getTimestamp() { return timestamp; }
}

