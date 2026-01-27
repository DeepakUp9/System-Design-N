# Use Case Diagram for the Elevator System

> **Comprehensive Design Analysis with UML Relationships**

---

## 📖 Overview

This document explores the **use case diagram** for an elevator system, focusing on:
- Actor interactions
- Core system functions
- UML relationships (Association, Include, Extend)
- Design implications for LLD interviews

---

## 🎯 System Definition

### System: Elevator Control System

**Components:**
- Physical elevator cars
- Floor panels (call buttons)
- In-car panels (selection buttons)
- Central controller (manages requests and dispatch)

**Purpose:**
Manages the entire elevator operation, including:
- ✅ Request handling from multiple floors
- ✅ Car dispatch and movement
- ✅ Door operations
- ✅ Safety monitoring
- ✅ Emergency handling

---

## 👥 Actors

### Primary Actors

#### Passenger 👤

**Role:** End user of the elevator system

**Capabilities:**
- Call the elevator from any floor (Up/Down button)
- Select destination floor (in-car panel)
- Request to open/close doors
- Trigger emergency stop

**Design Implications:**
```java
class Passenger {
    void callElevator(int floor, Direction direction) {
        FloorPanel panel = building.getFloor(floor).getPanel();
        panel.pressButton(direction);
    }
    
    void selectDestination(int carId, int targetFloor) {
        ElevatorCar car = building.getCar(carId);
        car.getControlPanel().selectFloor(targetFloor);
    }
    
    void requestDoorOpen(int carId) {
        ElevatorCar car = building.getCar(carId);
        car.pressOpenButton();
    }
    
    void triggerEmergency(int carId) {
        ElevatorCar car = building.getCar(carId);
        car.emergencyStop();
    }
}
```

---

### Secondary Actors

#### Operator 👨‍💼

**Role:** System maintenance and monitoring personnel

**Capabilities:**
- Enter maintenance mode for a car
- Exit maintenance mode
- Acknowledge/resolve alerts
- Monitor emergency situations

**Design Implications:**
```java
class Operator {
    private String operatorId;
    private AuthorizationLevel authLevel;
    
    void enterMaintenanceMode(int carId, String authCode) {
        if (!hasAuthorization(authCode)) {
            throw new UnauthorizedException();
        }
        
        ElevatorCar car = building.getCar(carId);
        car.enterMaintenance();
        logAction("Entered maintenance on Car " + carId);
    }
    
    void exitMaintenanceMode(int carId, String authCode) {
        if (!hasAuthorization(authCode)) {
            throw new UnauthorizedException();
        }
        
        ElevatorCar car = building.getCar(carId);
        car.exitMaintenance(authCode);
        logAction("Exited maintenance on Car " + carId);
    }
    
    void acknowledgeAlert(Alert alert) {
        alert.setStatus(AlertStatus.ACKNOWLEDGED);
        alert.setHandler(this);
        notificationSystem.updateAlert(alert);
    }
    
    void resolveAlert(Alert alert, String resolution) {
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolution(resolution);
        notificationSystem.closeAlert(alert);
    }
}
```

---

## 🎭 Use Cases

---

## Passenger Use Cases 👤

### 1. Call Elevator 📞

**Description:**
Press "Up" or "Down" button on floor panel to call an elevator

**Preconditions:**
- Passenger is on a floor
- At least one elevator is operational (not in maintenance)

**Flow:**
1. Passenger approaches floor panel
2. Presses Up or Down button
3. Button illuminates
4. System dispatches appropriate elevator
5. Elevator arrives and opens doors
6. Button light turns off

