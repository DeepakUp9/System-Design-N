# LLD Chapter: Design Approach

## Topic: Bottom-Up Design & Design Pattern Awareness

---

## WHAT: What does "bottom-up design" mean here?

**Bottom-up design means:**

1. You start from **stable, atomic domain concepts**
2. You compose them into **larger behavioral units**
3. You let complexity **emerge naturally**, not upfront

### In this problem:

```
Stock, StockPosition → fundamental concepts
Order, Inventory, Portfolio → compositions
```

### Interview framing:

> **"I prefer starting from core domain entities and building upward."**

**This sounds intentional and controlled.**

---

## WHY: Why bottom-up is the right choice here?

### 1. Financial systems are domain-driven

**Small mistakes at the lowest level propagate upward:**

- Incorrect stock quantity
- Wrong price precision
- Invalid position state

**Bottom-up ensures:**
- ✅ Correctness first
- ✅ Reuse later

> **Interviewers strongly favor this in finance-related systems.**

---

### 2. It avoids premature abstraction

**Top-down designs often:**
- ❌ Invent services before understanding data
- ❌ Force abstractions that don't fit reality

**Bottom-up lets:**
- ✅ Real behavior define structure
- ✅ Patterns appear naturally

**Strong interview signal:**

> **"I don't force patterns; I let the domain justify them."**

---

### 3. It supports extensibility

**When new requirements appear:**
- New order types
- New asset classes

**Bottom-up models adapt without refactoring everything.**

---

## HOW: How do you explain this approach in an interview?

**You do not say "Step 1, Step 2".**

**Instead, you say something like:**

> **"I start by identifying the smallest meaningful entities, ensure their behavior and invariants are correct, and then compose them into higher-level components."**

**This shows:**
- Intent
- Experience
- Design discipline

---

## Small Components → Big Components (Interview Interpretation)

### Small components

**Represent facts**
- Have clear ownership of data
- Enforce invariants

**Examples:**
- A stock knows its symbol and price
- A stock position knows quantity and cost basis

---

### Bigger components

**Represent processes**
- Coordinate multiple smaller objects
- Contain business workflows

**Examples:**
- An order coordinates validation, execution, and settlement
- Inventory aggregates multiple stocks

### Key insight:

> **"Entities hold state; higher-level components orchestrate behavior."**

**Interviewers love this distinction.**

---

## Design Patterns (How to Use Them Correctly)

### WHAT interviewers expect

**Not a list of patterns.**  
They expect:
- Awareness
- Correct application
- Justification

---

### WHY mentioning patterns helps

**Patterns act as:**

| Benefit | Purpose |
|---------|---------|
| **Shared vocabulary** | Common language with interviewer |
| **Credibility signal** | Shows experience |
| **Proof of OO maturity** | Design thinking |

**But overuse is a red flag.**

---

### HOW to talk about patterns here

**You hint, you don't lecture.**

**Examples of natural phrasing:**

- "Order types can vary in behavior"
- "Notifications react to stock price changes"
- "Execution strategies may differ per order"

> **You let the interviewer connect the dots.**

---

## How design patterns appear naturally (Interview Style)

### 1. Strategy Pattern (Order behavior)

#### What
Different order types (market, limit, stop-loss) behave differently.

#### Why
Hard-coding order behavior using if/else makes the system rigid.

#### How (Interview framing)

> **"Order execution logic can vary based on order type, so behavior can be encapsulated and swapped without changing the order itself."**

**This clearly signals Strategy pattern, without sounding theoretical.**

```java
interface OrderExecutionStrategy {
    void execute(Order order, Market market);
}

class MarketOrderStrategy implements OrderExecutionStrategy {
    public void execute(Order order, Market market) {
        // Execute at current market price immediately
    }
}

class LimitOrderStrategy implements OrderExecutionStrategy {
    public void execute(Order order, Market market) {
        // Execute only if price reaches limit
    }
}

class StopLossStrategy implements OrderExecutionStrategy {
    public void execute(Order order, Market market) {
        // Execute when price hits stop threshold
    }
}

class Order {
    private OrderExecutionStrategy strategy;
    
    public void execute(Market market) {
        strategy.execute(this, market);
    }
}
```

---

### 2. Observer Pattern (Price updates & alerts)

#### What
Users want notifications when stock prices hit certain thresholds.

#### Why
Stock price changes are external events and affect many consumers:
- Alerts
- Watchlists
- Portfolio valuation

