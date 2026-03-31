# Amazon Locker Service - Requirements Analysis & Design Approach

> **Comprehensive LLD Requirements Analysis with Design Implications**

---

## 📋 Requirements Overview

### Notational Convention
Each requirement is labeled as **"Rn"** where:
- **R** = Requirement
- **n** = Natural number (unique identifier)

---

## 🎯 Functional & Operational Requirements

---

### R1: Locker Location Selection 📍

**Requirement:**
> A customer can select a preferred locker location for order pickup during checkout.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Location Discovery** | Show nearby locker locations at checkout |
| **Availability Filter** | Only show locations with available lockers |
| **Persistence** | Save preferred location per customer |
| **Geo-sorting** | Sort by proximity to delivery address |

#### Key Classes Affected:
```java
class Customer {
    private String customerId;
    private String name;
    private String email;
    private Address defaultAddress;
    private LockerLocation preferredLockerLocation;

    public void selectLockerLocation(LockerLocation location) {
        if (location == null || !location.isOperational()) {
            throw new InvalidLockerLocationException("Location is not available");
        }
        this.preferredLockerLocation = location;
    }

    public LockerLocation getPreferredLockerLocation() {
        return preferredLockerLocation;
    }
}

class LockerLocation {
    private String locationId;
    private String name;
    private Address address;
    private GeoCoordinates coordinates;
    private OperatingHours operatingHours;
    private List<Locker> lockers;
    private LocationStatus status;

    public boolean isOperational() {
        return status == LocationStatus.OPERATIONAL;
    }

    public int getAvailableLockerCount() {
        return (int) lockers.stream()
            .filter(l -> l.getStatus() == LockerStatus.AVAILABLE)
            .count();
    }
}

class CheckoutService {
    public List<LockerLocation> getNearbyLocations(Address deliveryAddress, int radiusKm) {
        return locationRepository.findAll().stream()
            .filter(loc -> calculateDistance(deliveryAddress, loc.getAddress()) <= radiusKm)
            .filter(LockerLocation::isOperational)
            .filter(loc -> loc.getAvailableLockerCount() > 0)
            .sorted(Comparator.comparingDouble(loc ->
                calculateDistance(deliveryAddress, loc.getAddress())))
            .collect(Collectors.toList());
    }
}
```

#### Design Pattern:
- **Strategy Pattern** for distance calculation
- **Repository Pattern** for location data access

---

### R2: Order Packaging & Locker Assignment 📦

**Requirement:**
> An order may contain one or more items. Based on locker size availability, items are packaged together if possible.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Package Aggregation** | Combine items when dimensions allow |
| **Size Matching** | Calculate total size, find best fit locker |
| **Fallback Logic** | Split packages if single locker too small |
| **Optimization** | Bin-packing algorithm for best fit |

#### Key Design:
```java
class Order {
    private String orderId;
    private Customer customer;
    private List<OrderItem> items;
    private OrderStatus status;
    private Package assignedPackage;
    private LockerLocation selectedLocation;

    public Dimensions estimateTotalDimensions() {
        // Sum all item dimensions for packaging
        return items.stream()
            .map(OrderItem::getDimensions)
            .reduce(Dimensions.ZERO, Dimensions::combine);
    }
}

class Package {
    private String packageId;
    private Order order;
    private Dimensions dimensions;
    private double weight;
    private Locker assignedLocker;
    private PackageStatus status;

    public LockerSize getRequiredLockerSize() {
        return LockerSize.fromDimensions(this.dimensions);
    }
}

class PackagingService {
    public Package createPackageForOrder(Order order) {
        Dimensions totalDimensions = order.estimateTotalDimensions();
        Package pkg = new Package(order, totalDimensions);

        // Determine the minimum locker size that fits
        LockerSize requiredSize = LockerSize.fromDimensions(totalDimensions);
        pkg.setRequiredLockerSize(requiredSize);

        return pkg;
    }
}

class Dimensions {
    private double length;
    private double width;
    private double height;

    public static Dimensions ZERO = new Dimensions(0, 0, 0);

    public Dimensions combine(Dimensions other) {
        // Packing algorithm - simplify as bounding box
        return new Dimensions(
            Math.max(this.length, other.length),
            Math.max(this.width, other.width),
            this.height + other.height
        );
    }

    public boolean fitsIn(Dimensions container) {
        return this.length <= container.length
            && this.width <= container.width
            && this.height <= container.height;
    }
}
```

#### Design Pattern:
- **Strategy Pattern** for packaging algorithms
- **Factory Pattern** for package creation

