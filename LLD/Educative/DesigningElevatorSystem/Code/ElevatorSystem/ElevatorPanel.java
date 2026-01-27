import java.util.ArrayList;
import java.util.List;

public class ElevatorPanel {
    private final List<ElevatorButton> floorButtons;
    private final DoorButton openButton = new DoorButton();
    private final DoorButton closeButton = new DoorButton();
    private final EmergencyButton emergencyButton = new EmergencyButton();

    public ElevatorPanel(int numFloors) {
        floorButtons = new ArrayList<>();
        for (int i = 0; i < numFloors; i++) {
            floorButtons.add(new ElevatorButton(i));
        }
    }
    public List<ElevatorButton> getFloorButtons() { return floorButtons; }
    public DoorButton getOpenButton() { return openButton; }
    public DoorButton getCloseButton() { return closeButton; }
    public EmergencyButton getEmergencyButton() { return emergencyButton; }
    public void enterEmergency() { emergencyButton.pressDown(); }
    public void exitEmergency() { emergencyButton.reset(); }
}
