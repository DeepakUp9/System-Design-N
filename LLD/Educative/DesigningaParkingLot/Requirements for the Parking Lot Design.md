# Parking Lot System - Requirements Analysis & Design Approach

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

### R1: Capacity Constraint 🚗

**Requirement:**
> The parking lot must support a total capacity of up to 40,000 vehicles.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity** | `ParkingLot` needs a `maxCapacity` attribute |
| **Validation** | Entry controller must check `currentOccupancy < maxCapacity` |
| **State** | Track occupied vs available spots in real-time |
| **Scalability** | System must handle high-volume tracking |

#### Key Classes Affected:
```java
class ParkingLot {
    private int maxCapacity = 40000;
    private int currentOccupancy;
    
    boolean hasAvailableSpace() {
        return currentOccupancy < maxCapacity;
    }
}
```

#### Design Pattern Consideration:
- **Singleton** pattern for `ParkingLot` (single instance)
- **Observer** pattern to notify when capacity changes

---

### R2: Multiple Parking Spot Types 🅿️

**Requirement:**
> The parking lot must support multiple types of parking spots:
> - Accessible (for individuals with disabilities)
> - Compact
> - Large
> - Motorcycle

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity Hierarchy** | `ParkingSpot` as abstract/interface with subtypes |
| **Enum** | `SpotType` enum for categorization |
| **Validation** | Vehicle-to-spot compatibility checks |
| **Capacity Tracking** | Separate counters per spot type |

#### Key Design:
```java
enum SpotType {
    ACCESSIBLE,
    COMPACT,
    LARGE,
    MOTORCYCLE
}

abstract class ParkingSpot {
    protected String spotId;
    protected SpotType type;
    protected SpotStatus status;
    
    abstract boolean canFitVehicle(Vehicle vehicle);
}

class AccessibleSpot extends ParkingSpot {
    @Override
    boolean canFitVehicle(Vehicle vehicle) {
        // Accessible spot logic
        return true; // Can fit any vehicle
    }
}

class CompactSpot extends ParkingSpot {
    @Override
    boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.CAR 
            || vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}

class LargeSpot extends ParkingSpot {
    @Override
    boolean canFitVehicle(Vehicle vehicle) {
        return true; // Can fit all vehicles
    }
}

class MotorcycleSpot extends ParkingSpot {
    @Override
    boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.getType() == VehicleType.MOTORCYCLE;
    }
}
```

#### Design Patterns:
- **Strategy Pattern** for spot allocation logic
- **Factory Pattern** for creating different spot types

---

### R3: Multiple Entrance & Exit Points 🚪

**Requirement:**
> The parking lot should provide multiple entrance and exit points to support efficient traffic flow.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity** | `Entrance` and `Exit` as separate entities |
| **Concurrency** | Handle simultaneous vehicle entries/exits |
| **State Sync** | All entry/exit points share same `ParkingLot` state |
| **Thread Safety** | Atomic operations for spot allocation |

#### Key Design:
```java
class Entrance {
    private String entranceId;
    private TicketDispenser ticketDispenser;
    
    Ticket issueTicket(Vehicle vehicle) {
        if (!parkingLot.hasAvailableSpace()) {
            throw new ParkingFullException();
        }
        
        ParkingSpot spot = findAvailableSpot(vehicle);
        Ticket ticket = ticketDispenser.generateTicket(vehicle, spot);
        spot.assignVehicle(vehicle);
        
        return ticket;
    }
}

class Exit {
    private String exitId;
    private PaymentProcessor paymentProcessor;
    
    void processExit(Ticket ticket, Payment payment) {
        double fee = calculateFee(ticket);
        paymentProcessor.process(payment, fee);
        releaseSpot(ticket.getSpot());
    }
}
```

#### Concurrency Considerations:
```java
// Atomic spot assignment
synchronized ParkingSpot findAndAssignSpot(Vehicle vehicle) {
    ParkingSpot spot = findAvailableSpot(vehicle);
    if (spot != null) {
        spot.markOccupied();
    }
    return spot;
}
```

#### Design Patterns:
- **Facade Pattern** for entry/exit operations
- **Command Pattern** for vehicle entry/exit commands

---

### R4: Multiple Vehicle Types 🚙

**Requirement:**
> The system must support parking for four types of vehicles: cars, trucks, vans, and motorcycles.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity Hierarchy** | `Vehicle` as abstract/interface with subtypes |
| **Enum** | `VehicleType` enum |
| **Spot Matching** | Vehicle size determines compatible spots |
| **Pricing** | Different rates per vehicle type |