---

### R3: Locker Sizes 🗄️

**Requirement:**
> Locker locations contain multiple lockers of various sizes (extra small, small, medium, large, extra large, double extra large).

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Size Enum** | Ordered enum for comparison |
| **Dimension Mapping** | Each size has fixed max dimensions |
| **Availability Tracking** | Count per size at each location |
| **Assignment Strategy** | Best-fit vs first-fit |

#### Key Design:
```java
enum LockerSize {
    EXTRA_SMALL("XS",   new Dimensions(15, 10, 10)),
    SMALL      ("S",    new Dimensions(25, 20, 20)),
    MEDIUM     ("M",    new Dimensions(40, 35, 30)),
    LARGE      ("L",    new Dimensions(60, 50, 45)),
    EXTRA_LARGE("XL",   new Dimensions(80, 70, 60)),
    DOUBLE_XL  ("XXL",  new Dimensions(120, 100, 90));

    private final String label;
    private final Dimensions maxDimensions;

    LockerSize(String label, Dimensions maxDimensions) {
        this.label = label;
        this.maxDimensions = maxDimensions;
    }

    public static LockerSize fromDimensions(Dimensions packageDimensions) {
        return Arrays.stream(values())
            .filter(size -> packageDimensions.fitsIn(size.getMaxDimensions()))
            .findFirst()
            .orElseThrow(() -> new PackageTooLargeException(
                "Package does not fit any available locker size"));
    }

    public boolean canFit(Dimensions dimensions) {
        return dimensions.fitsIn(this.maxDimensions);
    }
}

class Locker {
    private String lockerId;
    private LockerSize size;
    private LockerStatus status;
    private LockerLocation location;
    private Package currentPackage;
    private String currentAccessCode;

    public boolean isAvailable() {
        return status == LockerStatus.AVAILABLE;
    }

    public boolean canAccommodate(Package pkg) {
        return isAvailable() && size.canFit(pkg.getDimensions());
    }
}
```

#### Locker Size Reference:

```
EXTRA_SMALL (XS):  15cm × 10cm × 10cm   → Envelopes, small accessories
SMALL (S):         25cm × 20cm × 20cm   → Books, small electronics
MEDIUM (M):        40cm × 35cm × 30cm   → Shoes, clothing
LARGE (L):         60cm × 50cm × 45cm   → Small appliances
EXTRA_LARGE (XL):  80cm × 70cm × 60cm   → Medium appliances
DOUBLE_XL (XXL):  120cm × 100cm × 90cm  → Large items
```

---

### R4: Package Fit Validation 📐

**Requirement:**
> Only packages that fit fully within the locker's interior dimensions are eligible for locker delivery.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Pre-check at Checkout** | Validate before locker option is shown |
| **Hard Rejection** | Block ineligible packages from locker route |
| **Dimension Source** | Use product catalog dimensions + packaging overhead |
| **Exception Handling** | Clear error message with alternatives |

#### Key Design:
```java
class LockerEligibilityService {
    private static final double PACKAGING_OVERHEAD = 1.1; // 10% packaging buffer

    public boolean isEligibleForLockerDelivery(Order order) {
        Dimensions packageDimensions = calculatePackageDimensions(order);
        LockerSize maxLockerSize = LockerSize.DOUBLE_XL;
        return packageDimensions.fitsIn(maxLockerSize.getMaxDimensions());
    }

    public LockerSize getRequiredLockerSize(Order order) {
        Dimensions dims = calculatePackageDimensions(order);
        return LockerSize.fromDimensions(dims);
    }

    private Dimensions calculatePackageDimensions(Order order) {
        Dimensions raw = order.estimateTotalDimensions();
        // Add packaging buffer
        return raw.scale(PACKAGING_OVERHEAD);
    }

    public EligibilityResult checkEligibility(Order order, LockerLocation location) {
        if (!isEligibleForLockerDelivery(order)) {
            return EligibilityResult.rejected("Package too large for any locker");
        }

        LockerSize required = getRequiredLockerSize(order);
        boolean sizeAvailable = location.hasAvailableLockerOfSize(required);

        if (!sizeAvailable) {
            return EligibilityResult.rejected(
                "No available " + required.getLabel() + " locker at selected location");
        }

        return EligibilityResult.approved(required);
    }
}

class EligibilityResult {
    private boolean eligible;
    private String reason;
    private LockerSize suggestedSize;

    public static EligibilityResult approved(LockerSize size) {
        return new EligibilityResult(true, "Eligible", size);
    }

    public static EligibilityResult rejected(String reason) {
        return new EligibilityResult(false, reason, null);
    }
}
```

