# Use Case Diagram for the Parking Lot System

> **Comprehensive Design Analysis with UML Relationships**

---

## 📖 Overview

This document explores the **use case diagram** for a parking lot system, focusing on:
- Actor interactions
- Core system functions
- UML relationships (Generalization, Association, Include)
- Design implications for LLD interviews

---

## 🎯 System Definition

### System: Parking Lot System

**Purpose:**
Manages the entire parking process, including:
- ✅ Vehicle entry and exit
- ✅ Parking spot allocation
- ✅ Payment processing
- ✅ Administration of parking lot resources

---

## 👥 Actors

### Primary Actor

#### Customer 🚗
**Role:** End user of the parking system

**Responsibilities:**
- Parks their vehicle
- Obtains a parking ticket
- Pays the parking fee
- Exits the parking lot

---

### Secondary Actor

#### Admin 👨‍💼
**Role:** System administrator/manager

**Responsibilities:**
- Manages system resources (parking spots, entry/exit panels)
- Configures pricing
- Handles account and system configuration
- Monitors system operations

---

## 🎭 Use Cases

---

## Admin Use Cases 👨‍💼

### 1. Add a Parking Spot ➕

**Description:**
Add a new parking spot to the system

**Inputs:**
- Spot type (Accessible, Compact, Large, Motorcycle)
- Location (Floor number, section)
- Spot ID

**Design Implications:**
```java
class ParkingSpotManager {
    void addParkingSpot(SpotType type, int floor, String section) {
        ParkingSpot spot = spotFactory.createSpot(type);
        spot.setLocation(floor, section);
        floor.addSpot(spot);
        updateDisplayBoards();
    }
}
```

---

### 2. Remove the Parking Spot ➖

**Description:**
Remove a spot from the system (maintenance, repurposing)

**Preconditions:**
- Spot must not be currently occupied
- Or force remove with vehicle relocation

**Design Implications:**
```java
void removeParkingSpot(String spotId) {
    ParkingSpot spot = findSpot(spotId);
    
    if (spot.isOccupied()) {
        throw new SpotOccupiedException();
    }
    
    spot.markRemoved();
    updateDisplayBoards();
}
```

---

### 3. Update the Parking Spot 🔄

**Description:**
Update existing spot details

**Modifiable Attributes:**
- Spot type
- Status (available/unavailable)
- Location
- Maintenance status

**Design Implications:**
```java
void updateParkingSpot(String spotId, SpotUpdate update) {
    ParkingSpot spot = findSpot(spotId);
    spot.updateType(update.getType());
    spot.updateStatus(update.getStatus());
    auditLog.recordUpdate(spot, update);
}
```

---

### 4. Add/Modify Rate 💰

**Description:**
Configure or change pricing rates

**Rate Types:**
- Duration-based (first hour, subsequent hours)
- Vehicle type-based
- Parking spot type-based

**Design Implications:**
```java
class PricingManager {
    void addRate(RateConfig config) {
        pricingStrategy.addRule(config);
    }
    
    void modifyRate(String rateId, RateConfig newConfig) {
        pricingStrategy.updateRule(rateId, newConfig);
    }
}

class RateConfig {
    VehicleType vehicleType;
    SpotType spotType;
    double firstHourRate;
    double subsequentHourRate;
}
```

---

### 5. Update the Account 👤

**Description:**
Update admin or agent account details

**Modifiable:**
- Payment information
- Permissions
- Contact details
- Access levels

---

### 6. Login/Logout 🔐

**Description:**
Secure authentication for admin access

**Security Considerations:**
- Role-based access control (RBAC)
- Session management
- Audit logging

---

### 7. View the Account 👁️

**Description:**
View account details and activity

**Information Displayed:**
- Payment status
- Outstanding balances
- Account activity history
- Transaction logs

---

## Customer Use Cases 🚗

### 1. Take Ticket 🎫

**Description:**
Receive parking ticket at entrance

**Process:**
1. Vehicle arrives at entrance
2. System detects vehicle
3. System assigns available spot
4. Ticket is generated with entry time
5. Customer receives ticket

