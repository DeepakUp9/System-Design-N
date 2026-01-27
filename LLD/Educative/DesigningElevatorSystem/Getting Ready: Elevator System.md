# Getting Ready: Elevator System - Complete LLD Analysis

> **Comprehensive Object-Oriented Design Guide for Elevator System Interviews**

---

## 📖 Problem Definition

Elevators are essential in multi-story buildings, enabling the efficient movement of people and goods. A typical system includes one or more elevator cars that respond to user requests from call buttons on each floor and destination selections inside the car.

To ensure efficiency and safety, an elevator system must handle requests intelligently, minimize passenger wait times, respect car capacity, and support centralized and decentralized control of multiple cars. The system must also manage concurrent requests, optimize car movements, and provide real-time feedback through displays inside and outside the cars.

Robust safety protocols are critical: in emergencies like power failures or fire alarms, the system may need to ignore inputs and keep doors closed until operation is safe. Handling such scenarios correctly is essential for user protection and reliable operation.

---

## 🎯 In This LLD Interview Case Study, You'll Focus On:

| Focus Area | Description |
|------------|-------------|
| **Multiple elevators coordination** | Distributing requests among cars and managing their interaction |
| **Request scheduling** | Prioritizing and queuing floor requests |
| **Capacity management** | Preventing overloading |
| **Real-time updates** | Showing current floor, direction, and occupancy |
| **Safety and fault tolerance** | Managing emergencies and critical faults |
| **User experience** | Minimizing wait times, supporting up/down requests, and providing clear information |

---

## 🔍 Expectations from the Interviewee

Numerous components are present in a typical elevator system, each with specific constraints and requirements. Interviewers look for your ability to break down and design the system, anticipate real-world scenarios, and identify components and interactions.

---

## 1. Multiple Elevators 🏢

### Questions to Ask:

- Can there be multiple elevator cars in the building?
- How could a one-elevator system differ from a multi-elevator system regarding user wait time and running cost?

---

### 🧩 Context

In real buildings, you rarely have just one elevator. Most medium to large buildings have **multiple elevator cars** working together to serve passengers efficiently.

The challenge is:
- How do you **coordinate** multiple elevators?
- How do you **distribute requests** among them?
- How do you **minimize wait time** and **optimize energy usage**?

---

### 💡 What the Interviewer is Testing

They want to see if you understand:
- **Resource coordination** (multiple cars = shared resource pool)
- **Load balancing** (distribute requests fairly)
- **Optimization algorithms** (which elevator should respond?)
- **State management** (each elevator has its own state)

---

### 💭 Real-World Meaning

**Single Elevator System:**
- Simple to design
- All requests go to one car
- Higher wait times during peak hours
- Lower operational cost
- Not scalable

**Multi-Elevator System:**
- Complex coordination needed
- Requests distributed intelligently
- Lower wait times
- Higher operational cost
- Highly scalable

---

### 🧠 In Design Terms

You need to model:

#### Elevator Car Entity:
```java
class ElevatorCar {
    private int carId;
    private int currentFloor;
    private Direction currentDirection; // UP, DOWN, IDLE
    private ElevatorState state; // MOVING, STOPPED, MAINTENANCE
    private int capacity;
    private int currentLoad;
    private Queue<Request> requestQueue;
    
    boolean canAcceptRequest() {
        return currentLoad < capacity && state != ElevatorState.MAINTENANCE;
    }
    
    void moveToFloor(int targetFloor) {
        // Movement logic
    }
}

enum Direction {
    UP, DOWN, IDLE
}

enum ElevatorState {
    MOVING, STOPPED, DOORS_OPEN, DOORS_CLOSED, MAINTENANCE, EMERGENCY
}
```

#### Elevator Controller (Dispatcher):
```java
class ElevatorController {
    private List<ElevatorCar> elevators;
    private RequestQueue pendingRequests;
    
    ElevatorCar findBestElevator(FloorRequest request) {
        // Algorithm to select optimal elevator
        // Factors: distance, direction, current load
        return elevators.stream()
            .filter(car -> car.canAcceptRequest())
            .min(Comparator.comparing(car -> 
                calculateCost(car, request)))
            .orElse(null);
    }
    
    private int calculateCost(ElevatorCar car, FloorRequest request) {
        int distance = Math.abs(car.getCurrentFloor() - request.getFloor());
        int directionPenalty = car.getCurrentDirection() == request.getDirection() ? 0 : 5;
        int loadPenalty = car.getCurrentLoad() * 2;
        
        return distance + directionPenalty + loadPenalty;
    }
}
```

---

### 🎯 Coordination Strategies

