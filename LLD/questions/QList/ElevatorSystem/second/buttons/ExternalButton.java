package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;
import questions.QList.ElevatorSystem.second.enums.Direction;

public class ExternalButton extends Button{
   private int floorNumber;
   private Direction direction;

    public ExternalButton(int floorNumber, Direction direction ) {
        super(direction == Direction.UP ? ButtonType.EXTERNAL_UP : ButtonType.EXTERNAL_DOWN);
        this.floorNumber = floorNumber;
        this.direction = direction;
    }

    @Override
    public void press() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'press'");
    }
    
    public Direction getDirection(){ return this.direction;}

}
