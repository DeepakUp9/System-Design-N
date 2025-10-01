package questions.QList.ElevatorSystem.second;

/**
 * Elevator door mechanism
 */
public class Door {
    private boolean isOpen;

    public Door() {
        this.isOpen = false;
    }

    public void open() {
        isOpen = true;
        System.out.println("🚪 Door opened");
    }

    public void close() {
        isOpen = false;
        System.out.println("🚪 Door closed");
    }

    public boolean isOpen() {
        return isOpen;
    }
}
