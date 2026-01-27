# Elevator System - Requirements Analysis & Design Approach

> **Comprehensive LLD Requirements Analysis with Design Implications**

---

## 📋 Requirements Overview

### Notational Convention
Each requirement is labeled as **"Rn"** where:
- **R** = Requirement
- **n** = Natural number (unique identifier)

---

## 🎯 Functional & Operational Requirements

---

### R1: System Configuration 🏢

**Requirement:**
> The elevator car system shall support a configurable number of floors (up to 15) and a configurable number of elevator cars (up to 3).

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Scalability** | System must be parameterized, not hardcoded |
| **Configuration** | Use config file or builder pattern |
| **Validation** | Enforce max limits (15 floors, 3 cars) |
| **Flexibility** | Easy to change without code modification |

#### Key Classes Affected:
```java
class BuildingConfiguration {
    private int numberOfFloors; // max 15
    private int numberOfCars;   // max 3
    
    // Validation in constructor
    public BuildingConfiguration(int floors, int cars) {
        if (floors < 1 || floors > 15) {
            throw new IllegalArgumentException("Floors must be 1-15");
        }
        if (cars < 1 || cars > 3) {
            throw new IllegalArgumentException("Cars must be 1-3");
        }
        this.numberOfFloors = floors;
        this.numberOfCars = cars;
    }
}

class Building {
    private BuildingConfiguration config;
    private List<ElevatorCar> elevators;
    private List<Floor> floors;
    
    public Building(BuildingConfiguration config) {
        this.config = config;
        initializeElevators(config.getNumberOfCars());
        initializeFloors(config.getNumberOfFloors());
    }
    
    private void initializeElevators(int count) {
        elevators = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            elevators.add(new ElevatorCar(i, config.getNumberOfFloors()));
        }
    }
    
    private void initializeFloors(int count) {
        floors = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            floors.add(new Floor(i));
        }
    }
}
```

#### Design Pattern:
- **Builder Pattern** for complex configuration
- **Factory Pattern** for creating elevators and floors

#### Configuration Example:
```java
BuildingConfiguration config = new BuildingConfiguration()
    .setNumberOfFloors(10)
    .setNumberOfCars(2)
    .setMaxLoadPerCar(680)
    .setDoorTimeout(5);

Building building = new Building(config);
```

---

### R2: Elevator Car States 🚦

**Requirement:**
> Each elevator car shall be capable of serving every floor and exist in one of four states: moving up, moving down, maintenance, or idle.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **State Management** | Use State Pattern for clean transitions |
| **State Validation** | Enforce valid state transitions |
| **Behavior Variation** | Each state has different behavior |
| **Thread Safety** | State changes must be atomic |

#### Key Design:
```java
enum ElevatorState {
    MOVING_UP,
    MOVING_DOWN,
    MAINTENANCE,
    IDLE
}

// State Pattern Implementation
interface ElevatorStateHandler {
    void handleRequest(Request request);
    void arrive();
    void enterMaintenance();
    void exitMaintenance();
}

class MovingUpState implements ElevatorStateHandler {
    private ElevatorCar car;
    
    @Override
    public void handleRequest(Request request) {
        // Only accept requests in same direction
        if (request.getDirection() == Direction.UP 
            && request.getFloor() > car.getCurrentFloor()) {
            car.addRequest(request);
        }
    }
    
    @Override
    public void arrive() {
        car.setState(new IdleState(car));
    }
    
    @Override
    public void enterMaintenance() {
        throw new IllegalStateException("Cannot enter maintenance while moving");
    }
}

class IdleState implements ElevatorStateHandler {
    private ElevatorCar car;
    
    @Override
    public void handleRequest(Request request) {
        car.addRequest(request);
        if (request.getFloor() > car.getCurrentFloor()) {
            car.setState(new MovingUpState(car));
        } else {
            car.setState(new MovingDownState(car));
        }
    }
    
    @Override
    public void enterMaintenance() {
        car.setState(new MaintenanceState(car));
    }
}

class MaintenanceState implements ElevatorStateHandler {
    @Override
    public void handleRequest(Request request) {
        // Ignore all requests in maintenance
        System.out.println("Car in maintenance - request ignored");
    }
    
    @Override
    public void exitMaintenance() {
        car.setState(new IdleState(car));
    }
}

class ElevatorCar {
    private int carId;
    private int currentFloor;
    private ElevatorStateHandler stateHandler;
    private ElevatorState state;
    
    void setState(ElevatorStateHandler newStateHandler) {
        this.stateHandler = newStateHandler;
    }
    
    void handleRequest(Request request) {
        stateHandler.handleRequest(request);
    }
    
    void enterMaintenance() {
        stateHandler.enterMaintenance();
    }
}
```

#### State Transition Diagram:
```
        IDLE
         ╱ ╲
        ╱   ╲
       ↓     ↓
  MOVING_UP  MOVING_DOWN
       ↓     ↓
        ╲   ╱
         ╲ ╱
        IDLE
         │
         ↓
    MAINTENANCE
         │
         ↓
        IDLE
```

#### State Transition Rules:

| From State | To State | Condition |
|------------|----------|-----------|
| IDLE | MOVING_UP | Request above current floor |
| IDLE | MOVING_DOWN | Request below current floor |
| IDLE | MAINTENANCE | Manual trigger |
| MOVING_UP | IDLE | Reached destination |
| MOVING_DOWN | IDLE | Reached destination |
| MOVING_UP | MAINTENANCE | ❌ Not allowed |
| MOVING_DOWN | MAINTENANCE | ❌ Not allowed |
| MAINTENANCE | IDLE | Manual release |

