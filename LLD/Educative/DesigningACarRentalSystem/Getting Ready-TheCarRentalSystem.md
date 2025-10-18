# Getting Ready: The Car Rental System

## Overview

Understand the car rental system problem and learn the questions to further simplify this problem.

A car rental system enables customers to rent vehicles for short periods, ranging from a few hours to several weeks. Such systems are commonly operated by companies with multiple offices across cities, often near airports or commercial centers. The platform allows customers to reserve, pick up, and return vehicles at different locations, depending on availability and convenience.

Each branch manages its fleet, consisting of various vehicle types and models, and tracks reservations, customer information, payments, and the logistical aspects of vehicle movement between locations. Customers can search for available vehicles by location, type, or features and reserve or cancel rentals subject to company policies. The system supports multiple payment methods and handles cases such as late returns, reservation modifications, and optional services (e.g., driver assignment and roadside assistance).

This scenario presents several design challenges, including distributed inventory management, concurrent reservations, flexible pick-up and drop-off options, dynamic pricing, and ensuring a seamless and reliable customer experience across all locations.

## Focus Areas

In this LLD interview case study, your focus will be on:

- Managing vehicle inventory and supporting multiple vehicle types and features
- Handling reservations, cancellations, and modifications across branches
- Facilitating vehicle search, pick-up, and drop-off at different locations
- Tracking customer information, rental history, and payments
- Supporting optional services and flexible payment methods
- Ensuring system reliability, data consistency, and scalability

## Expectations from the Interviewee

The car rental system consists of multiple components. Each component has its own functionality and constraints. This section provides an overview of some of the main expectations that the interviewer will want to hear you discuss in more detail during the interview.

### Vehicle Types

An interviewer would expect you to discuss the different vehicle types, and ask the following questions:

- What types of vehicles will that system support?
- How can we identify the specific vehicle?

### Search Interface

Members will use the application and add location and the reservation date. They will receive several options to select the vehicle. Therefore, an interviewer would expect you to ask questions listed below:

- Is it possible to search a vehicle using its name or type?
- Can we search for a vehicle by its model number?

### Services

An interviewer would also expect you to discuss the services of the car rental system and may ask the following questions:

- Does a car rental system assign a driver to its customer?
- Does a car rental system provide roadside assistance to its customer?

### Reservation Cancelation

There will be many duplicate instances in our system. The interviewer expects you to ask questions listed below:

- Can the member be able to cancel a reservation?
- Which member is allowed to request a vehicle reservation cancelation and when?

### Payment Flexibility

One of the car rental system's most significant attributes is its customer payment structure. The payment depends on the vehicle type and time stamp. Therefore, an interviewer would expect you to ask questions listed below:

- How can customers pay at different branch locations and by different methods (cash, credit, or cheque)?
- If there are multiple branches of the car rental system, how will the system keep track of the customer having already paid at a particular branch?

## Design Approach

We'll design this car rental system using the bottom-up design approach. For this purpose, we will follow the steps below:

1. **Identify core components:** Start with basic entities like Vehicle, Customer, Reservation, Payment, etc.
2. **Compose subsystems:** Build larger modules for inventory management, reservation management, and payment processing.
3. **Integrate subsystems:** Assemble the complete application, ensuring the design is modular and adheres to SOLID principles.
4. **Document assumptions:** Where requirements are unclear, clearly state and justify your assumptions.

This design approach will address concurrency, edge cases, and follow SOLID principles to ensure scalability and maintainability. Later on, diagrams and code will be used to illustrate major workflows and class structures.

---
# Design Patterns for Car Rental System

## Question

**Which design pattern(s) should be used to design a car rental system? Please elaborate on your choice(s).**

## Answer

### 🚗 Example: Car Rental System — Common Design Patterns Used

Here's how you can confidently mention some patterns in this context 👇

---

## 1. Factory Pattern

Used to create different types of vehicles based on input. Instead of directly instantiating `Car`, `Bike`, or `Truck`, you use a factory method.

```java
class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type) {
        switch (type) {
            case CAR: return new Car();
            case BIKE: return new Bike();
            case TRUCK: return new Truck();
            default: throw new IllegalArgumentException("Invalid type");
        }
    }
}
```

**🗣 In interview:**
> "I used the Factory pattern to create different types of Vehicles dynamically, depending on what the customer books."

---

## 2. Singleton Pattern

Used for shared resources — e.g., `PaymentService`, `DatabaseConnection`, or `BookingManager` — where only one instance should exist across the system.

```java
class PaymentService {
    private static PaymentService instance;
    private PaymentService() {}
    public static synchronized PaymentService getInstance() {
        if (instance == null) instance = new PaymentService();
        return instance;
    }
}
```

**🗣 In interview:**
> "I used the Singleton pattern for services like PaymentService to ensure only one global instance handles all transactions."

---

## 3. Strategy Pattern

Used when you have multiple ways to calculate payment (hourly, daily, per km, etc.) or different payment methods (credit card, UPI, wallet, etc.).

```java
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardPayment implements PaymentStrategy { ... }
class CashPayment implements PaymentStrategy { ... }
class UpiPayment implements PaymentStrategy { ... }
```

**🗣 In interview:**
> "I used the Strategy pattern for flexible payment methods, so the system can easily switch between Cash, Card, or UPI without modifying core logic."

---

## 4. Observer Pattern

Useful for notifications — e.g., when booking status changes, notify the customer via email/SMS.

```java
interface Observer {
    void update(String message);
}

class Customer implements Observer { ... }

class BookingNotifier {
    private List<Observer> observers = new ArrayList<>();
    void notifyAllObservers(String message) { ... }
}
```

**🗣 In interview:**
> "I used the Observer pattern to send real-time notifications to customers when booking status changes or payments are confirmed."

---

## 5. Decorator Pattern (optional but good to mention)

If you allow add-ons like GPS, child seat, or insurance, you can use a Decorator to dynamically add features to the car rental.

```java
interface CarRental {
    double cost();
}

class BasicRental implements CarRental {
    public double cost() { return 1000; }
}

class GpsDecorator implements CarRental {
    private CarRental rental;
    public GpsDecorator(CarRental rental) { this.rental = rental; }
    public double cost() { return rental.cost() + 200; }
}
```

**🗣 In interview:**
> "I used the Decorator pattern for optional add-ons like GPS or insurance, allowing flexible pricing without changing core classes."