**Design Implementation:**
```java
class FloorPanel {
    private int floorNumber;
    private CallButton upButton;
    private CallButton downButton;
    private IndicatorLight upLight;
    private IndicatorLight downLight;
    
    void pressUp() {
        if (floorNumber >= Building.getMaxFloor()) {
            return; // Top floor, no up button
        }
        
        upLight.illuminate();
        FloorRequest request = new FloorRequest(
            floorNumber, 
            Direction.UP,
            LocalDateTime.now()
        );
        
        // Includes: Dispatch elevator
        controller.dispatchElevator(request);
    }
    
    void pressDown() {
        if (floorNumber == 0) {
            return; // Ground floor, no down button
        }
        
        downLight.illuminate();
        FloorRequest request = new FloorRequest(
            floorNumber, 
            Direction.DOWN,
            LocalDateTime.now()
        );
        
        controller.dispatchElevator(request);
    }
    
    void onElevatorArrived(Direction direction) {
        if (direction == Direction.UP) {
            upLight.turnOff();
        } else {
            downLight.turnOff();
        }
    }
}
```

---

### 2. Select Destination Floor 🎯

**Description:**
Use in-car panel to select desired destination floor

**Preconditions:**
- Passenger is inside elevator
- Elevator is idle or moving
- Doors are open or closing

**Flow:**
1. Passenger enters elevator
2. Presses destination floor button
3. Button illuminates
4. Doors close
5. Elevator moves to destination
6. Elevator stops at floor
7. Doors open
8. Button light turns off

**Design Implementation:**
```java
class CarControlPanel {
    private int carId;
    private List<FloorButton> floorButtons;
    private ElevatorCar car;
    
    void selectFloor(int targetFloor) {
        if (targetFloor == car.getCurrentFloor()) {
            return; // Already at this floor
        }
        
        if (targetFloor < 0 || targetFloor > Building.getMaxFloor()) {
            throw new InvalidFloorException();
        }
        
        // Illuminate button
        floorButtons.get(targetFloor).illuminate();
        
        // Create internal request
        CarRequest request = new CarRequest(carId, targetFloor);
        
        // Includes: Move/stop elevator
        car.addRequest(request);
        car.moveToFloor(targetFloor);
    }
    
    void onFloorReached(int floor) {
        floorButtons.get(floor).turnOff();
    }
}
```

---

### 3. Request to Open/Close Door 🚪

**Description:**
Press "Open" or "Close" button to control doors

**Preconditions:**
- Elevator is idle (stopped at a floor)
- Not in maintenance or emergency mode

**Flow:**
1. Passenger presses Open/Close button
2. System validates car state
3. Door operation executes
4. Visual/audio feedback provided

**Design Implementation:**
```java
class CarControlPanel {
    private Button openButton;
    private Button closeButton;
    
    void pressOpen() {
        if (car.getState() != ElevatorState.IDLE) {
            return; // Ignore if not idle
        }
        
        // Includes: Operate doors
        car.holdDoorsOpen();
    }
    
    void releaseOpen() {
        car.releaseDoorsOpen();
    }
    
    void pressClose() {
        if (car.getState() != ElevatorState.IDLE) {
            return; // Ignore if not idle
        }
        
        // Includes: Operate doors
        car.closeDoors();
    }
}

class DoorSystem {
    void open() {
        if (state == DoorState.CLOSED || state == DoorState.CLOSING) {
            state = DoorState.OPENING;
            performDoorOpening();
            state = DoorState.OPEN;
            scheduleAutoClose();
        }
    }
    
    void close() {
        if (state == DoorState.OPEN && !openButtonHeld) {
            state = DoorState.CLOSING;
            performDoorClosing();
            state = DoorState.CLOSED;
        }
    }
    
    void holdOpen() {
        openButtonHeld = true;
        cancelAutoCloseTimer();
    }
}
```

---

### 4. Trigger Emergency Stop 🚨

**Description:**
Press emergency button to immediately halt elevator and alert support

**Preconditions:**
- Elevator is operational (not already in emergency or maintenance)

**Flow:**
1. Passenger presses emergency button
2. Elevator immediately stops
3. Brakes engage
4. Doors lock
5. Emergency alert sent to operator
6. Alarm sounds
7. System waits for operator intervention