---

### R5: Unique Access Code Generation 🔑

**Requirement:**
> When a package is delivered to the selected locker, the customer receives a unique code (e.g., a 6-digit PIN) to open the locker.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Code Format** | 6-digit numeric PIN |
| **Uniqueness** | No two active codes can be identical |
| **Secure Generation** | Cryptographic randomness |
| **Delivery Channel** | Email + SMS |
| **Code Validity** | Tied to package status |

#### Key Design:
```java
class AccessCode {
    private String code;           // 6-digit PIN
    private String lockerId;
    private String packageId;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    private CodeStatus status;

    public boolean isValid() {
        return status == CodeStatus.ACTIVE
            && LocalDateTime.now().isBefore(expiresAt);
    }

    public void invalidate() {
        this.status = CodeStatus.INVALIDATED;
    }
}

enum CodeStatus {
    ACTIVE,
    USED,
    EXPIRED,
    INVALIDATED
}

class AccessCodeService {
    private Set<String> activeCodesGlobal = new HashSet<>();
    private static final int CODE_LENGTH = 6;

    public AccessCode generateCode(Package pkg, Locker locker) {
        String code = generateUniqueCode();

        AccessCode accessCode = new AccessCode();
        accessCode.setCode(code);
        accessCode.setLockerId(locker.getLockerId());
        accessCode.setPackageId(pkg.getPackageId());
        accessCode.setGeneratedAt(LocalDateTime.now());
        accessCode.setExpiresAt(LocalDateTime.now().plusDays(3)); // R6
        accessCode.setStatus(CodeStatus.ACTIVE);

        activeCodesGlobal.add(code);
        return accessCode;
    }

    private String generateUniqueCode() {
        SecureRandom random = new SecureRandom();
        String code;
        do {
            code = String.format("%06d", random.nextInt(1_000_000));
        } while (activeCodesGlobal.contains(code)); // Ensure uniqueness
        return code;
    }

    public boolean validateCode(String code, Locker locker) {
        AccessCode accessCode = accessCodeRepository.findByCodeAndLocker(code, locker);
        return accessCode != null && accessCode.isValid();
    }
}
```

#### Delivery Trigger:
```java
class DeliveryService {
    public void onPackageDelivered(Package pkg, Locker locker) {
        // Assign locker
        locker.assign(pkg);

        // Generate access code
        AccessCode code = accessCodeService.generateCode(pkg, locker);
        pkg.setAccessCode(code);

        // Notify customer
        notificationService.sendDeliveryNotification(
            pkg.getOrder().getCustomer(),
            pkg,
            locker,
            code
        );
    }
}
```

---

### R6: 3-Day Package Hold Policy ⏳

**Requirement:**
> Packages are held in the locker for a maximum of three days.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Hold Start** | From delivery timestamp |
| **Expiry Calculation** | Delivery time + 72 hours |
| **Daily Checks** | Scheduled job to flag expired packages |
| **Grace Handling** | Notification before expiry |

#### Key Design:
```java
class Package {
    private LocalDateTime deliveredAt;
    private LocalDateTime expiresAt;
    private static final int HOLD_PERIOD_DAYS = 3;

    public void markAsDelivered(Locker locker) {
        this.deliveredAt = LocalDateTime.now();
        this.expiresAt = deliveredAt.plusDays(HOLD_PERIOD_DAYS);
        this.status = PackageStatus.IN_LOCKER;
        this.assignedLocker = locker;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public long getHoursUntilExpiry() {
        if (expiresAt == null) return -1;
        return ChronoUnit.HOURS.between(LocalDateTime.now(), expiresAt);
    }
}

class PackageExpiryScheduler {
    // Runs every hour
    @Scheduled(cron = "0 0 * * * *")
    public void processExpiredPackages() {
        List<Package> expiredPackages = packageRepository.findExpired();

        for (Package pkg : expiredPackages) {
            // Notify before processing
            notificationService.sendExpiryNotification(
                pkg.getOrder().getCustomer(), pkg);

            // Remove from locker
            Locker locker = pkg.getAssignedLocker();
            locker.release();

            // Invalidate access code
            pkg.getAccessCode().invalidate();

            // Trigger refund (R8)
            refundService.processRefund(pkg.getOrder());

            pkg.setStatus(PackageStatus.RETURNED_TO_WAREHOUSE);
        }
    }

    // Runs daily - send reminder 24hrs before expiry
    @Scheduled(cron = "0 0 9 * * *")
    public void sendExpiryReminders() {
        List<Package> soonExpiring = packageRepository
            .findExpiringSoon(Duration.ofHours(24));

        soonExpiring.forEach(pkg ->
            notificationService.sendPickupReminder(
                pkg.getOrder().getCustomer(), pkg));
    }
}
```

