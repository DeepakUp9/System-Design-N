# Step 2 — Domain Services & Concurrency Control

We'll implement **Option B: Domain-Driven Design (DDD)** for the Reservation domain and related services. This delivers production-quality, focused code for the core domain services, including concurrency control, availability checks, DTOs, exceptions, and repository queries.

---

## What This Step Delivers

All runnable Java + JPA code snippets you can copy into the project from Step 1:

✅ Domain exceptions  
✅ DTOs (request/response)  
✅ Repository additions (availability queries + locks)  
✅ Domain services interfaces  
✅ ReservationServiceImpl: transactional reservation creation with pessimistic locking (prevents double-booking)  
✅ VehicleService: availability search  
✅ PaymentService skeleton (for later integration)  
✅ Index / DB hints and notes for scale & concurrency fallback options  
✅ How to test and run locally

---

## 1 — Concurrency Strategy and Rationale

We use **aggregate-root pattern**: `Vehicle` and `VehicleReservation` belong to the Reservation aggregate. To prevent double-booking we will:

1. Attempt to reserve with **pessimistic lock** on the chosen vehicle row within a transaction (`PESSIMISTIC_WRITE`). This locks the vehicle row while we check reservations and create one.

2. Use an **availability query** that checks overlapping reservations.

3. In case of lock timeout or DB contention, we catch and retry (few times) or return a friendly error for client to retry.

### Why Pessimistic Lock?

* ✅ Simpler and deterministic for small-to-medium scale
* ✅ Prevents race at DB level
* ✅ Later we can evolve to optimistic locking with version and a queuing layer for high throughput

---

## 2 — Exceptions

Create `com.example.carrental.domain.exception` package.

### DomainException.java

```java
package com.example.carrental.domain.exception;

public class DomainException extends RuntimeException {
    public DomainException(String msg) { 
        super(msg); 
    }
}
```

### VehicleNotFoundException.java

```java
package com.example.carrental.domain.exception;

public class VehicleNotFoundException extends DomainException {
    public VehicleNotFoundException(String id) { 
        super("Vehicle not found: " + id); 
    }
}
```

### VehicleNotAvailableException.java

```java
package com.example.carrental.domain.exception;

public class VehicleNotAvailableException extends DomainException {
    public VehicleNotAvailableException(String id) { 
        super("Vehicle not available for requested time: " + id); 
    }
}
```

### ReservationConflictException.java

```java
package com.example.carrental.domain.exception;

public class ReservationConflictException extends DomainException {
    public ReservationConflictException(String msg) { 
        super(msg); 
    }
}
```

### ReservationNotFoundException.java

```java
package com.example.carrental.domain.exception;

public class ReservationNotFoundException extends DomainException {
    public ReservationNotFoundException(String id) { 
        super("Reservation not found: " + id); 
    }
}
```

### PaymentFailedException.java

```java
package com.example.carrental.domain.exception;

public class PaymentFailedException extends DomainException {
    public PaymentFailedException(String msg) { 
        super("Payment failed: " + msg); 
    }
}
```

---

## 3 — DTOs (Requests & Responses)

Create `com.example.carrental.service.dto` package.

### ReserveVehicleRequest.java

```java
package com.example.carrental.service.dto;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ReserveVehicleRequest(
    UUID customerId,
    UUID vehicleId,
    Instant startTime,
    Instant endTime,
    Set<UUID> equipmentIds,
    Set<UUID> serviceIds,
    boolean payNow // simple flag for now
) { }
```

### ReserveVehicleResponse.java

```java
package com.example.carrental.service.dto;

import java.time.Instant;
import java.util.UUID;

public record ReserveVehicleResponse(
    UUID reservationId,
    UUID vehicleId,
    UUID customerId,
    Instant startTime,
    Instant endTime,
    String status
) { }
```

---

## 4 — Repository Additions

Update or add custom methods in repository interfaces.

### VehicleRepository.java

