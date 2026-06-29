## **when to use abstract classs and when to use interface**

### Abstract Class vs Interface: When to Use Each

#### Key Differences

| Feature                | Abstract Class                      | Interface                     |
|------------------------|-------------------------------------|-------------------------------|
| **Instantiation**      | Cannot be instantiated directly     | Cannot be instantiated        |
| **Method Types**       | Can have both abstract and concrete methods | Only abstract methods (before Java 8) |
| **Fields**             | Can have instance variables         | Only constants (static final) |
| **Constructors**       | Can have constructors               | No constructors               |
| **Access Modifiers**   | Methods can have any modifier       | Methods are public by default |
| **Multiple Inheritance** | Single inheritance only           | Multiple implementations possible |
| **Default Methods**    | N/A                                 | Supported (Java 8+)           |
| **Static Methods**     | Supported                           | Supported (Java 8+)           |
| **Private Methods**    | Supported                           | Supported (Java 9+)           |

## When to Use Abstract Class

✅ **Shared Common Code**
- When you need to provide common method implementations to subclasses
- Example: Base `Vehicle` class with common `startEngine()` implementation

✅ **State Maintenance** 
- When you need to maintain object state through instance variables
- Example: `BankAccount` base class with balance field

✅ **Template Method Pattern**
- When you need to define a skeleton algorithm with customizable steps
- Example: `ReportGenerator` with fixed structure but customizable formatting

✅ **Controlled Evolution**
- When base class needs to evolve without breaking existing subclasses
- Example: Adding new concrete methods to abstract base class

✅ **Non-public Contracts**
- When you need protected/package-private methods for subclasses
- Example: Framework base classes with internal helper methods

## When to Use Interface

✅ **Multiple Behavior Contracts**
- When unrelated classes need to share common behavior
- Example: `Serializable`, `Comparable` interfaces

✅ **API Definition**
- When defining contracts for external implementations
- Example: `DataSource` interface for different database drivers

✅ **Loose Coupling**
- When you want to decouple implementation from definition
- Example: Service interfaces in dependency injection

✅ **Future Extensibility**
- When you anticipate adding more implementations later
- Example: `PaymentProcessor` interface for new payment methods

✅ **Functional Programming**
- When defining single-method contracts (Java 8+)
- Example: `Predicate`, `Function` functional interfaces

## Java-Specific Considerations

**Java 8+ Features:**
- Interfaces can have `default` and `static` methods
- Interfaces can have `private` methods (Java 9+)
- Abstract classes still necessary for:
  - Instance fields
  - Constructors
  - Non-public methods
  - Shared state management

## Decision Flowchart

1. **Need to share code?** → Abstract Class
2. **Need multiple inheritance?** → Interface
3. **Need to maintain state?** → Abstract Class  
4. **Defining behavior contract?** → Interface
5. **Creating API boundaries?** → Interface
6. **Building class hierarchy?** → Abstract Class

## Best Practices

- **Prefer interfaces** for type definitions and APIs
- Use **abstract classes** when you need to share code
- **Combine both** when needed:
  ```java
  abstract class Animal implements LivingBeing, Movable {
      // Shared code here
  }