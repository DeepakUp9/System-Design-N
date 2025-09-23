package questions.QList.ElevatorSystem.second;

import questions.QList.ElevatorSystem.second.buttons.ExternalButton;

public class Floor {
    private int floorNumber;
    private ExternalButton upButton;
    private ExternalButton downButton;

    public Floor(int floorNumber){
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber(){ return this.floorNumber;}

    public ExternalButton getUpButton(){ return this.upButton;}

    public ExternalButton getDownButton(){return this.downButton;}

    public void pressUpButton(){
         //need to write code 
    }

    public void pressDownButton(){
        //need to write code 
    }

}
