# SOLID Design Principles

> **Study Notes for Object-Oriented Design**

---

## 📖 Definition

**SOLID** is a set of 5 object-oriented design principles that help in writing clean, maintainable, and flexible code.

**Origin:**
- Proposed by **Robert C. Martin** (Uncle Bob)
- Used in **Object-Oriented Design (OOD)**

---

## 🎯 Why Use SOLID Principles?

### ❌ Without SOLID:
- Code becomes tightly coupled
- Difficult to add new features
- Hard to test
- Lots of duplication
- Fixing one bug creates others

### ✅ With SOLID:
- Loose coupling
- Easy to extend
- Highly testable
- Clean and maintainable code
- Modular and readable design

---

## 🔤 SOLID = 5 Principles

| Letter | Principle |
|--------|-----------|
| **S** | Single Responsibility Principle |
| **O** | Open/Closed Principle |
| **L** | Liskov Substitution Principle |
| **I** | Interface Segregation Principle |
| **D** | Dependency Inversion Principle |

---

## 1️⃣ S — Single Responsibility Principle (SRP)

### Definition
**A class should have only one responsibility.**

### Key Points
- One class → one job
- One reason to change

### Example (Bad) ❌
```java
class User {
    void saveToDatabase() { }
    void sendEmail() { }
    void generateReport() { }
}
// Too many responsibilities!
```

### Example (Good) ✅
```java
class User {
    // Only user data
}

class UserRepository {
    void saveToDatabase() { }
}

class EmailService {
    void sendEmail() { }
}

class ReportGenerator {
    void generateReport() { }
}
```

### Benefits
- ✅ Easy to understand
- ✅ Easy to maintain
- ✅ Easy to test
- ✅ Reduces coupling

---

## 2️⃣ O — Open/Closed Principle (OCP)

### Definition
**Classes should be open for extension but closed for modification.**

### Key Points
- Add new behavior without changing existing code
- Achieved using **inheritance** and **interfaces**

### Example (Bad) ❌
```java
class PaymentProcessor {
    void processPayment(String type) {
        if (type.equals("CreditCard")) {
            // credit card logic
        } else if (type.equals("PayPal")) {
            // PayPal logic
        }
        // Need to modify for new payment types
    }
}
```

### Example (Good) ✅
```java
interface Payment {
    void pay();
}

class CreditCardPayment implements Payment {
    void pay() { /* credit card logic */ }
}

class PayPalPayment implements Payment {
    void pay() { /* PayPal logic */ }
}

// Add new payment without modifying existing code
class BitcoinPayment implements Payment {
    void pay() { /* Bitcoin logic */ }
}
```

### Benefits
- ✅ Extensible
- ✅ Reduces risk of breaking existing code
- ✅ Easier to add new features

---

## 3️⃣ L — Liskov Substitution Principle (LSP)

### Definition
**Subclass objects should replace parent objects without breaking behavior.**

### Key Points
- Child class must behave like parent
- No unexpected behavior
- **"If it looks like a duck, quacks like a duck, but needs batteries — you have the wrong abstraction"**

### Example (Bad) ❌
```java
class Bird {
    void fly() { }
}

class Penguin extends Bird {
    void fly() {
        throw new Exception("Can't fly!");
        // Violates LSP - unexpected behavior
    }
}
```

### Example (Good) ✅
```java
class Bird {
    void eat() { }
}

class FlyingBird extends Bird {
    void fly() { }
}

class Penguin extends Bird {
    void swim() { }
    // No unexpected behavior
}

class Sparrow extends FlyingBird {
    void fly() { /* can fly */ }
}
```

### Benefits
- ✅ Reliable inheritance
- ✅ Predictable behavior
- ✅ Safer polymorphism

---

## 4️⃣ I — Interface Segregation Principle (ISP)

### Definition
**Clients should not be forced to depend on interfaces they don't use.**

### Key Points
- Many small interfaces are better than one big interface
- Interface should be client-specific

### Example (Bad) ❌
```java
interface Worker {
    void work();
    void eat();
    void sleep();
}

class Robot implements Worker {
    void work() { /* works */ }
    void eat() { /* ??? Robot doesn't eat */ }
    void sleep() { /* ??? Robot doesn't sleep */ }
}
```

### Example (Good) ✅
```java
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

interface Sleepable {
    void sleep();
}

class Human implements Workable, Eatable, Sleepable {
    void work() { }
    void eat() { }
    void sleep() { }
}

class Robot implements Workable {
    void work() { }
    // Only implements what it needs
}
```

### Benefits
- ✅ Flexible design
- ✅ No unnecessary dependencies
- ✅ Cleaner implementations