#### Key Design:
```java
enum VehicleType {
    CAR,
    TRUCK,
    VAN,
    MOTORCYCLE
}

abstract class Vehicle {
    protected String licensePlate;
    protected VehicleType type;
    
    abstract VehicleType getType();
    abstract List<SpotType> getCompatibleSpots();
}

class Car extends Vehicle {
    @Override
    VehicleType getType() {
        return VehicleType.CAR;
    }
    
    @Override
    List<SpotType> getCompatibleSpots() {
        return Arrays.asList(
            SpotType.COMPACT, 
            SpotType.LARGE, 
            SpotType.ACCESSIBLE
        );
    }
}

class Motorcycle extends Vehicle {
    @Override
    VehicleType getType() {
        return VehicleType.MOTORCYCLE;
    }
    
    @Override
    List<SpotType> getCompatibleSpots() {
        return Arrays.asList(
            SpotType.MOTORCYCLE,
            SpotType.COMPACT,
            SpotType.LARGE,
            SpotType.ACCESSIBLE
        );
    }
}

class Truck extends Vehicle {
    @Override
    VehicleType getType() {
        return VehicleType.TRUCK;
    }
    
    @Override
    List<SpotType> getCompatibleSpots() {
        return Arrays.asList(
            SpotType.LARGE,
            SpotType.ACCESSIBLE
        );
    }
}
```

#### Vehicle-Spot Compatibility Matrix:

| Vehicle Type | Compatible Spots |
|--------------|------------------|
| **Motorcycle** | Motorcycle, Compact, Large, Accessible |
| **Car** | Compact, Large, Accessible |
| **Van** | Large, Accessible |
| **Truck** | Large, Accessible |

#### Design Patterns:
- **Inheritance** for vehicle hierarchy
- **Template Method** for common vehicle operations

---

### R5: Display Boards 📊

**Requirement:**
> A display board at each entrance and on every floor should show the current number of available parking spots for each parking spot type.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity** | `DisplayBoard` as observer of parking state |
| **Real-time Updates** | Automatic refresh on spot changes |
| **Location** | Multiple boards (entrance + each floor) |
| **Data** | Count per spot type |

#### Key Design:
```java
class DisplayBoard {
    private String location;
    private Map<SpotType, Integer> availableSpots;
    
    void updateDisplay(Map<SpotType, Integer> availableCounts) {
        this.availableSpots = availableCounts;
        render();
    }
    
    private void render() {
        System.out.println("=== Display Board: " + location + " ===");
        for (SpotType type : SpotType.values()) {
            System.out.println(type + ": " + availableSpots.get(type) + " available");
        }
    }
}

class Floor {
    private int floorNumber;
    private List<ParkingSpot> spots;
    private DisplayBoard displayBoard;
    
    void updateDisplayBoard() {
        Map<SpotType, Integer> counts = calculateAvailableSpots();
        displayBoard.updateDisplay(counts);
    }
    
    private Map<SpotType, Integer> calculateAvailableSpots() {
        Map<SpotType, Integer> counts = new HashMap<>();
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable()) {
                SpotType type = spot.getType();
                counts.put(type, counts.getOrDefault(type, 0) + 1);
            }
        }
        return counts;
    }
}
```

#### Design Patterns:
- **Observer Pattern** - Display boards observe parking lot state
- **Composite Pattern** - Aggregate counts from multiple floors

---

### R6: Capacity Enforcement 🚫

**Requirement:**
> The system must not allow more vehicles to enter once the parking lot reaches its maximum capacity.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Validation** | Pre-entry capacity check |
| **Exception Handling** | `ParkingFullException` |
| **State Management** | Atomic capacity checks |
| **User Feedback** | Clear rejection at entrance |

#### Key Design:
```java
class ParkingLot {
    private int maxCapacity;
    private int currentOccupancy;
    
    synchronized boolean canAcceptVehicle() {
        return currentOccupancy < maxCapacity;
    }
    
    synchronized void incrementOccupancy() throws ParkingFullException {
        if (!canAcceptVehicle()) {
            throw new ParkingFullException("Parking lot is full");
        }
        currentOccupancy++;
    }
    
    synchronized void decrementOccupancy() {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
    }
}

class Entrance {
    Ticket issueTicket(Vehicle vehicle) throws ParkingFullException {
        if (!parkingLot.canAcceptVehicle()) {
            throw new ParkingFullException("No parking available");
        }
        
        // Proceed with ticket issuance
        parkingLot.incrementOccupancy();
        // ... rest of logic
    }
}
```

