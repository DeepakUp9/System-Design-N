# Getting Ready: Amazon Locker Service

> **Comprehensive Guide for Low-Level Design Interviews**

---

## 📖 Problem Definition

### What is Amazon Locker Service?

**Amazon** is an online retailer that allows customers to order products for delivery. Sometimes, customers are unavailable at their usual address to receive packages. In such cases, Amazon provides a secure, automated pickup and return service called **Amazon Locker**.

---

### How It Works

**Amazon Lockers** are available at various locations, each containing multiple lockers of different sizes and operating hours. 

**Key Features:**
- 📍 Customers can select a nearby locker location for delivery or returns
- 📦 Only packages that fit a locker's size are eligible
- 🔐 When a package arrives, customer receives a unique code to retrieve it
- ⏰ Packages stored for a limited period
- ♻️ If uncollected, packages are removed and customer may be refunded
- 📥 Customers can return eligible products by dropping them off at available locker locations
- 🚚 Logistics team picks up returned items

---

### System Challenges

This scenario presents interesting challenges:

| Challenge | Description |
|-----------|-------------|
| **Real-time resource allocation** | Assigning lockers dynamically |
| **Concurrent user requests** | Multiple users accessing system simultaneously |
| **Operating hour constraints** | Locations have specific hours |
| **Physical size management** | Matching package and locker sizes |
| **Dual workflows** | Supporting both delivery and returns |

---

## 🎯 In This LLD Interview Case Study, Your Focus Will Be On:

| Focus Area | Description |
|------------|-------------|
| **Locker assignment** | Assign lockers to orders/returns based on size and availability |
| **Package handling** | Manage pickups, returns, and uncollected items |
| **State management** | Track locker states, codes, and access windows |
| **Reliability & security** | Ensure system works reliably and securely at scale |

---

### 📌 Note:
> This system model can be adapted for any retailer providing automated, secure delivery and returns.

---

## 🔍 Expectations from the Interviewee

The Amazon Locker service consists of multiple components. Each component has its own functionality and constraints.

### In the Interview, You Are Expected To:

✅ **Clarify system requirements, constraints, and edge cases** by asking relevant questions  
✅ **Identify and design core system components**  
✅ **Justify design decisions** using OOD and SOLID principles

---

## 📋 Typical Areas You Should Probe and Discuss

---

## 1. Locker Size 📏

### Context
Every locker is of a specific size in the Amazon Locker system.

### Questions to Ask:

#### Q1: Are there different locker sizes? How does package size affect locker assignment?

**What Interviewer is Testing:**
- Understanding of size constraints
- Matching algorithm design
- Resource optimization

**Expected Discussion:**
> *"Yes, I'd design the system with multiple locker sizes (Small, Medium, Large, Extra Large). Package size should be validated before locker assignment. The system should find the smallest available locker that can fit the package to optimize space usage."*

**Design Implications:**
```java
enum LockerSize {
    SMALL(10, 10, 10),      // 10x10x10 cm
    MEDIUM(20, 20, 20),     // 20x20x20 cm
    LARGE(40, 40, 40),      // 40x40x40 cm
    EXTRA_LARGE(60, 60, 60); // 60x60x60 cm
    
    private int width;
    private int height;
    private int depth;
}

class Package {
    private double width;
    private double height;
    private double depth;
    
    boolean fitsIn(LockerSize size) {
        return width <= size.getWidth() 
            && height <= size.getHeight() 
            && depth <= size.getDepth();
    }
}
```

---

#### Q2: Are there restrictions on what can be stored in each locker?

**Design Considerations:**
- Prohibited items (hazardous materials)
- Temperature-sensitive items
- Weight limits per locker
- Item type restrictions

**Expected Answer:**
> *"I'd implement a validation system that checks package type, weight, and dimensions against locker specifications. Certain items like perishables might require specific locker types with temperature control."*

---

## 2. Locker Selection 🎯

### Context
The most significant part of the Amazon Locker service is the selection of the locker. The system has to make sure that more than one customer should not be able to access a locker at a single time.

### Questions to Ask:

#### Q1: How will the system make sure that multiple customers do not get the same locker?

**What Interviewer is Testing:**
- Concurrency control understanding
- Thread safety awareness
- State management

**Expected Answer:**
> *"I'd use optimistic or pessimistic locking when assigning lockers. Each locker assignment would be an atomic transaction. When a locker is assigned, its status changes to OCCUPIED and no other customer can get it until it's available again."*

