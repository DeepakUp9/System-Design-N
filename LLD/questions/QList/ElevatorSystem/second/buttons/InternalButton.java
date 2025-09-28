package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.request.InternalRequest;

public class InternalButton extends Button{
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
        InternalRequest request = new InternalRequest(targetFloor, elevator.getId());
        elevator.addRequest(request);

        // Reset button after short delay
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isPressed = false;
        }).start();
    }

    public int getTargetFloor() { return targetFloor; }
    
}
