package questions.QList.ElevatorSystem.first;

import java.util.HashMap;
import java.util.Map;

public class InternalPanel {
    private Map<Integer, InternalButton> buttons;
    private ElevatorCar elevator;
    
    public InternalPanel(ElevatorCar elevator, int maxFloors) {
        this.elevator = elevator;
        this.buttons = new HashMap<>();
        
        // Create buttons for each floor
        for (int i = 1; i <= maxFloors; i++) {
            buttons.put(i, new InternalButton(elevator, i));
        }
    }
    
    public void pressButton(int floor) {
        InternalButton button = buttons.get(floor);
        if (button != null) {
            button.press();
        } else {
            System.out.println("Invalid floor: " + floor);
        }
    }
    
    public InternalButton getButton(int floor) {
        return buttons.get(floor);
    }
}
