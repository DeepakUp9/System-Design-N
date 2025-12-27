# SOLID: Open Closed Principle (OCP)

> **Study Notes for Object-Oriented Design**

---

## 📖 Definition

The **Open Closed Principle** states:

> **"Software entities should be open for extension but closed for modification."**  
> — Bertrand Meyer (1988)

### This means:
- ✅ Add new behavior by adding new code
- ❌ Do not change existing, tested code

---

## 💡 Key Idea of OCP

- Core logic remains **unchanged**
- New features are added using **extension**
- Prevents breaking existing functionality
- Improves reusability and stability

---

## 🎯 Why OCP Is Important

### ❌ Without OCP:
- Adding new features requires modifying old code
- Increases risk of bugs
- Code becomes complex and hard to maintain
- Requires extensive regression testing

### ✅ With OCP:
- Core code stays safe
- Easy to add new features
- Less regression testing
- Cleaner and scalable design
- Reduces coupling

---

## 🔄 OCP and Polymorphism

**OCP is often implemented using:**
- Abstraction
- Inheritance
- Interfaces

👉 **That's why OCP is also called Polymorphic OCP**

---

## ❌ Problem Example (Without OCP)

### Scenario
Alex owns a box-selling business.

**Initially:**
- Only **Cuboid** boxes
- A `volume()` method calculates volume

**Later:**
- **Cone-shaped** boxes are added
- The existing `volume()` method must be modified

### 🚫 Problem:
- Each new box type forces changes in the same method
- Code grows complex with multiple conditions

### Code Example (Bad)

```java
class VolumeCalculator {
    double volume(Object shape) {
        if (shape instanceof Cuboid) {
            Cuboid cuboid = (Cuboid) shape;
            return cuboid.length * cuboid.width * cuboid.height;
        } 
        else if (shape instanceof Cone) {
            Cone cone = (Cone) shape;
            return (1.0/3) * Math.PI * cone.radius * cone.radius * cone.height;
        }
        else if (shape instanceof Cylinder) {
            // More if-else as we add shapes
        }
        // Keeps growing!
        return 0;
    }
}
```

### Problems with This Approach:
- ❌ Violates OCP
- ❌ High coupling
- ❌ Difficult to extend
- ❌ Error-prone logic
- ❌ Must modify existing method for each new shape

---

## ✅ Implementing OCP (Correct Approach)

### Step 1: Create an Abstract Parent Class

```java
abstract class Shape {
    abstract double volume();
}
```

**Visual:**
```
┌─────────────────┐
│  Shape          │
│  (abstract)     │
├─────────────────┤
│ + volume()      │
└─────────────────┘
```

---

### Step 2: Extend Shape for Each Box Type

Each class handles its own logic:

```java
class Cuboid extends Shape {
    private double length;
    private double width;
    private double height;
    
    @Override
    double volume() {
        return length * width * height;
    }
}

class Cone extends Shape {
    private double radius;
    private double height;
    
    @Override
    double volume() {
        return (1.0/3) * Math.PI * radius * radius * height;
    }
}

class Cylinder extends Shape {
    private double radius;
    private double height;
    
    @Override
    double volume() {
        return Math.PI * radius * radius * height;
    }
}
```

---

### Step 3: Create VolumeCalculator

**Responsible only for:**
- Adding volumes of shapes
- Does **not** know shape details

```java
class VolumeCalculator {
    double sumVolume(List<Shape> shapes) {
        double sum = 0;
        for (Shape shape : shapes) {
            sum += shape.volume();
        }
        return sum;
    }
}
```

**Visual:**
```
┌─────────────────────────┐
│  VolumeCalculator       │
├─────────────────────────┤
│ + sumVolume(List<Shape>)│
└─────────────────────────┘
```

---

## 🌟 Benefits of This Design

- ✅ New shapes can be added **without changing existing code**
- ✅ Core logic is **protected**
- ✅ Code is **easier to test**
- ✅ High cohesion and low coupling
- ✅ **Open for extension** (add new shapes)
- ✅ **Closed for modification** (VolumeCalculator stays unchanged)

---

## 📊 Class Diagram (Conceptual)

