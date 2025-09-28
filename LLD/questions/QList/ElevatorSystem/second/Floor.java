package questions.QList.ElevatorSystem.second;

import questions.QList.ElevatorSystem.second.buttons.ExternalButton;
import questions.QList.ElevatorSystem.second.enums.Direction;

public class Floor {
    private int floorNumber;
    private ExternalButton upButton;
    private ExternalButton downButton;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upButton = new ExternalButton(floorNumber, Direction.UP);
        this.downButton = new ExternalButton(floorNumber, Direction.DOWN);
    }

    public int getFloorNumber() { return floorNumber; }
    public ExternalButton getUpButton() { return upButton; }
    public ExternalButton getDownButton() { return downButton; }

    public void pressUpButton() { upButton.press(); }
    public void pressDownButton() { downButton.press(); }

}
