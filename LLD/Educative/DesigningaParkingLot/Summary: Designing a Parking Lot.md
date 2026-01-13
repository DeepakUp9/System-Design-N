# Summary: Designing a Parking Lot

Explore how to design a comprehensive parking lot system by reviewing requirements, core classes, and their interactions. Understand the application of SOLID principles and design patterns to build a maintainable, scalable system. Learn the end-to-end workflow from vehicle entry to exit and payment processing.

Now that you've completed the parking lot case study, let's take a moment to reflect on and consolidate what we've learned. We'll revisit the key system requirements, identify the core classes along with their responsibilities and relationships, and highlight the major design principles applied. We'll also examine how objects interact within the system and walk through the overall workflow to understand how the components come together to achieve the desired functionality.

## Key requirements

This section outlines the primary functional requirements that shaped the design of the parking lot system.

* The parking lot must support up to 40,000 vehicles.
* It must support multiple parking spots: accessible, compact, large, and motorcycle.
* The lot must have multiple entrances and exit points for efficient traffic flow.
* The system must accommodate various vehicle types: car, truck, van, and motorcycle.
* Display boards at entrances and on each floor must show the real-time number of available spots for each type.
* The system must prevent vehicles from entering when the lot is full.
* A "full" message must be displayed at all entrances without capacity.
* Upon entry, A parking ticket must be issued to track time and calculate fees.
* Customers must be able to pay for parking at an automated exit panel.
* The system must support configurable pricing rates based on vehicle/spot type and parking duration.
* Payments must be accepted via credit/debit card and cash.

## System actors

The following actors interact with the parking lot system in various capacities.

* **Customer:** Parks their vehicle, obtains a parking ticket, pays the required fee, and exits the lot.
* **Admin:** Manages system resources, including configuring parking spots, setting pricing rates, and managing accounts.

## Important classes and relationships

The system is built around core classes with distinct responsibilities and relationships.

| Class | Description | Important Relationships |
|-------|-------------|------------------------|
| ParkingLot | The central class that manages all components of the system. Implemented as a Singleton. | Composition: Composed of Entrance, Exit, ParkingSpot, ParkingRate, DisplayBoard, and ParkingTicket objects. |
| Vehicle | An abstract class representing a generic vehicle, with concrete subclasses like Car and Truck. | Inheritance: Base class for Car, Truck, Van, and Motorcycle.<br>Association: Holds a reference to its ParkingTicket. |
| ParkingSpot | An abstract class for a parking spot, with specialized subclasses for different spot types. | Inheritance: Base class for AccessibleSpot, CompactSpot, LargeSpot, etc.<br>Association: Associated with a Vehicle when occupied. |
| ParkingTicket | Stores information about a parking session, including entry/exit times, vehicle details, and payment status. | Association: Links Vehicle, Entrance, and Exit.<br>Composition: Composed of a Payment object. |
| Payment | An abstract class for processing payments, with concrete subclasses for different payment methods. | Inheritance: Base class for Cash and CreditCard payment types. |
| ParkingRate | Responsible for calculating the parking fee based on duration and other rules. | Association: Used by the ParkingLot to determine fees. |
| Entrance / Exit | Classes responsible for managing vehicle entry (issuing tickets) and exit (validating tickets). | Association: The ParkingLot has multiple Entrance and Exit points. |
| DisplayBoard | Displays real-time information about the number of available parking spots. | Association: Associated with ParkingSpot objects to show their status. |
| Admin | Represents an administrative user who can configure the system. | Inheritance: Extends the abstract Account class. |

## Design highlights

The design of the parking lot system was guided by a clear methodology and established software engineering principles.

### Design approach

The system was designed using a bottom-up approach. We started by identifying and modeling the core, fundamental entities like Vehicle and ParkingSpot. These were combined to build more complex components, which were finally integrated and orchestrated by the main ParkingLot class.

### Design principles

