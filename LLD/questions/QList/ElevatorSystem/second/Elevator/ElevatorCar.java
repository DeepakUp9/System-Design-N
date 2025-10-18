package questions.QList.ElevatorSystem.second.Elevator;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import questions.QList.ElevatorSystem.second.Display;
import questions.QList.ElevatorSystem.second.Door;

import questions.QList.ElevatorSystem.second.buttons.InternalPanel;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;




    /**
     * Represents a physical elevator car with movement and request processing capabilities
     */
    public class ElevatorCar {
       
        // Elevator identification and state
        private int id;                          // Unique ID for this elevator
        private int currentFloor;                // Current floor where elevator is located
        private Direction direction;             // Current movement direction (UP/DOWN/IDLE)
        private ElevatorState state;             // Current state (MOVING/IDLE/MAINTENANCE etc.)


        // Hardware components
        private Display display;                 // Display screen inside elevator
        private Door door;                       // Door object for opening/closing operations
        private InternalPanel internalPanel;     // Button panel inside elevator

        // Capacity tracking
        private int currentLoad;
        private int maxLoad;

       
        private boolean running;

       

        public ElevatorCar(int id, int maxLoad) {
            // Initialize basic properties
            this.id = id;
            this.currentFloor = 1; // Start from ground floor
            this.direction = Direction.IDLE; // Initially not moving
            this.state = ElevatorState.IDLE; // Initial state is idle
            this.currentLoad = 0;            // Empty at start
            this.maxLoad = maxLoad;          // Set maximum capacity

            // Initialize hardware components
            this.display = new Display();  // Create display screen
            this.door = new Door();        // Create door mechanism
            this.running = true;           // Set running flag to true

        }


         /**
         * Pure movement - just moves the elevator physically
         */
        public void moveToFloor(int targetFloor) {
            if (targetFloor == this.currentFloor) {
                System.out.println("ℹ️ Elevator " + id + " already at floor " + targetFloor);
                return;
            }

            this.direction = (targetFloor > this.currentFloor) ? Direction.UP : Direction.DOWN;
            this.state = ElevatorState.MOVING;
            display.updateDisplay(currentFloor, direction, state);

            System.out.println("🚀 Elevator " + id + " moving from " + currentFloor + " to " + targetFloor);

            // Simulate physical movement
            int step = (this.direction == Direction.UP) ? 1 : -1;
            while (this.currentFloor != targetFloor && running) {
                try {
                    Thread.sleep(1000);
                    this.currentFloor += step;
                    display.updateDisplay(currentFloor, direction, state);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        /**
         * Physical stop at a floor
         */
        public void stopAtFloor(int floor) {
            System.out.println("🛑 Elevator " + id + " stopping at floor " + floor);
            this.state = ElevatorState.DOOR_OPEN;
            display.updateDisplay(currentFloor, direction, state);

            door.open();
            try { Thread.sleep(2000); } catch (InterruptedException e) {}

            door.close();
            this.state = ElevatorState.MOVING;
            display.updateDisplay(currentFloor, direction, state);
        }

        /**
         * Set to idle state
         */
        public void setIdle() {
            this.direction = Direction.IDLE;
            this.state = ElevatorState.IDLE;
            display.updateDisplay(currentFloor, direction, state);
            System.out.println("💤 Elevator " + id + " is now idle");
        }


     public void stop() {
            running = false;
        }

        // ========== GETTER METHODS ==========
        public int getId() { return id; }
        public int getCurrentFloor() { return currentFloor; }
        public Direction getDirection() { return direction; }
        public ElevatorState getState() { return state; }
        public int getCurrentLoad() { return currentLoad; }
        public int getMaxLoad() { return maxLoad; }
        public InternalPanel getInternalPanel() { return internalPanel; }
        public void setInternalPanel(InternalPanel internalPanel) { this.internalPanel = internalPanel; }


        @Override
        public String toString() {
            return String.format(
                    "ElevatorCar [id=%s, currentFloor=%s, direction=%s, state=%s, display=%s, door=%s, internalPanel=%s, currentLoad=%s, maxLoad=%s, running=%s]",
                    id, currentFloor, direction, state, display, door, internalPanel, currentLoad, maxLoad, running);
        }


        
    }


