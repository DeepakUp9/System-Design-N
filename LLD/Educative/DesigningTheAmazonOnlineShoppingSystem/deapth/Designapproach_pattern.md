# LLD Chapter: Design Approach + Design Patterns

---

## 1. Design Approach — Bottom-Up

### WHAT is the Bottom-Up approach?

You start by designing the **smallest, most atomic components** first:

- `Product`
- `User`
- `CartItem`
- `Address`
- `PaymentMethod`
- `Category`

Then you **combine** these into larger constructs:

- `ProductCategory`
- `Cart`
- `Order`
- `Shipment`
- `Catalog`
- `SellerDashboard`

Finally, you **assemble** everything into:

- Entire marketplace system
- API boundaries
- Workflows

---

### WHY bottom-up for an Amazon-like system?

#### Because:

### A. The system is huge

Trying to design Amazon top-down leads to confusion and gaps.

> Bottom-up → your building blocks stay simple and strongly defined.

### B. Atomic components remain reusable

**Example:**  
A `Product` appears in:
- Search results
- Recommended items
- Cart items
- Orders
- Seller listings

> If the base class is correct, everything above becomes consistent.

### C. It shows strong OOP/Domain thinking

Interviewers love when you show:
- ✅ Encapsulation
- ✅ Reusability
- ✅ Clear domain modeling

### D. It avoids premature complexity

**Start simple → expand logically.**

---

### HOW to present this during interview?

**Say something like:**

> **"I'll follow a bottom-up design approach. I'll start by defining the core domain entities like Product, Category, User, CartItem, and OrderItem. Once those basics are stable, I'll compose higher-level structures like Catalog, Cart, Order, and Checkout. This gives a clean, reusable foundation and avoids modeling errors at higher levels."**

> **This immediately signals architectural maturity.**

---

## 2. Design Patterns — What interviewer expects you to mention

Mentioning patterns is **extremely impactful** because it shows:

- ✅ You're systematic
- ✅ You know OOP principles
- ✅ You can map real-world problems to known patterns

Below are the patterns the interviewer wants you to bring up — plus additional ones to score bonus points.

---

### A. Factory Pattern

#### WHAT:

Used to create objects without exposing creation logic.

#### WHY in Amazon system:

We may need different product types:
- Book
- Electronics
- Grocery
- Furniture

Each may require different initialization.

#### HOW:

A `ProductFactory` can decide which subclass to instantiate based on category.

```java
interface Product { }

class Book implements Product { }
class Electronics implements Product { }

class ProductFactory {
    public static Product create(String category) {
        switch(category) {
            case "BOOK": return new Book();
            case "ELECTRONICS": return new Electronics();
            default: throw new IllegalArgumentException();
        }
    }
}
```

---

### B. Strategy Pattern ⭐

#### WHAT:

Encapsulates different behaviors so they can be swapped at runtime.

#### WHY:

Perfect for:
- **Payment methods** (UPI, card, wallet)
- **Shipping cost calculation**
- **Sorting/filtering strategies**

#### HOW:

`PaymentMethodStrategy` → `PayViaCard`, `PayViaUPI`, `PayViaGiftCard`

```java
interface PaymentStrategy {
    boolean pay(double amount);
}

class CardPayment implements PaymentStrategy {
    public boolean pay(double amount) { 
        // Card processing logic
    }
}

class UPIPayment implements PaymentStrategy {
    public boolean pay(double amount) { 
        // UPI processing logic
    }
}
```

> **In the interview, saying this alone shows big-company thinking.**

---

### C. Observer Pattern

#### WHAT:

Used when an update in one component must notify others.

#### WHY Amazon needs it:

**Notifications for order status:**
- `OrderPlaced` → email/SMS/push
- `Shipped`
- `Delivered`

#### HOW:

`OrderService` notifies subscribed channels.

```java
interface OrderObserver {
    void onOrderStatusChange(Order order);
}

class EmailNotifier implements OrderObserver {
    public void onOrderStatusChange(Order order) {
        // Send email
    }
}

class SMSNotifier implements OrderObserver {
    public void onOrderStatusChange(Order order) {
        // Send SMS
    }
}
```

> **Real Amazon uses event-driven architecture (similar concept).**

---

### D. Composite Pattern

#### WHAT:

Represent part-whole hierarchies.

#### WHY:

**Product Categories are hierarchical:**

```
Electronics
├── Mobiles
├── Laptops
│   └── Gaming laptops
└── Accessories
```

#### HOW:

`Category` can contain both subcategories and products.

```java
interface CategoryComponent {
    void display();
}

class Category implements CategoryComponent {
    private List<CategoryComponent> children;
    
    public void display() {
        for (CategoryComponent child : children) {
            child.display();
        }
    }
}

class Product implements CategoryComponent {
    public void display() {
        // Display product details
    }
}
```

