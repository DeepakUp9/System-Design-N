# Object-Oriented Programming (OOP) Concepts

These fundamental OOP concepts enable developers to create modular, organized, maintainable, reusable and extensible software systems:

## Core Concepts

- **Classes**:  
  Classes are blueprints for creating objects. They define the structure and behavior of objects by specifying attributes (data) and methods (functions) that instances of the class will possess.

- **Objects**:  
  Objects are instances of classes. They represent real-world entities and contain data (attributes) and methods (functions) that operate on that data.

  Example: Order is class

- **Encapsulation**:  
  Encapsulation is the concept of bundling data (attributes) and the methods (functions) that operate on that data into a single unit, called a class. It restricts direct access to an object's internal state and enforces access through defined methods, thus hiding implementation details.

- **Abstraction**:  
  Abstraction is the process of simplifying complex reality by modeling classes based on their essential properties and behaviors while ignoring non-essential details. It allows developers to focus on what an object does rather than how it does it.

- **Inheritance**:  
  Inheritance allows a new class (subclass/derived class) to inherit attributes and methods from an existing class (superclass/base class). It promotes code reuse and establishes a hierarchical relationship between classes.

- **Polymorphism**:  
  - **Compile-time Polymorphism**: Also known as method overloading, it involves defining multiple methods with the same name in a class, but with different parameters or types. The correct method is determined at compile time based on the method signature.  
  - **Runtime Polymorphism**: Also known as method overriding, it occurs when a subclass provides a specific implementation for a method that is already defined in its superclass. The method to execute is determined at runtime based on the object's actual class.

## Abstract Classes and Interfaces

### Abstract Classes

- Serve as a blueprint or template for other classes, known as its subclasses or derived classes
- Cannot be instantiated directly; they exist solely to be inherited from by other classes
- Can include both method declarations (signatures) and method implementations
- Subclasses that inherit from an abstract class are required to implement the abstract methods declared in the base class

### Interfaces

- Focus on defining a contract specifying a set of method signatures that a class must adhere to
- Do not contain any method implementations; they solely declare the methods that implementing classes must provide
- Primary purpose is to establish a common set of behaviors that multiple, often unrelated, classes can adhere to
- Promotes a high degree of flexibility and polymorphism in the codebase, as classes from different hierarchies can implement the same interface


