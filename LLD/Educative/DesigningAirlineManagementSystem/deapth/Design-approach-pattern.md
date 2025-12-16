# LLD Chapter: Airline Management System

## Topic: Design Approach

---

## What

The **bottom-up design approach** starts by modeling the smallest, most stable building blocks of the system and gradually composing them into larger structures.

### In this system, those building blocks are:

- `Seat`
- `Flight`
- `Schedule`
- `Passenger`
- `Payment`

### These low-level entities are then combined to form:

- `Aircraft` (collection of seats)
- `Airport`
- `Itinerary`
- `Reservation`
- Entire airline workflow

---

## Why

**Interviewers like this approach because:**

| Reason | Impact |
|--------|--------|
| **Domain-heavy, not UI-heavy** | Core logic matters most |
| **Core entities rarely change** | Stable foundation |
| **Higher-level flows depend on primitives** | Correctness is critical |

### From an LLD perspective, this approach:

- ✅ Prevents bloated classes
- ✅ Encourages single responsibility
- ✅ Makes the design testable and extensible

> **It also signals that you are designing for correctness first, which is crucial in booking and scheduling systems.**

---

## How

**Using this approach in the interview:**

### Start with immutable or low-volatility objects

- `Seat`, `Airport`

### Define clear ownership

- `Aircraft` owns `Seats`

### Compose upwards:

```
Flights use Aircraft
Reservations use Flights and Seats
Itineraries use multiple Reservations
```

### At each step, you justify:

- Why this class exists
- What data it owns
- What behavior it exposes

> **This naturally leads to a clean object graph and avoids tight coupling.**

---

## Bottom-Up Design Flow

```
Level 1: Atomic Entities
├── Seat (number, class, status)
├── Airport (code, name, location)
├── Schedule (time, frequency)
└── Passenger (name, contact, ID)

Level 2: Composite Entities
├── Aircraft (model, seats collection)
├── Flight (number, route, schedule)
├── Payment (amount, method, status)
└── Crew (pilots, attendants)

Level 3: Behavioral Entities
├── Reservation (flight, seats, passengers)
├── Itinerary (multiple flights)
└── PricingRule (dynamic calculation)

Level 4: System Orchestration
├── BookingService
├── SchedulingService
└── PaymentService
```

---

## Design Patterns (Interview-Oriented)

**Discussing patterns here is not about name-dropping, but about design intent.**

---

### 1. Composite Pattern

#### Where
`Aircraft` → `Seats`, `Itinerary` → `Flights`

#### Why
Treat single and grouped elements uniformly

#### Interview signal
> **"An itinerary can contain one or multiple flights, but the booking flow remains the same."**

```java
interface TravelComponent {
    double getTotalPrice();
    List<Passenger> getPassengers();
}

class FlightReservation implements TravelComponent {
    private Flight flight;
    private List<Seat> seats;
    private List<Passenger> passengers;
    
    public double getTotalPrice() {
        return seats.stream()
            .mapToDouble(Seat::getPrice)
            .sum();
    }
    
    public List<Passenger> getPassengers() {
        return passengers;
    }
}

class Itinerary implements TravelComponent {
    private List<TravelComponent> components;
    
    public double getTotalPrice() {
        return components.stream()
            .mapToDouble(TravelComponent::getTotalPrice)
            .sum();
    }
    
    public List<Passenger> getPassengers() {
        return components.stream()
            .flatMap(c -> c.getPassengers().stream())
            .distinct()
            .collect(Collectors.toList());
    }
}
```

---

### 2. Strategy Pattern

#### Where
Pricing, Payment methods

#### Why
Pricing rules and payment modes change frequently

#### Interview signal
> **"Adding a new pricing rule doesn't impact booking logic."**

```java
interface PricingStrategy {
    double calculatePrice(Flight flight, SeatClass seatClass, LocalDate bookingDate);
}

class DynamicPricing implements PricingStrategy {
    public double calculatePrice(Flight flight, SeatClass seatClass, LocalDate bookingDate) {
        double basePrice = flight.getBasePrice(seatClass);
        double demandFactor = calculateDemand(flight, bookingDate);
        double timeFactor = calculateEarlyBookingDiscount(bookingDate, flight.getDepartureDate());
        return basePrice * demandFactor * timeFactor;
    }
}

class SeasonalPricing implements PricingStrategy {
    public double calculatePrice(Flight flight, SeatClass seatClass, LocalDate bookingDate) {
        double basePrice = flight.getBasePrice(seatClass);
        double seasonalMultiplier = getSeasonalMultiplier(flight.getRoute(), bookingDate);
        return basePrice * seasonalMultiplier;
    }
}

interface PaymentStrategy {
    PaymentResult process(Payment payment);
}

class CreditCardPayment implements PaymentStrategy {
    public PaymentResult process(Payment payment) {
        // Gateway integration
    }
}

class DigitalWalletPayment implements PaymentStrategy {
    public PaymentResult process(Payment payment) {
        // Wallet API integration
    }
}
```