---

### R7: Operating Hours Enforcement 🕐

**Requirement:**
> Each locker location has defined opening and closing hours; customers must pick up packages within the 3-day window and the location's operating hours.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Hours Storage** | Per day of week |
| **Holiday Support** | Special closure dates |
| **Access Control** | Block locker access outside hours |
| **Timezone** | Location-local timezone |

#### Key Design:
```java
class OperatingHours {
    private Map<DayOfWeek, TimeRange> weeklySchedule;
    private Set<LocalDate> closedDates; // Holidays
    private ZoneId timezone;

    public boolean isOpenNow() {
        ZonedDateTime localNow = ZonedDateTime.now(timezone);
        LocalDate today = localNow.toLocalDate();
        LocalTime timeNow = localNow.toLocalTime();

        if (closedDates.contains(today)) return false;

        TimeRange todayHours = weeklySchedule.get(localNow.getDayOfWeek());
        if (todayHours == null) return false; // Closed this day

        return todayHours.contains(timeNow);
    }

    public boolean isOpenAt(ZonedDateTime dateTime) {
        LocalDate date = dateTime.withZoneSameInstant(timezone).toLocalDate();
        LocalTime time = dateTime.withZoneSameInstant(timezone).toLocalTime();

        if (closedDates.contains(date)) return false;

        TimeRange hours = weeklySchedule.get(date.getDayOfWeek());
        return hours != null && hours.contains(time);
    }
}

class TimeRange {
    private LocalTime open;
    private LocalTime close;

    public boolean contains(LocalTime time) {
        return !time.isBefore(open) && !time.isAfter(close);
    }
}

class LockerAccessService {
    public boolean canAccessLocker(Customer customer, Locker locker, AccessCode code) {
        // Validate code
        if (!accessCodeService.validateCode(code.getCode(), locker)) {
            throw new InvalidAccessCodeException();
        }

        // Check operating hours
        LockerLocation location = locker.getLocation();
        if (!location.getOperatingHours().isOpenNow()) {
            throw new LocationClosedException(
                "Location is currently closed. " +
                "Opening hours: " + location.getTodayHours());
        }

        // Check 3-day window (R6)
        Package pkg = locker.getCurrentPackage();
        if (pkg.isExpired()) {
            throw new PackageExpiredException("Package hold period has expired");
        }

        return true;
    }
}
```

---

### R8: Auto-Removal & Refund 💸

**Requirement:**
> If a package is not picked up within three days, it is removed from the locker, the locker is released, and the customer is refunded.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Automated Process** | Scheduled job, no manual intervention |
| **Locker Release** | Status reset to AVAILABLE immediately |
| **Refund Trigger** | Full order refund on expiry |
| **Notification** | Inform customer of refund |
| **Audit Log** | Record expiry event |

#### Key Design:
```java
class RefundService {
    public RefundResult processRefund(Order order) {
        RefundResult result = new RefundResult();
        result.setOrderId(order.getOrderId());
        result.setRefundAmount(order.getTotalAmount());
        result.setRefundReason(RefundReason.PACKAGE_NOT_COLLECTED);

        // Process payment reversal
        PaymentGateway gateway = getGatewayForPaymentMethod(order.getPaymentMethod());
        boolean success = gateway.reverseTransaction(order.getTransactionId(),
            order.getTotalAmount());

        if (success) {
            result.setStatus(RefundStatus.PROCESSED);
            result.setProcessedAt(LocalDateTime.now());

            // Notify customer
            notificationService.sendRefundConfirmation(
                order.getCustomer(), result);
        } else {
            result.setStatus(RefundStatus.FAILED);
            alertOpsTeam(order, "Auto-refund failed for expired package");
        }

        return result;
    }
}

enum RefundReason {
    PACKAGE_NOT_COLLECTED,
    CUSTOMER_RETURN,
    DAMAGED_ITEM,
    WRONG_ITEM
}

class ExpiryHandlerService {
    @Transactional
    public void handleExpiredPackage(Package pkg) {
        Locker locker = pkg.getAssignedLocker();

        // Step 1: Release locker
        locker.setCurrentPackage(null);
        locker.setStatus(LockerStatus.AVAILABLE);
        locker.setCurrentAccessCode(null);

        // Step 2: Invalidate access code
        pkg.getAccessCode().invalidate();

        // Step 3: Update package status
        pkg.setStatus(PackageStatus.RETURNED_TO_WAREHOUSE);
        pkg.setRemovedAt(LocalDateTime.now());

        // Step 4: Trigger refund
        refundService.processRefund(pkg.getOrder());

        // Step 5: Log event
        auditService.log(AuditEvent.PACKAGE_EXPIRED, pkg);
    }
}
```