#### Design Pattern:
- **State Pattern** - Different behaviors for different states

---

### R3: Door Control 🚪

**Requirement:**
> Elevator doors shall only open when the car is idle and auto-close after a configurable timeout unless the "Open" button is actively held.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Safety** | Doors only open when idle |
| **Timer** | Auto-close with configurable timeout |
| **User Override** | Open button keeps door open |
| **State** | Door has its own state machine |

#### Key Design:
```java
enum DoorState {
    OPEN,
    CLOSED,
    OPENING,
    CLOSING
}

class DoorSystem {
    private DoorState state;
    private Timer autoCloseTimer;
    private int timeoutSeconds;
    private boolean openButtonHeld;
    
    public DoorSystem(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
        this.state = DoorState.CLOSED;
    }
    
    void open(ElevatorState carState) {
        if (carState != ElevatorState.IDLE) {
            throw new IllegalStateException("Cannot open doors while moving");
        }
        
        if (state == DoorState.CLOSED || state == DoorState.CLOSING) {
            state = DoorState.OPENING;
            // Physical door opening mechanism
            performDoorOpening();
            state = DoorState.OPEN;
            
            // Start auto-close timer
            scheduleAutoClose();
        }
    }
    
    void close() {
        if (state == DoorState.OPEN && !openButtonHeld) {
            state = DoorState.CLOSING;
            cancelAutoCloseTimer();
            performDoorClosing();
            state = DoorState.CLOSED;
        }
    }
    
    void holdOpen() {
        openButtonHeld = true;
        cancelAutoCloseTimer();
    }
    
    void releaseOpen() {
        openButtonHeld = false;
        scheduleAutoClose();
    }
    
    private void scheduleAutoClose() {
        cancelAutoCloseTimer();
        autoCloseTimer = new Timer();
        autoCloseTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!openButtonHeld) {
                    close();
                }
            }
        }, timeoutSeconds * 1000);
    }
    
    private void cancelAutoCloseTimer() {
        if (autoCloseTimer != null) {
            autoCloseTimer.cancel();
            autoCloseTimer = null;
        }
    }
}

class ElevatorCar {
    private DoorSystem doorSystem;
    private ElevatorState state;
    
    void arrive() {
        state = ElevatorState.IDLE;
        doorSystem.open(state); // Only opens if idle
    }
    
    void pressOpenButton() {
        doorSystem.holdOpen();
    }
    
    void releaseOpenButton() {
        doorSystem.releaseOpen();
    }
    
    void pressCloseButton() {
        doorSystem.close();
    }
}
```

#### Door State Machine:
```
    CLOSED
      │
      │ open() [if IDLE]
      ↓
   OPENING
      │
      ↓
     OPEN
      │
      │ auto-timeout OR close()
      ↓
   CLOSING
      │
      ↓
    CLOSED
```

#### Safety Considerations:
- **Obstruction Detection** - Reopen if blocked
- **Emergency Override** - Force close in emergency
- **Timeout Configuration** - Adjustable per building needs

---

### R4: Floor Panel System 🎛️

**Requirement:**
> Each floor shall have a panel with "Up"/"Down" call buttons, indicator lights, and an external display of the car's current floor and direction.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Components** | Button, Lights, Display as separate entities |
| **Interaction** | Panel communicates with controller |
| **Feedback** | Visual indicators for user feedback |
| **Real-time** | Display updates immediately |

#### Key Design:
```java
class FloorPanel {
    private int floorNumber;
    private CallButton upButton;
    private CallButton downButton;
    private IndicatorLight upLight;
    private IndicatorLight downLight;
    private FloorDisplay display;
    private ElevatorController controller;
    
    void pressUp() {
        if (floorNumber < Building.getMaxFloor()) {
            upLight.illuminate();
            FloorRequest request = new FloorRequest(floorNumber, Direction.UP);
            controller.handleRequest(request);
        }
    }
    
    void pressDown() {
        if (floorNumber > 0) {
            downLight.illuminate();
            FloorRequest request = new FloorRequest(floorNumber, Direction.DOWN);
            controller.handleRequest(request);
        }
    }
    
    void onElevatorArrived(int carId, Direction direction) {
        if (direction == Direction.UP) {
            upLight.turnOff();
        } else {
            downLight.turnOff();
        }
    }
    
    void updateDisplay(int carId, int currentFloor, Direction direction) {
        display.show(carId, currentFloor, direction);
    }
}

class CallButton {
    private ButtonState state;
    
    void press() {
        state = ButtonState.PRESSED;
        // Trigger action
    }
    
    void reset() {
        state = ButtonState.UNPRESSED;
    }
}

class IndicatorLight {
    private boolean isOn;
    
    void illuminate() {
        isOn = true;
    }
    
    void turnOff() {
        isOn = false;
    }
}

class FloorDisplay implements ElevatorObserver {
    private int floorNumber;
    
    @Override
    public void update(ElevatorEvent event) {
        if (event.getType() == EventType.FLOOR_CHANGED) {
            show(event.getCarId(), event.getCurrentFloor(), event.getDirection());
        }
    }
    
    void show(int carId, int currentFloor, Direction direction) {
        System.out.println("Floor " + floorNumber + 
            " Display: Car " + carId + 
            " at floor " + currentFloor + 
            " going " + direction);
    }
}
```

