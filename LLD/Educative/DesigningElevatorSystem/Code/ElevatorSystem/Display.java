public class Display {
    private int floor = 0;
    private int load = 0;
    private Direction direction = Direction.IDLE;
    private ElevatorState state = ElevatorState.IDLE;
    private boolean maintenance = false;
    private boolean overloaded = false;

    public void update(int f, Direction d, int load, ElevatorState s, boolean overloaded, boolean maintenance) {
        this.floor = f;
        this.direction = d;
        this.load = load;
        this.state = s;
        this.overloaded = overloaded;
        this.maintenance = maintenance;
    }

    public void showElevatorDisplay(int carId) {
        String msg = maintenance ? "MAINTENANCE"
                   : overloaded ? "OVERLOADED"
                   : state == ElevatorState.IDLE ? "IDLE"
                   : state == ElevatorState.UP ? "UP"
                   : state == ElevatorState.DOWN ? "DOWN"
                   : "UNKNOWN";
        System.out.printf("Elevator %d ► Floor: %d | Dir: %s | Load: %d | State: %s%n",
            carId+1, floor, direction, load, msg);
    }
}