---

### R9: One Package Per Locker 🔒

**Requirement:**
> Multiple lockers are available at every locker location; each locker can only be assigned to one customer/package at a time.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Status Guard** | AVAILABLE → OCCUPIED transition is atomic |
| **Concurrency** | Lock on locker assignment to avoid race condition |
| **Single Occupancy** | currentPackage is always one or null |
| **Status Machine** | AVAILABLE ↔ OCCUPIED lifecycle |

#### Key Design:
```java
class Locker {
    private String lockerId;
    private LockerSize size;
    private LockerStatus status;
    private Package currentPackage; // Always null or exactly one

    public synchronized void assign(Package pkg) {
        if (status != LockerStatus.AVAILABLE) {
            throw new LockerNotAvailableException(
                "Locker " + lockerId + " is already occupied");
        }
        if (currentPackage != null) {
            throw new LockerOccupiedException("Locker already has a package");
        }
        this.currentPackage = pkg;
        this.status = LockerStatus.OCCUPIED;
    }

    public synchronized void release() {
        this.currentPackage = null;
        this.currentAccessCode = null;
        this.status = LockerStatus.AVAILABLE;
    }

    public boolean isAvailable() {
        return status == LockerStatus.AVAILABLE && currentPackage == null;
    }
}

enum LockerStatus {
    AVAILABLE,
    OCCUPIED,
    MAINTENANCE,
    RESERVED,
    OUT_OF_SERVICE
}

class LockerAssignmentService {
    @Transactional
    public Locker assignLockerToPackage(Package pkg, LockerLocation location) {
        LockerSize requiredSize = pkg.getRequiredLockerSize();

        // Find best-fit available locker (smallest size that fits)
        Locker locker = location.getLockers().stream()
            .filter(l -> l.isAvailable())
            .filter(l -> l.getSize().canFit(pkg.getDimensions()))
            .sorted(Comparator.comparing(l -> l.getSize().ordinal()))
            .findFirst()
            .orElseThrow(() -> new NoAvailableLockerException(
                "No available locker of suitable size at " + location.getName()));

        // Atomic assignment
        locker.assign(pkg);
        return locker;
    }
}
```

---

### R10: Code Invalidation After Pickup ✅

**Requirement:**
> Once a package is collected, the locker is closed and locked; the provided access code is invalidated and cannot be reused.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Immediate Invalidation** | Code becomes invalid on successful pickup |
| **Code Lifecycle** | ACTIVE → USED (one-way transition) |
| **Reuse Prevention** | Codes removed from active pool |
| **Locker Reset** | Locker returns to AVAILABLE |

#### Key Design:
```java
class PickupService {
    @Transactional
    public PickupResult processPickup(String enteredCode, Locker locker) {
        // Validate code
        if (!accessCodeService.validateCode(enteredCode, locker)) {
            return PickupResult.failed("Invalid or expired access code");
        }

        Package pkg = locker.getCurrentPackage();
        Customer customer = pkg.getOrder().getCustomer();

        // Step 1: Mark package as collected
        pkg.setStatus(PackageStatus.COLLECTED);
        pkg.setCollectedAt(LocalDateTime.now());

        // Step 2: Invalidate access code (cannot be reused)
        AccessCode code = pkg.getAccessCode();
        code.setStatus(CodeStatus.USED);
        code.setUsedAt(LocalDateTime.now());
        accessCodePool.remove(code.getCode()); // Remove from active pool

        // Step 3: Release locker
        locker.release(); // Sets back to AVAILABLE

        // Step 4: Log pickup event
        auditService.log(AuditEvent.PACKAGE_COLLECTED, pkg, customer);

        // Step 5: Send confirmation
        notificationService.sendPickupConfirmation(customer, pkg);

        return PickupResult.success(pkg);
    }
}

class AccessCode {
    // Code can only transition to USED, never back to ACTIVE
    public void markAsUsed() {
        if (this.status != CodeStatus.ACTIVE) {
            throw new InvalidStateTransitionException(
                "Code can only be used when ACTIVE. Current status: " + status);
        }
        this.status = CodeStatus.USED;
        this.usedAt = LocalDateTime.now();
    }

    public boolean canBeUsed() {
        return status == CodeStatus.ACTIVE && !isExpired();
    }
}
```