#### Component Breakdown:
```
FloorPanel
  ├─ upButton: CallButton
  ├─ downButton: CallButton
  ├─ upLight: IndicatorLight
  ├─ downLight: IndicatorLight
  └─ display: FloorDisplay
```

#### Interaction Flow:
```
User presses UP button
    ↓
Light illuminates
    ↓
Request sent to Controller
    ↓
Controller assigns elevator
    ↓
Elevator arrives
    ↓
Light turns off
    ↓
Display updates
```

---

### R5: Car Control Panel 🎚️

**Requirement:**
> Each elevator car shall have buttons, ground (0) to 15th floor, for every floor, "Open," "Close," and an emergency-stop button, plus an internal display showing the current floor, direction, and load status.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Floor Selection** | Array of buttons (0-15) |
| **Special Buttons** | Open, Close, Emergency Stop |
| **Display** | Multi-line internal display |
| **Load Sensor** | Weight sensor integration |

#### Key Design:
```java
class CarControlPanel {
    private int carId;
    private List<FloorButton> floorButtons;
    private Button openButton;
    private Button closeButton;
    private EmergencyButton emergencyButton;
    private CarDisplay display;
    private ElevatorCar car;
    
    public CarControlPanel(int carId, int maxFloors, ElevatorCar car) {
        this.carId = carId;
        this.car = car;
        initializeFloorButtons(maxFloors);
        this.openButton = new Button("Open");
        this.closeButton = new Button("Close");
        this.emergencyButton = new EmergencyButton();
        this.display = new CarDisplay();
    }
    
    private void initializeFloorButtons(int maxFloors) {
        floorButtons = new ArrayList<>();
        for (int i = 0; i <= maxFloors; i++) {
            FloorButton button = new FloorButton(i);
            button.setOnPressListener(() -> selectFloor(i));
            floorButtons.add(button);
        }
    }
    
    void selectFloor(int targetFloor) {
        if (targetFloor == car.getCurrentFloor()) {
            return; // Already at floor
        }
        
        floorButtons.get(targetFloor).illuminate();
        CarRequest request = new CarRequest(carId, targetFloor);
        car.addRequest(request);
    }
    
    void pressOpen() {
        car.holdDoorsOpen();
    }
    
    void releaseOpen() {
        car.releaseDoorsOpen();
    }
    
    void pressClose() {
        car.closeDoors();
    }
    
    void pressEmergencyStop() {
        emergencyButton.activate();
        car.emergencyStop();
    }
    
    void onFloorReached(int floor) {
        floorButtons.get(floor).turnOff();
    }
}

class FloorButton {
    private int floorNumber;
    private boolean illuminated;
    private Runnable onPressListener;
    
    void press() {
        if (onPressListener != null) {
            onPressListener.run();
        }
    }
    
    void illuminate() {
        illuminated = true;
    }
    
    void turnOff() {
        illuminated = false;
    }
}

class CarDisplay {
    void update(int currentFloor, Direction direction, LoadStatus loadStatus) {
        System.out.println("╔════════════════════════╗");
        System.out.println("║  Floor: " + currentFloor + "           ║");
        System.out.println("║  Direction: " + direction + "     ║");
        System.out.println("║  Load: " + loadStatus + "      ║");
        System.out.println("╚════════════════════════╝");
    }
}

enum LoadStatus {
    LIGHT,    // < 50% capacity
    MODERATE, // 50-80% capacity
    HEAVY,    // 80-100% capacity
    OVERLOAD  // > 100% capacity
}

class EmergencyButton {
    private boolean isActivated;
    
    void activate() {
        isActivated = true;
        // Visual/audio alarm
        triggerAlarm();
    }
    
    void deactivate() {
        isActivated = false;
    }
    
    private void triggerAlarm() {
        System.out.println("🚨 EMERGENCY STOP ACTIVATED 🚨");
    }
}
```

#### Panel Layout:
```
╔═══════════════════════════╗
║     CAR CONTROL PANEL     ║
╠═══════════════════════════╣
║  [15] [14] [13] [12] [11] ║
║  [10] [ 9] [ 8] [ 7] [ 6] ║
║  [ 5] [ 4] [ 3] [ 2] [ 1] ║
║  [ G]                     ║
╠═══════════════════════════╣
║  [OPEN]  [CLOSE]          ║
╠═══════════════════════════╣
║  [🚨 EMERGENCY STOP]      ║
╠═══════════════════════════╣
║  Floor: 5                 ║
║  Direction: UP            ║
║  Load: MODERATE           ║
╚═══════════════════════════╝
```

---

### R6: Emergency Stop 🚨

**Requirement:**
> Pressing the emergency-stop button shall immediately halt the car, keep doors closed, and alert building security/operators.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Immediate Action** | Interrupt all operations |
| **Safety First** | Doors remain locked |
| **Alert System** | Notify security immediately |
| **State Change** | Enter EMERGENCY state |