**Design Implementation:**
```java
class EmergencyButton {
    private boolean isActivated;
    
    void press() {
        isActivated = true;
        
        // Trigger emergency stop
        car.emergencyStop();
    }
}

class ElevatorCar {
    void emergencyStop() {
        System.out.println("🚨 EMERGENCY STOP ACTIVATED 🚨");
        
        // 1. Immediate halt
        motorSystem.stop();
        brakeSystem.engage();
        
        // 2. Change state (extends normal operation)
        state = ElevatorState.EMERGENCY;
        
        // 3. Lock doors
        doorSystem.lock();
        doorSystem.ignoreAllInputs();
        
        // 4. Alert operator (include relationship)
        notifyOperator(new EmergencyAlert(
            carId,
            currentFloor,
            "EMERGENCY STOP BUTTON PRESSED"
        ));
        
        // 5. Sound alarm
        alarmSystem.activate();
        
        // 6. Update all displays
        updateAllDisplays(DisplayMessage.EMERGENCY);
    }
}
```

---

## Elevator Control System Use Cases 🏢

### 1. Move/Stop Elevator 🔄

**Description:**
Move elevator up/down or stop at specific floor

**Preconditions:**
- Elevator not in maintenance mode
- Load within capacity limits
- Valid destination exists

**Flow:**
1. Receive movement command
2. Validate state and conditions
3. Transition to MOVING_UP or MOVING_DOWN
4. Move through floors
5. Arrive at destination
6. Transition to IDLE
7. Update displays

**Design Implementation:**
```java
class ElevatorCar {
    void moveToFloor(int targetFloor) {
        if (state == ElevatorState.MAINTENANCE) {
            throw new IllegalStateException("Cannot move in maintenance");
        }
        
        if (!loadSystem.canMove()) {
            System.out.println("Cannot move - Overloaded");
            return; // Extended by: Detect overload
        }
        
        // Determine direction
        if (targetFloor > currentFloor) {
            state = ElevatorState.MOVING_UP;
            currentDirection = Direction.UP;
        } else {
            state = ElevatorState.MOVING_DOWN;
            currentDirection = Direction.DOWN;
        }
        
        // Move through floors
        while (currentFloor != targetFloor) {
            if (currentDirection == Direction.UP) {
                currentFloor++;
            } else {
                currentFloor--;
            }
            
            // Includes: Update display
            updateDisplays();
            
            // Check if need to stop at this floor
            checkIntermediateStop();
            
            Thread.sleep(1000); // Simulate movement time
        }
        
        // Arrived at destination
        stop();
    }
    
    void stop() {
        state = ElevatorState.IDLE;
        currentDirection = Direction.IDLE;
        
        // Includes: Update display
        updateDisplays();
        
        // Includes: Operate doors
        doorSystem.open(state);
    }
    
    private void updateDisplays() {
        ElevatorEvent event = new ElevatorEvent(
            carId,
            EventType.FLOOR_CHANGED,
            currentFloor,
            currentDirection,
            state
        );
        notifyObservers(event); // Within 200ms requirement
    }
}
```

---

### 2. Dispatch Elevator 📡

**Description:**
Run elevator assignment algorithm to select optimal car for request

**Preconditions:**
- At least one elevator available (not in maintenance)
- Valid floor request received

**Flow:**
1. Receive floor request
2. Filter available elevators
3. Calculate cost for each car
4. Select optimal elevator
5. Assign request to selected car
6. Command car to move