**Design Implications:**
```java
class Entrance {
    Ticket issueTicket(Vehicle vehicle) {
        // Check capacity
        if (!parkingLot.hasAvailableSpace()) {
            throw new ParkingFullException();
        }
        
        // Find and assign spot
        ParkingSpot spot = spotAssigner.findAvailableSpot(vehicle);
        
        // Generate ticket
        Ticket ticket = ticketGenerator.generateTicket(
            vehicle, 
            spot, 
            LocalDateTime.now()
        );
        
        // Update state
        spot.assignVehicle(vehicle);
        parkingLot.incrementOccupancy();
        
        return ticket;
    }
}
```

**Ticket Contents:**
- Ticket ID
- Vehicle information
- Assigned spot
- Entry timestamp

---

### 2. Scan Ticket at Exit 📱

**Description:**
Present ticket at exit for fee calculation

**Process:**
1. Customer arrives at exit
2. Scans/inserts ticket
3. System validates ticket
4. System calculates parking fee
5. System displays amount due

**Design Implications:**
```java
class Exit {
    PaymentInfo scanTicket(Ticket ticket) {
        // Validate ticket
        if (!ticket.isValid()) {
            throw new InvalidTicketException();
        }
        
        // Calculate duration
        ticket.setExitTime(LocalDateTime.now());
        double hours = ticket.calculateDuration();
        
        // Calculate fee (Include relationship)
        double fee = feeCalculator.calculate(
            hours,
            ticket.getVehicle().getType(),
            ticket.getSpot().getType()
        );
        
        return new PaymentInfo(ticket, fee);
    }
}
```

---

### 3. Pay for Ticket 💳

**Description:**
Pay parking fee using cash or card

**Payment Methods:**
- Credit/Debit card
- Cash

**Design Implications:**
```java
class PaymentProcessor {
    boolean processPayment(Ticket ticket, PaymentMethod method, double amount) {
        boolean success = method.processPayment(amount);
        
        if (success) {
            ticket.markPaid();
            releaseSpot(ticket.getSpot());
            openGate();
            return true;
        }
        
        return false;
    }
}
```

---

### 4. Park Vehicle 🅿️

**Description:**
Park vehicle in assigned spot

**Responsibilities:**
- Navigate to assigned spot
- Park within spot boundaries
- Lock vehicle

**System Responsibility:**
- Guide customer to spot
- Update spot status
- Monitor occupancy

---

## Parking Lot System Use Cases 🏢

### 1. Assign Parking Spot 🎯

**Description:**
Automatically allocate available spot based on vehicle type

**Algorithm Considerations:**
```java
class SpotAssigner {
    ParkingSpot findAvailableSpot(Vehicle vehicle) {
        // Get compatible spot types
        List<SpotType> compatibleTypes = vehicle.getCompatibleSpots();
        
        // Priority order (smallest to largest)
        for (SpotType type : compatibleTypes) {
            ParkingSpot spot = findFirstAvailable(type);
            if (spot != null) {
                return spot;
            }
        }
        
        throw new NoSpotAvailableException();
    }
    
    private ParkingSpot findFirstAvailable(SpotType type) {
        // Search strategy: closest to entrance
        return floors.stream()
            .flatMap(floor -> floor.getSpots().stream())
            .filter(spot -> spot.getType() == type)
            .filter(ParkingSpot::isAvailable)
            .min(Comparator.comparing(ParkingSpot::getDistanceToEntrance))
            .orElse(null);
    }
}
```

**Assignment Strategy:**
- Match vehicle to smallest compatible spot
- Prefer spots closest to entrance
- Balance load across floors

---

### 2. Remove Parking Spot 🚧

**Description:**
Update spot status to unavailable

**Triggers:**
- Maintenance required
- Spot out of service
- Permanent removal

**State Transition:**
```
AVAILABLE → MAINTENANCE
AVAILABLE → REMOVED
```

---

### 3. Show Full 🚫

**Description:**
Display "FULL" status when at capacity

**Trigger Conditions:**
- Total capacity reached
- Specific spot type exhausted

**Display Locations:**
- All entrance panels
- All display boards
- Mobile app notifications

**Design Implications:**
```java
class ParkingLot {
    void checkAndNotifyCapacity() {
        if (currentOccupancy >= maxCapacity) {
            notifyAllDisplays(DisplayMessage.FULL);
            notifyEntrances(EntranceAction.CLOSE);
        }
        
        // Check per spot type
        for (SpotType type : SpotType.values()) {
            if (getAvailableCount(type) == 0) {
                notifyDisplays(DisplayMessage.spotTypeFull(type));
            }
        }
    }
}
```