| Strategy | Description | Pros | Cons |
|----------|-------------|------|------|
| **Nearest Car** | Assign to closest available elevator | Simple, fast response | May overload some cars |
| **Direction-Based** | Assign to car already moving in request direction | Efficient for continuous traffic | Complex logic |
| **Load Balancing** | Distribute evenly across all cars | Fair distribution | May increase wait time |
| **Zone-Based** | Assign floors to specific elevators | Predictable, organized | Inflexible during odd hours |

---

### 💬 What to Say in Interview:

> *"I'd design a centralized **ElevatorController** that dispatches requests to the optimal elevator based on factors like distance, current direction, and load. This ensures minimal wait time while balancing usage across all cars."*

---

## 2. Display 📺

### Questions to Ask:

- How will users request elevators (call panels on each floor, selection panels inside cars)?
- What information is shown on displays inside and outside the elevator cars (current floor, direction, occupied status)?
- How do you keep users informed of the system state and updates?

---

### 🧩 Context

In an elevator system, **displays** serve multiple purposes:
- **Outside elevator**: Show which car is coming, direction, current floor
- **Inside elevator**: Show current floor, direction, destination floors
- **Call panels**: Allow users to request up/down

---

### 💡 What the Interviewer is Testing

- Can you model **different types of displays**?
- Can you handle **real-time updates**?
- Do you understand **observer pattern** for notifications?

---

### 🧠 In Design Terms

#### Display Types:

```java
// Outside display (on each floor)
class FloorDisplay {
    private int floorNumber;
    private Map<Integer, ElevatorStatus> elevatorStatuses; // carId -> status
    
    void updateDisplay(int carId, int currentFloor, Direction direction) {
        System.out.println("Floor " + floorNumber + 
            ": Elevator " + carId + 
            " at floor " + currentFloor + 
            " going " + direction);
    }
}

// Inside display (in each car)
class CarDisplay {
    private int carId;
    
    void showCurrentFloor(int floor) {
        System.out.println("Current Floor: " + floor);
    }
    
    void showDirection(Direction direction) {
        System.out.println("Direction: " + direction);
    }
    
    void showCapacity(int current, int max) {
        System.out.println("Occupancy: " + current + "/" + max);
    }
}

// Call panel (on each floor)
class CallPanel {
    private int floorNumber;
    private Button upButton;
    private Button downButton;
    
    void pressUp() {
        Request request = new Request(floorNumber, Direction.UP);
        elevatorController.handleRequest(request);
        upButton.illuminate();
    }
    
    void pressDown() {
        Request request = new Request(floorNumber, Direction.DOWN);
        elevatorController.handleRequest(request);
        downButton.illuminate();
    }
}

// Selection panel (inside car)
class SelectionPanel {
    private int carId;
    private List<Button> floorButtons;
    
    void selectFloor(int targetFloor) {
        Request request = new InternalRequest(carId, targetFloor);
        elevatorCar.addRequest(request);
        floorButtons.get(targetFloor).illuminate();
    }
}
```

---

### 🎨 Observer Pattern for Display Updates

```java
interface DisplayObserver {
    void update(ElevatorEvent event);
}

class FloorDisplay implements DisplayObserver {
    @Override
    public void update(ElevatorEvent event) {
        // Update floor display when elevator state changes
        if (event.getType() == EventType.FLOOR_CHANGED) {
            updateDisplay(event.getCarId(), event.getCurrentFloor(), event.getDirection());
        }
    }
}

class ElevatorCar {
    private List<DisplayObserver> observers = new ArrayList<>();
    
    void notifyFloorChange(int newFloor) {
        ElevatorEvent event = new ElevatorEvent(carId, EventType.FLOOR_CHANGED, newFloor, currentDirection);
        observers.forEach(observer -> observer.update(event));
    }
}
```

---

### 💬 What to Say in Interview:

> *"I'd use the **Observer pattern** to ensure all displays are automatically updated when elevator state changes. Each display subscribes to relevant events, maintaining real-time accuracy without tight coupling."*

---

## 3. Optimization ⚡

### Questions to Ask:

- What strategies can minimize passenger wait times and unnecessary car movements?
- How does the system handle peak hours and concurrent requests?
- How do you optimize user experience and operational efficiency (energy use, maintenance)?

---

### 🧩 Context

Elevator optimization is crucial for:
- **User experience** (wait time, travel time)
- **Energy efficiency** (minimize unnecessary movements)
- **System capacity** (handle peak loads)

---

### 💡 What the Interviewer is Testing

- Understanding of **scheduling algorithms**
- Ability to handle **edge cases** (peak hours, emergencies)
- Knowledge of **optimization trade-offs**

