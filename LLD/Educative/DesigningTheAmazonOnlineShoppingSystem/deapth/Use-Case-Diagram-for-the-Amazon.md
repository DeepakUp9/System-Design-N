# LLD Chapter: Use Case Diagram — Amazon Online Shopping System

This chapter explains:
- **WHAT** the use case diagram represents
- **WHY** it matters in a system design interview
- **HOW** each actor and use case fits into the system
- Extra real-world considerations to impress the interviewer

---

## 1. WHAT is a Use Case Diagram?

A use case diagram illustrates:

| Component | Description |
|-----------|-------------|
| **Actors** | Who interacts with the system |
| **Use cases** | What functionalities they use |
| **Relationships** | How use cases depend on each other |

It explains the **functional requirements** of the system at a high level.

### It answers:

> **"Who does what in the system?"**

### This is the foundation for:

- Sequence diagrams
- Class diagrams
- API definitions
- Domain modeling

---

## 2. WHY use case diagrams matter in an LLD interview

Because they prove you can:

- ✅ Understand system boundaries
- ✅ Identify all flows clearly
- ✅ Separate responsibilities between actors
- ✅ Think like a software architect

### Interviewers look for:

- Clean actor separation
- Correct functional coverage
- Proper include/extend usage
- Awareness of hidden flows (notifications, shipment, admin tasks)

> **This is one of the most important steps before discussing classes or APIs.**

---

## 3. HOW to construct the Use Case Diagram (Step-by-step)

Below is a structured LLD breakdown of your use cases.

---

## A. Actors in the System

### 1. Primary Actors

#### a. Authenticated User

A registered buyer/seller who can:

- Search
- Add to cart
- Checkout
- Track shipments
- Add/modify products (as seller)
- Make payments

> **This actor has full access to core features.**

---

#### b. Guest

Not logged in, but can:

- Search
- Add to cart
- Update cart
- Register account

> **Guests cannot place orders → must become authenticated.**

---

### 2. Secondary Actors

#### a. Admin

Responsible for:

- Category management
- Account management (block/unblock)

> **Admin ensures data hygiene and policy enforcement.**

---

#### b. System

An internal automated actor that:

- Sends order notifications
- Sends shipment notifications
- Updates shipment workflow (event-driven)

> **This is a backend services actor, not a human.**

---

## B. Use Cases (Functional Flows)

Breaking them down actor-wise:

### 1. Admin Use Cases

#### Block Account

To restrict abusive or fraudulent accounts.

#### Add/Modify/Delete Product Category

Maintains the catalog hierarchy.

**Interview depth point:**  
Category changes must trigger:
- Cache invalidation
- Search index update
- Product relinking

---

### 2. Authenticated User Use Cases

#### Add/Modify/Delete Product

**Seller functionalities:**
- Add products to marketplace
- Update description, images, price
- Remove products

---

#### Search Product

**By:**
- Name
- Category
- Filtering / sorting

> **(Uses include for sub-searches)**

---

#### Add Item to Cart

Adds product → `CartItem`

---

#### Update Cart

- Increase/decrease quantity
- Remove item

---

#### Checkout Cart

- Starts order creation
- Initiates payment

---

#### Add Shipping Address

Shipping details saved for orders.

---

#### Add Credit Card

Add/update default payment options.

---

#### Make Payment

**Pay via:**
- Credit card
- Net transfer
- Cash on delivery

> **(Extend relationship for multiple payment types.)**

---

#### Manage Shipment

**For seller/admin scenario:**
- Mark item as shipped
- Update tracking
- Trigger shipment notifications

---

#### Track Shipment

**Buyer can view:**
- Packed
- Shipped
- Out for delivery
- Delivered

---

### 3. Guest Use Cases

#### Register Account

Become authenticated.

#### Search Product

Same search flow, but restricted actions (no checkout)

---

### 4. System Use Cases

#### Send Order Notification

Triggered after payment success.

#### Send Shipment Update Notification

For every shipment stage change.

---

## C. Relationships (Include / Extend / Association)

### 1. Include (Mandatory sub-flow)

#### a. Make Payment → includes → Send Order Notification

**Meaning:**  
After successful payment, notification **must** be sent.

#### b. Search Product → includes → Search by Name / Category

Search includes sub-searches.

---

### 2. Extend (Optional variation)

#### Make Payment → extends → Credit Card / Bank Transfer / COD

**Meaning:**  
Payment method is chosen at runtime.  
Each method has its own extension flow.

---

### 3. Associations

Shows which actor performs which use case.

**Example:**

| Actor | Use Case |
|-------|----------|
| Guest | Search Product |
| Authenticated User | Checkout |
| Admin | Modify Category |
| System | Send Notifications |

> **These define clear system boundaries.**

---

## 4. Additional Elements to Add (For real Amazon-level design)

To impress the interviewer, you can add these **hidden but essential** use cases:

