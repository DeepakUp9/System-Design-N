# Summary: Designing an Elevator System

Explore the design of an elevator system with a focus on core requirements, class responsibilities, and system workflow. Understand how design principles like SOLID and patterns such as Strategy and State help build a modular and maintainable system. Learn how components interact dynamically to fulfill user requests efficiently.

Now that you've completed the elevator system case study, let's take a moment to reflect on and consolidate what we've learned. We'll revisit the key system requirements, identify the core classes along with their responsibilities and relationships, and highlight the major design principles applied. We'll also examine how objects interact within the system and walk through the overall workflow to understand how the components come together to achieve the desired functionality.

## Key requirements

This section outlines the primary functional requirements that guided the design of the elevator system.

* The system must support a configurable number of floors (up to 15) and elevator cars (up to 3).
* Each elevator car must be able to serve all floors and can be in one of four states: moving up, moving down, maintenance, or idle.
* Elevator doors should only open when the car is idle and must close automatically after a set time.
* Each floor needs a panel with up/down call buttons and a display showing the car's floor and direction.
* Each elevator car must have an internal panel with floor selection buttons, open/close buttons, an emergency-stop button, and a display for floor, direction, and load status.
* The emergency-stop button must immediately halt the car, keep doors closed, and alert security.
* Each car must enforce a maximum load (680 kg) and prevent movement if exceeded.
* A central controller must intelligently assign the most suitable car to floor calls to minimize wait times.
* The system must handle multiple passenger requests simultaneously.
* A maintenance state is required to take a car out of service for repairs.
* All displays must update in real time to reflect the elevator's current status.
* The system must communicate all states, errors, and messages to users through audio and visual indicators.

## System actors

The following actors interact with the elevator system in various capacities.

* **Passenger:** The primary user calls the elevator, selects destination floors, and operates the in-car buttons, such as opening/closing doors or making an emergency stop.
* **Operator:** A secondary actor who can place elevators into or out of maintenance mode and receives emergency alerts.

## Important classes and relationships

The system is designed using several key classes that interact to provide the required functionality. The table below summarizes the most important ones.

| Class | Description | Important Relationships |
|-------|-------------|------------------------|
| ElevatorSystem | The main class that manages the entire system, including monitoring cars and dispatching requests. | Aggregates Building. |
| Building | Represents the physical building, containing a collection of floors and elevator cars. | Aggregates Floor and ElevatorCar. |
| ElevatorCar | Represents a single elevator car, managing its state, movement, door, panel, and display. | Composed of Door, Display, and ElevatorPanel. |
| Floor | Represents a single floor in the building, containing hall panels and displays. | Composed of HallPanel and Display. |
| Button | An abstract class for all buttons in the system. | Base class for ElevatorButton, HallButton, EmergencyButton, and DoorButton. |
| ElevatorButton | A concrete button class representing the floor selection buttons inside the elevator. | Inherits from Button. |
| HallButton | A concrete button class for the up/down call buttons on each floor. | Inherits from Button. |
| Display | Represents the display units both inside the car and on the floors, showing status information. | Part of ElevatorCar and Floor (Composition). |
| Door | Represents the elevator door, managing its open and closed states. | Part of ElevatorCar (Composition). |
| ElevatorPanel | Represents the panel inside the car, containing all the floor, door, and emergency buttons. | Composed of various Button types. |
| HallPanel | Represents the panel on each floor, containing the up and down HallButtons. | Composed of HallButtons. |

## Design highlights

A specific approach guided the system's design and incorporated key software design principles and patterns.

### Design approach

The system was designed using a bottom-up approach. We identified and designed the smallest components, such as Button and Door. These were assembled into larger components like ElevatorCar and Floor, which were finally integrated into the complete ElevatorSystem. This modular approach ensures the design is extensible and robust.

### Design principles

The design implicitly applies the five SOLID principles to create a maintainable and scalable system.

