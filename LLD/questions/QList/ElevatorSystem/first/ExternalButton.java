package questions.QList.ElevatorSystem.first;

public class ExternalButton extends Button {
    private Floor floor;
    private Direction direction;
    
    public ExternalButton(Floor floor, Direction direction) {
        super(direction == Direction.UP ? ButtonType.EXTERNAL_UP : ButtonType.EXTERNAL_DOWN);
        this.floor = floor;
        this.direction = direction;
    }
    
    @Override
    public void press() {
        isPressed = true;
        System.out.println("External " + direction + " button pressed on floor " + 
                          floor.getFloorNumber());
        
        // Create request and submit to controller
        Request request = new Request(floor.getFloorNumber(), direction);
        ElevatorController.getInstance().submitRequest(request);
        
        // Reset button after short delay
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isPressed = false;
        }).start();
    }
    
    public Direction getDirection() { return direction; }
}