#### Key Design:
```java
class ElevatorCar {
    private ElevatorState state;
    private DoorSystem doorSystem;
    private MotorSystem motorSystem;
    private BrakeSystem brakeSystem;
    private SecurityAlertSystem alertSystem;
    
    void emergencyStop() {
        System.out.println("🚨 EMERGENCY STOP INITIATED 🚨");
        
        // 1. Immediate halt
        motorSystem.stop();
        brakeSystem.engage();
        
        // 2. Change state
        state = ElevatorState.EMERGENCY;
        
        // 3. Lock doors
        doorSystem.lock();
        doorSystem.ignoreAllInputs();
        
        // 4. Alert security
        EmergencyAlert alert = new EmergencyAlert(
            carId,
            currentFloor,
            LocalDateTime.now(),
            "EMERGENCY STOP BUTTON PRESSED"
        );
        alertSystem.sendAlert(alert);
        
        // 5. Update all displays
        notifyEmergency();
    }
    
    void releaseEmergency(String securityCode) {
        if (!SecurityManager.validate(securityCode)) {
            throw new UnauthorizedException("Invalid security code");
        }
        
        // Release emergency state
        brakeSystem.disengage();
        doorSystem.unlock();
        state = ElevatorState.IDLE;
        
        System.out.println("✅ Emergency released - Car returned to service");
    }
}

class SecurityAlertSystem {
    private List<SecurityOperator> operators;
    
    void sendAlert(EmergencyAlert alert) {
        // Send to security desk
        for (SecurityOperator operator : operators) {
            operator.receiveAlert(alert);
        }
        
        // Send to building management system
        buildingManagementSystem.logEmergency(alert);
        
        // Trigger audio alarm
        soundAlarm();
    }
    
    private void soundAlarm() {
        System.out.println("🔊 ALARM: Emergency in Car " + alert.getCarId());
    }
}

class EmergencyAlert {
    private int carId;
    private int floor;
    private LocalDateTime timestamp;
    private String reason;
    private AlertStatus status;
    
    // Getters and emergency logging
}

enum AlertStatus {
    ACTIVE,
    ACKNOWLEDGED,
    RESOLVED
}
```

#### Emergency Procedure:
```
Emergency Button Pressed
    ↓
Immediate Motor Stop
    ↓
Engage Emergency Brake
    ↓
Lock All Doors
    ↓
Change State to EMERGENCY
    ↓
Send Alert to Security
    ↓
Update All Displays
    ↓
Wait for Manual Release
```

#### Safety Rules:
- ❌ **Cannot** exit emergency without authorization
- ❌ **Cannot** accept new requests
- ❌ **Cannot** open doors
- ✅ **Can** communicate with outside (intercom)
- ✅ **Must** log all emergency events

---

### R7: Load Management ⚖️

**Requirement:**
> Each elevator car shall enforce a maximum load of 680 kg, inhibit motion if this limit is exceeded, and emit an audible and visual alarm when overloaded.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Weight Sensor** | Real-time load monitoring |
| **Motion Inhibit** | Prevent movement if overloaded |
| **Alarm System** | Audio + visual warnings |
| **User Feedback** | Clear instructions to passengers |

#### Key Design:
```java
class LoadManagementSystem {
    private final int MAX_LOAD_KG = 680;
    private WeightSensor weightSensor;
    private AlarmSystem alarmSystem;
    private CarDisplay display;
    private DoorSystem doorSystem;
    
    LoadStatus checkLoad() {
        int currentWeight = weightSensor.getCurrentWeight();
        
        if (currentWeight > MAX_LOAD_KG) {
            return LoadStatus.OVERLOAD;
        } else if (currentWeight > MAX_LOAD_KG * 0.8) {
            return LoadStatus.HEAVY;
        } else if (currentWeight > MAX_LOAD_KG * 0.5) {
            return LoadStatus.MODERATE;
        } else {
            return LoadStatus.LIGHT;
        }
    }
    
    boolean canMove() {
        LoadStatus status = checkLoad();
        
        if (status == LoadStatus.OVERLOAD) {
            handleOverload();
            return false;
        }
        
        return true;
    }
    
    private void handleOverload() {
        // 1. Prevent door closing
        doorSystem.preventClosing();
        
        // 2. Sound alarm
        alarmSystem.soundOverloadAlarm();
        
        // 3. Show visual warning
        display.showOverloadWarning();
        
        // 4. Display message
        display.showMessage("OVERLOADED - Please exit");
        
        System.out.println("⚠️  OVERLOAD DETECTED ⚠️");
        System.out.println("Current: " + weightSensor.getCurrentWeight() + " kg");
        System.out.println("Maximum: " + MAX_LOAD_KG + " kg");
    }
}

class WeightSensor {
    private int currentWeight;
    
    int getCurrentWeight() {
        // Read from physical sensor
        return currentWeight;
    }
    
    void calibrate() {
        // Calibration logic
    }
}

class AlarmSystem {
    void soundOverloadAlarm() {
        // Continuous beeping
        playSound("overload-beep.wav", true);
    }
    
    void stopAlarm() {
        stopSound();
    }
}

class ElevatorCar {
    private LoadManagementSystem loadSystem;
    
    void attemptMove() {
        if (!loadSystem.canMove()) {
            System.out.println("❌ Cannot move - Overloaded");
            return;
        }
        
        // Proceed with movement
        moveToNextFloor();
    }
    
    void onDoorClosing() {
        LoadStatus status = loadSystem.checkLoad();
        
        if (status == LoadStatus.OVERLOAD) {
            doorSystem.preventClosing();
            System.out.println("Doors cannot close - Reduce load");
        }
    }
}
```

#### Load Monitoring Flow:
```
Passenger enters car
    ↓
Weight sensor updates
    ↓
Check against MAX_LOAD
    ↓
    ├─ < 680kg → Allow operation
    └─ ≥ 680kg → OVERLOAD
          ↓
       Sound alarm
          ↓
       Show warning
          ↓
       Prevent door closing
          ↓
       Wait for weight reduction
```

