import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ElevatorSystem {
    private static ElevatorSystem system = null;
    private final Building building;
    private final Queue<FloorRequest> hallRequests = new LinkedList<>();

    // Now require all four parameters in the constructor
    private ElevatorSystem(int floors, int cars, int numPanels, int numDisplays) {
        building = new Building(floors, cars, numPanels, numDisplays);
    }

    // Updated getInstance: pass all 4 params, or provide overloads/defaults
    public static ElevatorSystem getInstance(int floors, int cars, int numPanels, int numDisplays) {
        if (system == null)
            system = new ElevatorSystem(floors, cars, numPanels, numDisplays);
        return system;
    }

    public List<ElevatorCar> getCars() { return building.getCars(); }
    public Building getBuilding() { return building; }

    public void callElevator(int floorNum, Direction dir) {
        hallRequests.offer(new FloorRequest(floorNum, dir));
    }

    public ElevatorCar getNearestIdleCar(int floor) {
        ElevatorCar best = null;
        int minDist = Integer.MAX_VALUE;
        for (ElevatorCar car : building.getCars()) {
            if (car.getState() == ElevatorState.IDLE && !car.isInMaintenance() && !car.isOverloaded()) {
                int dist = Math.abs(car.getCurrentFloor() - floor);
                if (dist < minDist) {
                    minDist = dist;
                    best = car;
                }
            }
        }
        return best;
    }

    public void dispatcher() {
        while (!hallRequests.isEmpty()) {
            FloorRequest req = hallRequests.poll();
            ElevatorCar car = getNearestIdleCar(req.floor);
            if (car == null) {
                System.out.println("No idle car available; re-queueing request");
                hallRequests.offer(req);
                break;
            }
            System.out.printf("Dispatching Elevator %d to floor %d%n", car.getId()+1, req.floor);
            car.registerRequest(req.floor);
            car.move();
        }
    }

    public void monitoring() {
        for (ElevatorCar car : getCars()) {
            car.getDisplay().showElevatorDisplay(car.getId()); // +1 for user-friendly ID
        }
    }

    private static class FloorRequest {
        int floor; Direction dir;
        FloorRequest(int f, Direction d) { floor = f; dir = d; }
    }
}