---

### 3. Factory Pattern

#### Where
Seat creation, Payment creation

#### Why
Encapsulate creation logic based on type

#### Interview signal
> **"Seat types or payment types can evolve independently."**

```java
class SeatFactory {
    public static Seat createSeat(String seatNumber, SeatClass seatClass) {
        switch(seatClass) {
            case ECONOMY:
                return new EconomySeat(seatNumber);
            case BUSINESS:
                return new BusinessSeat(seatNumber);
            case FIRST_CLASS:
                return new FirstClassSeat(seatNumber);
            default:
                throw new IllegalArgumentException("Unknown seat class");
        }
    }
}

class PaymentFactory {
    public static Payment createPayment(PaymentType type, double amount) {
        switch(type) {
            case CREDIT_CARD:
                return new CreditCardPayment(amount);
            case DEBIT_CARD:
                return new DebitCardPayment(amount);
            case DIGITAL_WALLET:
                return new DigitalWalletPayment(amount);
            case CASH:
                return new CashPayment(amount);
            default:
                throw new IllegalArgumentException("Unknown payment type");
        }
    }
}
```

---

### 4. State Pattern

#### Where
Reservation lifecycle

#### Why
Booking flows have well-defined states

#### Interview signal
> **"State transitions are explicit and controlled."**

```java
interface ReservationState {
    void confirm(Reservation reservation);
    void cancel(Reservation reservation);
    void checkIn(Reservation reservation);
}

class PendingState implements ReservationState {
    public void confirm(Reservation reservation) {
        // Process payment
        if (paymentSuccessful()) {
            reservation.setState(new ConfirmedState());
        }
    }
    
    public void cancel(Reservation reservation) {
        reservation.setState(new CancelledState());
    }
    
    public void checkIn(Reservation reservation) {
        throw new IllegalStateException("Cannot check in unconfirmed reservation");
    }
}

class ConfirmedState implements ReservationState {
    public void confirm(Reservation reservation) {
        throw new IllegalStateException("Already confirmed");
    }
    
    public void cancel(Reservation reservation) {
        // Apply cancellation policy
        if (cancellationAllowed()) {
            processRefund();
            reservation.setState(new CancelledState());
        }
    }
    
    public void checkIn(Reservation reservation) {
        reservation.setState(new CheckedInState());
    }
}

class CheckedInState implements ReservationState {
    public void confirm(Reservation reservation) {
        throw new IllegalStateException("Already checked in");
    }
    
    public void cancel(Reservation reservation) {
        throw new IllegalStateException("Cannot cancel after check-in");
    }
    
    public void checkIn(Reservation reservation) {
        // Already checked in
    }
}

enum ReservationStatus {
    PENDING, CONFIRMED, CHECKED_IN, CANCELLED, COMPLETED
}
```

---

### 5. Observer Pattern (Optional mention)

#### Where
Delays, cancellations, notifications

#### Why
Multiple systems react to flight changes

#### Interview signal
> **"We avoid tight coupling between scheduling and notification."**

```java
interface FlightObserver {
    void onFlightStatusChange(FlightInstance flight);
}

class NotificationService implements FlightObserver {
    public void onFlightStatusChange(FlightInstance flight) {
        // Send notifications to passengers
        notifyPassengers(flight);
    }
}

class AnalyticsService implements FlightObserver {
    public void onFlightStatusChange(FlightInstance flight) {
        // Log for analytics
        recordStatusChange(flight);
    }
}

class FlightInstance {
    private List<FlightObserver> observers = new ArrayList<>();
    private FlightStatus status;
    
    public void addObserver(FlightObserver observer) {
        observers.add(observer);
    }
    
    public void setStatus(FlightStatus newStatus) {
        this.status = newStatus;
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (FlightObserver observer : observers) {
            observer.onFlightStatusChange(this);
        }
    }
}
```