---

## 5️⃣ D — Dependency Inversion Principle (DIP)

### Definition
**Depend on abstractions, not concrete implementations.**

### Key Points
- High-level modules should not depend on low-level modules
- Both should depend on abstractions (interfaces)
- Use interfaces

### Example (Bad) ❌
```java
class MySQLDatabase {
    void save() { }
}

class UserService {
    MySQLDatabase db = new MySQLDatabase();
    // Tightly coupled to MySQL
    
    void saveUser() {
        db.save();
    }
}
```

### Example (Good) ✅
```java
interface Database {
    void save();
}

class MySQLDatabase implements Database {
    void save() { /* MySQL logic */ }
}

class MongoDatabase implements Database {
    void save() { /* MongoDB logic */ }
}

class UserService {
    Database db; // Depends on abstraction
    
    UserService(Database db) {
        this.db = db;
    }
    
    void saveUser() {
        db.save();
        // Can work with any database
    }
}
```

### Benefits
- ✅ Loose coupling
- ✅ Easy to switch implementations
- ✅ Highly testable (mock injection)

---

## 🎯 One-Line Summary

> **SOLID principles help create flexible, scalable, testable, and maintainable object-oriented software.**

---

## 🧠 Memory Trick

| Letter | Remember As |
|--------|-------------|
| **S** | **S**ingle job |
| **O** | **E**xtend, don't modify |
| **L** | Replace parent safe**L**y |
| **I** | Small **I**nterfaces |
| **D** | **D**epend on abstraction |

---

## 📊 SOLID Principles Quick Reference

| Principle | What | How | Benefit |
|-----------|------|-----|---------|
| **SRP** | One responsibility per class | Separate concerns | Easy to maintain |
| **OCP** | Extend, don't modify | Use inheritance/interfaces | Safe to extend |
| **LSP** | Subtypes must be substitutable | Proper inheritance | Reliable polymorphism |
| **ISP** | Small, focused interfaces | Split large interfaces | No forced dependencies |
| **DIP** | Depend on abstractions | Use interfaces | Loose coupling |

---

## 💡 Real-World Examples

### SRP Example: E-commerce Order
```
❌ Bad:
class Order {
    calculateTotal()
    saveToDatabase()
    sendEmail()
    generateInvoice()
}

✅ Good:
class Order { calculateTotal() }
class OrderRepository { save() }
class EmailService { send() }
class InvoiceGenerator { generate() }
```

### OCP Example: Payment System
```
❌ Bad: Modify class for each new payment type
✅ Good: Add new class implementing Payment interface
```

### LSP Example: Shapes
```
❌ Bad: Square extends Rectangle but breaks area calculation
✅ Good: Square and Rectangle extend Shape separately
```

### ISP Example: Printer
```
❌ Bad: All printers implement print, scan, fax
✅ Good: Printer, Scanner, Fax as separate interfaces
```

### DIP Example: Notification Service
```
❌ Bad: NotificationService depends on EmailSender
✅ Good: NotificationService depends on MessageSender interface
```

---

## 🎓 Interview Tips

### Common Questions:

**1. Which SOLID principle prevents code modification?**
- **OCP** (Open/Closed Principle)

**2. Which principle is about interface design?**
- **ISP** (Interface Segregation Principle)

**3. Which principle is violated if subclass breaks parent behavior?**
- **LSP** (Liskov Substitution Principle)

**4. How to achieve loose coupling?**
- **DIP** (Dependency Inversion Principle)

**5. What if a class does too many things?**
- Violates **SRP** (Single Responsibility Principle)

### Red Flags in Code:
- ❌ God classes (too many methods) → Violates **SRP**
- ❌ Lots of `if-else` for types → Violates **OCP**
- ❌ Subclass throws exceptions on parent methods → Violates **LSP**
- ❌ Empty interface implementations → Violates **ISP**
- ❌ Direct instantiation of concrete classes → Violates **DIP**

---

## 🔗 How SOLID Principles Work Together

```
    SRP
     │
     ├─── Clean, focused classes
     │
    OCP
     │
     ├─── Extend via interfaces
     │
    LSP
     │
     ├─── Reliable inheritance
     │
    ISP
     │
     ├─── Focused interfaces
     │
    DIP
     │
     └─── Depend on abstractions

= Maintainable, Testable, Flexible Code
```

---

## ✅ Benefits Summary

| Benefit | Which Principles Help |
|---------|----------------------|
| **Maintainability** | SRP, OCP |
| **Testability** | SRP, DIP |
| **Flexibility** | OCP, DIP |
| **Reliability** | LSP |
| **Clean Design** | All 5 |

---