**Design Implementation:**
```java
class ElevatorController {
    private List<ElevatorCar> elevators;
    private DispatchStrategy dispatchStrategy;
    
    void dispatchElevator(FloorRequest request) {
        // Filter available cars
        List<ElevatorCar> availableCars = elevators.stream()
            .filter(car -> car.getState() != ElevatorState.MAINTENANCE)
            .filter(car -> car.getState() != ElevatorState.EMERGENCY)
            .collect(Collectors.toList());
        
        if (availableCars.isEmpty()) {
            System.out.println("No elevators available");
            return;
        }
        
        // Select optimal car
        ElevatorCar selectedCar = dispatchStrategy.selectBestCar(
            availableCars, 
            request
        );
        
        // Assign request
        selectedCar.addRequest(request);
        
        System.out.println("Dispatched Car " + selectedCar.getCarId() + 
            " to floor " + request.getFloor());
    }
}

interface DispatchStrategy {
    ElevatorCar selectBestCar(List<ElevatorCar> cars, FloorRequest request);
}

class OptimalDispatchStrategy implements DispatchStrategy {
    @Override
    public ElevatorCar selectBestCar(List<ElevatorCar> cars, FloorRequest request) {
        return cars.stream()
            .min(Comparator.comparing(car -> calculateCost(car, request)))
            .orElse(null);
    }
    
    private int calculateCost(ElevatorCar car, FloorRequest request) {
        int distance = Math.abs(car.getCurrentFloor() - request.getFloor());
        int directionPenalty = getDirectionPenalty(car, request);
        int loadPenalty = car.getCurrentLoad() / 100;
        int queuePenalty = car.getPendingRequestCount() * 3;
        
        return distance + directionPenalty + loadPenalty + queuePenalty;
    }
}
```

---

### 3. Update Display (Inside/Outside) 📺

**Description:**
Refresh displays with current floor, direction, and state within 200ms

**Preconditions:**
- Display system operational
- State change occurred

**Flow:**
1. State change detected
2. Create update event
3. Notify all observers
4. Displays render new information
5. Complete within 200ms

**Design Implementation:**
```java
class ElevatorCar {
    private List<DisplayObserver> observers = new ArrayList<>();
    
    void updateAllDisplays() {
        long startTime = System.currentTimeMillis();
        
        ElevatorEvent event = new ElevatorEvent(
            carId,
            EventType.DISPLAY_UPDATE,
            currentFloor,
            currentDirection,
            state,
            loadSystem.getLoadStatus()
        );
        
        // Notify all observers
        for (DisplayObserver observer : observers) {
            observer.update(event);
        }
        
        long duration = System.currentTimeMillis() - startTime;
        if (duration > 200) {
            System.err.println("⚠️  Display update took " + duration + "ms (>200ms)");
        }
    }
}

class FloorDisplay implements DisplayObserver {
    @Override
    public void update(ElevatorEvent event) {
        render(event);
    }
    
    private void render(ElevatorEvent event) {
        System.out.println("╔════════════════════════╗");
        System.out.println("║ Floor " + floorNumber + " Display    ║");
        System.out.println("╠════════════════════════╣");
        System.out.println("║ Car " + event.getCarId() + ": Floor " + 
            event.getCurrentFloor() + "      ║");
        System.out.println("║ Direction: " + event.getDirection() + "      ║");
        System.out.println("╚════════════════════════╝");
    }
}

class CarDisplay implements DisplayObserver {
    @Override
    public void update(ElevatorEvent event) {
        System.out.println("╔════════════════════════╗");
        System.out.println("║ Current Floor: " + event.getCurrentFloor() + "    ║");
        System.out.println("║ Direction: " + event.getDirection() + "        ║");
        System.out.println("║ Load: " + event.getLoadStatus() + "          ║");
        System.out.println("╚════════════════════════╝");
    }
}
```

---

### 4. Operate Doors 🚪

**Description:**
Open or close elevator doors based on commands

**Preconditions:**
- Elevator is idle (stopped at floor)
- Not in emergency or maintenance (for normal operation)

**Flow:**
1. Receive door command
2. Validate state
3. Execute door operation
4. Provide feedback