#### How (Interview framing)

> **"Components interested in price changes can react automatically when market data updates."**

**This maps cleanly to Observer / Publisher-Subscriber, and interviewers immediately recognize it.**

```java
interface PriceObserver {
    void onPriceChange(Stock stock, double newPrice);
}

class AlertSystem implements PriceObserver {
    public void onPriceChange(Stock stock, double newPrice) {
        // Check user alerts and notify
    }
}

class PortfolioValuation implements PriceObserver {
    public void onPriceChange(Stock stock, double newPrice) {
        // Recalculate portfolio value
    }
}

class Stock {
    private List<PriceObserver> observers = new ArrayList<>();
    private double price;
    
    public void addObserver(PriceObserver observer) {
        observers.add(observer);
    }
    
    public void updatePrice(double newPrice) {
        this.price = newPrice;
        notifyObservers();
    }
    
    private void notifyObservers() {
        for (PriceObserver observer : observers) {
            observer.onPriceChange(this, price);
        }
    }
}
```

---

### 3. Factory Pattern (Creating orders)

#### What
The system supports multiple order types.

#### Why
Order creation logic shouldn't be scattered across the system.

#### How (Interview framing)

> **"Order creation can be centralized so that adding a new order type doesn't affect existing code."**

**This naturally introduces Factory, without explicitly naming it first.**

```java
enum OrderType {
    MARKET, LIMIT, STOP_LOSS
}

class OrderFactory {
    public static Order createOrder(OrderType type, Map<String, Object> params) {
        switch(type) {
            case MARKET:
                return new MarketOrder(
                    (String) params.get("stockSymbol"),
                    (int) params.get("quantity")
                );
            case LIMIT:
                return new LimitOrder(
                    (String) params.get("stockSymbol"),
                    (int) params.get("quantity"),
                    (double) params.get("limitPrice")
                );
            case STOP_LOSS:
                return new StopLossOrder(
                    (String) params.get("stockSymbol"),
                    (int) params.get("quantity"),
                    (double) params.get("stopPrice")
                );
            default:
                throw new IllegalArgumentException("Unknown order type");
        }
    }
}
```

---

### 4. Composite Pattern (Portfolio & inventory)

#### What
A portfolio contains multiple positions, each tied to a stock.

#### Why
Operations like valuation or profit calculation apply both at:
- Individual position level
- Portfolio level

#### How (Interview framing)

> **"Aggregated entities like portfolios can treat individual positions and collections uniformly for calculations."**

**This aligns with Composite, very interview-friendly.**

```java
interface Valuable {
    double getValue();
    double getProfitLoss();
}

class StockPosition implements Valuable {
    private Stock stock;
    private int quantity;
    private double costBasis;
    
    public double getValue() {
        return stock.getCurrentPrice() * quantity;
    }
    
    public double getProfitLoss() {
        return getValue() - costBasis;
    }
}

class Portfolio implements Valuable {
    private List<Valuable> components = new ArrayList<>();
    
    public void add(Valuable component) {
        components.add(component);
    }
    
    public double getValue() {
        return components.stream()
            .mapToDouble(Valuable::getValue)
            .sum();
    }
    
    public double getProfitLoss() {
        return components.stream()
            .mapToDouble(Valuable::getProfitLoss)
            .sum();
    }
}
```

---

### 5. State Pattern (Order lifecycle)

#### What
Orders move through multiple states:

```
Created → Placed → Executed → Settled
```

#### Why
State-dependent behavior is common in trading systems.

#### How (Interview framing)

> **"Order behavior changes as it progresses through its lifecycle, and invalid transitions should be prevented."**

**This cleanly signals State pattern, which interviewers love in finance systems.**

```java
interface OrderState {
    void place(Order order);
    void execute(Order order);
    void cancel(Order order);
}

class CreatedState implements OrderState {
    public void place(Order order) {
        // Validate and transition to PlacedState
        order.setState(new PlacedState());
    }
    
    public void execute(Order order) {
        throw new IllegalStateException("Cannot execute before placing");
    }
    
    public void cancel(Order order) {
        order.setState(new CancelledState());
    }
}

class PlacedState implements OrderState {
    public void place(Order order) {
        throw new IllegalStateException("Already placed");
    }
    
    public void execute(Order order) {
        // Execute trade and transition
        order.setState(new ExecutedState());
    }
    
    public void cancel(Order order) {
        order.setState(new CancelledState());
    }
}

class Order {
    private OrderState state;
    
    public void place() {
        state.place(this);
    }
    
    public void execute() {
        state.execute(this);
    }
}
```

