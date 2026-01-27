import java.util.ArrayList;
import java.util.List;

public class Building {
    private final List<Floor> floors = new ArrayList<>();
    private final List<ElevatorCar> cars = new ArrayList<>();

    public Building(int numFloors, int numCars, int numPanels, int numDisplays) {
        int topFloor = numFloors - 1;
        for (int i = 0; i < numFloors; i++)
            floors.add(new Floor(i, numPanels, numDisplays, topFloor));
        for (int i = 0; i < numCars; i++)
            cars.add(new ElevatorCar(i, numFloors));
    }

    public List<Floor> getFloors() { return floors; }
    public List<ElevatorCar> getCars() { return cars; }
}