#### Visual Feedback:
```
╔═══════════════════════════╗
║  ⚠️  OVERLOAD WARNING  ⚠️  ║
╠═══════════════════════════╣
║  Current Load: 720 kg     ║
║  Maximum Load: 680 kg     ║
╠═══════════════════════════╣
║  PLEASE EXIT ELEVATOR     ║
║  Doors will not close     ║
╚═══════════════════════════╝
```

---

### R8: Request Dispatch 📡

**Requirement:**
> The central controller shall assign the most appropriate car to each floor-call request, aiming to minimize average wait time, and command that car to move accordingly.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Optimization** | Implement scheduling algorithm |
| **Centralized** | Single controller coordinates all cars |
| **Wait Time** | Primary optimization metric |
| **Dynamic** | Real-time decision making |

#### Key Design:
```java
class ElevatorController {
    private static ElevatorController instance;
    private List<ElevatorCar> elevators;
    private SchedulingStrategy schedulingStrategy;
    
    private ElevatorController() {
        elevators = new ArrayList<>();
        schedulingStrategy = new OptimalSchedulingStrategy();
    }
    
    public static ElevatorController getInstance() {
        if (instance == null) {
            synchronized (ElevatorController.class) {
                if (instance == null) {
                    instance = new ElevatorController();
                }
            }
        }
        return instance;
    }
    
    void handleRequest(FloorRequest request) {
        ElevatorCar bestCar = findBestElevator(request);
        
        if (bestCar == null) {
            System.out.println("No available elevators");
            return;
        }
        
        bestCar.addRequest(request);
        System.out.println("Assigned Car " + bestCar.getCarId() + 
            " to floor " + request.getFloor());
    }
    
    private ElevatorCar findBestElevator(FloorRequest request) {
        return elevators.stream()
            .filter(car -> car.getState() != ElevatorState.MAINTENANCE)
            .filter(car -> car.getState() != ElevatorState.EMERGENCY)
            .min(Comparator.comparing(car -> calculateCost(car, request)))
            .orElse(null);
    }
    
    private int calculateCost(ElevatorCar car, FloorRequest request) {
        int requestFloor = request.getFloor();
        Direction requestDirection = request.getDirection();
        
        int currentFloor = car.getCurrentFloor();
        Direction currentDirection = car.getCurrentDirection();
        ElevatorState state = car.getState();
        
        // Distance cost
        int distanceCost = Math.abs(currentFloor - requestFloor);
        
        // Direction penalty
        int directionPenalty = 0;
        if (state == ElevatorState.MOVING_UP || state == ElevatorState.MOVING_DOWN) {
            if (currentDirection != requestDirection) {
                directionPenalty = 10;
            } else if ((currentDirection == Direction.UP && requestFloor < currentFloor) ||
                       (currentDirection == Direction.DOWN && requestFloor > currentFloor)) {
                directionPenalty = 20; // Wrong side of current direction
            }
        }
        
        // Load penalty
        int loadPenalty = car.getCurrentLoad() / 100; // Higher load = higher penalty
        
        // Queue penalty
        int queuePenalty = car.getPendingRequestCount() * 3;
        
        return distanceCost + directionPenalty + loadPenalty + queuePenalty;
    }
}

interface SchedulingStrategy {
    ElevatorCar selectElevator(List<ElevatorCar> cars, FloorRequest request);
}

class OptimalSchedulingStrategy implements SchedulingStrategy {
    @Override
    public ElevatorCar selectElevator(List<ElevatorCar> cars, FloorRequest request) {
        // Implement sophisticated algorithm
        // Consider: distance, direction, load, queue length
        return null; // Implementation details
    }
}
```

#### Cost Calculation Factors:

| Factor | Weight | Reasoning |
|--------|--------|-----------|
| **Distance** | 1x | Closer car = faster arrival |
| **Direction match** | 0-20x penalty | Going opposite way costs time |
| **Current load** | 0.01x per kg | Heavy car might be slower |
| **Queue length** | 3x per request | Many stops = longer wait |

#### Dispatch Decision Tree:
```
Request arrives
    ↓
Filter available cars
    │
    ├─ Remove MAINTENANCE cars
    └─ Remove EMERGENCY cars
    ↓
For each car, calculate cost:
    ├─ Distance to request floor
    ├─ Direction compatibility
    ├─ Current load
    └─ Pending requests
    ↓
Select car with minimum cost
    ↓
Assign request to selected car
    ↓
Command car to move
```

---

### R9: Multi-Passenger Support 👥

**Requirement:**
> The elevator car system shall support calls from multiple passengers, with each passenger able to go to the same or different floors in the same or opposite direction.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Request Queue** | Priority queue for efficient handling |
| **Batching** | Group requests in same direction |
| **Flexibility** | Support mixed destinations |
| **Fairness** | No passenger starvation |

