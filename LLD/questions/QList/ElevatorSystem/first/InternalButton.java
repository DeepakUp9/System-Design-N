package questions.QList.ElevatorSystem.first;

// InternalButton.java
public class InternalButton extends Button {
    private ElevatorCar elevator;
    private int targetFloor;
    
    public InternalButton(ElevatorCar elevator, int targetFloor) {
        super(ButtonType.INTERNAL);
        this.elevator = elevator;
        this.targetFloor = targetFloor;
    }
    
    @Override
    public void press() {
        isPressed = true;
        System.out.println("Internal button pressed for floor " + targetFloor + 
                          " in elevator " + elevator.getId());
        
        // Create request for destination floor
        Request request = new Request(elevator.getCurrentFloor(), targetFloor, 
        targetFloor > elevator.getCurrentFloor() ? Direction.UP : Direction.DOWN);
        elevator.addRequest(request);
        
        // Reset button after short delay
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isPressed = false;
        }).start();
    }
    
    public int getTargetFloor() { return targetFloor; }
}