### A. Wishlist Management

- Add to wishlist
- Remove from wishlist
- Convert wishlist item to cart

> **Real-world systems always include this.**

---

### B. Ratings Moderation

- Report review
- Upvote/downvote review
- Mark "Verified Purchase"

> **Shows depth in review management.**

---

### C. Inventory Updates

- Reduce stock after order
- Replenish stock from seller

> **Critical for preventing overselling.**

---

### D. Recommendation Use Cases

- View similar items
- View "Customers also bought"
- Personalized ranking

> **This shows real Amazon-scale thinking.**

---

### E. Returns & Refunds

- Initiate return
- Approve return
- Process refund

> **Interviewers expect this in a complete shopping system.**

---

## 5. Summary (Interview Sound Bite)

> **"Here is the use case diagram for the Amazon shopping system. The system involves four actors: Guest, Authenticated User, Admin, and System. Each actor interacts with specific functionalities such as search, cart management, category management, payment, and shipment tracking. We use include relationships for mandatory flows like sending notifications after payment and extend relationships for optional flows such as selecting a specific payment method. Additional real-world use cases like inventory updates, wishlists, returns, and recommendation flows can further complete the design."**

---

## Complete Use Case Diagram (Text Format)

```
┌─────────────────────────────────────────────────────────────┐
│                    Amazon Shopping System                    │
└─────────────────────────────────────────────────────────────┘

Actors:
  👤 Guest
  👤 Authenticated User
  👤 Admin
  🤖 System

┌──────────────────────────────────────────────────────────────┐
│                       Guest Use Cases                         │
├──────────────────────────────────────────────────────────────┤
│  • Search Product                                            │
│  • Add to Cart                                               │
│  • Update Cart                                               │
│  • Register Account                                          │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│              Authenticated User Use Cases                     │
├──────────────────────────────────────────────────────────────┤
│  Buyer Functions:                                            │
│  • Search Product                                            │
│  • Add to Cart                                               │
│  • Update Cart                                               │
│  • Checkout Cart                                             │
│  • Add Shipping Address                                      │
│  • Add Credit Card                                           │
│  • Make Payment                                              │
│    ├── «extend» Credit Card Payment                          │
│    ├── «extend» Bank Transfer                                │
│    └── «extend» Cash on Delivery                             │
│  • Track Shipment                                            │
│  • Add Product Review                                        │
│  • Manage Wishlist                                           │
│                                                              │
│  Seller Functions:                                           │
│  • Add Product                                               │
│  • Modify Product                                            │
│  • Delete Product                                            │
│  • Manage Shipment                                           │
│  • View Sales Analytics                                      │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                      Admin Use Cases                          │
├──────────────────────────────────────────────────────────────┤
│  • Add Product Category                                      │
│  • Modify Product Category                                   │
│  • Delete Product Category                                   │
│  • Block/Unblock Account                                     │
│  • Moderate Reviews                                          │
│  • Manage Disputes                                           │
└──────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────┐
│                     System Use Cases                          │
├──────────────────────────────────────────────────────────────┤
│  • Send Order Notification                                   │
│  • Send Shipment Update                                      │
│  • Update Inventory                                          │
│  • Generate Recommendations                                  │
│  • Process Refunds                                           │
└──────────────────────────────────────────────────────────────┘

Relationships:
  Make Payment ──«include»──> Send Order Notification
  Search Product ──«include»──> Search by Name/Category
  Make Payment ──«extend»──> Payment Method Options
  Checkout Cart ──«include»──> Validate Inventory
```

---

## Actor-Use Case Matrix

| Use Case | Guest | Auth User | Admin | System |
|----------|-------|-----------|-------|--------|
| **Search Product** | ✅ | ✅ | ✅ | ❌ |
| **Add to Cart** | ✅ | ✅ | ❌ | ❌ |
| **Update Cart** | ✅ | ✅ | ❌ | ❌ |
| **Register Account** | ✅ | ❌ | ❌ | ❌ |
| **Checkout** | ❌ | ✅ | ❌ | ❌ |
| **Make Payment** | ❌ | ✅ | ❌ | ❌ |
| **Track Shipment** | ❌ | ✅ | ✅ | ❌ |
| **Add Product** | ❌ | ✅ (Seller) | ❌ | ❌ |
| **Modify Category** | ❌ | ❌ | ✅ | ❌ |
| **Block Account** | ❌ | ❌ | ✅ | ❌ |
| **Send Notification** | ❌ | ❌ | ❌ | ✅ |
| **Update Inventory** | ❌ | ❌ | ❌ | ✅ |

---

## Detailed Use Case Breakdown

### Use Case: Checkout Cart