---

### 🧠 Common Elevator Algorithms

#### 1. FCFS (First Come First Serve)
```java
class FCFSScheduler implements SchedulingStrategy {
    private Queue<Request> requestQueue = new LinkedList<>();
    
    @Override
    public Request getNextRequest() {
        return requestQueue.poll();
    }
    
    @Override
    public void addRequest(Request request) {
        requestQueue.offer(request);
    }
}
```

**Pros:** Simple, fair  
**Cons:** Inefficient, high wait times

---

#### 2. SCAN (Elevator Algorithm)
```java
class SCANScheduler implements SchedulingStrategy {
    private PriorityQueue<Request> upQueue;
    private PriorityQueue<Request> downQueue;
    private Direction currentDirection = Direction.UP;
    
    @Override
    public Request getNextRequest() {
        if (currentDirection == Direction.UP) {
            if (!upQueue.isEmpty()) {
                return upQueue.poll();
            } else {
                currentDirection = Direction.DOWN;
                return downQueue.poll();
            }
        } else {
            if (!downQueue.isEmpty()) {
                return downQueue.poll();
            } else {
                currentDirection = Direction.UP;
                return upQueue.poll();
            }
        }
    }
}
```

**Pros:** Efficient, predictable  
**Cons:** Can cause starvation for opposite direction

---

#### 3. LOOK (Modified SCAN)
```java
class LOOKScheduler implements SchedulingStrategy {
    // Similar to SCAN but reverses direction when no more requests in current direction
    // instead of going to extreme floors
    
    @Override
    public Request getNextRequest() {
        // Only goes as far as last request in current direction
        // Then reverses
    }
}
```

**Pros:** More efficient than SCAN  
**Cons:** Still complex

---

#### 4. Destination Dispatch System (Modern)
```java
class DestinationDispatchSystem {
    // Users enter destination BEFORE entering elevator
    // System assigns specific car and groups passengers going same direction
    
    Map<Integer, List<Integer>> assignedDestinations; // carId -> floors
    
    int assignCar(int currentFloor, int destination) {
        // Algorithm to group passengers efficiently
        return findOptimalCar(currentFloor, destination);
    }
}
```

**Pros:** Highly efficient, reduces stops  
**Cons:** Complex, requires pre-selection

---

### 🎯 Optimization Strategies

| Strategy | Purpose | Implementation |
|----------|---------|----------------|
| **Collective Control** | Serve requests in current direction first | SCAN/LOOK algorithms |
| **Zoning** | Assign floors to specific elevators | Zone-based dispatch |
| **Peak Hour Mode** | Express elevators, skip floors | Time-based configuration |
| **Energy Saving** | Idle cars to specific floors | Parking strategy |

---

### 💬 What to Say in Interview:

> *"I'd implement a **LOOK scheduling algorithm** with **direction-based optimization**. The elevator serves all requests in its current direction before reversing, minimizing unnecessary stops and travel time."*

---

## 4. Reliability and Safety 🛡️

### Questions to Ask:

- How does the system respond to faults, emergencies, or overloaded conditions?
- What features ensure passenger safety at all times?

---

### 🧩 Context

Safety is **NON-NEGOTIABLE** in elevator systems. Lives depend on it.

Critical safety scenarios:
- **Overload** (too many passengers)
- **Emergency stop** (fire alarm, power failure)
- **Door obstruction** (someone/something blocking door)
- **Mechanical failure** (cable issue, motor failure)

---

### 💡 What the Interviewer is Testing

- Understanding of **fail-safe mechanisms**
- Ability to handle **emergency scenarios**
- Knowledge of **state transitions** during faults

---

### 🧠 Safety Features in Design

#### 1. Overload Detection
```java
class ElevatorCar {
    private int maxCapacity = 1000; // kg
    private WeightSensor weightSensor;
    
    void checkCapacity() {
        int currentWeight = weightSensor.getCurrentWeight();
        
        if (currentWeight > maxCapacity) {
            state = ElevatorState.OVERLOADED;
            doorSystem.preventClosing();
            alarmSystem.soundOverloadAlarm();
            display.showMessage("OVERLOADED - Please exit");
        }
    }
}
```

---

#### 2. Emergency Handling
```java
class EmergencyManager {
    void handleFireAlarm() {
        for (ElevatorCar car : allCars) {
            car.ignoreAllRequests();
            car.moveToGroundFloor();
            car.openDoorsAndDisable();
        }
    }
    
    void handlePowerFailure() {
        for (ElevatorCar car : allCars) {
            car.activateEmergencyBrake();
            car.switchToBackupPower();
            car.moveToNearestFloor();
            car.openDoors();
        }
    }
    
    void handleEmergencyStop() {
        elevatorCar.immediateStop();
        elevatorCar.activateBrake();
        elevatorCar.callForHelp();
    }
}
```

