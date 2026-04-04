# Types of Design Patterns

Design patterns for object-oriented programs are divided into three broad categories — the same categories used by the **Gang of Four (GoF)** in their seminal work on design patterns:

1. **Creational** — *how objects are created*
2. **Structural** — *how classes are composed*
3. **Behavioral** — *how classes and objects interact*

Each category targets a different layer of your design, and together they cover the full lifecycle of objects in a well-architected system.

---

## 🏗️ Creational Patterns

Creational patterns relate to **how objects are constructed from classes**. Simply instantiating objects with `new` may seem harmless, but unthoughtfully littering code with object creations can lead to tight coupling, duplication, and maintenance headaches down the road.

Creational patterns offer powerful strategies to **encapsulate and control the object creation process**, keeping your code flexible and decoupled from concrete implementations.

| Pattern | Purpose |
|---|---|
| **Builder** | Construct complex objects step by step, separating construction from representation |
| **Prototype** | Clone existing objects instead of creating new ones from scratch |
| **Singleton** | Ensure a class has only one instance and provide a global access point to it |
| **Abstract Factory** | Create families of related objects without specifying their concrete classes |

---

## 🧱 Structural Patterns

Structural patterns are concerned with the **composition of classes** — how they are assembled and relate to one another to form larger, more capable structures.

Think of these as the blueprints for how your building blocks fit together, enabling you to build flexible hierarchies without tightly coupling components.

| Pattern | Purpose |
|---|---|
| **Adapter** | Bridge incompatible interfaces so classes can work together |
| **Bridge** | Decouple an abstraction from its implementation so both can evolve independently |
| **Composite** | Compose objects into tree structures to represent part-whole hierarchies |
| **Decorator** | Attach additional responsibilities to an object dynamically, as an alternative to subclassing |
| **Facade** | Provide a simplified interface to a complex subsystem |
| **Flyweight** | Share common state among many fine-grained objects to reduce memory usage |
| **Proxy** | Provide a surrogate or placeholder to control access to another object |

---

## 🤝 Behavioral Patterns

Behavioral patterns dictate **how classes and objects communicate and collaborate**, and how responsibility is delegated among them.

These patterns help you design clean, loosely coupled interactions — making it easier to extend or modify behavior without rewriting entire systems.

| Pattern | Purpose |
|---|---|
| **Interpreter** | Define a grammar and provide an interpreter for a language |
| **Template Method** | Define the skeleton of an algorithm in a base class, deferring some steps to subclasses |
| **Chain of Responsibility** | Pass requests along a chain of handlers until one handles it |
| **Command** | Encapsulate a request as an object, allowing parameterization and queuing |
| **Iterator** | Provide a standard way to traverse a collection without exposing its internals |
| **Mediator** | Define an object that encapsulates how a set of objects interact |
| **Memento** | Capture and restore an object's state without violating encapsulation |
| **Observer** | Define a one-to-many dependency so dependents are notified automatically of state changes |
| **State** | Allow an object to alter its behavior when its internal state changes |
| **Strategy** | Define a family of algorithms, encapsulate each one, and make them interchangeable |
| **Visitor** | Add new operations to objects without modifying their classes |

---

## Quick Reference Summary

```
Design Patterns
│
├── Creational       → HOW objects are made
│   ├── Builder
│   ├── Prototype
│   ├── Singleton
│   └── Abstract Factory
│
├── Structural       → HOW objects are composed
│   ├── Adapter
│   ├── Bridge
│   ├── Composite
│   ├── Decorator
│   ├── Facade
│   ├── Flyweight
│   └── Proxy
│
└── Behavioral       → HOW objects communicate
    ├── Interpreter
    ├── Template Method
    ├── Chain of Responsibility
    ├── Command
    ├── Iterator
    ├── Mediator
    ├── Memento
    ├── Observer
    ├── State
    ├── Strategy
    └── Visitor
```

> **Pro Tip:** You don't need to memorize all 23 patterns at once. Start with the most commonly used ones — **Singleton**, **Builder**, **Observer**, **Strategy**, and **Decorator** — and build from there as you encounter real-world problems they solve.