**Design Implications:**
```java
class LockerAssignmentService {
    // Thread-safe assignment
    public synchronized Locker assignLocker(Package pkg, LockerLocation location) {
        Locker locker = findAvailableLocker(pkg.getSize(), location);
        
        if (locker != null && locker.getStatus() == LockerStatus.AVAILABLE) {
            locker.setStatus(LockerStatus.OCCUPIED);
            locker.setPackage(pkg);
            locker.setAssignedAt(LocalDateTime.now());
            return locker;
        }
        
        throw new NoLockerAvailableException();
    }
}
```

---

#### Q2: Will the customer choose the locker of their own choice, or will the system assign based on availability?

**Design Options:**

| Option | Pros | Cons |
|--------|------|------|
| **Customer chooses** | User preference, transparency | May select inefficiently, UI complexity |
| **System assigns** | Optimal space usage, simple UX | Less control for user |
| **Hybrid** | Best of both | More complex logic |

**Recommended Approach:**
> *"The system should automatically assign the optimal locker based on package size and availability. This ensures efficient space utilization and prevents users from making poor choices."*

---

#### Q3: Can a customer get two lockers for different orders at the same time?

**Design Decision:**
> *"Yes, a customer can have multiple active locker assignments for different orders. The system should track all active assignments per customer."*

**Implementation:**
```java
class Customer {
    private String customerId;
    private List<LockerAssignment> activeAssignments;
    private final int MAX_ACTIVE_LOCKERS = 5; // Business rule
    
    boolean canAssignNewLocker() {
        return activeAssignments.size() < MAX_ACTIVE_LOCKERS;
    }
}
```

---

#### Q4: Will the system keep in mind the locker and package sizes while assigning the locker to the customer?

**Expected Answer:**
> *"Absolutely. The assignment algorithm should find the smallest available locker that can accommodate the package. This optimizes space and ensures larger lockers remain available for larger packages."*

**Algorithm:**
```java
class LockerSelectionStrategy {
    Locker findOptimalLocker(Package pkg, LockerLocation location) {
        // Find smallest locker that fits package
        return location.getAvailableLockers().stream()
            .filter(locker -> locker.canFit(pkg))
            .min(Comparator.comparing(Locker::getSize))
            .orElse(null);
    }
}
```

---

## 3. Locker Status ⏰

### Context
As this problem revolves around the locker, understanding locker status is crucial.

### Questions to Ask:

#### Q1: Is there any time constraint on the package that can be kept in the locker?

**What Interviewer is Testing:**
- Time-based business logic
- Timeout handling
- Cleanup processes

**Expected Answer:**
> *"Yes, packages should have a retention period (e.g., 3 days). The system should track when the package was deposited and automatically mark it as expired after the time limit."*

**Design Implications:**
```java
class LockerAssignment {
    private LocalDateTime depositTime;
    private LocalDateTime expiryTime;
    private final int RETENTION_DAYS = 3;
    
    public LockerAssignment(Package pkg, Locker locker) {
        this.depositTime = LocalDateTime.now();
        this.expiryTime = depositTime.plusDays(RETENTION_DAYS);
    }
    
    boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
    
    long getHoursUntilExpiry() {
        return ChronoUnit.HOURS.between(LocalDateTime.now(), expiryTime);
    }
}
```

---

#### Q2: What will happen if the customer does not come to pick up their package within the valid time period?

**Expected Process:**
1. System detects expired package
2. Send final notification to customer
3. Mark locker for cleanup
4. Logistics team removes package
5. Initiate refund process
6. Free up the locker

**Implementation:**
```java
class ExpiryManagementService {
    void processExpiredPackages() {
        List<LockerAssignment> expired = getExpiredAssignments();
        
        for (LockerAssignment assignment : expired) {
            // Send final notification
            notificationService.sendExpiryNotification(assignment.getCustomer());
            
            // Mark for pickup by logistics
            logisticsService.schedulePickup(assignment.getLocker());
            
            // Initiate refund
            refundService.processRefund(assignment.getOrder());
            
            // Update status
            assignment.setStatus(AssignmentStatus.EXPIRED);
        }
    }
}
```

---

## 4. Returning an Item 📥

### Context
Similar to the order delivery process, items can also be returned through the Amazon Locker service.

### Questions to Ask:

#### Q1: Can the customer return an item through the Amazon Locker service?

**Expected Answer:**
> *"Yes, the system should support returns. Customers can initiate a return and drop off the package at any available locker location."*

---

#### Q2: If yes, will they get the same locker from which they picked up the item?

**Design Decision:**
> *"No, that's not necessary. Returns should be treated as new assignments. The customer selects a convenient location, and the system assigns an available locker based on the return package size."*

