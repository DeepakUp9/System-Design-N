# SOLID Principles & Core Design Principles

SOLID is a set of five design principles that make object-oriented code easier to maintain, extend, and test. Below is the complete picture, plus three companion principles (KISS, YAGNI, DRY) that come up in the same conversation.

---

## S — Single Responsibility Principle (SRP)

A class should have only **one reason to change** — i.e., a single responsibility.

- It's a guideline, not a strict law — what counts as "one responsibility" is often subjective and depends on context.
- **Example**: Order validation can legitimately be:
  - One class's job if the validation logic is simple, or
  - Split across multiple classes (input validation, error-message generation, permission checks) if each piece has its own reason to evolve.

```java
// Violates SRP — this class validates AND sends notifications
class OrderValidator {
    boolean validate(Order order) { /* ... */ }
    void sendConfirmationEmail(Order order) { /* ... */ }
}

// Better — each class has exactly one reason to change
class OrderValidator {
    boolean validate(Order order) { /* ... */ }
}

class OrderNotifier {
    void sendConfirmationEmail(Order order) { /* ... */ }
}
```

## O — Open-Closed Principle (OCP)

Software entities (classes, modules, functions) should be **open for extension, but closed for modification** — once code is written and tested, you shouldn't have to touch it to add new behavior.

- **Example**: A `Logger` supporting `Info` and `Error` levels should let you add a `Debug` level without editing existing logging code.

```java
interface LogHandler {
    void log(String message);
}

class InfoLogHandler implements LogHandler {
    public void log(String message) { System.out.println("[INFO] " + message); }
}

class ErrorLogHandler implements LogHandler {
    public void log(String message) { System.out.println("[ERROR] " + message); }
}

// Adding Debug later = new class, zero changes to existing ones
class DebugLogHandler implements LogHandler {
    public void log(String message) { System.out.println("[DEBUG] " + message); }
}
```

## L — Liskov Substitution Principle (LSP)

Subtypes should be substitutable for their base types **without breaking program correctness**.

- **Example**: You should be able to swap an `Employee` object for a `Manager` or `Intern` object and the program should keep working correctly.

```java
class Employee {
    double calculateSalary() { return 50000; }
}

class Manager extends Employee {
    @Override
    double calculateSalary() { return 80000; } // still behaves like an Employee — safe substitution
}

void printSalary(Employee e) {
    System.out.println(e.calculateSalary()); // works correctly for Employee OR Manager
}
```

A classic LSP violation: a `Square extends Rectangle` that overrides `setWidth`/`setHeight` to keep both sides equal — this breaks code that assumes a `Rectangle`'s width and height can be set independently.

## I — Interface Segregation Principle (ISP)

Clients shouldn't be forced to depend on methods they don't use. Prefer several small, specific interfaces over one large, general one.

- **Example**: In a food-delivery app, a `Customer` shouldn't be forced to implement KYC methods — only `DeliveryPartner` and `RestaurantOwner` should need to.

```java
// Bad — fat interface forces Customer to implement irrelevant methods
interface User {
    void placeOrder();
    void submitKyc();
}

// Better — segregated interfaces
interface Orderable {
    void placeOrder();
}

interface KycVerifiable {
    void submitKyc();
}

class Customer implements Orderable { /* only needs placeOrder */ }
class DeliveryPartner implements Orderable, KycVerifiable { /* needs both */ }
```

## D — Dependency Inversion Principle (DIP)

1. High-level modules should not depend on low-level modules — **both should depend on abstractions**.
2. Abstractions should not depend on details — **details should depend on abstractions**.

- **Example**: High-level order-processing logic should depend on an abstract `PaymentGateway` interface, not on concrete implementations like `PayPalService` or `CreditCardProcessor`.

```java
interface PaymentGateway {
    void pay(double amount);
}

class PayPalService implements PaymentGateway {
    public void pay(double amount) { System.out.println("Paid via PayPal: " + amount); }
}

class OrderProcessor {
    private final PaymentGateway gateway; // depends on abstraction, not a concrete class

    OrderProcessor(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    void checkout(double amount) {
        gateway.pay(amount);
    }
}

// Swapping PayPal for Stripe later requires zero changes to OrderProcessor
```

---

## Other Important Design Principles

### KISS — Keep It Simple, Stupid

Systems work best when kept simple rather than made complex.

- Avoid unnecessary complexity
- Prefer straightforward solutions over clever ones
- Write code that's easy to understand and maintain

```java
// Simple is better:
public double calculateArea(double radius) {
    return Math.PI * radius * radius;
}

// Than:
public double calculateArea(double radius) {
    ComplexAreaCalculator calculator = new ComplexAreaCalculator();
    calculator.setPrecision(15);
    return calculator.computeCircularArea(radius);
}
```

### YAGNI — You Aren't Gonna Need It

Only write code you actually need right now, not what you *might* need later.