---

#### 3. Door Safety
```java
class DoorSystem {
    private DoorSensor obstructionSensor;
    private DoorState state;
    private int retryCount = 0;
    private final int MAX_RETRIES = 3;
    
    void closeDoor() {
        state = DoorState.CLOSING;
        
        while (state != DoorState.CLOSED && retryCount < MAX_RETRIES) {
            if (obstructionSensor.detectObstruction()) {
                state = DoorState.OPENING;
                waitForClearance();
                retryCount++;
            } else {
                state = DoorState.CLOSED;
            }
        }
        
        if (retryCount >= MAX_RETRIES) {
            reportMalfunction();
        }
    }
}
```

---

#### 4. State Machine for Safety
```java
class ElevatorStateMachine {
    private ElevatorState currentState;
    
    void transitionTo(ElevatorState newState) {
        if (!isValidTransition(currentState, newState)) {
            throw new InvalidStateTransitionException();
        }
        
        currentState = newState;
    }
    
    boolean isValidTransition(ElevatorState from, ElevatorState to) {
        // EMERGENCY can be entered from any state
        if (to == ElevatorState.EMERGENCY) return true;
        
        // Cannot leave EMERGENCY without manual intervention
        if (from == ElevatorState.EMERGENCY && to != ElevatorState.MAINTENANCE) {
            return false;
        }
        
        // Define valid transitions
        // IDLE -> MOVING, DOORS_OPEN
        // MOVING -> STOPPED, EMERGENCY
        // STOPPED -> DOORS_OPEN, MOVING
        // etc.
        
        return true;
    }
}
```

---

### 🎯 Safety Checklist

| Safety Feature | Implementation |
|----------------|----------------|
| **Overload Protection** | Weight sensor + door prevention |
| **Emergency Stop** | Immediate brake activation |
| **Fire Safety** | Return to ground, disable normal operation |
| **Door Obstruction** | Sensors + auto-reopen |
| **Power Failure** | Backup power + nearest floor protocol |
| **Communication** | Emergency call button + speaker |
| **Mechanical Failure** | Automatic braking + alert system |

---

### 💬 What to Say in Interview:

> *"I'd implement a **state machine** for safety states with strict transition rules. Emergency states can be entered from anywhere but require manual intervention to exit, ensuring the system fails safe."*

---

## 🏗️ Design Approach: Bottom-Up

We'll take a **bottom-up design approach**:

### Step 1: Start Small
Identify and design the smallest building blocks:
- Buttons
- Doors
- Displays
- Sensors

### Step 2: Compose Larger Components
Use these small components to assemble larger modules:
- Elevator Car
- Request Panels
- Floor Subsystems

### Step 3: Integrate into Full System
Combine these pieces:
- Complete Elevator Control System
- Building Configuration

### Step 4: Iterative Refinement
At each stage:
- Consider interactions
- Handle edge cases
- Refine based on requirements

---

## 🎨 Design Patterns

### Question:
Which design pattern(s) should be used for solving the problem of managing an elevator system efficiently? Explain your choice(s).

---

### ✅ Correct Answer:

#### 1. **State Pattern** ⭐⭐⭐
**Usage:** Managing elevator car states (IDLE, MOVING, STOPPED, DOORS_OPEN, EMERGENCY)

```java
interface ElevatorState {
    void handleRequest(Request request);
    void moveToFloor(int floor);
    void openDoors();
    void closeDoors();
}

class MovingState implements ElevatorState {
    @Override
    public void openDoors() {
        throw new IllegalStateException("Cannot open doors while moving");
    }
    
    @Override
    public void moveToFloor(int floor) {
        // Continue moving logic
    }
}

class IdleState implements ElevatorState {
    @Override
    public void handleRequest(Request request) {
        // Transition to MOVING state
    }
}
```

**Why:** Elevator behavior changes dramatically based on state. State pattern cleanly separates these behaviors.

---

#### 2. **Strategy Pattern** ⭐⭐⭐
**Usage:** Different scheduling algorithms (FCFS, SCAN, LOOK)

```java
interface SchedulingStrategy {
    Request getNextRequest();
    void addRequest(Request request);
}

class ElevatorCar {
    private SchedulingStrategy scheduler;
    
    void setScheduler(SchedulingStrategy scheduler) {
        this.scheduler = scheduler;
    }
    
    void processNextRequest() {
        Request next = scheduler.getNextRequest();
        moveToFloor(next.getTargetFloor());
    }
}
```