**Design Implementation:**
```java
class DoorSystem {
    private DoorState state;
    private Timer autoCloseTimer;
    private int timeoutSeconds;
    private ObstructionSensor sensor;
    
    void operateDoor(DoorCommand command) {
        switch (command) {
            case OPEN:
                open();
                break;
            case CLOSE:
                close();
                break;
            case HOLD_OPEN:
                holdOpen();
                break;
        }
    }
    
    void open() {
        if (elevatorState != ElevatorState.IDLE) {
            throw new IllegalStateException("Can only open when idle");
        }
        
        state = DoorState.OPENING;
        performDoorOpening();
        state = DoorState.OPEN;
        
        // Auto-close after timeout
        scheduleAutoClose();
        
        communicationSystem.playChime();
        communicationSystem.announce("Doors opening");
    }
    
    void close() {
        if (sensor.isObstructed()) {
            reopen();
            return;
        }
        
        state = DoorState.CLOSING;
        communicationSystem.announce("Doors closing");
        
        performDoorClosing();
        state = DoorState.CLOSED;
        
        cancelAutoCloseTimer();
    }
    
    void reopen() {
        state = DoorState.OPENING;
        performDoorOpening();
        state = DoorState.OPEN;
        scheduleAutoClose();
    }
}

enum DoorCommand {
    OPEN,
    CLOSE,
    HOLD_OPEN
}
```

---

### 5. Detect Overload/Signal Alarm ⚠️

**Description:**
Monitor load, inhibit motion if exceeded, emit alarms

**Preconditions:**
- Weight sensor operational
- Elevator attempting to move

**Flow:**
1. Passenger enters elevator
2. Weight sensor updates
3. System checks against max load
4. If exceeded:
   - Prevent door closing
   - Sound alarm
   - Display warning
   - Wait for weight reduction

**Design Implementation:**
```java
class LoadManagementSystem {
    private final int MAX_LOAD_KG = 680;
    private WeightSensor weightSensor;
    private AlarmSystem alarmSystem;
    
    boolean canMove() {
        int currentWeight = weightSensor.getCurrentWeight();
        
        if (currentWeight > MAX_LOAD_KG) {
            // Extends: Move/stop elevator
            handleOverload(currentWeight);
            return false;
        }
        
        return true;
    }
    
    private void handleOverload(int currentWeight) {
        // Prevent movement
        elevatorCar.inhibitMotion();
        
        // Prevent door closing
        doorSystem.preventClosing();
        
        // Sound alarm
        alarmSystem.soundOverloadAlarm();
        
        // Flash lights
        alarmSystem.flashRedLight();
        
        // Display warning
        display.showOverloadWarning(currentWeight, MAX_LOAD_KG);
        
        System.out.println("⚠️  OVERLOAD: " + currentWeight + "kg / " + MAX_LOAD_KG + "kg");
    }
    
    void clearOverload() {
        if (weightSensor.getCurrentWeight() <= MAX_LOAD_KG) {
            alarmSystem.stopAlarm();
            display.clearWarning();
            elevatorCar.allowMotion();
        }
    }
}
```

---

### 6. Notify Operator 📢

**Description:**
Alert operator/security in emergency or fault scenarios

**Preconditions:**
- Emergency or fault condition detected
- Notification system operational

**Flow:**
1. Emergency/fault detected
2. Create alert
3. Send to operator console
4. Send to building management
5. Log incident
6. Wait for acknowledgment

**Design Implementation:**
```java
class NotificationSystem {
    private List<Operator> operators;
    private BuildingManagementSystem bms;
    private AlertLogger logger;
    
    void notifyOperator(Alert alert) {
        // Send to all operators
        for (Operator operator : operators) {
            operator.receiveAlert(alert);
        }
        
        // Send to building management
        bms.logAlert(alert);
        
        // Log for audit trail
        logger.log(alert);
        
        // If critical, escalate
        if (alert.getSeverity() == AlertSeverity.CRITICAL) {
            escalate(alert);
        }
    }
    
    private void escalate(Alert alert) {
        // Send to emergency services if needed
        // Send SMS/email to senior staff
        System.out.println("🚨 CRITICAL ALERT ESCALATED: " + alert.getMessage());
    }
}

class Alert {
    private int carId;
    private AlertType type;
    private AlertSeverity severity;
    private String message;
    private LocalDateTime timestamp;
    private AlertStatus status;
    
    // Getters and setters
}

enum AlertType {
    EMERGENCY_STOP,
    OVERLOAD,
    DOOR_MALFUNCTION,
    MOTOR_FAILURE,
    SENSOR_ERROR,
    COMMUNICATION_FAILURE
}

enum AlertSeverity {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum AlertStatus {
    ACTIVE,
    ACKNOWLEDGED,
    IN_PROGRESS,
    RESOLVED
}
```

