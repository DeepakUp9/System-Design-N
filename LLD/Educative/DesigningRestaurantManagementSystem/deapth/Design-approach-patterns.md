# LLD Chapter — Design Approach & Design Patterns

---

## Topic: Choosing the Bottom-Up Design Approach

### What?

**Bottom-up design means:**

1. You start by designing the **smallest, atomic building blocks** first
2. Then combine them to form **larger components**
3. Continue this expansion until the **entire system emerges naturally**

In this system, the atomic components are:

- `Table`
- `Seat`
- `Meal`
- `MealItem`
- `SeatingChart`

Then these combine into:

- `Menu`
- `Branch`
- `Restaurant`

Ultimately building:
> **The full Restaurant Management System.**

---

### Why Bottom-Up?

Interviewers love this approach because:

#### 1. It shows deep understanding of core entities

You start from real-world objects → design becomes intuitive and correct.

#### 2. It reduces mistakes

You don't guess the big picture.  
You build it from verified small components.

#### 3. Natural object-oriented modeling

OOP works best when designing bottom-up since:
- Each object gets clear responsibility
- Each object has minimal dependencies
- Larger systems become clean and maintainable

#### 4. Easier to extend

If later the interviewer adds:
- Online order
- Delivery
- Inventory

You can extend small components → **no redesign needed**.

#### 5. Clear interviewer communication

You show a structured, methodical approach.

> **This makes you look senior.**

---

### How (Step-by-Step in Interview Style)

#### 1. Identify the smallest entities

**You think:**  
*"What exists physically inside a restaurant?"*

**Smallest units:**

| Entity | Description |
|--------|-------------|
| **Table** | Collection of seats + ID + status |
| **Seat** | A place for a customer |
| **Meal** | The entire order for a table |
| **MealItem** | Each food item ordered |
| **SeatingChart** | Layout of seats for each table |

> **These become your foundational classes.**

---

#### 2. Compose larger structures

Now combine these into bigger logical modules:

**Menu**  
Collection of MealItems that can be ordered.

**Branch**
- Multiple tables
- Staff
- Inventory (optional)
- Menu
- Kitchen queue
- Payment desk

**Restaurant**
- Multiple branches
- Corporate-level reporting
- Inventory suppliers
- Standardized menu (optional)

---

#### 3. Expand to full system

Finally, you build:

- Reservations
- Ordering Process
- Billing
- Payments
- Kitchen workflow
- Staff management
- Delivery (optional)
- Online orders (optional)

> **Each layer is built on top of previously defined components.**

> **This approach shows strong system-design maturity.**

---

## Topic: Design Patterns for the Restaurant Management System

Mentioning design patterns is a **major plus** in interviews.

Here's what you MUST mention and how to explain them clearly.

---

## What Patterns Apply Here?

### 1. State Pattern ⭐

#### Used for:

- **Order status** (`PLACED → COOKING → READY → SERVED → PAID`)
- **Table status** (`AVAILABLE → RESERVED → OCCUPIED → CLEANING`)

#### Why:

Because states change dynamically and behavior depends on the state.

#### How:

Each state is a class with its own transitions + validations.

```java
interface OrderState {
    void next(Order order);
    void cancel(Order order);
}

class PlacedState implements OrderState {
    public void next(Order order) {
        order.setState(new CookingState());
    }
    
    public void cancel(Order order) {
        order.setState(new CancelledState());
    }
}

class CookingState implements OrderState {
    public void next(Order order) {
        order.setState(new ReadyState());
    }
    
    public void cancel(Order order) {
        throw new IllegalStateException("Cannot cancel while cooking");
    }
}

class Order {
    private OrderState state;
    
    public void moveToNext() {
        state.next(this);
    }
    
    public void cancel() {
        state.cancel(this);
    }
}
```

---

### 2. Strategy Pattern

#### Used for:

- **Payment methods** (Cash, Card, UPI, Wallet)
- **Pricing or discount strategies**
- **Seat allocation strategies** (e.g., smallest fit, nearest table, VIP section)

#### Why:

You want to switch algorithms at runtime without modifying code.

#### How:

Define an interface → provide multiple implementations.

```java
interface PaymentStrategy {
    boolean processPayment(double amount);
}

class CashPayment implements PaymentStrategy {
    public boolean processPayment(double amount) {
        // Process cash payment
        return true;
    }
}

class CardPayment implements PaymentStrategy {
    public boolean processPayment(double amount) {
        // Process card payment via gateway
        return true;
    }
}

class UPIPayment implements PaymentStrategy {
    public boolean processPayment(double amount) {
        // Process UPI payment
        return true;
    }
}

class Bill {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }
    
    public boolean pay(double amount) {
        return paymentStrategy.processPayment(amount);
    }
}
```

---

### 3. Factory Pattern

#### Used for:

- Creating payments
- Creating bills
- Creating branch-specific menu versions
- Generating reservation objects

#### Why:

Object creation varies based on restaurant type / context.

#### How:

Use factories to hide complex initialization logic.