```java
package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.Vehicle;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    // Pessimistic lock when loading a specific vehicle to allocate it
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT v FROM Vehicle v WHERE v.id = :id")
    Optional<Vehicle> findByIdForUpdate(@Param("id") UUID id);

    // Find candidate vehicles by type & location (no lock) — we'll further filter availability
    @Query("SELECT v FROM Vehicle v WHERE v.vehicleType = :type AND v.branch.id = :branchId AND v.status = 'AVAILABLE'")
    List<Vehicle> findByTypeAndBranchAvailable(
        @Param("type") com.example.carrental.domain.model.VehicleType type,
        @Param("branchId") UUID branchId
    );

    // Alternative: search available vehicles by time range: exclude vehicles having overlapping reservation
    @Query("""
    SELECT v FROM Vehicle v
    WHERE v.branch.id = :branchId
      AND v.vehicleType = :type
      AND v.status = 'AVAILABLE'
      AND v.id NOT IN (
        SELECT r.vehicle.id FROM VehicleReservation r
        WHERE r.status IN ('PENDING', 'CONFIRMED', 'PICKED_UP')
          AND NOT (r.endTime <= :start OR r.startTime >= :end)
      )
    """)
    List<Vehicle> findAvailableByTypeAndBranchAndTimeRange(
        @Param("type") com.example.carrental.domain.model.VehicleType type,
        @Param("branchId") UUID branchId,
        @Param("start") Instant start,
        @Param("end") Instant end
    );
}
```

### VehicleReservationRepository.java

```java
package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.VehicleReservation;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, UUID> {

    // Find reservations for a vehicle that overlap a time range
    @Query("""
      SELECT r FROM VehicleReservation r
      WHERE r.vehicle.id = :vehicleId
        AND r.status IN ('PENDING','CONFIRMED','PICKED_UP')
        AND NOT (r.endTime <= :start OR r.startTime >= :end)
    """)
    List<VehicleReservation> findOverlapping(
        @Param("vehicleId") UUID vehicleId,
        @Param("start") Instant start,
        @Param("end") Instant end
    );

    // Optional: use for read-only checks
    @Query("""
      SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM VehicleReservation r
      WHERE r.vehicle.id = :vehicleId
        AND r.status IN ('PENDING','CONFIRMED','PICKED_UP')
        AND NOT (r.endTime <= :start OR r.startTime >= :end)
    """)
    boolean existsOverlapping(
        @Param("vehicleId") UUID vehicleId,
        @Param("start") Instant start,
        @Param("end") Instant end
    );
}
```

**Note:** Adjust enum string names if your enums differ.

---

## 5 — Service Interfaces (DDD Style)

### ReservationService.java

```java
package com.example.carrental.domain.service;

import com.example.carrental.service.dto.ReserveVehicleRequest;
import com.example.carrental.service.dto.ReserveVehicleResponse;

public interface ReservationService {
    ReserveVehicleResponse reserveVehicle(ReserveVehicleRequest request);
    // Other methods to be added later: 
    // cancelReservation, updateReservation, getReservation, listReservationsByCustomer...
}
```

### VehicleService.java

```java
package com.example.carrental.domain.service;

import com.example.carrental.domain.model.Vehicle;
import com.example.carrental.domain.model.VehicleType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface VehicleService {
    List<Vehicle> searchAvailable(VehicleType type, UUID branchId, Instant start, Instant end);
}
```

---

## 6 — ReservationServiceImpl (Core Implementation)

