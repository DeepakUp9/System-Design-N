package questions.QList.ElevatorSystem.first;


public class Display {
    private int currentFloor;
    private Direction direction;
    private State state;
    
    public Display() {
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.state = State.STOPPED;
    }
    
    public void updateDisplay(int currentFloor, Direction direction, State state) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.state = state;
        show();
    }
    
    public void show() {
        String stateSymbol = state == State.MOVING ? "→" : 
                           state == State.DOOR_OPEN ? "↔" : "•";
        String dirSymbol = direction == Direction.UP ? "↑" : 
                          direction == Direction.DOWN ? "↓" : "•";
        
        System.out.printf("[Display] Floor: %2d %s %s%n", 
                         currentFloor, dirSymbol, stateSymbol);
    }
    
    public void showMessage(String message) {
        System.out.println("[Message] " + message);
    }
}