---

### E. Singleton Pattern

#### WHAT:

One instance globally.

#### WHY:

Useful for:
- Cache managers
- Config providers
- Search index loaders

#### HOW:

E.g., a global `SearchIndexManager`

```java
class SearchIndexManager {
    private static SearchIndexManager instance;
    
    private SearchIndexManager() {}
    
    public static SearchIndexManager getInstance() {
        if (instance == null) {
            synchronized (SearchIndexManager.class) {
                if (instance == null) {
                    instance = new SearchIndexManager();
                }
            }
        }
        return instance;
    }
}
```

---

### F. Builder Pattern

#### WHAT:

Helps construct complex objects step-by-step.

#### WHY:

`Order` object is complex:
- Items
- Pricing
- Shipping address
- Payment details
- Discounts

#### HOW:

`OrderBuilder` ensures immutability and correctness.

```java
class Order {
    private List<OrderItem> items;
    private Address shippingAddress;
    private Payment payment;
    private double totalPrice;
    
    private Order(OrderBuilder builder) {
        this.items = builder.items;
        this.shippingAddress = builder.shippingAddress;
        this.payment = builder.payment;
        this.totalPrice = builder.totalPrice;
    }
    
    static class OrderBuilder {
        private List<OrderItem> items;
        private Address shippingAddress;
        private Payment payment;
        private double totalPrice;
        
        public OrderBuilder withItems(List<OrderItem> items) {
            this.items = items;
            return this;
        }
        
        public OrderBuilder withAddress(Address address) {
            this.shippingAddress = address;
            return this;
        }
        
        public Order build() {
            return new Order(this);
        }
    }
}
```

---

### G. Proxy Pattern 🌟 (Bonus point — interviewers love this)

#### WHY:

Used for:
- **Caching**
- **Rate limiting**
- **Access control**

#### Example:

`CatalogServiceProxy` → caches product details.

```java
interface CatalogService {
    Product getProduct(String productId);
}

class RealCatalogService implements CatalogService {
    public Product getProduct(String productId) {
        // Database call
    }
}

class CachedCatalogServiceProxy implements CatalogService {
    private RealCatalogService realService;
    private Map<String, Product> cache;
    
    public Product getProduct(String productId) {
        if (cache.containsKey(productId)) {
            return cache.get(productId);
        }
        Product product = realService.getProduct(productId);
        cache.put(productId, product);
        return product;
    }
}
```

---

### H. Adapter Pattern (Bonus)

#### WHY:

Payment gateways differ; adapter normalizes them.

```java
interface PaymentGateway {
    boolean processPayment(PaymentRequest request);
}

class StripeAdapter implements PaymentGateway {
    private StripeAPI stripeAPI;
    
    public boolean processPayment(PaymentRequest request) {
        // Adapt our request to Stripe's format
        return stripeAPI.charge(/* adapted params */);
    }
}

class PayPalAdapter implements PaymentGateway {
    private PayPalAPI paypalAPI;
    
    public boolean processPayment(PaymentRequest request) {
        // Adapt our request to PayPal's format
        return paypalAPI.processTransaction(/* adapted params */);
    }
}
```

---

### I. Repository Pattern (Enterprise-level)

#### WHY:

Data access abstraction:
- `ProductRepository`
- `UserRepository`
- `OrderRepository`

Clean separation of business logic and storage.

```java
interface ProductRepository {
    Product findById(String id);
    List<Product> findByCategory(String category);
    void save(Product product);
}

class ProductRepositoryImpl implements ProductRepository {
    // Database access logic
}
```

---

### J. Domain-Driven Design (DDD) principles 🌟 (Huge bonus)

You can mention:

| Concept | Example in Amazon |
|---------|------------------|
| **Entities** | Product, Order, User |
| **Value Objects** | Price, Address, Money |
| **Aggregates** | Order (root) with OrderItems |
| **Bounded Contexts** | Catalog, Checkout, Payment, Shipping |

> **Interviewers absolutely love DDD references.**

---

## 3. Additional Concepts to Mention (to look senior)

### A. Event-Driven Architecture

`OrderPlaced` event triggers:
- Payment service
- Notification service
- Inventory deduction
- Shipment initiation

```
OrderPlaced Event
    │
    ├──► Payment Service
    ├──► Notification Service
    ├──► Inventory Service
    └──► Shipment Service
```

---

### B. CQRS (Command Query Responsibility Segregation)

#### Why needed?

- **Read-heavy** catalog
- **Write-heavy** order system