---

## Interview Add-on (High-value)

**You can conclude this section by stating:**

### Bottom-up ensures domain correctness

> **"By starting with stable entities like Seat and Flight, we ensure the foundation is solid before building complex workflows."**

### Patterns are used only where change is expected

> **"We apply Strategy for pricing because rates change frequently, but we don't over-engineer Seat, which is stable."**

### The design is open for extension but closed for modification

> **"New payment methods or pricing rules can be added without touching existing booking logic."**

> **This wraps the design approach in a confident, interview-ready narrative.**

---

## Design Pattern Mapping

| Pattern | Use Case | Benefit |
|---------|----------|---------|
| **Composite** | Itinerary with multiple flights | Uniform treatment |
| **Strategy** | Pricing rules, payment methods | Runtime flexibility |
| **Factory** | Seat/payment creation | Encapsulated creation |
| **State** | Reservation lifecycle | Explicit transitions |
| **Observer** | Flight status changes | Decoupled notifications |

---

## SOLID Principles Application

### Single Responsibility Principle (SRP)

```java
class Seat {
    // Only manages seat data
}

class SeatAvailability {
    // Only manages availability status
}

class SeatPricing {
    // Only manages pricing logic
}
```

---

### Open/Closed Principle (OCP)

```java
// New pricing strategies can be added without modifying existing code
class EarlyBirdDiscount implements PricingStrategy {
    public double calculatePrice(Flight flight, SeatClass seatClass, LocalDate bookingDate) {
        // New discount logic
    }
}
```

---

### Liskov Substitution Principle (LSP)

```java
// All payment strategies are substitutable
PaymentStrategy payment = new CreditCardPayment();
payment = new DigitalWalletPayment(); // Works seamlessly
```

---

### Interface Segregation Principle (ISP)

```java
interface Bookable {
    void book();
}

interface Cancellable {
    void cancel();
}

interface Modifiable {
    void modify();
}

// Reservation implements only what it needs
class Reservation implements Bookable, Cancellable, Modifiable {
    // Implementation
}
```

---

### Dependency Inversion Principle (DIP)

```java
class BookingService {
    private PricingStrategy pricingStrategy;  // Depends on abstraction
    private PaymentStrategy paymentStrategy;  // Depends on abstraction
    
    // Not dependent on concrete implementations
}
```

---

## Interview Response Template

### When discussing design approach:

> **"I'll use a bottom-up approach for the airline system. Starting with atomic entities like Seat and Airport, I'll compose them into Aircraft and Flight. Higher-level entities like Reservation and Itinerary will then use these foundations. This ensures correctness at each level and prevents tight coupling. The design naturally supports patterns like Composite for itineraries, Strategy for flexible pricing, and State for reservation lifecycle management."**

---

### When discussing patterns:

> **"Several patterns emerge naturally from this domain. Strategy handles variable pricing and payment methods without impacting core booking logic. State manages reservation lifecycle with explicit transitions. Composite allows treating single flights and multi-leg journeys uniformly. Factory encapsulates seat and payment creation. Observer decouples flight status changes from notification delivery. These aren't forced—they solve real problems in the domain."**

---

## Red Flags to Avoid

| Mistake | Why It's Bad |
|---------|--------------|
| **Starting with BookingService** | Guessing entity details |
| **Mixing pricing in Flight** | Tight coupling |
| **No state management** | Bugs in lifecycle |
| **Hardcoded payment logic** | Inflexible |
| **Ignoring composition** | Duplicate code |

---

## Green Flags That Impress

| What You Say | Why It Impresses |
|--------------|------------------|
| **"Seat is atomic, Aircraft composes seats"** | Shows composition thinking |
| **"Pricing is Strategy-based for flexibility"** | Shows pattern understanding |
| **"Reservation has explicit state transitions"** | Shows lifecycle awareness |
| **"Bottom-up ensures domain correctness"** | Shows design discipline |
| **"Patterns used only where change expected"** | Shows judgment |

---

## Summary

**This design approach establishes:**

- ✅ Clear bottom-up progression (Seat → Aircraft → Reservation)
- ✅ Natural pattern emergence (not forced)
- ✅ Strong separation of concerns
- ✅ Extensibility through Strategy and Factory
- ✅ Correctness through composition and immutability

> **Everything is justified by domain needs, not design dogma.**

---
