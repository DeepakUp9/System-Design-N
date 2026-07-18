# UML Class Diagram Relationships

## Core Elements

**Interface**:  
`<<interface>>`  
`Name`  
`method1()`  

- Classes implement interfaces (denoted by Generalization arrow)

**Class**:  
`ClassName`  
`property_name: type`  
`method(): type`  

- Every class can have properties and methods
- Abstract classes are identified by italicized names

---

## Relationship Types

### Generalization (Implementation)
`A ---▻ B` (dashed line with hollow arrow)  
- **A implements B** (interface/abstract class)
- Class A must fulfill the contract specified by B

### Inheritance (Is-A)
`A ——▻ B` (solid line with hollow arrow)  
- **A inherits from B**
- A "is-a" B
- Subclass A gets all properties/methods of superclass B
- Example : (BuyOrder, SellOrder) ——▻ Order(Zerodha-app), Driver ——▻ User(Uber), King ——▻ Piece(Chess)

### Interface Usage
`A ╌╌> B` (dashed line with open arrow)  
- **A uses interface B**
- Class A depends on interface B's methods

### Association
`A —— B` (solid line)  
- **A and B call each other**
- Bidirectional relationship
- Example: Driver and customer 

### Uni-directional Association
`A ——> B` (solid line with open arrow)  
- **A can call B**, but not vice versa
- One-way relationship
- Example: User can call Payment gateway, User and reviews

### Aggregation (Has-A)
`A ◇—— B` (solid line with hollow diamond)  
- **A "has-an" instance of B**
- B can exist without A (weaker relationship)
- Example: 1. Department has Employees, but Employees exist without Department  
           2. Driver and ride because ride have belongs to both driver and customer  
           3. car and driver  

### Composition (Strong Has-A or part-of)
`A ◆—— B` (solid line with filled diamond)  
- **A "has-an" instance of B**
- B **cannot** exist without A (strong relationship)
- Example: House has Rooms (rooms don't exist without house), person has heart

---

### 1. Aggregation (A $\diamond$—— B)

In this setup, Class A **has a** reference to Class B, but Class B is created outside of Class A. If Class A is destroyed, Class B still lives.

```java
// Class B (The independent object)
class B {
    private String name;

    public B(String name) {
        this.name = name;
    }

    public void display() {
        System.out.println("Hello from Class B instance: " + name);
    }
}

// Class A (The container - Aggregation)
class A {
    private B bInstance; // Has-A relationship

    // Class B is passed in from the outside (weaker relationship)
    public A(B bInstance) {
        this.bInstance = bInstance;
    }

    public void performAction() {
        if (bInstance != null) {
            bInstance.display();
        }
    }
}

```

---

### 2. Composition (A $\blacklozenge$—— B)

In this setup, Class A **owns** Class B entirely. Class B is instantiated *inside* Class A's constructor. If Class A is destroyed, Class B goes down with it.

```java
// Class B (The dependent part)
class B {
    private String details;

    public B(String details) {
        this.details = details;
    }

    public void showDetails() {
        System.out.println("Class B Part: " + details);
    }
}

// Class A (The container - Composition)
class A {
    private B bInstance; // Strong Part-Of relationship

    public A(String detailsForB) {
        // Class B is created internally and managed strictly by Class A
        this.bInstance = new B(detailsForB);
    }

    public void operate() {
        bInstance.showDetails();
    }
}

```
---

## Summary of Relationships

| Relationship        | UML Notation        | Description                                                                 |
|---------------------|---------------------|-----------------------------------------------------------------------------|
| Generalization      | `╌╌▻` (dashed)      | Class implements interface/abstract class                                   |
| Inheritance         | `——▻` (solid)       | Subclass inherits from superclass ("is-a")                                  |
| Interface Usage     | `╌╌>` (dashed)      | Class depends on interface methods                                          |
| Association         | `——` (solid)        | Classes know about each other (bidirectional)                               |
| Uni-directional Ass.| `——>` (solid)       | One class knows about another (one-way)                                     |
| Aggregation         | `◇——` (hollow diam)| "Has-a" relationship where child can exist independently                     |
| Composition         | `◆——` (filled diam)| Strong "has-a" where child cannot exist without parent                       |




(1) ◆─────→ (1)    = One-to-One
(1) ◆─────→ (*)    = One-to-Many  
(1) ◆─────→ (0..1) = One-to-Zero-or-One
(1) ◆─────→ (2)    = One-to-Exactly-Two (our case)


