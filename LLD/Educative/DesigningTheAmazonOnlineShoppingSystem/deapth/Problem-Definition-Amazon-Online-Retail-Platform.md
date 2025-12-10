# LLD Chapter: Problem Definition — Amazon Online Retail Platform

---

## 1. WHAT is the problem? (Core System Goal)

We need to design an **online retail marketplace like Amazon** where:

- ✅ Users can **buy products**
- ✅ Users can also **sell products**
- ✅ Users can **search, browse, and filter** across many categories
- ✅ Users can **read/write reviews and ratings**
- ✅ Users maintain a **shopping cart** and place orders
- ✅ Orders go through **payment, tracking, shipment, and delivery** stages
- ✅ Users get **notifications** for each event
- ✅ Sellers can **list, update, and manage** products

> **This is essentially a multi-actor, multi-workflow e-commerce platform.**

---

## 2. WHY do we need this design? (Purpose in LLD Interview)

A problem definition helps you:

| Benefit | Description |
|---------|-------------|
| **Set system boundaries** | What is IN scope, what is OUT of scope |
| **Identify actors** | Customer, Seller, Delivery Partner, System Services |
| **Identify core flows** | Search, cart, order, payment, tracking |
| **Identify non-functional goals** | Scalability, reliability, consistency |
| **Prepare for diagrams** | Use Case, Class Diagram, Sequence Diagram |

### Interviewers want to see:

- ✅ How clearly you understand the domain
- ✅ How you break a huge system into components
- ✅ If you think like a real architect, not just a coder

> **This step shows you understand the end-to-end business flow.**

---

## 3. HOW to think about this problem? (LLD thinking framework)

Break the problem into **five logical domains**, each leading to classes/microservices:

### A. User Domain

- Customer profile
- Seller profile
- Authentication (login, registration)

**Questions interviewer expects:**  
> "How will you model users differently if one user can be both buyer and seller?"

---

### B. Product & Catalog Domain

- Categories
- Product details
- Product availability (inventory)
- Reviews & Ratings

**Thinking point:**  
> "Catalog must be read-heavy → should be optimized for fast search and fast reads."

---

### C. Cart & Checkout Domain

- Add/remove items
- Maintain quantities
- Cart expiration
- Price calculation
- Shipping options

**Important LLD note:**  
> The cart is usually **session-based**, not stored as an order.

---

### D. Order & Payment Domain

- Create order
- Payment selection (UPI, card, wallet, COD)
- Order lifecycle: Placed → Packed → Shipped → Delivered
- Refunds and cancellations
- Notifications at each stage

**Critical interview point:**  
> "Orders are **immutable**. Once placed, they get separate tracking objects."

---

### E. Seller Domain

- Product listing
- Inventory management
- Price updates
- Order fulfillment integration

**Interview angle:**  
> "How will you handle multiple sellers selling the same product?"

---

## 4. KEY INTERVIEW INSIGHTS (This is where candidates score marks)

### a. Mention scalability early

**Amazon scale = millions of products & orders**

Therefore use:
- Caching
- Indexing
- Queues for notification
- Event-driven updates

---

### b. Mention consistency trade-offs

| Component | Consistency Model |
|-----------|------------------|
| Cart and catalog | Eventually consistent ✓ |
| Payment and order | Strongly consistent ✓ |

---

### c. Mention read-heavy architecture

**90% of traffic = Search, browse, view product**

> So **catalog service** must be optimized.

---

### d. Mention extensibility

**Design should allow future extensions:**
- New payment method?
- New seller type?
- New recommendation system?

---

### e. Clear boundaries

```
Catalog ≠ Inventory
Order ≠ Cart
Payment ≠ Shipment
```

> **This clarity shows strong architectural thinking.**

---

## Final Short Summary (Interview Sound-bite)

> **"We are designing an e-commerce marketplace where users can buy and sell products. The system supports product catalog browsing, reviews, cart management, order creation, payments, shipment tracking, and notifications. Each domain—Catalog, Cart, Order, Payment, User, Seller—is independent and scalable. The design must handle read-heavy search traffic, maintain strong consistency for payment and order workflows, and support extensibility."**

---

