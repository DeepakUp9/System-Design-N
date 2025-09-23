package questions.QList.ElevatorSystem.second.request;

import java.time.LocalDateTime;

import questions.QList.ElevatorSystem.second.enums.Direction;

public class Request {
    private int currentFloor;
    private int distinationFloor;
    private LocalDateTime requestTime;
    private Direction direction;

    
    public Request(int currentFloor , int distinationFloor, Direction direction){
        this.currentFloor = currentFloor;
        this.distinationFloor = distinationFloor;
        this.direction = direction;
        LocalDateTime localDate = LocalDateTime.now();
        this.requestTime = localDate;
    }

    public Request(int currentFloor, Direction direction){
        this.currentFloor = currentFloor;
        this.direction = direction;
        LocalDateTime localDate = LocalDateTime.now();
        this.requestTime = localDate;
    }

    public int getDestionationFloor(){return this.distinationFloor;}
    public int getcurrentFloor(){return this.currentFloor;};

    public Direction getDirection(){return this.direction;};

    public void setDistinationfloor(int distinationFloor){this.distinationFloor = distinationFloor;}

   
    @Override
    public String toString() {
        return String.format("Request [currentFloor=%s, distinationFloor=%s, requestTime=%s, direction=%s]",
                currentFloor, distinationFloor, requestTime, direction);
    }


}
