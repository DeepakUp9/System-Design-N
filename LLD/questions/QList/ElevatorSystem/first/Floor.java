package questions.QList.ElevatorSystem.first;

public class Floor {
    private int floorNumber;
    private ExternalButton upButton;
    private ExternalButton downButton;
    
    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.upButton = new ExternalButton(this, Direction.UP);
        this.downButton = new ExternalButton(this, Direction.DOWN);
    }
    
    public int getFloorNumber() { return floorNumber; }
    public ExternalButton getUpButton() { return upButton; }
    public ExternalButton getDownButton() { return downButton; }
    
    public void pressUpButton() { upButton.press(); }
    public void pressDownButton() { downButton.press(); }
}

