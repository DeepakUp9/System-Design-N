# Principles of Object-Oriented Programming (OOP)

> **Core Study Notes for Interviews & Fundamentals**

---

## 📌 Four Core Principles of OOP

1. **Encapsulation**
2. **Abstraction**
3. **Inheritance**
4. **Polymorphism**

---

## 1. Encapsulation 🔒

### Definition
Encapsulation is the process of binding data and methods together inside a class and restricting direct access to the internal data.

### Key Points
- Achieves **data hiding**
- Class acts like a **capsule**
- Variables are usually **private**
- Access is provided via **public methods** (getters/setters)

### Why Encapsulation?
- ✅ Prevents unauthorized access
- ✅ Protects object state
- ✅ Improves maintainability

### Example Concept
```
Private variables → accessed via public methods
```

---

## 2. Data Hiding 🛡️

### Definition
Data hiding means hiding internal implementation details and exposing only what is necessary.

### Goals
- Protect data from misuse
- Reduce dependency between classes
- Improve security

### Components of Data Hiding
1. Encapsulation
2. Abstraction

---

## 3. Abstraction 🎭

### Definition
Abstraction shows **what** an object does, **not how** it does it.

### Key Points
- Hides implementation details
- Focuses on essential features
- Achieved using **interfaces** and **abstract classes**

### Real-Life Examples
- 📺 TV remote volume button
- 🚗 Car accelerator pedal

### Advantages
- ✅ Reduces complexity
- ✅ Improves reusability
- ✅ Makes code easier to maintain
- ✅ Enhances modularity

### Abstraction vs Encapsulation

| Abstraction | Encapsulation |
|-------------|---------------|
| Design-level concept | Implementation-level concept |
| Hides complexity | Hides data |
| Focuses on **what** | Focuses on **how** |
| Uses interfaces/abstract classes | Uses access modifiers & getters/setters |

---

## 4. Inheritance 🧬

### Definition
Inheritance allows a class to reuse properties and methods of another class.

### Relationship
Based on **IS-A** relationship
- Example: Car **IS-A** Vehicle

### Benefits
- ✅ Code reusability
- ✅ Easy extension
- ✅ Reduced duplication
- ✅ Supports data hiding

### Types of Inheritance

#### 1. Single Inheritance
One parent → one child

#### 2. Multi-Level Inheritance
Class → subclass → sub-subclass

#### 3. Hierarchical Inheritance
One parent → multiple children

#### 4. Hybrid Inheritance
Combination of multiple inheritance types

---

## 5. Generalization 🌳

### Definition
Generalization extracts common features from multiple classes into a single parent class.

### Purpose
- Improve reusability
- Reduce duplication
- Enable polymorphism
- Simplify maintenance

### Generalization vs Specialization

| Generalization | Specialization |
|----------------|----------------|
| Common → Parent | Parent → More specific child |

### Example
- **Vehicle** → Car, Truck
- **Bird** → Sparrow, Eagle

### Supports
- ✅ Open/Closed Principle (OCP)

---

## 6. Polymorphism 🎨

### Definition
Polymorphism allows objects to behave differently using the same interface.

**Meaning:** "One method, many forms"

**Example:** `makeNoise()` behaves differently for Lion, Dog, etc.

### Types of Polymorphism

#### 1. Dynamic Polymorphism (Runtime)
- Achieved using **method overriding**
- Decision made at **runtime**
- Same method signature in parent & child

#### 2. Static Polymorphism (Compile-time)
- Achieved using:
  - **Method overloading**
  - **Operator overloading**
- Decision made at **compile time**

---

## 📝 Method Overriding

### Characteristics
- Child class provides its own implementation
- Same method name & parameters
- Used in **runtime polymorphism**

---

## 📝 Method Overloading

### Characteristics
- Same method name
- Different parameters (type or count)
- Used in **compile-time polymorphism**

---

## Dynamic vs Static Polymorphism

| Static | Dynamic |
|--------|---------|
| Compile-time | Runtime |
| Method overloading | Method overriding |
| Faster | Slightly slower |
| Parameters differ | Parameters same |
| Return type can differ | Return type must match |

---

## 🎯 Quick Reference Summary

| Principle | Key Feature | Achieved By |
|-----------|-------------|-------------|
| **Encapsulation** | Data hiding | Access modifiers, getters/setters |
| **Abstraction** | Hide complexity | Interfaces, abstract classes |
| **Inheritance** | Code reuse | IS-A relationship |
| **Polymorphism** | Multiple forms | Overloading, Overriding |

---

## 💡 Interview Tips

### When to mention what:
- **Encapsulation**: When discussing data security and access control
- **Abstraction**: When simplifying complex systems
- **Inheritance**: When reusing code and establishing relationships
- **Polymorphism**: When same interface needs different behaviors

### Common Interview Questions:
1. Difference between abstraction and encapsulation?
2. Types of inheritance?
3. Method overloading vs overriding?
4. Real-world examples of OOP principles?

---

## 📥 Download Instructions

To save these notes:
1. Click the **download button (⬇️)** above
2. Or copy and save as `oop-principles-notes.md`
3. Open in Obsidian, Notion, or any Markdown editor

---