#### Key Design:
```java
class ElevatorCar {
    private PriorityQueue<Request> upQueue;
    private PriorityQueue<Request> downQueue;
    private Set<Integer> destinationFloors;
    
    public ElevatorCar(int carId, int maxFloors) {
        this.carId = carId;
        
        // Up queue: sorted ascending
        upQueue = new PriorityQueue<>(
            Comparator.comparing(Request::getTargetFloor)
        );
        
        // Down queue: sorted descending
        downQueue = new PriorityQueue<>(
            Comparator.comparing(Request::getTargetFloor).reversed()
        );
        
        destinationFloors = new HashSet<>();
    }
    
    void addRequest(Request request) {
        int targetFloor = request.getTargetFloor();
        
        // Avoid duplicates
        if (destinationFloors.contains(targetFloor)) {
            return;
        }
        
        destinationFloors.add(targetFloor);
        
        // Add to appropriate queue
        if (targetFloor > currentFloor) {
            upQueue.offer(request);
        } else {
            downQueue.offer(request);
        }
        
        System.out.println("Request added: Floor " + targetFloor);
    }
    
    Request getNextRequest() {
        if (currentDirection == Direction.UP && !upQueue.isEmpty()) {
            return upQueue.poll();
        } else if (currentDirection == Direction.DOWN && !downQueue.isEmpty()) {
            return downQueue.poll();
        } else {
            // Switch direction
            if (!upQueue.isEmpty()) {
                currentDirection = Direction.UP;
                return upQueue.poll();
            } else if (!downQueue.isEmpty()) {
                currentDirection = Direction.DOWN;
                return downQueue.poll();
            }
        }
        return null;
    }
    
    void processFloor(int floor) {
        // Check if any passenger needs to get off
        if (destinationFloors.contains(floor)) {
            openDoors();
            destinationFloors.remove(floor);
        }
    }
}
```

#### Example Scenario:
```
Car at Floor 5, going UP
Passengers request:
  - Passenger A: Floor 8 (UP)
  - Passenger B: Floor 3 (DOWN)
  - Passenger C: Floor 10 (UP)
  - Passenger D: Floor 7 (UP)

Execution:
1. Serve UP queue first: [7, 8, 10]
   → Stops at 7, 8, 10
2. Switch direction to DOWN
3. Serve DOWN queue: [3]
   → Stops at 3
```

#### Queue Management:
```
         upQueue (sorted ascending)
              ↓
    [7] → [8] → [10]
    
         downQueue (sorted descending)
              ↓
           [3]
```

---

### R10: Maintenance State 🔧

**Requirement:**
> The system shall support a maintenance state for each car in which:
> - R10a: The car is removed from normal dispatch (no new requests assigned).
> - R10b: All elevator doors remain closed, and panel inputs are ignored.
> - R10c: All UIs display "Maintenance" for that car.
> - R10d: Upon exiting maintenance, the car returns to idle state at its current floor before resuming service.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **State Isolation** | Separate maintenance state handling |
| **Request Blocking** | Filter car from dispatcher |
| **UI Updates** | All displays show maintenance |
| **Safe Return** | Controlled exit from maintenance |

#### Key Design:
```java
class MaintenanceState implements ElevatorStateHandler {
    private ElevatorCar car;
    
    @Override
    public void handleRequest(Request request) {
        // R10a & R10b: Ignore all requests
        System.out.println("Car " + car.getCarId() + 
            " in maintenance - request ignored");
    }
    
    @Override
    public void openDoors() {
        // R10b: Doors remain closed
        throw new IllegalStateException("Cannot open doors in maintenance");
    }
    
    @Override
    public void closeDoors() {
        // Already closed
    }
    
    void exitMaintenance(String authCode) {
        if (!MaintenanceManager.validateAuthCode(authCode)) {
            throw new UnauthorizedException("Invalid auth code");
        }
        
        // R10d: Return to idle at current floor
        car.setState(new IdleState(car));
        car.clearAllRequests();
        
        // Notify all displays
        car.notifyMaintenanceExit();
        
        System.out.println("✅ Car " + car.getCarId() + 
            " returned to service at floor " + car.getCurrentFloor());
    }
}

class ElevatorCar {
    void enterMaintenance() {
        if (state == ElevatorState.MOVING_UP || state == ElevatorState.MOVING_DOWN) {
            // First, stop at nearest floor
            moveToNearestFloor();
        }
        
        state = ElevatorState.MAINTENANCE;
        stateHandler = new MaintenanceState(this);
        
        // Close and lock doors
        doorSystem.close();
        doorSystem.lock();
        doorSystem.ignoreInputs();
        
        // Clear all pending requests
        clearAllRequests();
        
        // R10c: Update all displays
        notifyMaintenanceEntry();
        
        System.out.println("🔧 Car " + carId + " entered maintenance mode");
    }
    
    void exitMaintenance(String authCode) {
        if (!(stateHandler instanceof MaintenanceState)) {
            throw new IllegalStateException("Car not in maintenance");
        }
        
        ((MaintenanceState) stateHandler).exitMaintenance(authCode);
    }
    
    private void notifyMaintenanceEntry() {
        ElevatorEvent event = new ElevatorEvent(
            carId,
            EventType.MAINTENANCE_ENTERED,
            currentFloor,
            Direction.IDLE
        );
        notifyObservers(event);
    }
    
    private void notifyMaintenanceExit() {
        ElevatorEvent event = new ElevatorEvent(
            carId,
            EventType.MAINTENANCE_EXITED,
            currentFloor,
            Direction.IDLE
        );
        notifyObservers(event);
    }
}

class ElevatorController {
    void handleRequest(FloorRequest request) {
        // R10a: Filter out maintenance cars
        ElevatorCar bestCar = elevators.stream()
            .filter(car -> car.getState() != ElevatorState.MAINTENANCE)
            .filter(car -> car.getState() != ElevatorState.EMERGENCY)
            .min(Comparator.comparing(car -> calculateCost(car, request)))
            .orElse(null);
        
        if (bestCar != null) {
            bestCar.addRequest(request);
        }
    }
}

class FloorDisplay implements ElevatorObserver {
    @Override
    public void update(ElevatorEvent event) {
        // R10c: Show maintenance on all displays
        if (event.getType() == EventType.MAINTENANCE_ENTERED) {
            showMaintenanceMessage(event.getCarId());
        } else if (event.getType() == EventType.MAINTENANCE_EXITED) {
            clearMaintenanceMessage(event.getCarId());
        }
    }
    
    private void showMaintenanceMessage(int carId) {
        System.out.println("╔═══════════════════════╗");
        System.out.println("║  Car " + carId + ": MAINTENANCE  ║");
        System.out.println("║  Out of Service       ║");
        System.out.println("╚═══════════════════════╝");
    }
}
```

