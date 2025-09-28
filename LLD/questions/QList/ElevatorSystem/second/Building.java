package questions.QList.ElevatorSystem.second;

import java.util.ArrayList;
import java.util.List;

import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.buttons.InternalPanel;

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
                ElevatorCar elevator = new ElevatorCar(i, 10); // 10 max load
                InternalPanel panel = new InternalPanel(elevator, maxFloors);
                elevator.setInternalPanel(panel);
                elevators.add(elevator);
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