---

### R11: Customer Returns via Locker 🔄

**Requirement:**
> Customers may return eligible items by selecting a nearby locker location. Based on package size and location, an available locker is assigned. A new, unique code is sent to the user to open the locker and place the return package.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Return Eligibility** | Check return policy before allowing |
| **New Code Required** | Fresh unique code for return drop-off |
| **Locker Assignment** | Same logic as delivery assignment |
| **Return Package Size** | Customer estimates or uses original dimensions |

#### Key Design:
```java
class ReturnRequest {
    private String returnId;
    private Order originalOrder;
    private List<OrderItem> itemsToReturn;
    private ReturnReason reason;
    private LockerLocation selectedLocation;
    private Locker assignedLocker;
    private AccessCode returnCode;
    private ReturnStatus status;
}

enum ReturnReason {
    DAMAGED,
    WRONG_ITEM,
    NOT_AS_DESCRIBED,
    NO_LONGER_NEEDED,
    DEFECTIVE
}

class ReturnService {
    public ReturnRequest initiateReturn(Customer customer, Order order,
                                        List<OrderItem> items,
                                        LockerLocation location) {
        // Check return eligibility
        validateReturnEligibility(order, items);

        // Create return request
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setOriginalOrder(order);
        returnRequest.setItemsToReturn(items);
        returnRequest.setStatus(ReturnStatus.INITIATED);

        // Estimate return package size
        Dimensions returnDimensions = estimateReturnDimensions(items);
        LockerSize requiredSize = LockerSize.fromDimensions(returnDimensions);

        // Assign locker
        Locker locker = lockerAssignmentService.assignLockerToReturn(
            returnRequest, location);
        returnRequest.setAssignedLocker(locker);

        // Generate new unique access code for return
        AccessCode returnCode = accessCodeService.generateReturnCode(
            returnRequest, locker);
        returnRequest.setReturnCode(returnCode);

        // Notify customer with return code
        notificationService.sendReturnInstructions(customer, returnRequest, returnCode);

        return returnRequest;
    }

    private void validateReturnEligibility(Order order, List<OrderItem> items) {
        // Check return window (e.g., 30 days)
        LocalDate orderDate = order.getOrderDate();
        if (orderDate.plusDays(30).isBefore(LocalDate.now())) {
            throw new ReturnWindowExpiredException("Return window has closed");
        }

        // Check each item's returnability
        items.forEach(item -> {
            if (!item.getProduct().isReturnable()) {
                throw new ItemNotReturnableException(
                    item.getProduct().getName() + " is not eligible for return");
            }
        });
    }
}
```

---

### R12: Logistics Team Pickup & Customer Notification 📬

**Requirement:**
> The logistics team stores returned items in lockers for pickup; the logistics team uses a unique code to collect the returned package. The customer is notified once the return is processed, and the refund policy is applied per product eligibility.

#### Design Implications:

| Aspect | Design Decision |
|--------|-----------------|
| **Separate Logistics Code** | Different from customer return code |
| **Locker-to-Logistics Flow** | Customer drops off → Logistics picks up |
| **Refund Trigger** | On logistics confirmation, not customer drop-off |
| **Refund Policy** | Per-product rules apply |