---

### 4. Show Available Spots 📊

**Description:**
Display real-time availability by spot type

**Information Shown:**
- Available count per spot type
- Total available spots
- Floor-wise breakdown

**Update Triggers:**
- Vehicle entry
- Vehicle exit
- Spot status change

**Design Implications:**
```java
class DisplayBoard {
    void updateAvailability(Map<SpotType, Integer> availability) {
        this.currentAvailability = availability;
        render();
    }
    
    private void render() {
        System.out.println("╔════════════════════════════╗");
        System.out.println("║   AVAILABLE PARKING SPOTS  ║");
        System.out.println("╠════════════════════════════╣");
        
        for (SpotType type : SpotType.values()) {
            int count = currentAvailability.get(type);
            System.out.printf("║ %-15s: %5d    ║%n", type, count);
        }
        
        System.out.println("╚════════════════════════════╝");
    }
}
```

---

### 5. Calculate Parking Fee 💵

**Description:**
Determine total amount owed by customer

**Calculation Factors:**
- Parking duration (entry to exit)
- Vehicle type
- Parking spot type
- Current pricing rules

**Design Implications:**
```java
interface FeeCalculator {
    double calculate(double hours, VehicleType vehicleType, SpotType spotType);
}

class TieredFeeCalculator implements FeeCalculator {
    private PricingConfig config;
    
    @Override
    public double calculate(double hours, VehicleType vehicleType, SpotType spotType) {
        double baseRate = config.getRate(vehicleType, spotType);
        
        if (hours <= 1) {
            return baseRate;
        }
        
        double firstHour = baseRate;
        double additionalHours = (hours - 1) * config.getSubsequentRate();
        
        return firstHour + additionalHours;
    }
}
```

**Trigger:**
Automatically triggered when customer scans ticket at exit

---

## 🔗 Relationships

---

## 1. Generalization (Inheritance) 🧬

### Definition
Describes an **"is-a"** relationship between a parent class and specialized child classes.

---

### Example 1: Vehicle Hierarchy

**Parent Class:** `Vehicle`

**Child Classes:**
- `Car`
- `Truck`
- `Van`
- `Motorcycle`

**Inherited Attributes:**
- License plate
- Color
- Entry time

**Design:**
```java
abstract class Vehicle {
    protected String licensePlate;
    protected String color;
    protected VehicleType type;
    
    abstract List<SpotType> getCompatibleSpots();
    abstract VehicleType getType();
}

class Car extends Vehicle {
    @Override
    List<SpotType> getCompatibleSpots() {
        return Arrays.asList(
            SpotType.COMPACT, 
            SpotType.LARGE, 
            SpotType.ACCESSIBLE
        );
    }
    
    @Override
    VehicleType getType() {
        return VehicleType.CAR;
    }
}

class Motorcycle extends Vehicle {
    @Override
    List<SpotType> getCompatibleSpots() {
        return Arrays.asList(
            SpotType.MOTORCYCLE,
            SpotType.COMPACT,
            SpotType.LARGE,
            SpotType.ACCESSIBLE
        );
    }
    
    @Override
    VehicleType getType() {
        return VehicleType.MOTORCYCLE;
    }
}
```

**UML Notation:**
```
        Vehicle
           △
           │
    ┌──────┼──────┬──────┐
    │      │      │      │
   Car  Truck   Van  Motorcycle
```

---

### Example 2: ParkingSpot Hierarchy

**Parent Class:** `ParkingSpot`

**Child Classes:**
- `AccessibleSpot`
- `CompactSpot`
- `LargeSpot`
- `MotorcycleSpot`

**Inherited Properties:**
- Spot number
- Occupancy status
- Location

