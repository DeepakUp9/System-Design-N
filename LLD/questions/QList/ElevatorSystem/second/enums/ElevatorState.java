package questions.QList.ElevatorSystem.second.enums;

/**
 * Current state of elevator
 */
public enum ElevatorState {
    MOVING,         // Currently moving between floors
    IDLE,           // Stationary and available
    MAINTENANCE,    // Under maintenance
    DOOR_OPEN,      // Doors are open
    DOOR_CLOSED     // Doors are closed
}