#### Concurrency Safety:
- **Synchronized methods** prevent race conditions
- **Atomic operations** ensure consistency

---

### R7: Full Capacity Messaging 💬

**Requirement:**
> When the parking lot is fully occupied, a clear message should be displayed at each entrance and on all display boards.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Notification** | Broadcast to all display boards |
| **State** | Boolean `isFull` flag |
| **UI** | Prominent "FULL" message |
| **Observer** | All boards listen to capacity events |

#### Key Design:
```java
class ParkingLot {
    private List<DisplayBoard> displayBoards;
    
    void notifyCapacityFull() {
        for (DisplayBoard board : displayBoards) {
            board.showFullMessage();
        }
    }
    
    void notifyCapacityAvailable() {
        for (DisplayBoard board : displayBoards) {
            board.clearFullMessage();
        }
    }
}

class DisplayBoard {
    private boolean showingFullMessage;
    
    void showFullMessage() {
        showingFullMessage = true;
        System.out.println("*** PARKING LOT FULL - NO ENTRY ***");
    }
    
    void clearFullMessage() {
        showingFullMessage = false;
    }
}
```

#### Design Pattern:
- **Observer Pattern** - Boards observe parking lot state changes

---

### R8: Parking Ticket System 🎫

**Requirement:**
> Customers must be issued a parking ticket at entry. The ticket will be used to track parking time and calculate payment at exit.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity** | `Ticket` with entry time, vehicle, spot |
| **ID Generation** | Unique ticket ID |
| **Tracking** | Store active tickets |
| **Exit** | Ticket required for payment |

#### Key Design:
```java
class Ticket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot assignedSpot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus status;
    
    double calculateParkingDuration() {
        if (exitTime == null) {
            exitTime = LocalDateTime.now();
        }
        return Duration.between(entryTime, exitTime).toHours();
    }
}

enum TicketStatus {
    ACTIVE,
    PAID,
    LOST
}

class TicketDispenser {
    private int ticketCounter = 0;
    
    synchronized Ticket generateTicket(Vehicle vehicle, ParkingSpot spot) {
        String ticketId = "TKT-" + (++ticketCounter);
        return new Ticket(ticketId, vehicle, spot, LocalDateTime.now());
    }
}
```

#### Design Patterns:
- **Factory Pattern** for ticket generation
- **State Pattern** for ticket status transitions

---

### R9: Automated Payment 💳

**Requirement:**
> Customers should be able to pay for parking at the automated exit panel.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Entity** | `PaymentProcessor` at exit |
| **Calculation** | Fee based on duration + vehicle/spot type |
| **Automation** | Self-service payment |
| **Validation** | Ticket verification before exit |

#### Key Design:
```java
class Exit {
    private PaymentProcessor paymentProcessor;
    
    void processExit(Ticket ticket, Payment payment) {
        // Calculate fee
        double fee = calculateParkingFee(ticket);
        
        // Process payment
        boolean success = paymentProcessor.processPayment(payment, fee);
        
        if (success) {
            ticket.markPaid();
            releaseSpot(ticket.getAssignedSpot());
            openGate();
        } else {
            throw new PaymentFailedException();
        }
    }
    
    private double calculateParkingFee(Ticket ticket) {
        double hours = ticket.calculateParkingDuration();
        VehicleType vehicleType = ticket.getVehicle().getType();
        return pricingStrategy.calculate(hours, vehicleType);
    }
}
```

---

### R10: Configurable Pricing 💰

**Requirement:**
> The parking lot system must support configurable pricing rates based on vehicle type and/or parking spot type and rates for different parking durations (e.g., first hour, subsequent hours).

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Strategy Pattern** | Multiple pricing strategies |
| **Configuration** | External pricing rules |
| **Flexibility** | Easy to change rates |
| **Calculation** | Duration-based pricing |

