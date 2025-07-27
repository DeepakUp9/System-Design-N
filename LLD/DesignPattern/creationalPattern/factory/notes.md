# Factory Design Patterns

## Simple Factory Pattern

### When to Use
- When you want to create objects without exposing instantiation logic to clients
- When you have multiple classes sharing a common interface/base class that need conditional instantiation

### Key Characteristics
- Central factory class with **static** creation method
- Client interacts only with factory, unaware of concrete types
- All creation logic centralized in one place

### Components
| Component          | Description                                                                 |
|--------------------|-----------------------------------------------------------------------------|
| Creator (Factory)  | Contains static factory method for object creation                          |
| Concrete Products  | Classes implementing common interface/extending common base class           |

### Benefits
✔ Centralizes object creation logic (reduces duplication)  
✔ Encapsulates creation details (improves maintainability)  
✔ Provides single entry point for object creation  

---

## Factory Method Pattern

### When to Use
- When object creation responsibility should be delegated to subclasses
- When working with families of related classes through common interfaces

### Key Characteristics
- **Abstract** creator class/interface declares factory method  
- Subclasses implement factory method for specific object creation  
- Follows "open-closed" principle (extensible without modification)  

### Components
| Component          | Description                                                                 |
|--------------------|-----------------------------------------------------------------------------|
| Creator            | Declares factory method returning interface/base class object               |
| Concrete Creators  | Subclasses implementing factory method for specific products                |
| Products           | Classes implementing common interface/extending base class                  |

### Benefits
✔ Supports extensibility (add new products without modifying code)  
✔ Promotes loose coupling (clients work with interfaces)  
✔ Enables subclass-specific object creation  

---

## Abstract Factory Pattern

### When to Use
- When creating **families of related/dependent objects**  
- For complex systems with multiple interrelated components  

### Key Characteristics
- Multiple abstract factories (one per product family)  
- Concrete factories produce compatible object families  
- Objects created are guaranteed to work together  

### Components
| Component          | Description                                                                 |
|--------------------|-----------------------------------------------------------------------------|
| Abstract Factory   | Declares factory methods for product family creation                        |
| Concrete Factory   | Implements abstract factory for specific product family                     |
| Products           | Related objects implementing common interfaces within a family              |

### Benefits
✔ Ensures created objects are compatible and consistent  
✔ Simplifies complex system construction  
✔ Scalable - easy to add new product families  

---

## Pattern Comparison

| Aspect              | Simple Factory         | Factory Method          | Abstract Factory            |
|---------------------|------------------------|-------------------------|-----------------------------|
| Creation Logic      | Centralized static     | Delegated to subclasses | Per product family          |
| Complexity          | Low                    | Medium                  | High                        |
| Use Case            | Simple conditional     | Single product variants | Related product families    |
| Extensibility       | Limited                | High                    | High (for families)         |
| Client Knowledge    | Knows factory          | Knows creator interface | Knows abstract factory      |