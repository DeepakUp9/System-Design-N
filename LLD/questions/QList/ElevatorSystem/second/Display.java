package questions.QList.ElevatorSystem.second;

import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.enums.State;

public class Display {
    
    private State state;
    private Direction direction;
    private int floor;

    Display(){
        state = State.STOPPED;
        direction = Direction.IDEAL;
        floor = -1; //underground
    }

   public void updateDisplay(int currentFloor, Direction direction,  State state){
       this.floor = currentFloor;
       this.direction = direction;
       this.state = state;
       show();
   }

    public void show() {
        String stateSymbol = state == State.MOVING ? "→" : 
                           state == State.DOOR_OPEN ? "↔" : "•";
        String dirSymbol = direction == Direction.UP ? "↑" : 
                          direction == Direction.DOWN ? "↓" : "•";
        
        System.out.printf("[Display] Floor: %2d %s %s%n", 
                         floor, dirSymbol, stateSymbol);
    }
    
    public void showMessage(String message) {
        System.out.println("[Message] " + message);
    }

}
