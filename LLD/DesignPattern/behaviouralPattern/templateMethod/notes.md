# Template Design Pattern

Defining the skeleton of an algorithm, allowing certain steps to be implemented by subclasses while maintaining the overall structure.

## Key Components:
- **Abstract Template**: Declares the structure of the algorithm with placeholder methods.
- **Concrete Templates**: Implement specific versions of the algorithm by filling in the placeholder methods.
- **Hook Methods**: Optional methods that subclasses can override (but don't have to).

---

## Advantages:
- **Code Reuse**: Promotes reuse of common algorithm structure.
- **Consistency**: Enforces uniform algorithm execution.
- **Extensibility**: Customizable steps without altering structure.
- **Control**: Parent class controls the overall flow.
- **Reduced Duplication**: Eliminates repetitive code patterns.

## Disadvantages:
- **Rigidity**: Algorithm structure becomes fixed.
- **Complexity**: Many hook methods can complicate subclassing.
- **Inheritance Limitations**: Tied to class inheritance model.
- **Overhead**: May create deep inheritance hierarchies.

---

## Common Use Cases:
- **Document Generation**:
  - Reports with fixed structure/variable content
  - PDF/HTML document templates
- **Web Development**:
  - Page rendering with common layout
  - Email template systems
- **Data Processing**:
  - ETL pipelines with customizable steps
  - Standardized data validation flows
- **Software Testing**:
  - Test case templates
  - Standardized test fixtures
- **Game Development**:
  - Character behavior templates
  - Level generation algorithms

---

## Implementation Guidelines:
1. **Template Method**: Should be final to prevent overriding
2. **Abstract Methods**: For mandatory implementations
3. **Hook Methods**: For optional customizations
4. **Access Control**: Use protected visibility for template methods
5. **Documentation**: Clearly document expected behavior of each step