```java
interface PaymentFactory {
    Payment createPayment(PaymentType type, double amount);
}

class RestaurantPaymentFactory implements PaymentFactory {
    public Payment createPayment(PaymentType type, double amount) {
        switch(type) {
            case CASH:
                return new CashPayment(amount);
            case CARD:
                return new CardPayment(amount);
            case UPI:
                return new UPIPayment(amount);
            default:
                throw new IllegalArgumentException("Unknown payment type");
        }
    }
}
```

---

### 4. Observer Pattern

#### Used for:

- **Kitchen notifying waiters** when an order is READY
- **Low-stock inventory alerts**
- **Real-time table availability dashboard**
- **Push notifications** to customers for online orders

#### Why:

Events need to propagate automatically.

#### How:

Chef updates → Waiter UI updates via observers/websockets.

```java
interface OrderObserver {
    void onOrderStatusChange(Order order);
}

class WaiterNotifier implements OrderObserver {
    public void onOrderStatusChange(Order order) {
        if (order.getStatus() == OrderStatus.READY) {
            // Notify waiter
            System.out.println("Order " + order.getId() + " is ready!");
        }
    }
}

class KitchenDisplay implements OrderObserver {
    public void onOrderStatusChange(Order order) {
        // Update kitchen display
    }
}

class Order {
    private List<OrderObserver> observers = new ArrayList<>();
    private OrderStatus status;
    
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.onOrderStatusChange(this);
        }
    }
}
```

---

### 5. Composite Pattern

#### Used for:

- **Menu** → Category → Subcategory → MenuItem
- **Meal** → MealItem
- **Restaurant** → Branches

#### Why:

Allows you to treat groups and items uniformly.

```java
interface MenuComponent {
    void display();
    double getPrice();
}

class MenuItem implements MenuComponent {
    private String name;
    private double price;
    
    public void display() {
        System.out.println(name + ": $" + price);
    }
    
    public double getPrice() {
        return price;
    }
}

class MenuCategory implements MenuComponent {
    private String name;
    private List<MenuComponent> items = new ArrayList<>();
    
    public void add(MenuComponent item) {
        items.add(item);
    }
    
    public void display() {
        System.out.println("Category: " + name);
        for (MenuComponent item : items) {
            item.display();
        }
    }
    
    public double getPrice() {
        return items.stream()
            .mapToDouble(MenuComponent::getPrice)
            .sum();
    }
}
```

---

### 6. Singleton Pattern (Careful Use)

#### Used for:

- Configuration
- Logging
- Restaurant-level constants

#### Why:

Only one instance should exist worldwide.

```java
class RestaurantConfig {
    private static volatile RestaurantConfig instance;
    
    private RestaurantConfig() {
        // Load configuration
    }
    
    public static RestaurantConfig getInstance() {
        if (instance == null) {
            synchronized (RestaurantConfig.class) {
                if (instance == null) {
                    instance = new RestaurantConfig();
                }
            }
        }
        return instance;
    }
}
```

---

## Why Discussing Patterns Helps in an Interview?

### 1. Shows your knowledge of scalable OOP

Patterns prove you can design clean, extensible software.

### 2. Makes you stand out from average candidates

Most candidates only talk about classes → **you talk about architecture**.

### 3. Helps structure your design

LLD becomes **predictable, not chaotic**.

### 4. Proves you understand behavior, not just data

**State pattern alone makes you look senior.**

---

## How to Present These Patterns in the Interview (Pro Tip)

**Say it like this:**

> **"The system has a lot of dynamic behavior, especially in order processing and table management. To handle this cleanly, I'll apply the State Pattern for order and table lifecycle. For payments and pricing variations, the Strategy Pattern helps me plug in new methods easily. The Factory Pattern helps create bills and payments cleanly. And since the kitchen must notify waiters, the Observer Pattern fits naturally."**

> **This will impress them instantly.**

---

## Extra Points You Can Add (Advanced Level)

### 1. Use interfaces for high-level modules

- `IOrderService`
- `ITableService`
- `IBillingService`

**Shows testability.**

---

### 2. Mention SRP and OCP (SOLID principles)

**Interviewers love this.**

| Principle | Application |
|-----------|-------------|
| **SRP** | OrderService handles only order logic |
| **OCP** | New payment methods without modifying existing code |
| **LSP** | All payment types can substitute PaymentStrategy |
| **ISP** | Separate interfaces: IPayable, INotifiable, ITrackable |
| **DIP** | Services depend on abstractions, not concrete classes |

---

### 3. Discuss scalability briefly

- Peak dinner hours require **concurrency management**
- Avoid blocking **kitchen queues**
- Keep order processing **asynchronous**

---

### 4. Mention data consistency

Especially with **reservations and table allocation**.

---

## Design Pattern Mapping

| Pattern | Use Case | Benefit |
|---------|----------|---------|
| **State** | Order/Table lifecycle | Clean state transitions |
| **Strategy** | Payment methods, seat allocation | Runtime algorithm selection |
| **Factory** | Payment/Bill creation | Centralized object creation |
| **Observer** | Kitchen-Waiter notifications | Event-driven updates |
| **Composite** | Menu hierarchy | Uniform interface for tree structure |
| **Singleton** | Configuration | Global consistency |

