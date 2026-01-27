import java.util.Random;
import java.util.List;

public class Driver {
    public static void main(String[] args) {
        int numFloors = 13;
        int numCars = 3;
        int numPanels = 1;    // Number of HallPanels per floor
        int numDisplays = 3;  // Number of Displays per floor

        ElevatorSystem system = ElevatorSystem.getInstance(numFloors, numCars, numPanels, numDisplays);

        // SCENARIO 1
        System.out.println("=== Scenario 1: Elevator 3 in maintenance, passenger calls elevator from floor 7 ===\n");
        system.monitoring();
        System.out.println();

        ElevatorCar car3 = system.getCars().get(2);
        car3.enterMaintenance();
        System.out.println();
        system.monitoring();
        System.out.println();

        runCall(system, 7, Direction.UP);

        car3.exitMaintenance();
        System.out.println("\n--- Resetting maintenance for all elevators ---\n");
        system.monitoring();
        System.out.println();

        System.out.println("=== Scenario 2: Random positions, passenger calls elevator from ground (0) to top (12) ===");

        Random rand = new Random();
        for (ElevatorCar car : system.getCars()) {
            int randomFloor = rand.nextInt(numFloors);
            System.out.printf("\n== Setting random position for Elevator %d ==", car.getId()+1);
            System.out.printf("\n→ Teleporting Elevator %d to floor %d%n", car.getId()+1, randomFloor);
            car.registerRequest(randomFloor);
            car.move();
        }

        System.out.println("\nElevator positions after random repositioning:");
        for (ElevatorCar car : system.getCars()) {
            System.out.printf("Elevator %d ► Floor: %d | State: %s%n",
                              car.getId()+1, car.getCurrentFloor(), car.getState());
        }
        System.out.println();

        runCall(system, 0, Direction.UP);
    }

    private static void runCall(ElevatorSystem system, int floor, Direction dir) {
        System.out.printf("Passenger calls lift on floor %d (%s)%n", floor, dir);
        ElevatorCar nearest = system.getNearestIdleCar(floor);
        if (nearest == null) {
            System.out.println("No idle elevator available right now.");
            return;
        }
        System.out.printf("→ Nearest elevator is %d at floor %d. Lift going %s.%n",
                          nearest.getId()+1, nearest.getCurrentFloor(), dir);
        system.callElevator(floor, dir);
        system.dispatcher();
        System.out.println("\n[Status after dispatch]");
        system.monitoring();
        System.out.println(new String(new char[100]).replace('\0', '-'));
    }
}
