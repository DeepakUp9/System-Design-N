package questions.QList.ElevatorSystem.second.Elevator;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import questions.QList.ElevatorSystem.second.Display;
import questions.QList.ElevatorSystem.second.Door;
import questions.QList.ElevatorSystem.second.buttons.InternalPanel;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;
import questions.QList.ElevatorSystem.second.request.InternalRequest;
import questions.QList.ElevatorSystem.second.request.Request;

public class ElevatorCar {
    private int id;
    private int currentFloor;
    private Direction direction;
    private ElevatorState state;
    private Display display;
    private Door door;
    private InternalPanel internalPanel;
    private int currentLoad;
    private int maxLoad;

    // Request queues for internal processing
    private Set<Integer> pendingFloors;
    private Queue<ExternalRequest> externalRequests;
    private BlockingQueue<Request> requests;
    private boolean running;


    public ElevatorCar(int id, int maxLoad) {
        this.id = id;
        this.currentFloor = 1;
        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;
        this.currentLoad = 0;
        this.maxLoad = maxLoad;
        this.pendingFloors = new HashSet<>();
        this.externalRequests = new LinkedList<>();
        this.requests = new LinkedBlockingQueue<>();
        this.display = new Display();
        this.door = new Door();
        this.running = true;

        // Start elevator operation thread
        new Thread(this::operate).start();
    }

    public void addPendingFloor(int floor) {
        pendingFloors.add(floor);
    }

    public void addExternalRequest(ExternalRequest request) {
        externalRequests.add(request);
        pendingFloors.add(request.getFloor());
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public boolean hasPendingFloors() {
        return !pendingFloors.isEmpty();
    }

    public Set<Integer> getPendingFloors() {
        return new HashSet<>(pendingFloors);
    }

    public void clearFloor(int floor) {
        pendingFloors.remove(floor);
        externalRequests.removeIf(req -> req.getFloor() == floor);
    }

    // Simulation methods
    public void moveTo(int floor) {
        this.currentFloor = floor;
        if (floor > currentFloor) {
            this.direction = Direction.UP;
        } else if (floor < currentFloor) {
            this.direction = Direction.DOWN;
        } else {
            this.direction = Direction.IDLE;
        }
        this.state = ElevatorState.MOVING;
        display.updateDisplay(currentFloor, direction, state);
    }

    public void setIdle() {
        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;
        display.updateDisplay(currentFloor, direction, state);
    }

    private void operate() {
        while (running) {
            try {
                Request request = requests.take();
                processRequest(request);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void processRequest(Request request) {
        if (request instanceof InternalRequest) {
            processInternalRequest((InternalRequest) request);
        } else if (request instanceof ExternalRequest) {
            processExternalRequest((ExternalRequest) request);
        }
    }

    private void processInternalRequest(InternalRequest request) {
        System.out.println("Processing internal request: " + request);
        moveTo(request.getFloor());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        door.opened();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        door.closed();
        clearFloor(request.getFloor());
        setIdle();
    }

    private void processExternalRequest(ExternalRequest request) {
        System.out.println("Processing external request: " + request);
        moveTo(request.getFloor());
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
        door.opened();
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        door.closed();
        clearFloor(request.getFloor());
        setIdle();
    }

    public void processInternalRequests() {
        // Process all pending internal requests using current strategy
        for (Integer floor : pendingFloors) {
            moveTo(floor);
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            door.opened();
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            door.closed();
            clearFloor(floor);
        }
        setIdle();
    }

    public void stop() {
        running = false;
        Thread.currentThread().interrupt();
    }

    // Getters
    public int getId() { return id; }
    public int getCurrentFloor() { return currentFloor; }
    public Direction getDirection() { return direction; }
    public ElevatorState getState() { return state; }
    public int getCurrentLoad() { return currentLoad; }
    public int getMaxLoad() { return maxLoad; }
    public InternalPanel getInternalPanel() { return internalPanel; }

    public void setInternalPanel(InternalPanel internalPanel) {
        this.internalPanel = internalPanel;
    }

    @Override
    public String toString() {
        return String.format("Elevator[ID:%d, Floor:%d, Dir:%s, State:%s, Load:%d/%d, Pending:%s]",
                id, currentFloor, direction, state, currentLoad, maxLoad, pendingFloors);
    }

}