## Domain Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    E-Commerce Platform                       │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼────────┐   ┌────────▼────────┐   ┌──────▼──────┐
│  User Domain   │   │ Product Domain  │   │Cart Domain  │
├────────────────┤   ├─────────────────┤   ├─────────────┤
│• Customer      │   │• Catalog        │   │• CartItem   │
│• Seller        │   │• Category       │   │• Quantity   │
│• Auth          │   │• Product        │   │• Price Calc │
└────────────────┘   │• Inventory      │   └─────────────┘
                     │• Reviews        │
                     └─────────────────┘
        
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼────────┐   ┌────────▼────────┐   ┌──────▼──────┐
│ Order Domain   │   │ Payment Domain  │   │Seller Domain│
├────────────────┤   ├─────────────────┤   ├─────────────┤
│• Order         │   │• Transaction    │   │• Listing    │
│• OrderItem     │   │• PaymentMethod  │   │• Inventory  │
│• Tracking      │   │• Invoice        │   │• Pricing    │
│• Shipment      │   │• Refund         │   │• Fulfillment│
└────────────────┘   └─────────────────┘   └─────────────┘
```

---

## Actors in the System

| Actor | Role | Key Actions |
|-------|------|-------------|
| **Customer** | Buyer | Browse, search, add to cart, order, review |
| **Seller** | Product provider | List products, manage inventory, fulfill orders |
| **Admin** | Platform manager | Manage categories, resolve disputes, analytics |
| **Delivery Partner** | Logistics | Update shipment status, confirm delivery |
| **System** | Backend services | Notifications, recommendations, analytics |

---

## Core User Flows

### 1. Product Discovery Flow

```
User → Search/Browse → Filter Results → View Product → Read Reviews
```

### 2. Purchase Flow

```
Add to Cart → Update Quantity → Proceed to Checkout → 
Select Address → Choose Payment → Place Order → 
Receive Confirmation
```

### 3. Order Tracking Flow

```
Order Placed → Payment Confirmed → Packed → 
Shipped → Out for Delivery → Delivered
```

### 4. Seller Flow

```
Register as Seller → List Product → Set Price → 
Manage Inventory → Fulfill Order → Receive Payment
```

---

## Scope Definition

### ✅ In Scope

- Product catalog management
- Search and filtering
- Shopping cart
- Order management
- Payment processing
- Review and rating system
- Seller management
- Shipment tracking
- Notifications

### ❌ Out of Scope (Unless Specified)

- Recommendation engine (ML-based)
- Fraud detection system
- Customer service / chat support
- Warehouse management system
- Supply chain optimization
- Advertisement platform
- Pricing algorithms (dynamic pricing)

---

## Non-Functional Requirements

### Performance

- Product search < 100ms
- Cart operations < 50ms
- Order placement < 2 seconds

### Scalability

- Support millions of products
- Handle thousands of concurrent users
- Process thousands of orders per minute

### Availability

- 99.9% uptime
- No single point of failure
- Graceful degradation

### Consistency

- Strong consistency: Orders, Payments
- Eventual consistency: Catalog, Reviews
- Cart: Session-based (no persistence requirement)

### Security

- Secure payment processing
- User data encryption
- Authentication & authorization
- PCI DSS compliance for payments

---

## Key Technical Challenges

| Challenge | Consideration |
|-----------|---------------|
| **Inventory Management** | Multiple sellers, real-time updates, race conditions |
| **Search Performance** | Millions of products, complex filters, relevance ranking |
| **Cart Abandonment** | Session management, expiration, recovery |
| **Payment Processing** | Multiple gateways, retries, reconciliation |
| **Order Consistency** | Distributed transactions, rollback handling |
| **Notification Delivery** | Email, SMS, push notifications, queue management |

---

## Interview Discussion Points

### When asked "Design Amazon":

1. **Start with scope clarification:**
   - "Should we focus on the buyer experience or also include seller management?"
   - "Do we need to handle payments or assume a payment gateway integration?"
   - "Should we design the recommendation system or focus on core e-commerce?"

2. **Identify main domains:**
   - User, Product, Cart, Order, Payment, Seller

3. **Highlight key decisions:**
   - Consistency models (strong vs eventual)
   - Read vs write optimization
   - Synchronous vs asynchronous processing

4. **Show scalability thinking:**
   - Caching strategies
   - Database partitioning
   - Microservices architecture

5. **Mention extensibility:**
   - New payment methods
   - New product categories
   - International expansion

---

## Component Breakdown

### Product Catalog Service

- **Read-heavy** (90% reads, 10% writes)
- Needs: Caching, indexing, search optimization
- Technologies: Elasticsearch, Redis cache

### Order Service

- **Write-heavy during peaks**
- Needs: Strong consistency, ACID transactions
- Technologies: Relational DB, event sourcing

### Cart Service

- **Session-based, temporary**
- Needs: Fast access, expiration handling
- Technologies: Redis, in-memory store

### Payment Service

- **Mission-critical**
- Needs: Strong consistency, idempotency, security
- Technologies: Payment gateway integration, secure vault

### Notification Service

- **Asynchronous, high volume**
- Needs: Queue management, retry logic
- Technologies: Message queue (RabbitMQ, Kafka)

---

## Critical Design Decisions

### 1. User Can Be Both Buyer and Seller

**Solution:**
- Single `User` entity with `roles` (BUYER, SELLER)
- Separate `SellerProfile` for seller-specific data
- Role-based access control (RBAC)

### 2. Multiple Sellers for Same Product

**Options:**
- **Option A:** Product is unique, sellers have inventory entries
- **Option B:** Each seller creates separate product listing
- **Recommendation:** Option A for better UX (single product page)

### 3. Cart Persistence

**Decision:**
- Session-based for guests (no persistence)
- Database-backed for logged-in users
- Auto-expiration after 30 days

### 4. Inventory Consistency

**Challenge:** Prevent overselling when multiple users buy simultaneously

**Solution:**
- Pessimistic locking during checkout
- Reserve inventory temporarily
- Release if payment fails

---

## System Boundaries

```
┌─────────────────────────────────────────────────────────┐
│                     OUR SYSTEM                           │
│                                                          │
│  • User Management                                       │
│  • Product Catalog                                       │
│  • Cart & Checkout                                       │
│  • Order Management                                      │
│  • Payment Processing                                    │
│  • Seller Management                                     │
│  • Notification Service                                  │
│                                                          │
└─────────────────────────────────────────────────────────┘
            │                           │
            │                           │
    ┌───────▼────────┐         ┌───────▼────────┐
    │  External      │         │   External     │
    │  Payment       │         │   Shipping     │
    │  Gateway       │         │   Partners     │
    └────────────────┘         └────────────────┘
```

---