#### Maintenance State Flow:
```
Normal Operation
    ↓
Enter Maintenance (Auth Required)
    ↓
Stop at nearest floor (if moving)
    ↓
Close and lock doors
    ↓
Ignore all requests
    ↓
Update all displays to "MAINTENANCE"
    ↓
Perform maintenance work
    ↓
Exit Maintenance (Auth Required)
    ↓
Return to IDLE at current floor
    ↓
Resume normal service
```

---

### R11: Real-Time Display Updates 📡

**Requirement:**
> All internal and external displays and indicators shall update in real time to reflect each elevator car's current floor, direction, and operational state (idle, moving, maintenance, or emergency).

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Observer Pattern** | Displays subscribe to car events |
| **Event-Driven** | Push updates immediately |
| **Decoupling** | Cars don't know about displays |
| **Consistency** | All displays show same state |

#### Key Design:
```java
interface ElevatorObserver {
    void update(ElevatorEvent event);
}

class ElevatorEvent {
    private int carId;
    private EventType type;
    private int currentFloor;
    private Direction direction;
    private ElevatorState state;
    private LocalDateTime timestamp;
    
    // Constructor and getters
}

enum EventType {
    FLOOR_CHANGED,
    DIRECTION_CHANGED,
    STATE_CHANGED,
    DOORS_OPENED,
    DOORS_CLOSED,
    MAINTENANCE_ENTERED,
    MAINTENANCE_EXITED,
    EMERGENCY_TRIGGERED,
    LOAD_CHANGED
}

class ElevatorCar {
    private List<ElevatorObserver> observers = new ArrayList<>();
    
    void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }
    
    void removeObserver(ElevatorObserver observer) {
        observers.remove(observer);
    }
    
    void notifyObservers(ElevatorEvent event) {
        for (ElevatorObserver observer : observers) {
            observer.update(event);
        }
    }
    
    void moveToFloor(int targetFloor) {
        while (currentFloor != targetFloor) {
            if (targetFloor > currentFloor) {
                currentFloor++;
                currentDirection = Direction.UP;
            } else {
                currentFloor--;
                currentDirection = Direction.DOWN;
            }
            
            // Real-time update
            notifyFloorChange();
        }
        
        currentDirection = Direction.IDLE;
        state = ElevatorState.IDLE;
        notifyStateChange();
    }
    
    private void notifyFloorChange() {
        ElevatorEvent event = new ElevatorEvent(
            carId,
            EventType.FLOOR_CHANGED,
            currentFloor,
            currentDirection,
            state
        );
        notifyObservers(event);
    }
    
    private void notifyStateChange() {
        ElevatorEvent event = new ElevatorEvent(
            carId,
            EventType.STATE_CHANGED,
            currentFloor,
            currentDirection,
            state
        );
        notifyObservers(event);
    }
}

class FloorDisplay implements ElevatorObserver {
    @Override
    public void update(ElevatorEvent event) {
        if (event.getType() == EventType.FLOOR_CHANGED ||
            event.getType() == EventType.STATE_CHANGED) {
            render(event);
        }
    }
    
    private void render(ElevatorEvent event) {
        System.out.println("Floor " + floorNumber + " Display:");
        System.out.println("  Car " + event.getCarId());
        System.out.println("  At floor: " + event.getCurrentFloor());
        System.out.println("  Direction: " + event.getDirection());
        System.out.println("  State: " + event.getState());
    }
}

class CarDisplay implements ElevatorObserver {
    @Override
    public void update(ElevatorEvent event) {
        switch (event.getType()) {
            case FLOOR_CHANGED:
                updateFloor(event.getCurrentFloor());
                break;
            case DIRECTION_CHANGED:
                updateDirection(event.getDirection());
                break;
            case LOAD_CHANGED:
                updateLoadStatus(event.getLoadStatus());
                break;
            case STATE_CHANGED:
                updateState(event.getState());
                break;
        }
    }
}
```

#### Real-Time Update Flow:
```
Elevator state changes
    ↓
Create ElevatorEvent
    ↓
Notify all observers
    ↓
    ├─ FloorDisplays update
    ├─ CarDisplay updates
    ├─ IndicatorLights update
    └─ Building system logs
    ↓
All displays synchronized
```

---

### R12: Status Communication 📢

**Requirement:**
> The system shall communicate all operational states, errors, and safety messages to users via audio/visual indicators and displays, ensuring passengers are always aware of the elevator's status.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Multi-Modal** | Audio + Visual feedback |
| **Clear Messages** | User-friendly language |
| **Timely** | Immediate notification |
| **Comprehensive** | Cover all scenarios |