**Why:** Allows switching scheduling algorithms without changing elevator code.

---

#### 3. **Singleton Pattern** ⭐⭐
**Usage:** Centralized Elevator Controller

```java
class ElevatorController {
    private static ElevatorController instance;
    private List<ElevatorCar> elevators;
    
    private ElevatorController() { }
    
    public static synchronized ElevatorController getInstance() {
        if (instance == null) {
            instance = new ElevatorController();
        }
        return instance;
    }
    
    public void dispatchRequest(Request request) {
        ElevatorCar best = findBestElevator(request);
        best.addRequest(request);
    }
}
```

**Why:** Only one controller should manage all elevators in a building.

---

#### 4. **Observer Pattern** ⭐⭐
**Usage:** Display updates when elevator state changes

```java
interface ElevatorObserver {
    void update(ElevatorEvent event);
}

class ElevatorCar {
    private List<ElevatorObserver> observers = new ArrayList<>();
    
    void addObserver(ElevatorObserver observer) {
        observers.add(observer);
    }
    
    void notifyStateChange() {
        ElevatorEvent event = new ElevatorEvent(this, currentFloor, currentDirection);
        observers.forEach(obs -> obs.update(event));
    }
}
```

**Why:** Displays need real-time updates without tight coupling to elevator logic.

---

#### 5. **Factory Pattern** ⭐
**Usage:** Creating different types of requests

```java
class RequestFactory {
    static Request createFloorRequest(int floor, Direction direction) {
        return new FloorRequest(floor, direction);
    }
    
    static Request createCarRequest(int carId, int targetFloor) {
        return new CarRequest(carId, targetFloor);
    }
}
```

**Why:** Centralized request creation with type safety.

---

#### 6. **Command Pattern** ⭐
**Usage:** Encapsulating elevator operations

```java
interface ElevatorCommand {
    void execute();
    void undo();
}

class MoveToFloorCommand implements ElevatorCommand {
    private ElevatorCar car;
    private int targetFloor;
    
    @Override
    public void execute() {
        car.moveToFloor(targetFloor);
    }
    
    @Override
    public void undo() {
        // Return to previous floor
    }
}
```

**Why:** Allows queuing, logging, and undo of operations.

---

### 🎯 Pattern Summary

| Pattern | Primary Use | Criticality |
|---------|-------------|-------------|
| **State** | Elevator state management | ⭐⭐⭐ Essential |
| **Strategy** | Scheduling algorithms | ⭐⭐⭐ Essential |
| **Singleton** | Central controller | ⭐⭐ Important |
| **Observer** | Display updates | ⭐⭐ Important |
| **Factory** | Request creation | ⭐ Helpful |
| **Command** | Operation encapsulation | ⭐ Helpful |

---

## 🧠 Core Entity Model

```
Building
  ├─ List<ElevatorCar>
  ├─ List<Floor>
  └─ ElevatorController

ElevatorCar
  ├─ currentFloor: int
  ├─ currentDirection: Direction
  ├─ state: ElevatorState
  ├─ capacity: int
  ├─ currentLoad: int
  ├─ doorSystem: DoorSystem
  ├─ display: CarDisplay
  ├─ selectionPanel: SelectionPanel
  └─ requestQueue: Queue<Request>

Floor
  ├─ floorNumber: int
  ├─ callPanel: CallPanel
  └─ display: FloorDisplay

Request (Abstract)
  ├─ FloorRequest (external)
  │   ├─ floor: int
  │   └─ direction: Direction
  └─ CarRequest (internal)
      ├─ carId: int
      └─ targetFloor: int

ElevatorController (Singleton)
  ├─ elevators: List<ElevatorCar>
  ├─ scheduler: SchedulingStrategy
  └─ dispatchRequest(Request)
```

---

## 💬 Interview-Winning Statements

### On Multiple Elevators:
> *"I'd implement a centralized **dispatcher** that selects the optimal car based on distance, direction, and load, minimizing overall wait time."*

### On Displays:
> *"I'd use the **Observer pattern** to decouple display updates from elevator logic, ensuring real-time accuracy without tight coupling."*

### On Optimization:
> *"I'd implement the **LOOK algorithm** for efficient request scheduling, serving all requests in the current direction before reversing."*

### On Safety:
> *"I'd use a **State Machine** with strict transition rules, ensuring the system can enter emergency mode from any state but requires manual intervention to exit."*

### On Design Approach:
> *"I'm taking a **bottom-up approach**, starting with atomic components like buttons and sensors, then composing them into larger modules like elevator cars and control systems."*

---