**Design:**
```java
abstract class ParkingSpot {
    protected String spotId;
    protected SpotType type;
    protected SpotStatus status;
    protected int floor;
    protected String section;
    
    abstract boolean canFitVehicle(Vehicle vehicle);
    abstract double getPricingMultiplier();
}

class CompactSpot extends ParkingSpot {
    @Override
    boolean canFitVehicle(Vehicle vehicle) {
        VehicleType type = vehicle.getType();
        return type == VehicleType.CAR || type == VehicleType.MOTORCYCLE;
    }
    
    @Override
    double getPricingMultiplier() {
        return 1.0; // Base rate
    }
}

class LargeSpot extends ParkingSpot {
    @Override
    boolean canFitVehicle(Vehicle vehicle) {
        return true; // Can fit all vehicle types
    }
    
    @Override
    double getPricingMultiplier() {
        return 1.5; // 50% premium
    }
}
```

**UML Notation:**
```
       ParkingSpot
           △
           │
    ┌──────┼──────┬──────┐
    │      │      │      │
Accessible Compact Large Motorcycle
  Spot     Spot   Spot    Spot
```

---

## 2. Associations 🔗

### Actor-Use Case Mapping

#### Admin Use Cases:

| Use Case | Description |
|----------|-------------|
| Add a parking spot | Create new spot in system |
| Remove the parking spot | Delete/deactivate spot |
| Update the parking spot | Modify spot properties |
| Add/modify rate | Configure pricing |
| Update account | Manage admin account |
| Login/Logout | Authentication |
| View account | View account details |

---

#### Customer Use Cases:

| Use Case | Description |
|----------|-------------|
| Take ticket | Get ticket at entrance |
| Scan ticket at exit | Present ticket for payment |
| Pay for the ticket | Process payment |
| Park vehicle | Park in assigned spot |

---

#### Parking Lot System Use Cases:

| Use Case | Description |
|----------|-------------|
| Assign a parking spot | Allocate spot to vehicle |
| Remove the parking spot | Mark spot unavailable |
| Show full | Display full status |
| Show available spots | Display availability |
| Calculate the parking fee | Compute payment amount |

---

## 3. Include Relationships «include» ✅

### Definition
Used when one use case **always** uses the functionality of another (required step).

---

### Include Relationship 1: Scan Ticket → Calculate Fee

**Primary Use Case:** Scan ticket at exit

**Included Use Case:** Calculate the parking fee

**Relationship:**
```
Customer scans ticket
    ↓ «include»
System calculates parking fee
    ↓
Display amount due
```

**Why Include?**
- Fee calculation is **mandatory**
- Always happens during ticket scan
- Cannot scan ticket without calculating fee

**Design:**
```java
class Exit {
    PaymentInfo scanTicket(Ticket ticket) {
        // Validate ticket
        validateTicket(ticket);
        
        // ALWAYS includes: Calculate fee
        double fee = calculateParkingFee(ticket); // «include»
        
        return new PaymentInfo(ticket, fee);
    }
    
    private double calculateParkingFee(Ticket ticket) {
        double hours = ticket.calculateDuration();
        VehicleType vehicleType = ticket.getVehicle().getType();
        SpotType spotType = ticket.getSpot().getType();
        
        return feeCalculator.calculate(hours, vehicleType, spotType);
    }
}
```

---

### Include Relationship 2: Calculate Fee → Pay for Ticket

**Primary Use Case:** Calculate the parking fee

**Included Use Case:** Pay for ticket

**Relationship:**
```
Fee calculated
    ↓ «include»
Customer must pay
    ↓
Process payment
```

**Design Flow:**
```java
// After fee calculation, payment is mandatory
double fee = calculateParkingFee(ticket);

// Customer MUST pay before exit
PaymentMethod method = selectPaymentMethod();
boolean paid = processPayment(ticket, method, fee); // «include»

if (!paid) {
    throw new PaymentRequiredException();
}
```

---

### Include Relationship 3: Take Ticket → Assign Parking Spot

**Primary Use Case:** Take ticket

**Included Use Case:** Assign parking spot

**Relationship:**
```
Customer takes ticket
    ↓ «include»
System assigns parking spot
    ↓
Ticket generated with spot info
```

**Why Include?**
- Cannot issue ticket without assigning spot
- Spot assignment is **mandatory** step
- Ticket contains spot information

**Design:**
```java
class Entrance {
    Ticket issueTicket(Vehicle vehicle) {
        // ALWAYS includes: Assign spot
        ParkingSpot spot = assignParkingSpot(vehicle); // «include»
        
        // Generate ticket with assigned spot
        Ticket ticket = new Ticket(
            generateTicketId(),
            vehicle,
            spot,
            LocalDateTime.now()
        );
        
        return ticket;
    }
    
    private ParkingSpot assignParkingSpot(Vehicle vehicle) {
        return spotAssigner.findAvailableSpot(vehicle);
    }
}
```

