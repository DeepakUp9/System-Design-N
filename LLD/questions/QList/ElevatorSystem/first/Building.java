package questions.QList.ElevatorSystem.first;

// Building.java
import java.util.ArrayList;
import java.util.List;

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
        
        // Create elevators
        for (int i = 1; i <= numberOfElevators; i++) {
            elevators.add(new ElevatorCar(i, maxFloors));
        }
        
        // Initialize elevator controller
        ElevatorController.initialize(this);
    }
    
    public Floor getFloor(int number) {
        if (number < 1 || number > maxFloors) return null;
        return floors.get(number - 1);
    }
    
    public List<ElevatorCar> getElevators() { return elevators; }
    public List<Floor> getFloors() { return floors; }
    public int getMaxFloors() { return maxFloors; }
}