```java
package com.example.carrental.domain.service.impl;

import com.example.carrental.domain.exception.*;
import com.example.carrental.domain.model.*;
import com.example.carrental.domain.repository.*;
import com.example.carrental.domain.service.ReservationService;
import com.example.carrental.service.dto.ReserveVehicleRequest;
import com.example.carrental.service.dto.ReserveVehicleResponse;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final VehicleRepository vehicleRepository;
    private final VehicleReservationRepository reservationRepository;
    private final AccountRepository accountRepository;
    private final EquipmentRepository equipmentRepository;
    private final ServiceRepository serviceRepository;
    private final EntityManager em;

    private static final int LOCK_RETRY = 3;
    private static final long LOCK_RETRY_DELAY_MS = 150L;

    @Override
    @Transactional
    public ReserveVehicleResponse reserveVehicle(ReserveVehicleRequest request) {
        // Validate times
        if (request.startTime().isAfter(request.endTime()) || 
            request.startTime().equals(request.endTime())) {
            throw new ReservationConflictException("Invalid time window");
        }

        UUID vehicleId = request.vehicleId();
        UUID customerId = request.customerId();
        Instant start = request.startTime();
        Instant end = request.endTime();

        // Ensure account exists
        var account = accountRepository.findById(customerId)
                .orElseThrow(() -> new DomainException("Customer account not found: " + customerId));

        // Try to get vehicle with PESSIMISTIC_WRITE to avoid concurrent allocations
        Vehicle vehicle = tryLockVehicle(vehicleId);

        // Re-check availability: overlapping reservations
        boolean existsOverlap = reservationRepository.existsOverlapping(vehicleId, start, end);
        if (existsOverlap) {
            throw new VehicleNotAvailableException(vehicleId.toString());
        }

        // Build reservation
        VehicleReservation reservation = new VehicleReservation();
        reservation.setId(UUID.randomUUID());
        reservation.setVehicle(vehicle);
        reservation.setAccount(account);
        reservation.setStartTime(start);
        reservation.setEndTime(end);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setCreatedAt(Instant.now());

        // Attach equipments and services if present
        if (request.equipmentIds() != null && !request.equipmentIds().isEmpty()) {
            var eqs = equipmentRepository.findAllById(request.equipmentIds());
            reservation.setEquipments(new HashSet<>(eqs));
        }
        if (request.serviceIds() != null && !request.serviceIds().isEmpty()) {
            var svs = serviceRepository.findAllById(request.serviceIds());
            reservation.setServices(new HashSet<>(svs));
        }

        // Calculate estimated cost (simple example)
        double estimated = calculateEstimatedCost(
            vehicle, 
            reservation.getStartTime(), 
            reservation.getEndTime(),
            reservation.getEquipments(), 
            reservation.getServices()
        );
        reservation.setEstimatedCost(estimated);

        // Persist reservation
        reservationRepository.save(reservation);

        // Update vehicle status to RESERVED
        vehicle.setStatus(VehicleStatus.RESERVED);
        vehicleRepository.save(vehicle);

        // If payNow flagged, delegate to PaymentService (not implemented here)
        // Payment creation/integration handled in PaymentService — can be called here

        return new ReserveVehicleResponse(
            reservation.getId(), 
            vehicle.getId(), 
            account.getId(),
            reservation.getStartTime(), 
            reservation.getEndTime(), 
            reservation.getStatus().name()
        );
    }

    private Vehicle tryLockVehicle(UUID vehicleId) {
        int attempts = 0;
        while (true) {
            attempts++;
            try {
                // Use repository method annotated with @Lock(PESSIMISTIC_WRITE)
                return vehicleRepository.findByIdForUpdate(vehicleId)
                        .orElseThrow(() -> new VehicleNotFoundException(vehicleId.toString()));
            } catch (Exception e) {
                if (attempts >= LOCK_RETRY) {
                    throw new ReservationConflictException(
                        "Unable to lock vehicle for reservation (contention). Try again later."
                    );
                }
                try {
                    Thread.sleep(LOCK_RETRY_DELAY_MS);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private double calculateEstimatedCost(
        Vehicle vehicle, 
        Instant start, 
        Instant end, 
        Set<Equipment> equipments, 
        Set<Service> services
    ) {
        long seconds = end.getEpochSecond() - start.getEpochSecond();
        long hours = Math.max(1, seconds / 3600);
        double base = hours * (vehicle.getHourlyRate() > 0 ? 
            vehicle.getHourlyRate() : vehicle.getDailyRate() / 24.0);
        double eqCost = (equipments == null ? 0.0 : 
            equipments.stream().mapToDouble(e -> e.getPrice().doubleValue()).sum());
        double svcCost = (services == null ? 0.0 : 
            services.stream().mapToDouble(s -> s.getPrice().doubleValue()).sum());
        return base + eqCost + svcCost;
    }
}
```

### Key Implementation Notes:

* `@Transactional` ensures all DB work is atomic
* We lock vehicle record (pessimistic) before checking overlapping reservations — prevents race
* We set vehicle status to `RESERVED` after saving reservation
* Payment flow is decoupled; `PaymentService` should be called after reservation creation or as part of a saga if distributed

---

## 7 — VehicleServiceImpl (Search)

```java
package com.example.carrental.domain.service.impl;

import com.example.carrental.domain.model.Vehicle;
import com.example.carrental.domain.repository.VehicleRepository;
import com.example.carrental.domain.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    @Override
    public List<Vehicle> searchAvailable(
        com.example.carrental.domain.model.VehicleType type, 
        UUID branchId, 
        Instant start, 
        Instant end
    ) {
        // Use repository query that excludes vehicles with overlapping reservations
        return vehicleRepository.findAvailableByTypeAndBranchAndTimeRange(
            type, branchId, start, end
        );
    }
}
```

---

## 8 — PaymentService Skeleton (For Later)

```java
package com.example.carrental.domain.service;

import java.util.UUID;

public interface PaymentService {
    // Try to capture payment; returns transaction id or throws PaymentFailedException
    String capturePayment(UUID reservationId, double amount, String currency, String paymentMethod);
}
```