---

### Include Relationship 4: Assign Spot → Show Available Spots

**Primary Use Case:** Assign parking spot

**Included Use Case:** Show available spots

**Relationship:**
```
Spot assigned to vehicle
    ↓ «include»
Update availability count
    ↓
Display boards refresh
```

**Design:**
```java
class SpotAssigner {
    ParkingSpot assignSpot(Vehicle vehicle) {
        ParkingSpot spot = findAvailableSpot(vehicle);
        spot.assignVehicle(vehicle);
        
        // ALWAYS includes: Update display
        updateAvailableSpots(); // «include»
        
        return spot;
    }
    
    private void updateAvailableSpots() {
        Map<SpotType, Integer> availability = calculateAvailability();
        displayBoards.forEach(board -> board.updateDisplay(availability));
    }
}
```

---

### Include Relationship 5: Show Available Spots → Show Full

**Primary Use Case:** Show available spots

**Included Use Case:** Show full

**Relationship:**
```
Display available spots
    ↓ «include»
Check if any type is full
    ↓ (if full)
Display "FULL" message
```

**Design:**
```java
class DisplayBoard {
    void showAvailableSpots(Map<SpotType, Integer> availability) {
        displayAvailability(availability);
        
        // ALWAYS includes: Check and show if full
        checkAndShowFull(availability); // «include»
    }
    
    private void checkAndShowFull(Map<SpotType, Integer> availability) {
        for (Map.Entry<SpotType, Integer> entry : availability.entrySet()) {
            if (entry.getValue() == 0) {
                displayFullMessage(entry.getKey());
            }
        }
        
        if (availability.values().stream().allMatch(count -> count == 0)) {
            displayFullMessage("ALL TYPES");
        }
    }
}
```

---

## 📊 Complete Use Case Diagram

### Visual Representation

```
┌──────────┐                    ┌─────────────────────────┐
│  Admin   │                    │   Parking Lot System    │
└────┬─────┘                    └───────────┬─────────────┘
     │                                      │
     ├──── Add parking spot                 │
     ├──── Remove parking spot              │
     ├──── Update parking spot ─────────────┼─── Assign parking spot
     ├──── Add/modify rate                  │           │
     ├──── Update account                   │           │ «include»
     ├──── Login/Logout                     │           ↓
     └──── View account                     │    Show available spots
                                            │           │
                                            │           │ «include»
┌──────────┐                                │           ↓
│ Customer │                                │      Show full
└────┬─────┘                                │
     │                                      │
     ├──── Take ticket ─────────────────────┼─── Assign parking spot
     │        │ «include»                   │
     │        └────────────────────────────►│
     │                                      │
     ├──── Scan ticket at exit ─────────────┼─── Calculate parking fee
     │        │ «include»                   │
     │        └────────────────────────────►│
     │                                      │
     ├──── Pay for ticket                   │
     │        △ «include»                   │
     │        │                             │
     │        └──── Calculate parking fee   │
     │                                      │
     └──── Park vehicle                     │
                                            │
                                            │
                                    Remove parking spot
```

---

## 🎯 Design Principles Applied

### 1. Single Responsibility
Each use case has one clear purpose

### 2. Separation of Concerns
- Admin operations separate from customer operations
- System operations isolated

### 3. Dependency Management
- Use «include» for mandatory dependencies
- Clear relationship definitions

### 4. Extensibility
- Easy to add new use cases
- Generalization allows new vehicle/spot types

---

## 💡 Interview Tips

### What to Emphasize:

**1. Actor Identification:**
- ✅ "I've identified Admin and Customer as actors"
- ✅ "Admin is secondary, Customer is primary"

**2. Include Relationships:**
- ✅ "Calculate fee ALWAYS happens when scanning ticket"
- ✅ "This is an «include» relationship, not optional"

**3. Generalization:**
- ✅ "Vehicle and ParkingSpot use inheritance"
- ✅ "This allows easy extension for new types"

**4. System Boundaries:**
- ✅ "The system handles spot assignment automatically"
- ✅ "Payment processing is within system scope"

---

