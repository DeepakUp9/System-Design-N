# SOLID Principles

## Single Responsibility Principle (SRP)

- A class should have only one reason to change, meaning it should have a single responsibility or job.
- It is only a guideline for designing.
- Single Responsibility can be subjective and dependent on the specific context of application.
- **Example**: Order Validation can be either:
  - A single class's responsibility, or
  - Split into multiple classes to validate user inputs, generate error messages, and check permissions.

## Open-Closed Principle (OCP)

- Software entities (classes, modules, functions) should be:
  - **Open for extension** but 
  - **Closed for modification**
- Allows new functionality to be added without altering existing code.
- Once code is written and tested, it shouldn't need modification to add new features.
- Code should be extensible.
- **Example**: A Logger with Info and Error Levels should allow adding a Debug Level seamlessly.

## Liskov Substitution Principle (LSP)

- Subtypes (derived classes) should be substitutable for their base types (parent classes) without affecting program correctness.
- **Example**: We should be able to replace an `Employee` object with a `Manager` or `Intern` object seamlessly.

## Interface Segregation Principle (ISP)

- Clients should not be forced to depend on interfaces they don't use.
- Aim for smaller, more specific interfaces rather than large, general ones.
- **Example**: In a food delivery app (Swiggy):
  - A Customer shouldn't be forced to implement KYC functionality
  - KYC should only be required for Delivery Partners and Restaurant Owners

## Dependency Inversion Principle (DIP)

1. **High-level modules should not depend on low-level modules**  
   Both should depend on abstractions.

2. **Abstractions should not depend on details**  
   Details should depend on abstractions.

- **Example**: 
  - High-level order processing modules should depend on an abstract `PaymentGateway` interface
  - Not on specific implementations like `PayPalService` or `CreditCardProcessor`


## Other Important Software Design Principles------
  ### KISS (Keep It Simple, Stupid)
   - **Definition**: Systems work best when they are kept simple rather than made complex.
   - **Key Aspects**:
     - Avoid unnecessary complexity
     - Prefer simple, straightforward solutions
     - Write code that's easy to understand and maintain
   - **Example**:
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

  ### YAGNI - You Aren't Gonna Need It
    **What It Means**
       - Only write code you **actually need right now**, not what you *might* need later.
     **Why It Matters**
      ✅ Keeps code simple  
      ✅ Saves time  
      ✅ Easier to change  
      ✅ Less bugs  
    - **Example**:
      ❌ Don't do (implementing unneeded features):
      ```java
      // ShoppingCart.java
      public class ShoppingCart {
          // Unnecessary future features
          public void applyDiscount() {} // Not currently needed
          public void giftWrap() {}     // Not currently needed
      }
      ✅ Do this instead (current needs only):
      // ShoppingCart.java
      public class ShoppingCart {
          // Only implemented required features
          public void addItem() {}    // Currently needed
          public void checkout() {}   // Currently needed
      }
  
### DRY - Don't Repeat Yourself
   **What It Means**
    **Avoid duplication** - Every piece of knowledge should have a **single, unambiguous representation** in your system.
   **Why It Matters**
      ✅ Easier maintenance (fix in one place)  
      ✅ Fewer bugs  
      ✅ More consistent behavior  
      ✅ Cleaner code  

  - **Example**:
      ❌ Duplicate code:
      ```python
      # utils.py
      def calculate_tax(amount):
          return amount * 0.20  # 20% VAT

      # checkout.py
      def process_order(total):
          tax = total * 0.20  # Same logic repeated!
          return total + tax

 