**Why it matters:** simpler code, saved time, easier to change, fewer bugs.

```java
// ❌ Don't — implementing unneeded features
public class ShoppingCart {
    public void applyDiscount() {} // not currently needed
    public void giftWrap() {}      // not currently needed
}

// ✅ Do — current needs only
public class ShoppingCart {
    public void addItem() {}   // currently needed
    public void checkout() {}  // currently needed
}
```

### DRY — Don't Repeat Yourself

Every piece of knowledge should have a **single, unambiguous representation** in your system.

**Why it matters:** easier maintenance (fix in one place), fewer bugs, consistent behavior, cleaner code.

```python
# ❌ Duplicate logic
# utils.py
def calculate_tax(amount):
    return amount * 0.20  # 20% VAT

# checkout.py
def process_order(total):
    tax = total * 0.20  # same logic repeated!
    return total + tax
```

```python
# ✅ Single source of truth
# utils.py
def calculate_tax(amount):
    return amount * 0.20

# checkout.py
from utils import calculate_tax
def process_order(total):
    return total + calculate_tax(total)
```

---

## Quick Recap

| Principle | One-liner |
|---|---|
| SRP | One class, one reason to change |
| OCP | Extend behavior without modifying existing code |
| LSP | Subtypes must behave like their base type |
| ISP | Small, focused interfaces over fat ones |
| DIP | Depend on abstractions, not concrete implementations |
| KISS | Simplest solution that works |
| YAGNI | Don't build for hypothetical future needs |
| DRY | One source of truth per piece of knowledge |

---
---
---

# SOLID Principles — Short Notes

**SOLID** — 5 OOD principles by Robert C. Martin ("Uncle Bob").

**Without SOLID:** tight coupling, untestable code, duplication, one fix causes more bugs.
**With SOLID:** loose coupling, less complexity, extensible/maintainable code, modular & testable.

| Letter | Principle | One-liner |
|---|---|---|
| **S** | Single Responsibility | A class should have only one reason to change |
| **O** | Open/Closed | Open for extension, closed for modification |
| **L** | Liskov Substitution | Subclass objects must be substitutable for superclass objects |
| **I** | Interface Segregation | Prefer small, client-specific interfaces over fat ones |
| **D** | Dependency Inversion | Depend on abstractions, not concretions |

---

## S — Single Responsibility Principle (SRP)

"A class should have only one reason to change."

**Example:** `Invoice` class doing price calc + printing + DB storage → violates SRP (3 reasons to change).

**Fix:** Split into `Invoice`, `InvoicePrinter`, `InvoiceStorage` — each with one job.

✅ Smaller, self-explanatory classes → easier maintenance & reuse.

---

## O — Open/Closed Principle (OCP)

"Open for extension, closed for modification." (Bertrand Meyer, 1988)

**Example:** A `volume(Shape)` function with `if/else` per shape type → adding a new shape means editing the function (violation).

**Fix:** Make `Shape` an abstract class with its own `volume()`; `Cuboid`, `Cylinder`, `Cone` extend it and implement their own. `VolumeCalculator` just sums via `sumVolume()` — no changes needed when a new shape is added.

> Achieved via inheritance/interfaces — hence also called **polymorphic OCP**.

---

## L — Liskov Substitution Principle (LSP)

"Subclass objects should be replaceable for superclass objects without breaking the system."

**Violation example:** `Vehicle` has `startEngine()`. `Car extends Vehicle` — fine. But `Bicycle extends Vehicle` — a bicycle has no engine, so overriding `startEngine()` breaks the contract.

**Fix:** Split into `Motorized extends Vehicle` (has `startEngine()`) and `Manual extends Vehicle` (no engine). `Car → Motorized`, `Bicycle → Manual`.

✅ Avoids forcing unrelated behavior into subclasses; keeps hierarchy substitutable.

---

## I — Interface Segregation Principle (ISP)

"No fat interfaces — many small, client-specific ones."

**Violation example:** `Shape` interface has `area()` + `volume()`. `Square`/`Rectangle` (2D) are forced to implement `volume()`, which makes no sense for them.

**Fix:** Split into `TwoDimensionalShape` (`area()`) and `ThreeDimensionalShape` (`area()` + `volume()`). 2D shapes implement only what applies to them.

> Most commonly violated SOLID principle in practice.

---

## D — Dependency Inversion Principle (DIP)

"High-level modules shouldn't depend on low-level modules — both should depend on abstractions."

**Violation example:** `Headmaster` directly knows concrete `Teacher`, `Assistant`, `Helper` classes. Adding a new faculty type (e.g. `Secretary`) forces changes in `Headmaster`.

**Fix:** Introduce abstract `Faculty` parent class. `Headmaster` depends on `Faculty`, not concrete subclasses. New faculty types (e.g. `Secretary extends Faculty`) plug in with zero changes to `Headmaster`.

✅ Decouples modules → flexible, stable, reusable design.

---