#### Key Design:
```java
interface PricingStrategy {
    double calculateFee(double hours, VehicleType vehicleType);
}

class HourlyPricingStrategy implements PricingStrategy {
    private Map<VehicleType, Double> hourlyRates;
    
    @Override
    public double calculateFee(double hours, VehicleType vehicleType) {
        double rate = hourlyRates.get(vehicleType);
        return hours * rate;
    }
}

class TieredPricingStrategy implements PricingStrategy {
    private double firstHourRate;
    private double subsequentHourRate;
    
    @Override
    public double calculateFee(double hours, VehicleType vehicleType) {
        if (hours <= 1) {
            return firstHourRate;
        }
        return firstHourRate + (hours - 1) * subsequentHourRate;
    }
}

class PricingConfig {
    private PricingStrategy strategy;
    
    void setPricingStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }
    
    double calculateFee(Ticket ticket) {
        double hours = ticket.calculateParkingDuration();
        VehicleType type = ticket.getVehicle().getType();
        return strategy.calculateFee(hours, type);
    }
}
```

#### Example Pricing Configuration:
```java
// Configuration example
Map<VehicleType, Double> rates = new HashMap<>();
rates.put(VehicleType.MOTORCYCLE, 5.0);
rates.put(VehicleType.CAR, 10.0);
rates.put(VehicleType.VAN, 15.0);
rates.put(VehicleType.TRUCK, 20.0);

PricingStrategy strategy = new HourlyPricingStrategy(rates);
```

#### Design Pattern:
- **Strategy Pattern** - Interchangeable pricing algorithms

---

### R11: Multiple Payment Methods 💵

**Requirement:**
> Payments must be accepted via credit/debit card and cash at all payment points.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Strategy Pattern** | Different payment methods |
| **Abstraction** | Common `Payment` interface |
| **Extensibility** | Easy to add new payment types |
| **Processing** | Method-specific validation |

#### Key Design:
```java
interface PaymentMethod {
    boolean processPayment(double amount);
}

class CreditCardPayment implements PaymentMethod {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    
    @Override
    public boolean processPayment(double amount) {
        // Validate card details
        // Process through payment gateway
        return true; // Success
    }
}

class CashPayment implements PaymentMethod {
    private double cashReceived;
    
    @Override
    public boolean processPayment(double amount) {
        if (cashReceived >= amount) {
            double change = cashReceived - amount;
            dispenseCash(change);
            return true;
        }
        return false;
    }
    
    private void dispenseCash(double change) {
        System.out.println("Change: $" + change);
    }
}

class PaymentProcessor {
    boolean processPayment(PaymentMethod paymentMethod, double amount) {
        try {
            return paymentMethod.processPayment(amount);
        } catch (Exception e) {
            return false;
        }
    }
}
```

#### Payment Flow:
```
Customer selects payment method
    ↓
System calculates total fee
    ↓
Payment method validates & processes
    ↓
If successful → Release spot & open gate
    ↓
If failed → Request alternative payment
```

#### Design Pattern:
- **Strategy Pattern** - Different payment processing strategies

---

## 🏗️ High-Level Architecture

### Core Entities:
```
ParkingLot (Singleton)
    ├─ List<Floor>
    ├─ List<Entrance>
    ├─ List<Exit>
    └─ List<DisplayBoard>

Floor
    ├─ List<ParkingSpot>
    └─ DisplayBoard

ParkingSpot (Abstract)
    ├─ AccessibleSpot
    ├─ CompactSpot
    ├─ LargeSpot
    └─ MotorcycleSpot

Vehicle (Abstract)
    ├─ Car
    ├─ Truck
    ├─ Van
    └─ Motorcycle

Ticket
    ├─ Vehicle
    ├─ ParkingSpot
    └─ Entry/Exit time

PaymentMethod (Interface)
    ├─ CreditCardPayment
    └─ CashPayment
```

---

## 🎯 Design Patterns Used

| Pattern | Usage | Benefit |
|---------|-------|---------|
| **Singleton** | ParkingLot instance | Single source of truth |
| **Factory** | Creating vehicles, spots, tickets | Centralized creation |
| **Strategy** | Pricing, payment methods | Flexible algorithms |
| **Observer** | Display boards | Real-time updates |
| **State** | Ticket status, spot status | State management |
| **Template Method** | Vehicle operations | Code reuse |
| **Facade** | Entry/exit operations | Simplified interface |

---

## ✅ SOLID Principles Applied

| Principle | Application |
|-----------|-------------|
| **SRP** | Each class has one responsibility |
| **OCP** | Extendable via inheritance (spots, vehicles) |
| **LSP** | Subtypes are substitutable |
| **ISP** | Focused interfaces |
| **DIP** | Depend on abstractions (Payment, Pricing) |

---


![alt text](parkinglot.png)
![alt text](VehicleType.png)
![alt text](PaymentExit.png)