```
┌─────────────────┐         ┌─────────────────┐
│  Command Model  │         │   Query Model   │
│   (Writes)      │         │    (Reads)      │
├─────────────────┤         ├─────────────────┤
│ • Place Order   │         │ • Search        │
│ • Update Cart   │         │ • Browse        │
│ • Add Product   │         │ • View Details  │
└─────────────────┘         └─────────────────┘
        │                           │
        └──────────┬────────────────┘
                   │
           ┌───────▼────────┐
           │  Event Store   │
           └────────────────┘
```

---

### C. Caching Layers

- Product cache
- Category cache
- Frequently accessed item details

```
Request → CDN → Redis Cache → Database
```

---

### D. Idempotency

Essential for **checkout & payments**.

```java
class PaymentService {
    private Set<String> processedTransactions;
    
    public boolean processPayment(String idempotencyKey, PaymentRequest request) {
        if (processedTransactions.contains(idempotencyKey)) {
            return true; // Already processed
        }
        // Process payment
        processedTransactions.add(idempotencyKey);
        return true;
    }
}
```

---

### E. Observability

- Logs
- Metrics
- Tracing

---

## Summary (Interview-ready sound bite)

> **"I'll use a bottom-up design approach to model the smallest building blocks first and then compose them into higher-level workflows. For patterns, this system naturally aligns with Strategy (for payments), Factory (for product creation), Observer (for notifications), Composite (for category hierarchy), Builder (for complex orders), Adapter (for payment gateways), and Proxy (for caching). These choices help maintain scalability, flexibility, and extensibility in an Amazon-scale platform."**

---

## Design Pattern Mapping Table

| Pattern | Use Case in Amazon | Benefit |
|---------|-------------------|---------|
| **Factory** | Product creation by category | Encapsulates object creation |
| **Strategy** | Payment methods, shipping calculation | Runtime behavior switching |
| **Observer** | Order status notifications | Decoupled event handling |
| **Composite** | Category hierarchy | Tree structure management |
| **Singleton** | Cache manager, config | Single global instance |
| **Builder** | Complex Order construction | Step-by-step object creation |
| **Proxy** | Catalog caching | Performance optimization |
| **Adapter** | Payment gateway integration | Interface normalization |
| **Repository** | Data access layer | Business-data separation |

---

## Bottom-Up Design Flow

```
Level 1: Atomic Entities
├── Product
├── User
├── CartItem
├── Address
├── PaymentMethod
└── Category

Level 2: Composite Entities
├── Cart (collection of CartItems)
├── Order (collection of OrderItems)
├── Catalog (collection of Products)
└── ProductCategory (hierarchical)

Level 3: Service Layer
├── ProductService
├── CartService
├── OrderService
├── PaymentService
└── NotificationService

Level 4: System Orchestration
├── CheckoutWorkflow
├── SearchEngine
└── RecommendationEngine

Level 5: API Gateway
└── External Interfaces
```

---

## SOLID Principles Application

| Principle | Application in Amazon |
|-----------|----------------------|
| **SRP** | Each service has one responsibility (ProductService handles products only) |
| **OCP** | New payment methods can be added without modifying existing code |
| **LSP** | All payment strategies are interchangeable |
| **ISP** | Separate interfaces for Searchable, Purchasable, Reviewable |
| **DIP** | Services depend on interfaces, not concrete implementations |

---

## Advanced Architecture Concepts

### Microservices Architecture

```
API Gateway
    │
    ├──► User Service
    ├──► Product Service (read-optimized)
    ├──► Cart Service (session-based)
    ├──► Order Service (write-optimized)
    ├──► Payment Service (strongly consistent)
    ├──► Notification Service (async)
    └──► Search Service (Elasticsearch)
```

### Event-Driven Flow

```
User Action: Place Order
    │
    ▼
┌──────────────┐
│ Order Service│
└──────┬───────┘
       │ (publishes event)
       ▼
┌──────────────┐
│ Event Bus    │
└──────┬───────┘
       │
       ├──► Payment Service (deduct amount)
       ├──► Inventory Service (reduce stock)
       ├──► Notification Service (send confirmation)
       └──► Analytics Service (track metrics)
```

---

## Interview Bonus Points

### Mention These for Maximum Impact:

| Topic | What to Say | Why It Impresses |
|-------|-------------|------------------|
| **DDD** | "I'd use bounded contexts for Catalog, Payment, and Shipping" | Shows enterprise experience |
| **CQRS** | "Separate read/write models for performance" | Shows scalability knowledge |
| **Event Sourcing** | "Store all state changes as events" | Shows advanced architecture |
| **Saga Pattern** | "For distributed transactions across services" | Shows microservices expertise |
| **Circuit Breaker** | "To handle payment gateway failures" | Shows resilience thinking |

---

