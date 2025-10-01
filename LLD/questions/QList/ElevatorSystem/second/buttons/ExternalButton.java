package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.enums.Direction;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;

/**
 * External button located on building floors for calling elevators
 * Handles UP and DOWN direction requests from hallway
 */
public class ExternalButton extends Button {
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
        System.out.println("🎯 External " + direction + " button pressed on floor " + floorNumber);

        // Create external request and submit to controller
        ExternalRequest request = new ExternalRequest(floorNumber, direction);
        ElevatorController.getInstance().submitExternalRequest(request);

        // Reset button after short delay (simulates button release)
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            isPressed = false;
        }).start();
    }

    public Direction getDirection() {
        return direction;
    }
}
