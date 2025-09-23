package questions.QList.ElevatorSystem.first;

// ElevatorCar.java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ElevatorCar {
    private int id;
    private int currentFloor;
    private Direction direction;
    private State state;
    private Display display;
    private Door door;
    private InternalPanel internalPanel;
    private BlockingQueue<Request> requests;
    private boolean running;
    
    public ElevatorCar(int id, int maxFloors) {
        this.id = id;
        this.currentFloor = 1; // Start at ground floor
        this.direction = Direction.IDLE;
        this.state = State.STOPPED;
        this.display = new Display();
        this.door = new Door();
        this.internalPanel = new InternalPanel(this, maxFloors);
        this.requests = new LinkedBlockingQueue<>();
        this.running = true;
        
        // Start elevator operation thread
        new Thread(this::operate).start();
    }
    
    private void operate() {
        while (running) {
            try {
                Request request = requests.take(); // Wait for requests
                processRequest(request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void processRequest(Request request) {
        int targetFloor = request.getDestinationFloor() != -1 ? 
                         request.getDestinationFloor() : request.getSourceFloor();
        
        System.out.println("Elevator " + id + " processing request: " + request);
        
        // Determine direction
        direction = targetFloor > currentFloor ? Direction.UP : Direction.DOWN;
        state = State.MOVING;
        display.updateDisplay(currentFloor, direction, state);
        
        // Move to target floor
        while (currentFloor != targetFloor) {
            try {
                Thread.sleep(1000); // Simulate movement time
                
                // Move one floor
                currentFloor += (direction == Direction.UP ? 1 : -1);
                System.out.println("Elevator " + id + " at floor " + currentFloor);
                display.updateDisplay(currentFloor, direction, state);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        
        // Arrived at floor
        state = State.STOPPED;
        display.updateDisplay(currentFloor, Direction.IDLE, state);
        
        // Open and close door
        door.open();
        try { Thread.sleep(2000); } catch (InterruptedException e) {} // Wait for people
        door.close();
        
        direction = Direction.IDLE;
        display.updateDisplay(currentFloor, direction, State.DOOR_CLOSED);
    }
    
    public void addRequest(Request request) {
        requests.add(request);
    }
    
    public void stop() {
        running = false;
        Thread.currentThread().interrupt();
    }
    
    // Getters
    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getDirection() { return direction; }
    public State getState() { return state; }
    public InternalPanel getInternalPanel() { return internalPanel; }
}
