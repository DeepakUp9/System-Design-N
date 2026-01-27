import java.util.LinkedList;
import java.util.Queue;

public class ElevatorCar {
    private final int id;
    private int currentFloor = 0;
    private ElevatorState state = ElevatorState.IDLE;
    private final Door door = new Door();
    private final Display display = new Display();
    private final ElevatorPanel panel;
    private final Queue<Integer> requestQueue = new LinkedList<>();
    private int load = 0;
    private final int MAX_LOAD = 680; // kg
    private boolean overloaded = false;
    private boolean maintenance = false;

    public ElevatorCar(int id, int numFloors) {
        this.id = id;
        this.panel = new ElevatorPanel(numFloors);
        updateDisplay();
    }

    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public ElevatorState getState() { return state; }
    public ElevatorPanel getPanel() { return panel; }
    public boolean isInMaintenance() { return maintenance; }
    public boolean isOverloaded() { return overloaded; }

    public void registerRequest(int floor) {
        if (maintenance) return;
        requestQueue.offer(floor);
    }

    public void move() {
        if (maintenance || overloaded || requestQueue.isEmpty()) {
            state = ElevatorState.IDLE;
            updateDisplay();
            return;
        }
        int target = requestQueue.poll();
        if (target == currentFloor) {
            stop();
            return;
        }
        state = (target > currentFloor) ? ElevatorState.UP : ElevatorState.DOWN;
        while (currentFloor != target && !maintenance && !overloaded) {
            currentFloor += (state == ElevatorState.UP ? 1 : -1);
            updateDisplay();
            display.showElevatorDisplay(id);
            System.out.printf("Elevator %d passing floor %d%n", id+1, currentFloor);
        }
        stop();
    }

    public void stop() {
        if (maintenance || overloaded) return;
        state = ElevatorState.IDLE;
        updateDisplay();
        door.open();
        System.out.printf("Elevator %d doors opening at floor %d%n", id+1, currentFloor);
    }

    public void enterMaintenance() {
        maintenance = true;
        state = ElevatorState.MAINTENANCE;
        door.close();
        updateDisplay();
        System.out.printf(">>> Elevator %d entered MAINTENANCE mode%n", id+1);
    }

    public void exitMaintenance() {
        maintenance = false;
        state = ElevatorState.IDLE;
        updateDisplay();
        System.out.printf(">>> Elevator %d exited MAINTENANCE mode, now IDLE%n", id+1);
    }

    public void emergencyStop() {
        state = ElevatorState.IDLE;
        overloaded = false;
        door.close();
        updateDisplay();
        System.out.printf(">>> Elevator %d EMERGENCY STOP activated!%n", id+1);
    }

    public void addLoad(int kg) {
        load += kg;
        if (load > MAX_LOAD) triggerOverloadAlarm();
        updateDisplay();
    }
    public void removeLoad(int kg) {
        load -= kg;
        if (load <= MAX_LOAD) clearOverloadAlarm();
        updateDisplay();
    }
    private void triggerOverloadAlarm() {
        overloaded = true;
        System.out.printf("!!! Elevator %d OVERLOAD ALARM !!!%n", id+1);
    }
    private void clearOverloadAlarm() {
        overloaded = false;
        System.out.printf("Overload cleared for Elevator %d.%n", id+1);
    }

    private void updateDisplay() {
        Direction dir = state == ElevatorState.UP ? Direction.UP :
                        state == ElevatorState.DOWN ? Direction.DOWN : Direction.IDLE;
        display.update(currentFloor, dir, load, state, overloaded, maintenance);
    }

    public Display getDisplay() { return display; }
    public Door getDoor() { return door; }
}