---

## WHY this pattern discussion works in interviews

**Because:**

| Reason | Impact |
|--------|--------|
| **You're not reciting patterns** | Shows understanding |
| **You're deriving patterns from the domain** | Shows problem-solving |
| **You're showing OO thinking, not memorization** | Shows experience |

**Interviewers think:**

> **"This person has built or understood real systems."**

---

## Common Mistakes to Avoid (Important)

### ❌ Saying "I will use Singleton, Factory, Observer" upfront

### ❌ Designing the entire system first

### ❌ Treating patterns as requirements

### Instead:

### ✔ Let domain drive structure

### ✔ Introduce patterns when complexity demands it

---

## How NOT to present patterns (Quick warning)

**Avoid:**
- "I will use Strategy, Factory, Observer…"
- Pattern dumps without context

**Prefer:**
- Problem → Reason → Design choice

---

## Design Pattern Mapping

| Pattern | Use Case | Interview Phrasing |
|---------|----------|-------------------|
| **Strategy** | Order execution behavior | "Execution logic varies by order type" |
| **Observer** | Price updates & alerts | "Components react to price changes" |
| **Factory** | Order creation | "Centralized order creation" |
| **Composite** | Portfolio structure | "Uniform operations on positions and portfolios" |
| **State** | Order lifecycle | "Behavior changes with order state" |

---

## Bottom-Up Design Flow

```
Level 1: Atomic Entities
├── Stock (symbol, price)
├── StockPosition (quantity, cost basis)
├── User (account info)
└── Price (value, timestamp)

Level 2: Behavioral Components
├── Order (placement, validation)
├── Trade (execution record)
├── Alert (threshold, notification)
└── Transaction (fund movement)

Level 3: Aggregations
├── Portfolio (multiple positions)
├── Watchlist (multiple stocks)
├── Inventory (all holdings)
└── OrderBook (pending orders)

Level 4: System Services
├── TradingEngine
├── PortfolioManager
├── MarketDataService
└── NotificationService
```

---

## Interview Response Template

### When discussing design approach:

> **"I'll use a bottom-up approach, starting with core financial entities like Stock and StockPosition. These form the foundation for higher-level components like Order and Portfolio. This ensures correctness at the atomic level before building orchestration layers. Design patterns will emerge naturally—for example, different order types suggest Strategy, price change notifications suggest Observer, and order lifecycle suggests State pattern. This approach keeps the system both correct and extensible."**

---

### Strong Closing Line (Optional, High Impact)

> **"By starting bottom-up, design patterns emerge naturally from domain behavior rather than being imposed upfront."**

**This is exactly the level interviewers expect.**

---

## SOLID Principles Application

### Single Responsibility Principle (SRP)

```java
class Stock {
    // Only manages stock data
}

class StockPriceService {
    // Only handles price updates
}

class OrderExecutor {
    // Only executes orders
}
```

---

### Open/Closed Principle (OCP)

```java
// New order types can be added without modifying existing code
class TrailingStopOrder implements OrderExecutionStrategy {
    public void execute(Order order, Market market) {
        // New order type
    }
}
```

---

### Liskov Substitution Principle (LSP)

```java
// All order strategies are substitutable
OrderExecutionStrategy strategy = new MarketOrderStrategy();
strategy = new LimitOrderStrategy(); // Works seamlessly
```

---

### Interview-Level Summary Line (Very Useful)

**A strong candidate often concludes this section with:**

> **"I'll use a bottom-up approach so that core financial entities remain stable, and higher-level workflows can evolve independently."**

**That line alone scores points.**

---

## Why this section matters more than it looks

**This section tells the interviewer:**

- How you think
- How you control complexity
- Whether you design for change

> **It's not about what you design — it's about how you reason.**

---

## Interview Bonus Points

| Topic | What to Say | Why It Impresses |
|-------|-------------|------------------|
| **Bottom-up reasoning** | "Start with stable entities, compose upward" | Shows discipline |
| **Pattern emergence** | "Patterns arise from domain, not imposed" | Shows maturity |
| **State machines** | "Order lifecycle needs state management" | Shows finance knowledge |
| **Strategy for behavior** | "Different order types have different execution" | Shows OOP thinking |
| **Observer for events** | "Price changes trigger multiple reactions" | Shows event-driven thinking |

---