---

## Operator Use Cases 👨‍💼

### 1. Enter Maintenance Mode 🔧

**Description:**
Place elevator in maintenance, removing from dispatch

**Preconditions:**
- Operator has valid authorization
- Elevator is idle (or can be brought to idle)

**Flow:**
1. Operator authenticates
2. Selects car for maintenance
3. System moves car to nearest floor (if moving)
4. Doors close and lock
5. Car removed from dispatch
6. All displays show "Maintenance"
7. Inputs ignored

**Design Implementation:**
```java
class Operator {
    void enterMaintenanceMode(int carId, String authCode) {
        // Validate authorization
        if (!MaintenanceAuthority.validate(authCode)) {
            throw new UnauthorizedException("Invalid auth code");
        }
        
        ElevatorCar car = building.getCar(carId);
        
        // Put car in maintenance
        car.enterMaintenance();
        
        System.out.println("✅ Car " + carId + " entered maintenance mode");
    }
}

class ElevatorCar {
    void enterMaintenance() {
        // If moving, stop at nearest floor
        if (state == ElevatorState.MOVING_UP || 
            state == ElevatorState.MOVING_DOWN) {
            moveToNearestFloor();
        }
        
        // Change state
        state = ElevatorState.MAINTENANCE;
        
        // Close and lock doors
        doorSystem.close();
        doorSystem.lock();
        doorSystem.ignoreInputs();
        
        // Clear all pending requests
        clearAllRequests();
        
        // Notify controller to remove from dispatch
        controller.removeFromDispatch(carId);
        
        // Update all displays
        notifyMaintenanceEntry();
    }
}
```

---

### 2. Exit Maintenance Mode ✅

**Description:**
Return car to idle state and resume normal service

**Preconditions:**
- Car is in maintenance mode
- Operator has valid authorization
- Maintenance work completed

**Flow:**
1. Operator authenticates
2. Releases maintenance mode
3. Car returns to IDLE at current floor
4. Doors unlock
5. Car added back to dispatch
6. Displays updated to normal

**Design Implementation:**
```java
class Operator {
    void exitMaintenanceMode(int carId, String authCode) {
        if (!MaintenanceAuthority.validate(authCode)) {
            throw new UnauthorizedException("Invalid auth code");
        }
        
        ElevatorCar car = building.getCar(carId);
        car.exitMaintenance(authCode);
        
        System.out.println("✅ Car " + carId + " returned to service");
    }
}

class ElevatorCar {
    void exitMaintenance(String authCode) {
        if (state != ElevatorState.MAINTENANCE) {
            throw new IllegalStateException("Car not in maintenance");
        }
        
        // Return to idle
        state = ElevatorState.IDLE;
        currentDirection = Direction.IDLE;
        
        // Unlock doors
        doorSystem.unlock();
        doorSystem.acceptInputs();
        
        // Add back to dispatch
        controller.addToDispatch(carId);
        
        // Update displays
        notifyMaintenanceExit();
        
        System.out.println("Car " + carId + " ready at floor " + currentFloor);
    }
}
```

---

### 3. Acknowledge/Resolve Alerts 📋

**Description:**
Address alarms and emergency situations

**Preconditions:**
- Alert exists in system
- Operator has access

**Flow:**
1. Operator receives alert
2. Reviews alert details
3. Acknowledges receipt
4. Takes corrective action
5. Resolves and closes alert

**Design Implementation:**
```java
class Operator {
    void acknowledgeAlert(Alert alert) {
        alert.setStatus(AlertStatus.ACKNOWLEDGED);
        alert.setAcknowledgedBy(this.operatorId);
        alert.setAcknowledgedAt(LocalDateTime.now());
        
        notificationSystem.updateAlert(alert);
        
        System.out.println("Alert " + alert.getId() + " acknowledged by " + operatorId);
    }
    
    void resolveAlert(Alert alert, String resolution) {
        if (alert.getStatus() != AlertStatus.ACKNOWLEDGED) {
            throw new IllegalStateException("Alert must be acknowledged first");
        }
        
        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolvedBy(this.operatorId);
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolution(resolution);
        
        notificationSystem.closeAlert(alert);
        
        System.out.println("Alert " + alert.getId() + " resolved: " + resolution);
    }
}
```