**Note:** Implementations will integrate with payment gateway, use idempotency keys, webhooks, and store Payment entity.

---

## 9 — DB Index & Performance Suggestions

Add indexes in Flyway to speed up availability checks.

**Create:** `src/main/resources/db/migration/V2__indexes.sql`

```sql
-- V2__indexes.sql

CREATE INDEX idx_reservation_vehicle_times ON reservation(vehicle_id, start_time, end_time);
CREATE INDEX idx_vehicle_branch_type_status ON vehicle(branch_id, vehicle_type, status);
CREATE INDEX idx_reservation_status ON reservation(status);
```

These help queries that find overlapping reservations and available vehicles.

---

## 10 — Scaling / Advanced Concurrency Options (Summary)

If throughput grows, you can replace pessimistic locking + synchronous reservation with:

### Option A: Allocation Queue (Kafka)
Requests enqueue and a single consumer assigns vehicles — avoids DB contention.

**Implementation approach:**
```java
// Producer: REST controller publishes reservation request to Kafka
@PostMapping("/reserve")
public CompletableFuture<ReservationResponse> reserve(@RequestBody ReservationRequest req) {
    kafkaTemplate.send("reservation-requests", req);
    return reservationStatusPoller.pollForResult(req.getRequestId());
}

// Consumer: Single instance processes queue sequentially
@KafkaListener(topics = "reservation-requests")
public void processReservation(ReservationRequest req) {
    try {
        var result = reservationService.reserveVehicle(req);
        kafkaTemplate.send("reservation-results", result);
    } catch (Exception e) {
        // handle failure, DLQ, etc.
    }
}
```

### Option B: Optimistic Locking
Use `@Version` and retry on `OptimisticLockException`.

**Implementation approach:**
```java
// Add to Vehicle entity
@Version
private Long version;

// In service, retry on conflict
@Transactional
public ReservationResponse reserveVehicle(ReservationRequest req) {
    int retries = 3;
    while (retries-- > 0) {
        try {
            Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new VehicleNotFoundException());
            // ... check availability and create reservation
            return response;
        } catch (OptimisticLockException e) {
            if (retries == 0) throw new ReservationConflictException("Too much contention");
            Thread.sleep(100); // backoff
        }
    }
}
```

### Option C: Distributed Lock (Redis RedLock)
For multi-instance safety—careful with correctness.

**Implementation approach:**
```java
// Use Redisson library
@Autowired
private RedissonClient redisson;

@Transactional
public ReservationResponse reserveVehicle(ReservationRequest req) {
    RLock lock = redisson.getLock("vehicle:" + req.getVehicleId());
    try {
        if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
            // check availability and create reservation
            Vehicle vehicle = vehicleRepository.findById(req.getVehicleId())
                .orElseThrow(() -> new VehicleNotFoundException());
            // ... rest of logic
        } else {
            throw new ReservationConflictException("Could not acquire lock");
        }
    } finally {
        lock.unlock();
    }
}
```

### Option D: CQRS
Read database for fast availability search (denormalized), write DB for authoritative reservations.

**Implementation approach:**
```java
// Read model: Fast search using denormalized view
@Service
public class VehicleSearchService {
    @Autowired
    private VehicleReadRepository readRepo; // connects to read-optimized DB/cache
    
    public List<VehicleAvailability> searchAvailable(SearchCriteria criteria) {
        return readRepo.findAvailableVehicles(criteria); // fast query, no joins
    }
}

// Write model: Authoritative source
@Service
public class ReservationCommandService {
    @Transactional
    public ReservationResponse createReservation(ReservationRequest req) {
        // write to main DB with pessimistic lock
        Vehicle vehicle = vehicleRepository.findByIdForUpdate(req.getVehicleId());
        // ... create reservation
        
        // Publish event to update read model
        eventPublisher.publish(new ReservationCreatedEvent(reservation));
        return response;
    }
}

// Event handler updates read model
@EventListener
public void onReservationCreated(ReservationCreatedEvent event) {
    vehicleAvailabilityCache.updateAvailability(event.getVehicleId());
}
```

### Option E: Multi-Region / Multi-Datacenter
Design a global allocator with strong consistency (leader election or sharding vehicles by branch).