* **Single Responsibility principle (SRP):** Each class has a distinct responsibility. For instance, ElevatorCar manages its state and movement, Dispatcher handles request allocation, and Display is solely for showing information.
* **Open/Closed principle (OCP):** The system is open for extension but closed for modification. Using the Strategy pattern for dispatching allows new routing algorithms to be added without changing the core ElevatorSystem class.
* **Liskov Substitution principle (LSP):** Subtypes can be substituted for their base types. For example, different button types like ElevatorButton and HallButton inherit from the abstract Button class and can be used interchangeably where a Button is expected.
* **Interface Segregation principle (ISP):** The system is broken down into fine-grained classes (Door, Display, Button) rather than a single monolithic class. This ensures that components like ElevatorCar only depend on the interfaces they use.
* **Dependency Inversion principle (DIP):** High-level modules do not depend on low-level modules; both depend on abstractions. The ElevatorSystem depends on an abstract dispatching strategy, not a concrete one, allowing for greater flexibility.

### Design patterns

The system leverages several design patterns to solve common problems effectively.

* **Strategy pattern:** This pattern is used for the dispatching logic. It allows the algorithm for assigning an elevator car to a request (e.g., nearest-car, least-busy) to be selected and swapped at runtime without altering the ElevatorSystem.
* **State pattern:** This pattern is used to manage the various states of an ElevatorCar (e.g., idle, moving up, moving down, maintenance). The car's behavior changes based on its state, and this pattern encapsulates state-specific logic into separate classes, making the ElevatorCar class cleaner and easier to manage.
* **Delegation:** To avoid overly complex classes, the system uses delegation to assign specific tasks to helper objects. For example, the main ElevatorSystem delegates choosing a car to a Dispatcher object. This approach enhances modularity and makes the system easier to maintain and extend.

## Object interactions

The system's dynamic behavior is best understood by examining how objects collaborate. The sequence of interactions for a passenger calling an elevator is a key example.

* **Request initiation:** A Passenger presses a HallButton on a specific floor.
* **System notification:** The HallButton sends a callElevator() message to the central ElevatorSystem.
* **Car selection:** The ElevatorSystem invokes the Dispatcher to run its algorithm (selectBestCar()). The Dispatcher checks the state of all ElevatorCar objects and returns the most appropriate one.
* **Car movement:** The ElevatorSystem commands the selected ElevatorCar to move() to the passenger's floor.
* **Arrival and door operation:** Once the ElevatorCar arrives, it notifies the ElevatorSystem. The system then commands the car's Door to openDoor(), allowing the passenger to enter.

## System workflow

The primary workflow describes the end-to-end journey of a passenger from requesting to arriving at their destination.

* **Calling the elevator:** A passenger presses the "Up" or "Down" button on a floor's HallPanel.
* **Entering the car:** The system dispatches the nearest available car. Once it arrives and the doors open, the passenger enters. The system checks if the car's maximum capacity is exceeded; if so, an alarm sounds, and the passenger cannot proceed.
* **Selecting a destination:** The passenger presses a floor button on the ElevatorPanel inside the car.
* **Servicing requests:** The elevator begins moving, stopping at other floors to pick up or drop off passengers moving in the same direction. Requests for floors in the opposite direction are queued and serviced later.
* **Reaching the destination:** The elevator arrives at the passenger's desired floor, the doors open, and the passenger exits, completing the journey.



### Related case studies
Explore these related case studies to deepen your understanding of the design principles applied in the elevator system:


- **Designing a parking lot:** This case study also involves managing a finite set of resources (parking spots vs. elevators) and processing user requests based on availability and proximity.

- **Designing a vending machine:** This problem requires careful state management (e.g., Idle, AcceptingCoins, DispensingItem) and handling user input through a panel, similar to the elevator’s states and panels.

- **Designing a traffic control system:** This is a classic system design problem that involves managing the flow of entities (vehicles) through a network of nodes (intersections) using algorithms to optimize throughput and minimize wait times, analogous to the elevator dispatcher.

- **Designing a job scheduler for a computing cluster:** This problem requires an intelligent dispatcher to assign incoming computing jobs to the most appropriate server based on resource availability and priority, mirroring how an elevator system assigns cars to passenger requests.

- **Designing a ride sharing service (e.g., Uber):** This involves a central system that dispatches the nearest available driver (resource) to a user’s request, which is a direct parallel to the core logic of the elevator dispatch system.