```
        ┌─────────────────┐
        │  Shape          │
        │  (abstract)     │
        ├─────────────────┤
        │ + volume()      │
        └────────┬────────┘
                 │
                 │ (Inheritance)
        ┌────────┼────────┐
        │        │        │
        ↓        ↓        ↓
   ┌────────┐ ┌──────┐ ┌──────────┐
   │ Cuboid │ │ Cone │ │ Cylinder │
   └────────┘ └──────┘ └──────────┘
        │        │        │
        │        │        │
        └────────┼────────┘
                 │
                 ↓
      ┌──────────────────────┐
      │  VolumeCalculator    │
      │  (uses Shape)        │
      └──────────────────────┘
```

---

## 🎯 Before vs After Comparison

| Aspect | Without OCP ❌ | With OCP ✅ |
|--------|---------------|------------|
| **Adding new shape** | Modify existing method | Create new class |
| **Risk** | High (break existing) | Low (isolated) |
| **Testing** | Re-test everything | Test only new class |
| **Complexity** | Increases with each shape | Stays constant |
| **Coupling** | High | Low |
| **Maintenance** | Difficult | Easy |

---

## 💡 Real-World Examples

### Example 1: Payment System

**❌ Without OCP:**
```java
class PaymentProcessor {
    void process(String type) {
        if (type.equals("CreditCard")) {
            // credit card logic
        } else if (type.equals("PayPal")) {
            // PayPal logic
        } else if (type.equals("Bitcoin")) {
            // Bitcoin logic - MODIFIED CODE!
        }
    }
}
```

**✅ With OCP:**
```java
interface Payment {
    void process();
}

class CreditCardPayment implements Payment {
    void process() { /* logic */ }
}

class PayPalPayment implements Payment {
    void process() { /* logic */ }
}

class BitcoinPayment implements Payment {
    void process() { /* NEW CLASS - NO MODIFICATION */ }
}
```

---

### Example 2: Notification System

**❌ Without OCP:**
```java
class NotificationService {
    void send(String type, String message) {
        if (type.equals("Email")) {
            // email logic
        } else if (type.equals("SMS")) {
            // SMS logic
        }
        // Keep modifying for each new type
    }
}
```

**✅ With OCP:**
```java
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    void send(String message) { /* email logic */ }
}

class SMSNotification implements Notification {
    void send(String message) { /* SMS logic */ }
}

class PushNotification implements Notification {
    void send(String message) { /* NEW - NO MODIFICATION */ }
}
```

---

## 🎓 Interview Tips

### Common Questions:

**1. What does "Open for extension, closed for modification" mean?**
- Open: Can add new functionality
- Closed: Don't change existing code

**2. How do you achieve OCP?**
- Use abstraction (interfaces/abstract classes)
- Use inheritance and polymorphism
- Depend on abstractions, not concrete classes

**3. What's an example of OCP violation?**
- Multiple if-else statements for different types
- Modifying existing methods to add new features

**4. Why is OCP important?**
- Protects tested code from bugs
- Makes system easier to extend
- Reduces regression testing
- Improves maintainability

### Key Phrases to Use:
- ✅ "By using abstraction and polymorphism..."
- ✅ "Each new feature is a new class, not a code modification..."
- ✅ "Core business logic remains stable..."
- ✅ "Follows the principle of extension over modification..."

---

## 🧠 Memory Aid

### Think: **"Plugin Architecture"**

Just like browser plugins:
- Browser (core) doesn't change
- New plugins (extensions) add features
- Each plugin is independent

### OCP Formula:
```
Abstraction + Inheritance = OCP

Extension (New Code) ✅
    ≠
Modification (Old Code) ❌
```

---

## ⚠️ Common Mistakes

### Mistake 1: Using switch/if-else for types
```java
❌ Bad:
switch(type) {
    case "A": // logic A
    case "B": // logic B
    // keeps growing
}
```

### Mistake 2: Concrete dependencies
```java
❌ Bad:
class Service {
    CreditCard card = new CreditCard();
    // tightly coupled
}

✅ Good:
class Service {
    Payment payment; // depends on abstraction
}
```

### Mistake 3: Over-engineering too early
- ⚠️ Don't create abstractions prematurely
- Wait until you have 2-3 similar implementations
- Balance between flexibility and simplicity

---

## 🎯 Conclusion

- OCP ensures systems grow by **extension**, not **modification**
- Core code remains **stable** and **reusable**
- Achieved using **abstraction** and **polymorphism**
- Essential for **scalable** and **maintainable** systems

---

## 📝 One-Line Interview Answer

> **"The Open Closed Principle states that software should be open for extension but closed for modification, ensuring stability and scalability through abstraction and polymorphism."**

---