The design adheres to the SOLID principles to ensure it is robust, maintainable, and scalable.

* **Single Responsibility principle (SRP):** Each class has a well-defined purpose. For instance, ParkingTicket manages session data, Payment processes financial transactions, and ParkingRate is solely responsible for fee calculation.
* **Open/Closed principle (OCP):** Using abstract classes like Vehicle and ParkingSpot allows the system to be extended with new types (e.g., Bus, ElectricVehicleSpot) without modifying the existing, tested code.
* **Liskov Substitution principle (LSP):** Subclasses can stand in for their parent classes. For example, a Car or Truck object can be used wherever a Vehicle object is expected, ensuring consistent behavior.
* **Interface Segregation principle (ISP):** Classes have focused roles. The Entrance class is only concerned with issuing tickets, and the Exit class is only concerned with validating them, preventing classes from depending on methods they don't use.
* **Dependency Inversion principle (DIP):** The system depends on abstractions, not concrete implementations. The main ParkingLot class works with the abstract Vehicle and ParkingSpot classes, decoupling it from specific types of vehicles or spots.

### Design patterns

The system leverages established design patterns to solve common architectural challenges.

* **Singleton:** The ParkingLot class is implemented as a Singleton to ensure that only one instance controls all system resources and provides a single, global point of access.
* **Factory / Abstract Factory:** These patterns create objects of different types, such as Vehicle or ParkingSpot. This decouples the client code from the concrete classes, making it easy to add new types in the future without changing the core logic.

## Object interactions

The system's dynamic behavior is defined by how its core objects collaborate.

* **Ticket issuance:** When a Customer arrives, the Entrance panel, managed by the ParkingLot, creates ParkingTicket. The system then finds a suitable ParkingSpot based on the vehicle type and assigns it, updating the spot's status and the information on the DisplayBoard.
* **Fee calculation and payment:** At an Exit panel, the Customer presents their ParkingTicket. The Exit object retrieves the ticket details and uses the ParkingRate object to calculate the total fee. It then initiates a transaction through a Payment object (e.g., CreditCard). Once the payment is confirmed, the ParkingTicket status is updated to "Paid," and the exit gate opens.

## System workflow

The primary workflow illustrates the end-to-end journey of a customer using the parking lot.

* **Vehicle entry:** A customer arrives at an Entrance and the DisplayBoard shows available spots. The customer selects their vehicle type, and the system checks for an available ParkingSpot. If a spot is free, the system assigns it, prints a ParkingTicket with the entry time and vehicle details, and opens the gate. If no spots are available, access is denied.
* **Parking:** The customer parks their vehicle in the assigned ParkingSpot, which the system internally marks as occupied.
* **Vehicle exit:** The customer drives to an Exit panel and scans their ParkingTicket. The system calculates the parking duration and the total fee. The customer pays via cash or card. Upon successful payment, the system validates the ticket, opens the exit gate, and updates the ParkingTicket status to "Available" on the DisplayBoard.


## Related case studies
Explore these related case studies to deepen your understanding of the design principles applied in the parking lot system:

- **Designing a car rental system**: This case study also involves managing a fleet of vehicles, handling different vehicle types, and processing bookings and payments, reinforcing concepts of resource management and state tracking.

- **Designing an elevator system:** This problem focuses on managing a limited set of resources (elevators) to efficiently serve user requests, similar to how a parking lot manages a limited number of spots.

- **Designing a movie ticket booking system:** This involves managing seats (finite resources) in different screens and handling user transactions, sharing the core challenge of resource allocation and management with the parking lot design.

- **Designing a warehouse management system:** This requires tracking inventory in specific locations (bins/shelves), managing incoming and outgoing items, and optimizing placement, analogous to managing vehicles in parking spots.

- **Designing a toll booth system:** This case study focuses on tracking vehicles passing through specific points, calculating fees (often variable), and processing payments, sharing the transaction and vehicle tracking aspects.