**Reasoning:**
- Customer may return from different location
- Original locker might be occupied
- More flexible for customer
- Simpler system logic

---

#### Q3: How will the locker be assigned to the customer while returning an item?

**Expected Answer:**
> *"The return flow is similar to delivery. Customer initiates return, selects location, system assigns optimal locker based on package size, customer receives a code, drops off package, and logistics team collects it."*

**Return Workflow:**
```java
class ReturnService {
    LockerAssignment initiateReturn(ReturnRequest request) {
        // 1. Validate return eligibility
        if (!isEligibleForReturn(request.getOrder())) {
            throw new ReturnNotAllowedException();
        }
        
        // 2. Find available locker at selected location
        LockerLocation location = request.getPreferredLocation();
        Package returnPackage = estimateReturnPackageSize(request);
        
        Locker locker = lockerService.assignLocker(returnPackage, location);
        
        // 3. Generate return code
        String returnCode = generateReturnCode();
        
        // 4. Create assignment
        LockerAssignment assignment = new LockerAssignment(
            returnPackage,
            locker,
            returnCode,
            AssignmentType.RETURN
        );
        
        // 5. Notify customer
        notificationService.sendReturnCode(request.getCustomer(), returnCode);
        
        return assignment;
    }
}
```

---

## 🏗️ Design Approach

We will design this Amazon Locker service using the **bottom-up design approach**.

### Step-by-Step Approach:

#### Step 1: Identify Core Entities
**Simple core entities:**
- 🔐 Locker
- 📍 LockerLocation
- 📦 Package
- 🛒 Order
- 👤 Customer
- 🔑 LockerAssignment
- 📧 Notification

---

#### Step 2: Model Workflows
**Key workflows:**
- Locker assignment (based on size and availability)
- Package pickups (code validation, status updates)
- Returns (reverse flow)
- Uncollected items (expiry handling)

---

#### Step 3: Time & Access Constraints
**Ensure:**
- Locker operations respect time limits
- Location operating hours enforced
- Secure access via unique codes
- Expiry management

---

#### Step 4: Address Non-Functional Requirements
**Focus on:**
- Concurrency control
- Edge case handling
- SOLID principles adherence
- Scalability
- Maintainability

---

#### Step 5: Diagrams and Code
**Illustrate:**
- Major workflows with sequence diagrams
- Class structures with class diagrams
- Complete implementation

---

## 🎨 Design Patterns

During an interview, it is always a good practice to discuss the design patterns that the Amazon Locker system falls under. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

### Applicable Design Patterns:

| Pattern | Usage | Benefit |
|---------|-------|---------|
| **Singleton** | LockerManager, NotificationService | Single instance for centralized management |
| **Strategy** | Locker selection algorithms | Flexible assignment strategies |
| **State** | Locker status transitions | Clean state management |
| **Factory** | Creating different notification types | Centralized object creation |
| **Observer** | Notifying customers of events | Event-driven notifications |
| **Repository** | Data access layer | Separation of concerns |
| **Builder** | Complex object creation (LockerAssignment) | Cleaner construction |

---

## 💡 Interview-Winning Statements

### On Concurrency:
> *"I'd use synchronized blocks or database-level locks when assigning lockers to prevent race conditions where two customers could get the same locker."*

### On Size Matching:
> *"The system should implement a greedy algorithm that assigns the smallest available locker that fits the package, optimizing space utilization."*

### On Expiry:
> *"I'd implement a scheduled job that runs periodically to detect expired packages, notify customers, and trigger the cleanup process."*

### On Returns:
> *"Returns are essentially reverse deliveries. The same locker assignment logic applies, but the workflow ends with logistics pickup instead of customer pickup."*

### On Scalability:
> *"Each LockerLocation can operate independently, allowing the system to scale horizontally by adding more locations without impacting existing ones."*

---

## 🎯 Key Takeaways

### Remember These Points:

1. **Size matters** - Always match package to smallest fitting locker
2. **Concurrency is critical** - Prevent double-booking of lockers
3. **Time-based logic** - Packages expire, operating hours matter
4. **Dual workflows** - Delivery and returns are similar but distinct
5. **State management** - Locker states must be carefully tracked
6. **Code security** - Unique codes prevent unauthorized access
7. **Cleanup processes** - Expired packages must be handled

---

## 📋 Clarification Checklist

Before designing, clarify:

- [ ] Locker sizes and quantities per location
- [ ] Package retention period
- [ ] Maximum active lockers per customer
- [ ] Operating hours per location
- [ ] Return eligibility rules
- [ ] Expiry notification schedule
- [ ] Code generation algorithm
- [ ] Cleanup process for expired items

---

