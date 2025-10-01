package questions.QList.ElevatorSystem.second.buttons;

import questions.QList.ElevatorSystem.first.ButtonType;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorCar;
import questions.QList.ElevatorSystem.second.Elevator.ElevatorController;
import questions.QList.ElevatorSystem.second.request.ExternalRequest;
import questions.QList.ElevatorSystem.second.request.InternalRequest;
import questions.QList.ElevatorSystem.second.request.RequestProcessor;

/**
 * Internal button located inside elevator for selecting destination floors
 */
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
        System.out.println("🏢 Internal button pressed for floor " + targetFloor +
                " in elevator " + elevator.getId());

        //here we can use internalRequesthanlde just like exnternlaRequestHandle
        // Create internal request and send to specific elevator
        // ExternalRequest request = new ExternalRequest(floorNumber, direction);
        // ElevatorController.getInstance().submitExternalRequest(request);
       
        // Create internal request
        InternalRequest request = new InternalRequest(targetFloor, elevator.getId());
        
        // Send to global RequestProcessor (just like ExternalButton)
        RequestProcessor requestProcessor = ElevatorController.getInstance().getRequestProcessor();
        requestProcessor.addRequest(request);

        // Reset button after short delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            isPressed = false;
        }).start();
    }

    public int getTargetFloor() {
        return targetFloor;
    }
}