---

## Bottom-Up Design Flow

```
Level 1: Atomic Components
├── Table
├── Seat
├── Meal
├── MealItem
└── SeatingChart

Level 2: Logical Modules
├── Menu (collection of MealItems)
├── Reservation (table + customer + time)
├── Order (meal + items)
└── Bill (order + payments)

Level 3: Branch Level
├── Branch
│   ├── Tables
│   ├── Staff
│   ├── Menu
│   └── Kitchen

Level 4: Business Logic
├── OrderService
├── TableService
├── ReservationService
├── BillingService
└── PaymentService

Level 5: Full System
└── Restaurant Management System
    ├── Multiple Branches
    ├── Corporate Reporting
    ├── Staff Management
    └── Inventory (optional)
```

---

## Architecture Visualization

```
┌─────────────────────────────────────────────────────┐
│         Restaurant Management System                 │
└─────────────────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
   ┌────▼────┐   ┌────▼────┐   ┌───▼────┐
   │ Table   │   │ Order   │   │ Payment│
   │ Mgmt    │   │ Mgmt    │   │ System │
   └────┬────┘   └────┬────┘   └───┬────┘
        │             │             │
   State Pattern  Observer     Strategy
        │          Pattern       Pattern
        │             │             │
   ┌────▼────┐   ┌────▼────┐   ┌───▼────┐
   │Available│   │Kitchen  │   │Cash    │
   │Reserved │   │Notifier │   │Card    │
   │Occupied │   │Waiter   │   │UPI     │
   └─────────┘   └─────────┘   └────────┘
```

---

## Interview Response Template

### When discussing design approach:

> **"I'll use a bottom-up approach. Starting with atomic entities like Table, Seat, and MealItem, I'll build larger structures like Menu and Branch. This ensures each component is well-defined before composing the full system. This approach makes the design extensible—if we later add delivery or inventory, we just extend existing components without major refactoring."**

---

### When discussing patterns:

> **"For this system, I see several patterns fitting naturally:**
> 
> **1. State Pattern** - Order and table lifecycles have clear state transitions
> 
> **2. Strategy Pattern** - Multiple payment methods and seat allocation algorithms
> 
> **3. Observer Pattern** - Kitchen needs to notify waiters when orders are ready
> 
> **4. Factory Pattern** - Creating different types of payments and bills
> 
> **5. Composite Pattern** - Menu hierarchy with categories and items
> 
> **These patterns ensure the system is maintainable, extensible, and follows SOLID principles."**

---

## SOLID Principles in Action

### Single Responsibility Principle (SRP)

```java
class OrderService {
    // Only handles order logic
    public Order createOrder(Table table, List<MenuItem> items) { }
    public void updateOrder(Order order) { }
}

class BillingService {
    // Only handles billing logic
    public Bill generateBill(Order order) { }
    public void applyDiscount(Bill bill, Discount discount) { }
}
```

---

### Open/Closed Principle (OCP)

```java
// Open for extension, closed for modification
interface PaymentStrategy {
    boolean process(double amount);
}

// New payment methods can be added without modifying existing code
class CryptoPayment implements PaymentStrategy {
    public boolean process(double amount) {
        // New payment method
    }
}
```

---

### Liskov Substitution Principle (LSP)

```java
// All payment strategies are substitutable
PaymentStrategy payment = new CardPayment();
payment = new CashPayment(); // Works seamlessly
payment = new UPIPayment();  // Works seamlessly
```

---

## Advanced Concepts

### Event-Driven Architecture

```
Order Status Change
    │
    ▼
┌──────────────┐
│ Event Bus    │
└──────┬───────┘
       │
       ├──► Waiter Notifier
       ├──► Kitchen Display
       ├──► Customer App
       └──► Analytics Service
```

---

### Asynchronous Processing

```java
class OrderService {
    private ExecutorService executor;
    
    public void processOrder(Order order) {
        // Non-blocking order processing
        executor.submit(() -> {
            sendToKitchen(order);
            notifyWaiter(order);
            updateAnalytics(order);
        });
    }
}
```

---

### Caching Strategy

```
Request → Cache (Popular Items) → Database
   │           │                      │
   │      Hit  │                 Miss │
   │           ▼                      ▼
   └───────Return              Fetch & Cache
```

---

## Interview Bonus Points

| Topic | What to Say | Why It Impresses |
|-------|-------------|------------------|
| **SOLID** | "Following SRP, each service has single responsibility" | Shows OOP principles |
| **Patterns** | "Using State, Strategy, Observer patterns" | Shows design maturity |
| **Scalability** | "Async processing for peak hours" | Shows performance thinking |
| **Testability** | "Interface-based design for easy mocking" | Shows quality focus |
| **Extensibility** | "Bottom-up allows easy feature additions" | Shows long-term thinking |

---