---

## 🔗 Relationships

---

## 1. Association 🔗

### Definition
An association links an actor to a use case they participate in or initiate.

### Actor-Use Case Mapping

#### Passenger Use Cases:

| Use Case | Description |
|----------|-------------|
| Call elevator | Press Up/Down button on floor panel |
| Select destination floor | Choose floor from in-car panel |
| Request to open/close door | Control doors via panel buttons |
| Trigger emergency stop | Press emergency button |

---

#### Elevator Control System Use Cases:

| Use Case | Description |
|----------|-------------|
| Move/stop elevator | Control car movement and stopping |
| Dispatch elevator | Assign cars to requests |
| Update display (inside/outside) | Refresh all displays in real-time |
| Operate doors | Execute door open/close operations |
| Detect overload/signal alarm | Monitor load and trigger alarms |
| Notify operator | Send alerts for emergencies |

---

#### Operator Use Cases:

| Use Case | Description |
|----------|-------------|
| Enter maintenance mode | Take car out of service |
| Exit maintenance mode | Return car to service |
| Acknowledge/resolve alerts | Handle emergency notifications |

---

## 2. Include Relationships «include» ✅

### Definition
One use case **always** incorporates the behavior of another. The included use case is mandatory.

---

### Include Relationship 1: "Select destination floor" includes "Move/stop elevator"

**Primary Use Case:** Select destination floor

**Included Use Case:** Move/stop elevator

**Relationship:**
```
Passenger selects floor
    ↓ «include»
Elevator moves/stops at floor
    ↓ (always happens)
```

**Why Include?**
- Cannot select destination without movement
- Movement is **mandatory** result of selection

**Design:**
```java
void selectFloor(int targetFloor) {
    floorButtons.get(targetFloor).illuminate();
    CarRequest request = new CarRequest(carId, targetFloor);
    
    // ALWAYS includes: Move/stop elevator
    car.moveToFloor(targetFloor); // «include»
}
```

---

### Include Relationship 2: "Move/stop elevator" includes "Update display(inside/outside)"

**Primary Use Case:** Move/stop elevator

**Included Use Case:** Update display

**Relationship:**
```
Elevator moves/stops
    ↓ «include»
Displays update within 200ms
    ↓ (always happens)
```

**Design:**
```java
void moveToFloor(int targetFloor) {
    while (currentFloor != targetFloor) {
        currentFloor += (targetFloor > currentFloor) ? 1 : -1;
        
        // ALWAYS includes: Update display
        updateAllDisplays(); // «include» - within 200ms
        
        Thread.sleep(1000);
    }
}
```

---

### Include Relationship 3: "Request door open/close" includes "Operate doors"

**Primary Use Case:** Request door open/close

**Included Use Case:** Operate doors

**Relationship:**
```
Passenger presses Open/Close button
    ↓ «include»
Door system operates doors
    ↓ (always happens)
```

**Design:**
```java
void pressOpen() {
    if (car.getState() == ElevatorState.IDLE) {
        // ALWAYS includes: Operate doors
        doorSystem.open(); // «include»
    }
}

void pressClose() {
    if (car.getState() == ElevatorState.IDLE) {
        // ALWAYS includes: Operate doors
        doorSystem.close(); // «include»
    }
}
```

---

### Include Relationship 4: "Call elevator" includes "Dispatch elevator"

**Primary Use Case:** Call elevator

**Included Use Case:** Dispatch elevator

**Relationship:**
```
Passenger calls elevator from floor
    ↓ «include»
System runs dispatch algorithm
    ↓ (always happens)
Selects and assigns optimal car
```

**Design:**
```java
void pressUp() {
    upLight.illuminate();