**Implementation approach:**
```java
// Shard by branch: Each region owns certain branches
@Service
public class RegionalReservationService {
    
    @Value("${region.owned-branches}")
    private Set<UUID> ownedBranches;
    
    public ReservationResponse reserveVehicle(ReservationRequest req) {
        UUID branchId = req.getBranchId();
        
        if (ownedBranches.contains(branchId)) {
            // This region handles this branch - process locally
            return localReservationService.reserveVehicle(req);
        } else {
            // Forward to correct region via HTTP/gRPC
            String targetRegion = branchToRegionMapping.get(branchId);
            return remoteReservationClient.reserveInRegion(targetRegion, req);
        }
    }
}

// Or use distributed consensus (Raft/Paxos) for leader election
// Leader instance handles all reservations, followers redirect
@Service
public class LeaderAwareReservationService {
    
    @Autowired
    private RaftConsensusService raftService;
    
    public ReservationResponse reserveVehicle(ReservationRequest req) {
        if (raftService.isLeader()) {
            return reservationService.reserveVehicle(req);
        } else {
            // Forward to current leader
            String leaderUrl = raftService.getLeaderUrl();
            return restTemplate.postForObject(leaderUrl + "/reserve", req, ReservationResponse.class);
        }
    }
}
```

**Note:** These are advanced patterns. Start with pessimistic locking (Step 2), then evolve based on actual throughput requirements.

---

## 11 — Tests & Validation

Write integration tests that simulate concurrent reservation attempts:

```java
// Test: Start 2 threads calling reserveVehicle for same vehicle 
// and overlapping times; assert only one succeeds

@Test
void testConcurrentReservation() {
    // Implementation here
}
```

**Test Coverage:**
* ✅ Overlapping detection queries
* ✅ Cancellation transitions revert vehicle status to `AVAILABLE`
* ✅ Concurrent reservation attempts (only one succeeds)

---

## 12 — Next Items for Step 3

When you're ready for the next step, we'll implement:

1. ✅ Reservation cancellation & modification flows (with rules & state transitions)
2. ✅ Payment integration + idempotency + partial payments & holds (deposit)
3. ✅ Notification events (event publisher + subscribers)
4. ✅ Scheduler to detect overdue returns and auto-create fines
5. ✅ Controllers + DTO validation + error handling (`RestControllerAdvice`)
6. ✅ Integration tests and concurrency tests

---

## 13 — Files to Add Now (Summary)

Add these new classes/files to your project:

```
com.example.carrental/
├── domain/
│   ├── exception/
│   │   ├── DomainException.java
│   │   ├── VehicleNotFoundException.java
│   │   ├── VehicleNotAvailableException.java
│   │   ├── ReservationConflictException.java
│   │   ├── ReservationNotFoundException.java
│   │   └── PaymentFailedException.java
│   ├── service/
│   │   ├── ReservationService.java
│   │   ├── VehicleService.java
│   │   ├── PaymentService.java
│   │   └── impl/
│   │       ├── ReservationServiceImpl.java
│   │       └── VehicleServiceImpl.java
│   └── repository/
│       ├── VehicleRepository.java (updated)
│       └── VehicleReservationRepository.java (updated)
└── service/
    └── dto/
        ├── ReserveVehicleRequest.java
        └── ReserveVehicleResponse.java

resources/
└── db/migration/
    └── V2__indexes.sql
```

---

## 14 — How to Run & Smoke Test Locally

### Step 1: Start Database
```bash
docker compose up -d
```

### Step 2: Build & Run Application
```bash
./mvnw spring-boot:run
```

### Step 3: Seed Data
Use a quick data loader or insert seed rows into `branch`, `vehicle`, `account` tables. You can create a `CommandLineRunner` to bootstrap test data.

### Step 4: Test Reservation
Call `ReservationService.reserveVehicle(...)` from:
* A test class
* Temporary REST controller to simulate reservation

### Step 5: Run Concurrent Tests
Ensure lock works by running concurrent reservation attempts.

---

## 📊 Implementation Checklist

| **Component** | **Status** | **File** |
|--------------|-----------|----------|
| Domain Exceptions | ✅ Ready | `domain/exception/*` |
| DTOs | ✅ Ready | `service/dto/*` |
| Repository Queries | ✅ Ready | Updated repos |
| Service Interfaces | ✅ Ready | `domain/service/*` |
| ReservationServiceImpl | ✅ Ready | `impl/ReservationServiceImpl` |
| VehicleServiceImpl | ✅ Ready | `impl/VehicleServiceImpl` |
| PaymentService | 🔄 Skeleton | `PaymentService` |
| DB Indexes | ✅ Ready | `V2__indexes.sql` |
| Tests | ⏳ Next Step | - |

---

**Step 2 Complete! Ready for Step 3 when you are. 🚀**