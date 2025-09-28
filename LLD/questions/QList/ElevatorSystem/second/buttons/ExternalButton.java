package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

public class ExternalButton extends Button{
    private int floorNumber;
    private Direction direction;

    public ExternalButton(int floorNumber, Direction direction) {
        super(direction == Direction.UP ? ButtonType.EXTERNAL_UP : ButtonType.EXTERNAL_DOWN);
        this.floorNumber = floorNumber;
        this.direction = direction;
    }

    @Override
    public void press() {
        isPressed = true;
        System.out.println("External " + direction + " button pressed on floor " + floorNumber);

        // Create request and submit to controller
        ExternalRequest request = new ExternalRequest(floorNumber, direction);
        ElevatorController.getInstance().submitExternalRequest(request);

        // Reset button after short delay
        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            isPressed = false;
        }).start();
    }

     public Direction getDirection() { return direction; }

}
