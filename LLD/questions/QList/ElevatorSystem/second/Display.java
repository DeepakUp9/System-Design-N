package questions.QList.ElevatorSystem.second;

import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.enums.ElevatorState;

public class Display {
    
    private int currentFloor;
    private Direction direction;
    private ElevatorState state;

    public Display() {
        this.currentFloor = 0;
        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;
    }

    public void updateDisplay(int currentFloor, Direction direction, ElevatorState state) {
        this.currentFloor = currentFloor;
        this.direction = direction;
        this.state = state;
        show();
    }

    public void show() {
        String stateSymbol = state == ElevatorState.MOVING ? "→" :
                state == ElevatorState.DOOR_OPEN? "↔" : "•";
        String dirSymbol = direction == Direction.UP ? "↑" :
                direction == Direction.DOWN ? "↓" : "•";

        System.out.printf("[Display] Floor: %2d %s %s%n",
                currentFloor, dirSymbol, stateSymbol);
    }

    public void showMessage(String message) {
        System.out.println("[Message] " + message);
    }
}
