package questions.QList.ElevatorSystem.second;

import java.util.ArrayList;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.buttons.InternalPanel;

/**
 * Represents the building containing floors and elevators
 */
public class Building {
    private List<Floor> floors;
    private List<ElevatorCar> elevators;
    private int maxFloors;

    public Building(int maxFloors, int numberOfElevators) {
        this.maxFloors = maxFloors;
        this.floors = new ArrayList<>();
        this.elevators = new ArrayList<>();

        // Create floors
        for (int i = 1; i <= maxFloors; i++) {
            floors.add(new Floor(i));
        }

        // lits of Create elevators
        for (int i = 1; i <= numberOfElevators; i++) {
            ElevatorCar elevator = new ElevatorCar(i, 10);
            InternalPanel panel = new InternalPanel(elevator, maxFloors);
            elevator.setInternalPanel(panel);
            elevators.add(elevator);
        }

        // Initialize elevator controller
        ElevatorController.initialize(this);
        System.out.println("🏢 Building created with " + maxFloors + " floors and " + numberOfElevators + " elevators");
    }

    public Floor getFloor(int number) {
        if (number < 1 || number > maxFloors) {
            System.out.println("❌ Invalid floor number: " + number);
            return null;
        }
        return floors.get(number - 1);
    }

    public List<ElevatorCar> getElevators() {
        return elevators;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public int getMaxFloors() {
        return maxFloors;
    }
}