| Attribute | Details |
|-----------|---------|
| **Actor** | Authenticated User |
| **Precondition** | Cart has items, user is logged in |
| **Main Flow** | 1. User clicks "Proceed to Checkout"<br>2. System validates cart items<br>3. System displays shipping options<br>4. User selects shipping address<br>5. System displays payment options<br>6. User proceeds to payment |
| **Postcondition** | User redirected to payment page |
| **Extensions** | Out of stock item → Remove from cart<br>Price changed → Show updated price |
| **Includes** | Validate Inventory, Calculate Total |

---

### Use Case: Make Payment

| Attribute | Details |
|-----------|---------|
| **Actor** | Authenticated User |
| **Precondition** | Checkout completed |
| **Main Flow** | 1. User selects payment method<br>2. System processes payment<br>3. System creates order<br>4. System sends confirmation |
| **Postcondition** | Order created, payment recorded |
| **Extensions** | Credit Card → Process card payment<br>UPI → Process UPI payment<br>COD → Mark as pending |
| **Includes** | Send Order Notification |

---

### Use Case: Track Shipment

| Attribute | Details |
|-----------|---------|
| **Actor** | Authenticated User |
| **Precondition** | Order placed and shipped |
| **Main Flow** | 1. User views order details<br>2. System shows shipment status<br>3. User tracks location<br>4. System shows delivery estimate |
| **Postcondition** | User informed of shipment status |
| **Extensions** | Delay → Show updated delivery date<br>Issue → Show contact support |
| **Includes** | Fetch Tracking Info |

---

## Relationship Types Explained

### Include Relationship (<<include>>)

**Definition:** A mandatory sub-flow that must execute.

**Examples:**
- `Checkout Cart` **includes** `Validate Inventory`
- `Make Payment` **includes** `Send Order Notification`

**Why use it:**  
When a use case always requires another use case to complete.

---

### Extend Relationship (<<extend>>)

**Definition:** An optional variation of a use case.

**Examples:**
- `Make Payment` **extends to** `Credit Card Payment`, `UPI Payment`, `COD`
- `Search Product` **extends to** `Apply Filters`

**Why use it:**  
When behavior varies based on conditions or user choice.

---

### Association

**Definition:** Simple connection between actor and use case.

**Examples:**
- `Guest` → `Search Product`
- `Admin` → `Modify Category`

**Why use it:**  
Shows who can perform what action.

---

## Advanced Use Cases (Amazon-Scale)

### 1. Personalized Recommendations

| Aspect | Details |
|--------|---------|
| **Actor** | System, Authenticated User |
| **Trigger** | User views product, adds to cart, makes purchase |
| **Flow** | System analyzes behavior → Generates recommendations → Displays on UI |
| **Technology** | ML models, collaborative filtering |

---

### 2. Dynamic Pricing

| Aspect | Details |
|--------|---------|
| **Actor** | System |
| **Trigger** | Time-based, demand-based, competitor pricing |
| **Flow** | System monitors factors → Adjusts prices → Updates catalog |
| **Constraint** | Must not exceed max threshold |

---

### 3. Fraud Detection

| Aspect | Details |
|--------|---------|
| **Actor** | System |
| **Trigger** | Suspicious activity during checkout/payment |
| **Flow** | System flags transaction → Requires additional verification → Blocks if confirmed fraud |
| **Integration** | Payment gateway, ML models |

---

### 4. Inventory Auto-Replenishment

| Aspect | Details |
|--------|---------|
| **Actor** | System, Seller |
| **Trigger** | Stock falls below threshold |
| **Flow** | System notifies seller → Seller restocks → System updates inventory |
| **Automation** | Can be fully automated for certain sellers |

---

## Interview Tips

### When drawing/explaining use case diagram:

1. **Start with actors**  
   "The system has four main actors: Guest, Authenticated User, Admin, and System"

2. **Explain primary flows first**  
   "The most critical flows are search, cart management, checkout, and payment"

3. **Add relationships**  
   "We use include for mandatory sub-flows and extend for optional variations"

4. **Mention extensibility**  
   "The design supports future additions like wishlists, recommendations, and returns"

5. **Show system boundaries**  
   "Guest actors can browse but not purchase. Only authenticated users can complete transactions"

---

## Common Interview Questions

### Q: Why is Guest separate from Authenticated User?

**A:** Different permission levels. Guests can browse but cannot complete transactions. This separation maintains security and allows for registration conversion tracking.

---

### Q: Why is System an actor?

**A:** System represents automated backend processes like notifications and inventory updates. Treating it as an actor clarifies that these are triggered behaviors, not user-initiated actions.

---

### Q: How do you handle a user who is both buyer and seller?

**A:** The Authenticated User role encompasses both. We use role-based access control (RBAC) to determine which functions are available based on user's active role.

---

### Q: What's the difference between include and extend?

**A:**  
- **Include:** Mandatory sub-flow (Checkout **always includes** Validate Inventory)
- **Extend:** Optional variation (Payment **can extend to** different payment methods)

---
