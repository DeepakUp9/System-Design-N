package questions.QList.ElevatorSystem.second.Elevator;


import java.util.concurrent.BlockingQueue;

import questions.QList.ElevatorSystem.second.Display;
import questions.QList.ElevatorSystem.second.Door;
import questions.QList.ElevatorSystem.second.buttons.InternalPanel;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.enums.State;
import questions.QList.ElevatorSystem.second.request.Request;

public class ElevatorCar {
    private int id;
    private State state;
    private Display display;
    private InternalPanel internalpanel; 
    private int currentFloor;
    private Door door;
    private Direction direction;
    private BlockingQueue<Request> requests;
    private boolean running;


    public ElevatorCar(int id, int maxFloor){
    
    }

    public void move(){

    }
    public void stop(){
       this.running = false;
    }
    public void openDoor(){
       door.opened();
    }
    public void closedDoor(){
        door.closed();
    }
    public void addRequest(Request request){

    }
    public int  getCurrentFloor(){return this.currentFloor;}

    public Direction getDirection(){return this.direction;}

    public State getState(){return this.state;};

    public InternalPanel getInternalPanel(){return this.internalpanel;};

    public int getId(){ return this.id;}

    public void operate(){
        // automaticaly
    }
    public void processRequest(Request request){
        // manial
    }

}