#### Key Design:
```java
class CommunicationSystem {
    private AudioSystem audioSystem;
    private VisualSystem visualSystem;
    
    void communicateStatus(ElevatorStatus status) {
        switch (status) {
            case NORMAL_OPERATION:
                // No special communication needed
                break;
                
            case OVERLOAD:
                audioSystem.playMessage("Overload. Please exit.");
                visualSystem.showWarning("OVERLOADED - Please reduce load");
                visualSystem.flashRedLight();
                break;
                
            case EMERGENCY:
                audioSystem.playAlarm();
                audioSystem.playMessage("Emergency stop activated. Help is on the way.");
                visualSystem.showEmergencyMessage();
                visualSystem.flashRedLight();
                break;
                
            case MAINTENANCE:
                visualSystem.showMessage("This elevator is under maintenance");
                break;
                
            case DOORS_CLOSING:
                audioSystem.playChime();
                audioSystem.playMessage("Doors closing");
                break;
                
            case ARRIVING:
                audioSystem.playChime();
                visualSystem.showFloorNumber(targetFloor);
                break;
        }
    }
    
    void communicateError(ErrorType error) {
        String message = generateErrorMessage(error);
        audioSystem.playMessage(message);
        visualSystem.showError(message);
        logError(error);
    }
    
    private String generateErrorMessage(ErrorType error) {
        return switch (error) {
            case DOOR_OBSTRUCTION -> "Door blocked. Please clear obstruction.";
            case MOTOR_FAILURE -> "Technical difficulty. Please use stairs.";
            case SENSOR_MALFUNCTION -> "Sensor error detected.";
            case COMMUNICATION_ERROR -> "System error. Contacting maintenance.";
        };
    }
}

class AudioSystem {
    void playMessage(String message) {
        System.out.println("🔊 Audio: " + message);
    }
    
    void playChime() {
        System.out.println("🔔 Ding!");
    }
    
    void playAlarm() {
        System.out.println("🚨 ALARM SOUND");
    }
}

class VisualSystem {
    void showMessage(String message) {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ " + centerText(message, 26) + " ║");
        System.out.println("╚════════════════════════════╝");
    }
    
    void showWarning(String warning) {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ ⚠️  " + centerText(warning, 23) + " ⚠️  ║");
        System.out.println("╚════════════════════════════╝");
    }
    
    void showEmergencyMessage() {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║ 🚨  EMERGENCY STOP  🚨      ║");
        System.out.println("║                            ║");
        System.out.println("║ Help has been notified     ║");
        System.out.println("║ Please remain calm         ║");
        System.out.println("╚════════════════════════════╝");
    }
    
    void flashRedLight() {
        // Control LED lights
    }
}

enum ElevatorStatus {
    NORMAL_OPERATION,
    OVERLOAD,
    EMERGENCY,
    MAINTENANCE,
    DOORS_CLOSING,
    ARRIVING
}

enum ErrorType {
    DOOR_OBSTRUCTION,
    MOTOR_FAILURE,
    SENSOR_MALFUNCTION,
    COMMUNICATION_ERROR
}
```

#### Communication Matrix:

| Event | Audio | Visual | Lights |
|-------|-------|--------|--------|
| **Floor arrival** | Chime | Floor number | Green |
| **Doors closing** | Warning beep | "Doors closing" | Yellow |
| **Overload** | Continuous beep | Warning message | Red flashing |
| **Emergency** | Loud alarm | Emergency message | Red solid |
| **Maintenance** | None | "Out of service" | Yellow solid |
| **Error** | Error sound | Error message | Red flashing |

---

## 🏗️ High-Level Architecture

### Core Entity Model:
```
Building
  ├─ Configuration
  ├─ List<ElevatorCar>
  ├─ List<Floor>
  └─ ElevatorController (Singleton)

ElevatorCar
  ├─ carId: int
  ├─ currentFloor: int
  ├─ state: ElevatorState
  ├─ direction: Direction
  ├─ doorSystem: DoorSystem
  ├─ loadSystem: LoadManagementSystem
  ├─ controlPanel: CarControlPanel
  ├─ display: CarDisplay
  ├─ upQueue: PriorityQueue<Request>
  ├─ downQueue: PriorityQueue<Request>
  └─ observers: List<ElevatorObserver>

Floor
  ├─ floorNumber: int
  ├─ callPanel: FloorPanel
  └─ display: FloorDisplay

ElevatorController (Singleton)
  ├─ elevators: List<ElevatorCar>
  ├─ schedulingStrategy: SchedulingStrategy
  └─ dispatchRequest(Request)

Request (Abstract)
  ├─ FloorRequest (external calls)
  └─ CarRequest (internal selections)
```

---

## 🎨 Design Patterns Summary

| Pattern | Usage | Requirements Addressed |
|---------|-------|------------------------|
| **Singleton** | ElevatorController | R8 - Central dispatch |
| **State** | Elevator states | R2 - State management |
| **Observer** | Display updates | R11 - Real-time updates |
| **Strategy** | Scheduling algorithms | R8 - Optimal dispatch |
| **Factory** | Request creation | R9 - Multi-passenger support |
| **Command** | Button press operations | R5 - Control panel |

---

## ✅ SOLID Principles Applied

| Principle | Application |
|-----------|-------------|
| **SRP** | Each class has single responsibility (DoorSystem, LoadSystem, etc.) |
| **OCP** | Extensible via strategies and states |
| **LSP** | All state handlers are substitutable |
| **ISP** | Focused interfaces (ElevatorObserver, SchedulingStrategy) |
| **DIP** | Depend on abstractions (StateHandler, SchedulingStrategy) |

---