#### Key Design:
```java
class LogisticsCode {
    private String code;
    private String returnRequestId;
    private String logisticsTeamId;
    private LocalDateTime generatedAt;
    private LocalDateTime expiresAt;
    private CodeStatus status;
}

class LogisticsPickupService {
    @Transactional
    public void processLogisticsPickup(String logisticsCode, Locker locker,
                                        LogisticsAgent agent) {
        // Validate logistics code
        LogisticsCode code = validateLogisticsCode(logisticsCode, locker);

        ReturnRequest returnRequest = code.getReturnRequest();
        Package returnPkg = locker.getCurrentPackage();
        Customer customer = returnRequest.getOriginalOrder().getCustomer();

        // Step 1: Remove package from locker
        locker.release();
        code.setStatus(CodeStatus.USED);

        // Step 2: Update return status
        returnRequest.setStatus(ReturnStatus.COLLECTED_BY_LOGISTICS);
        returnRequest.setCollectedAt(LocalDateTime.now());
        returnRequest.setCollectedBy(agent);

        // Step 3: Apply refund policy per product eligibility
        List<OrderItem> returnedItems = returnRequest.getItemsToReturn();
        double totalRefund = calculateRefundAmount(returnedItems);

        if (totalRefund > 0) {
            RefundResult refund = refundService.processReturnRefund(
                returnRequest.getOriginalOrder(), totalRefund);

            // Step 4: Notify customer
            notificationService.sendReturnProcessedNotification(
                customer, returnRequest, refund);
        } else {
            notificationService.sendReturnReceivedNotification(
                customer, returnRequest);
        }

        // Step 5: Audit log
        auditService.log(AuditEvent.RETURN_COLLECTED, returnRequest, agent);
    }

    private double calculateRefundAmount(List<OrderItem> items) {
        return items.stream()
            .filter(item -> item.getProduct().getRefundPolicy() == RefundPolicy.FULL)
            .mapToDouble(OrderItem::getPricePaid)
            .sum();
    }
}

enum RefundPolicy {
    FULL,
    PARTIAL,
    NO_REFUND,
    STORE_CREDIT_ONLY
}
```

---

## 🏗️ High-Level Architecture

### Core Entity Model:

```
AmazonLockerSystem
  ├─ List<LockerLocation>
  │   ├─ OperatingHours
  │   └─ List<Locker>
  ├─ List<Customer>
  ├─ List<Order>
  │   ├─ List<OrderItem>
  │   └─ Package
  ├─ List<ReturnRequest>
  └─ Services
      ├─ CheckoutService
      ├─ PackagingService
      ├─ DeliveryService
      ├─ LockerAssignmentService
      ├─ AccessCodeService
      ├─ PickupService
      ├─ ReturnService
      ├─ LogisticsPickupService
      ├─ RefundService
      ├─ NotificationService
      └─ ExpirySchedulerService

Locker (Physical Unit)
  ├─ LockerId
  ├─ LockerSize (XS/S/M/L/XL/XXL)
  ├─ LockerStatus
  ├─ CurrentPackage (0 or 1)
  └─ CurrentAccessCode

Package (Shipment)
  ├─ PackageId
  ├─ Order Reference
  ├─ Dimensions
  ├─ RequiredLockerSize
  ├─ AssignedLocker
  ├─ DeliveredAt
  ├─ ExpiresAt (DeliveredAt + 3 days)
  └─ AccessCode

AccessCode
  ├─ 6-digit PIN
  ├─ PackageId
  ├─ LockerId
  ├─ Status (ACTIVE → USED/EXPIRED/INVALIDATED)
  └─ ExpiresAt
```

---

## 🎨 Design Patterns Summary

| Pattern | Usage | Requirements Addressed |
|---------|-------|------------------------|
| **Strategy** | Locker size matching, packaging algorithms | R2, R3 - Best-fit selection |
| **State** | Locker and package status transitions | R5, R9, R10 - Status machine |
| **Observer** | Notifications on delivery/expiry/return | R6, R8, R12 - Event-driven alerts |
| **Factory** | Access code generation | R5, R11 - Code creation |
| **Scheduler** | Expiry detection and auto-refund | R6, R8 - Time-based automation |
| **Repository** | Data access for lockers, packages, codes | R1, R9 - Persistence |
| **Template Method** | Return flow vs delivery flow | R11, R12 - Shared structure |
| **Command** | Audit event logging | R8, R10 - Immutable log |

---

## ✅ SOLID Principles Applied

| Principle | Application |
|-----------|-------------|
| **SRP** | Each service owns exactly one responsibility (pickup, refund, notification) |
| **OCP** | New locker sizes added via enum extension; new notification channels via interface |
| **LSP** | LogisticsPickupService and CustomerPickupService interchangeable via PickupService interface |
| **ISP** | Separate interfaces for NotificationService, RefundService, AccessCodeService |
| **DIP** | Services depend on abstractions; concrete implementations injected |

---

## 📊 Requirements Summary Table

