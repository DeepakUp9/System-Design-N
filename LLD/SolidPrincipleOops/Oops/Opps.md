# Object-Oriented Programming (OOP) Concepts

OOP concepts enable developers to create modular, organized, maintainable, reusable, and extensible software systems. Below is the complete picture — core concepts, abstract classes vs interfaces, and the extra pillars that usually come up in senior-level discussions (association/aggregation/composition, SOLID).

---

## 1. Class

A class is a blueprint for creating objects. It defines the structure (attributes) and behavior (methods) that instances of the class will possess.

```java
class Order {
    private String orderId;
    private double amount;

    public Order(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
```

## 2. Object

An object is an instance of a class. It represents a real-world entity and holds actual data.

```java
Order order1 = new Order("ORD-101", 499.99);
```

`Order` is the class; `order1` is the object.

---

## 3. Encapsulation

Bundling data and the methods that operate on it into a single unit (a class), while restricting direct access to internal state. Access happens only through defined methods (getters/setters, or more meaningful domain methods), which hides implementation details and protects invariants.

```java
class BankAccount {
    private double balance; // hidden from outside

    public void deposit(double amount) {
        if (amount > 0) balance += amount;
    }

    public double getBalance() {
        return balance;
    }
}
```

Nobody can do `account.balance = -500;` — the class controls how its state changes.

## 4. Abstraction

Simplifying complex reality by exposing only essential properties/behaviors and hiding non-essential details. Focus on **what** an object does, not **how** it does it.

```java
interface PaymentProcessor {
    void pay(double amount);
}
```

A caller just calls `pay()` — whether it's UPI, card, or wallet internally is irrelevant to them.

> **Encapsulation vs Abstraction (common interview trap):** Encapsulation is about *hiding data/state* via access control. Abstraction is about *hiding implementation complexity* via simplified interfaces. Encapsulation is a technique; abstraction is a design goal.

## 5. Inheritance

A subclass (derived class) inherits attributes and methods from a superclass (base class), promoting code reuse and a hierarchical relationship.

```java
class Vehicle {
    void start() { System.out.println("Vehicle starting"); }
}

class Car extends Vehicle {
    void honk() { System.out.println("Beep beep"); }
}
```

`Car` gets `start()` for free and adds its own behavior.

## 6. Polymorphism

**"Many forms"** — the same interface behaves differently depending on context.

### Compile-time Polymorphism (Method Overloading)
Multiple methods, same name, different parameters. Resolved at compile time.

```java
class Calculator {
    int add(int a, int b) { return a + b; }
    double add(double a, double b) { return a + b; }
}
```

### Runtime Polymorphism (Method Overriding)
A subclass provides its own implementation of a method already defined in its superclass. Resolved at runtime based on the actual object type.

```java
class Vehicle {
    void start() { System.out.println("Generic start"); }
}

class ElectricCar extends Vehicle {
    @Override
    void start() { System.out.println("Silent electric start"); }
}

Vehicle v = new ElectricCar();
v.start(); // "Silent electric start" — decided at runtime
```

---

## Abstract Classes vs Interfaces

### Abstract Classes
- A blueprint/template for subclasses
- Cannot be instantiated directly
- Can mix method declarations **and** actual implementations
- Subclasses must implement the abstract methods

```java
abstract class Shape {
    abstract double area();          // must be implemented by subclass

    void describe() {                 // shared implementation
        System.out.println("This shape has area: " + area());
    }
}

class Circle extends Shape {
    double radius;
    Circle(double radius) { this.radius = radius; }

    double area() { return Math.PI * radius * radius; }
}
```

### Interfaces
- A pure contract — a set of method signatures a class must adhere to
- No implementation (until `default` methods, which are the exception, not the norm)
- Lets unrelated classes share common behavior
- A class can implement multiple interfaces (Java doesn't allow multiple class inheritance, but does allow this)

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

class Duck implements Flyable, Swimmable {
    public void fly()  { System.out.println("Duck flying"); }
    public void swim() { System.out.println("Duck swimming"); }
}
```

| | Abstract Class | Interface |
|---|---|---|
| Instantiable | No | No |
| Implementation | Can have some | None (except `default`) |
| Multiple inheritance | Not supported (single) | A class can implement many |
| Fields | Can have instance state | Only `public static final` constants |
| Use when | Sharing common code among closely related classes | Defining a capability/contract across unrelated classes |

---

## Bonus: Relationships Between Objects (often asked alongside inheritance)

- **Association** — a general relationship where objects know about each other (e.g., a `Teacher` and a `Student`).
- **Aggregation** — a "has-a" relationship where the child can exist independently of the parent (e.g., a `Department` has `Employees`, but employees still exist if the department is dissolved).
- **Composition** — a stronger "has-a" relationship where the child's lifecycle depends on the parent (e.g., a `House` has `Rooms`; destroy the house, the rooms are gone too).

```java
class Engine {}                     // Composition: Engine can't meaningfully exist without a Car
class Car {
    private final Engine engine = new Engine();
}
```

---

## Quick Recap

| Concept | One-liner |
|---|---|
| Class | Blueprint |
| Object | Instance of a class |
| Encapsulation | Hide state, expose behavior |
| Abstraction | Hide complexity, expose essentials |
| Inheritance | Reuse via "is-a" |
| Polymorphism | One interface, many forms |
| Abstract Class | Partial blueprint, single inheritance |
| Interface | Pure contract, multiple implementation |