| Req ID | Category | Description | Key Classes |
|--------|----------|-------------|-------------|
| **R1** | Selection | Customer picks locker location at checkout | Customer, CheckoutService, LockerLocation |
| **R2** | Packaging | Multi-item orders combined by size | PackagingService, Order, Dimensions |
| **R3** | Sizes | XS to XXL lockers at every location | LockerSize, Locker, LockerLocation |
| **R4** | Validation | Package must fit locker dimensions | LockerEligibilityService, EligibilityResult |
| **R5** | Access | 6-digit PIN sent on delivery | AccessCodeService, AccessCode |
| **R6** | Hold Policy | 3-day maximum hold period | Package, ExpirySchedulerService |
| **R7** | Hours | Pickup only within operating hours | OperatingHours, LockerAccessService |
| **R8** | Auto-Refund | Removal + refund on expiry | ExpiryHandlerService, RefundService |
| **R9** | Occupancy | One package per locker at a time | Locker (synchronized assign/release) |
| **R10** | Invalidation | Code invalidated after pickup | AccessCode, PickupService |
| **R11** | Returns | Locker assigned for customer return drop-off | ReturnService, ReturnRequest |
| **R12** | Logistics | Logistics collects return; customer refunded | LogisticsPickupService, RefundService |

---

## 💡 Interview Tips

### What to Emphasize:

**1. Locker as a State Machine:**
> ✅ *"A locker transitions: AVAILABLE → OCCUPIED (on delivery) → AVAILABLE (on pickup or expiry). Each transition is atomic and synchronized to prevent race conditions when multiple deliveries hit the same location."*

**2. Access Code Lifecycle:**
> ✅ *"Codes are one-time-use only. On pickup, the code transitions to USED and is removed from the active pool, making reuse impossible even if someone observed the code."*

**3. Separation of Book (Order) from Physical Item (Package):**
> ✅ *"I separate Order metadata from Package (the physical shipment). One order maps to one package, but the package independently tracks locker assignment, expiry, and access code."*

**4. Expiry + Refund Automation:**
> ✅ *"The system runs a scheduled job every hour to detect expired packages. When found, it atomically releases the locker, invalidates the code, and triggers a payment reversal — all in a single transaction."*

**5. Return Flow is Symmetric:**
> ✅ *"The return flow mirrors the delivery flow: customer gets a unique code → drops off at locker → logistics gets a separate code → picks it up. This separation of codes ensures accountability on both ends."*

---

## 🎯 Common Interview Questions

### Q1: "How do you ensure two customers don't get assigned the same locker?"

**Answer:**
> *"The `assign()` method on Locker is synchronized and wrapped in a database transaction. When assigning, we first check `isAvailable()` and then immediately set status to OCCUPIED atomically. Any concurrent attempt will either see OCCUPIED status or wait on the synchronized block, preventing double assignment."*

---

### Q2: "What happens if a customer's package expires but they never got the notification?"

**Answer:**
> *"The system sends a reminder 24 hours before expiry. On expiry, the package is removed, refund is auto-processed, and the customer receives a refund confirmation email. The refund is unconditional — we don't require the customer to have read the notification."*

---

### Q3: "Can two packages use the same access code at the same time?"

**Answer:**
> *"No. The `AccessCodeService` maintains a global pool of active codes and uses a do-while loop with `SecureRandom` to regenerate until uniqueness is guaranteed. Once a code is issued, it's added to the pool and removed on use or expiry."*

---

### Q4: "How do you handle a locker location being closed when a package is about to expire?"

**Answer:**
> *"The 3-day hold clock runs independently of operating hours. If a location is unexpectedly closed, the customer would need to contact support. A more robust design could pause the expiry clock during extended closures — tracked via the `closedDates` set in `OperatingHours` — and resume once reopened."*

---

### Q5: "How is the return code different from the delivery code?"

**Answer:**
> *"They are separate `AccessCode` instances with different purposes. The delivery code is issued to the customer to retrieve their order. The return code is issued for a `ReturnRequest` and is used first by the customer to drop off, then a separate logistics code is generated for the logistics agent to collect. This ensures neither code can be used for the wrong purpose."*

---

## 🔍 Edge Cases to Consider

| Edge Case | Handling |
|-----------|----------|
| **Package too large for any locker** | Blocked at checkout via EligibilityService |
| **All lockers at location full** | Show alternate locations; don't offer locker option |
| **Customer uses code after expiry** | Code marked EXPIRED; access denied with refund message |
| **Locker hardware malfunction** | Mark locker OUT_OF_SERVICE; reassign package |
| **Return item is non-returnable** | Validated before return request is created |
| **Logistics code stolen/guessed** | 6-digit code + time-bound + locker-specific = low risk; add OTP as 2FA for logistics |
| **Power outage at location** | Locker status unchanged in DB; manual override by ops team |
| **Customer enters wrong code 3 times** | Lockout period + alert to support team |

---
