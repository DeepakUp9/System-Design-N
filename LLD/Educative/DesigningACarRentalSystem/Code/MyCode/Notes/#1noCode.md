# Step 1 — Project Foundation & Core Domain Model

---

## 1 — Tech Stack (Why)

| **Technology** | **Version** | **Purpose** |
|---------------|-------------|-------------|
| **Java** | 17+ | Stable LTS, compatible with Spring Boot 3.2.9 |
| **Spring Boot** | 3.2.x | Modern features, Jakarta packages |
| **Spring Data JPA** | - | Entity mapping and repositories |
| **PostgreSQL** | 15 | Reliable relational DB for transactions and constraints |
| **Flyway** | - | Deterministic DB migrations (production friendly) |
| **Lombok** | - | Reduces boilerplate (optional) |
| **Docker Compose** | - | Local dev DB |
| **Maven** | - | Build management |

---

## 2 — Project Structure (Packages)

```
com.example.carrental
 ├─ CarRentalApplication.java
 ├─ config
 ├─ domain
 │   ├─ model
 │   │   ├─ Person, Account, Customer, Receptionist, Driver
 │   │   ├─ Address (Embeddable)
 │   │   ├─ Vehicle (abstract) + Car/Van/Truck/Motorcycle
 │   │   ├─ VehicleReservation
 │   │   ├─ Payment (abstract) + CreditCard/Cash
 │   │   ├─ Equipment, Service
 │   │   ├─ Branch, ParkingStall, VehicleLog
 │   │   └─ enums...
 │   └─ repository
 ├─ service
 └─ web (later, controllers/dtos)
```

---

## 3 — Maven pom.xml

Save as `pom.xml`. (Includes Flyway, Lombok; remove Lombok if you want explicit getters/setters.)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" ...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>car-rental</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <java.version>17</java.version>
    <spring.boot.version>3.2.9</spring.boot.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <!-- Core -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Postgres -->
    <dependency>
      <groupId>org.postgresql</groupId>
      <artifactId>postgresql</artifactId>
    </dependency>

    <!-- Flyway migrations -->
    <dependency>
      <groupId>org.flywaydb</groupId>
      <artifactId>flyway-core</artifactId>
    </dependency>

    <!-- Lombok (optional) -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Testing (later) -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

---

## 4 — Docker Compose (Postgres)

**File:** `docker-compose.yml`

```yaml
version: "3.9"
services:
  db:
    image: postgres:15
    environment:
      POSTGRES_USER: caruser
      POSTGRES_PASSWORD: carpass
      POSTGRES_DB: carrental_dev
    ports:
      - "5432:5432"
    volumes:
      - db-data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U caruser"]
      interval: 10s
      timeout: 5s
      retries: 5

  pgadmin:
    image: dpage/pgadmin4
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@local
      PGADMIN_DEFAULT_PASSWORD: admin
    ports:
      - "8081:80"

volumes:
  db-data:
```

**Run:** `docker compose up -d`

---

## 5 — Flyway Initial Migration

**File:** `src/main/resources/db/migration/V1__init.sql`

This creates core tables and enums using Postgres native enums for some fields where helpful.

```sql
-- V1__init.sql

CREATE TABLE branch (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  street VARCHAR(255),
  city VARCHAR(100),
  state VARCHAR(100),
  postal_code VARCHAR(20),
  country VARCHAR(100),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE parking_stall (
  id UUID PRIMARY KEY,
  branch_id UUID NOT NULL REFERENCES branch(id),
  stall_code VARCHAR(50),
  status VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE vehicle (
  id UUID PRIMARY KEY,
  vehicle_type VARCHAR(50) NOT NULL,
  subtype VARCHAR(50),
  license_plate VARCHAR(50) UNIQUE,
  vin VARCHAR(100) UNIQUE,
  make VARCHAR(100),
  model VARCHAR(100),
  year INTEGER,
  mileage BIGINT,
  status VARCHAR(50) NOT NULL,
  parking_stall_id UUID,
  branch_id UUID,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT fk_branch FOREIGN KEY (branch_id) REFERENCES branch(id),
  CONSTRAINT fk_stall FOREIGN KEY (parking_stall_id) REFERENCES parking_stall(id)
);

CREATE TABLE account (
  id UUID PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255),
  account_type VARCHAR(50) NOT NULL,
  full_name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  phone VARCHAR(50),
  status VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE vehicle_log (
  id UUID PRIMARY KEY,
  vehicle_id UUID NOT NULL REFERENCES vehicle(id),
  log_type VARCHAR(50),
  description TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE reservation (
  id UUID PRIMARY KEY,
  vehicle_id UUID NOT NULL REFERENCES vehicle(id),
  account_id UUID NOT NULL REFERENCES account(id),
  pickup_branch_id UUID,
  dropoff_branch_id UUID,
  start_time TIMESTAMP WITH TIME ZONE,
  end_time TIMESTAMP WITH TIME ZONE,
  status VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE equipment (
  id UUID PRIMARY KEY,
  name VARCHAR(100),
  description TEXT,
  price NUMERIC(10,2)
);

CREATE TABLE service (
  id UUID PRIMARY KEY,
  name VARCHAR(100),
  description TEXT,
  price NUMERIC(10,2)
);

CREATE TABLE reservation_equipment (
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  equipment_id UUID NOT NULL REFERENCES equipment(id),
  PRIMARY KEY (reservation_id, equipment_id)
);

CREATE TABLE reservation_service (
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  service_id UUID NOT NULL REFERENCES service(id),
  PRIMARY KEY (reservation_id, service_id)
);

CREATE TABLE payment (
  id UUID PRIMARY KEY,
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  amount NUMERIC(12,2),
  currency VARCHAR(10),
  method VARCHAR(50),
  status VARCHAR(50),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE fine (
  id UUID PRIMARY KEY,
  reservation_id UUID NOT NULL REFERENCES reservation(id),
  amount NUMERIC(12,2),
  reason TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
```

**Note:** Flyway will run this on application startup (as `spring.flyway.enabled=true`).

---

## 6 — application.yml (Dev)

**File:** `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/carrental_dev
    username: caruser
    password: carpass
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        format_sql: true
    show-sql: true

  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 8080
```

**Note:** `ddl-auto: validate` ensures schema matches Flyway migration. For early dev you can use `update`, but for production use migrations + validate.

---

## 7 — Core JPA Entities (Key Ones)

Create Java files under `com.example.carrental.domain.model`.

### Address.java (Embeddable)

```java
package com.example.carrental.domain.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Address {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
```

### AccountType Enum

```java
package com.example.carrental.domain.model;

public enum AccountType {
    CUSTOMER,
    RECEPTIONIST,
    WORKER
}
```

### Account.java (Simplified)

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    private String password; // store hashed in production

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private String fullName;
    private String email;
    private String phone;

    @Embedded
    private Address address;
}
```

**Note:** For demo we used a single `Account` entity with `accountType`. If you want separate `Customer` and `Receptionist` subclasses mapped via single-table or joined strategy, we can change later.

### VehicleStatus & VehicleType Enums

```java
package com.example.carrental.domain.model;

public enum VehicleStatus {
    AVAILABLE,
    RESERVED,
    IN_SERVICE,
    MAINTENANCE,
    LOST
}

public enum VehicleType {
    CAR, VAN, TRUCK, MOTORCYCLE
}
```

### Vehicle.java (Base)

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle")
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    private String subtype; // e.g., economy, luxury, etc.

    @Column(unique = true)
    private String licensePlate;

    @Column(unique = true)
    private String vin;

    private String make;
    private String model;
    private Integer year;
    private Long mileage;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_stall_id")
    private ParkingStall parkingStall;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private CarRentalBranch branch;
}
```

**Note:** For simplicity, we made `Vehicle` concrete. If you prefer subclass tables (Car, Truck), we can create `@Inheritance(strategy=InheritanceType.JOINED)` later.

### CarRentalBranch.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "branch")
@NoArgsConstructor
@AllArgsConstructor
public class CarRentalBranch {
    @Id
    private UUID id;

    private String name;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private Set<ParkingStall> parkingStalls;
}
```

### ParkingStall.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "parking_stall")
@NoArgsConstructor
@AllArgsConstructor
public class ParkingStall {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private CarRentalBranch branch;

    private String stallCode;

    private String status; // enum possible, keep string for now

    @OneToOne(mappedBy = "parkingStall")
    private Vehicle vehicle;
}
```

### VehicleLog.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle_log")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLog {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    private String logType;
    private String description;
    private Instant createdAt = Instant.now();
}
```

### ReservationStatus Enum

```java
package com.example.carrental.domain.model;

public enum ReservationStatus {
    PENDING,
    CONFIRMED,
    PICKED_UP,
    COMPLETED,
    CANCELLED
}
```

### VehicleReservation.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReservation {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "pickup_branch_id")
    private CarRentalBranch pickupBranch;

    @ManyToOne
    @JoinColumn(name = "dropoff_branch_id")
    private CarRentalBranch dropoffBranch;

    private Instant startTime;
    private Instant endTime;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    private Instant createdAt = Instant.now();

    @ManyToMany
    @JoinTable(
      name = "reservation_equipment",
      joinColumns = @JoinColumn(name = "reservation_id"),
      inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private Set<Equipment> equipments;

    @ManyToMany
    @JoinTable(
      name = "reservation_service",
      joinColumns = @JoinColumn(name = "reservation_id"),
      inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<Service> services;
}
```

### Equipment.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "equipment")
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
}
```

### Service.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
}
```

### Payment.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private VehicleReservation reservation;

    private BigDecimal amount;
    private String currency;
    private String method;
    private String status;
    private Instant createdAt = Instant.now();
}
```

### Fine.java

```java
package com.example.carrental.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "fine")
@NoArgsConstructor
@AllArgsConstructor
public class Fine {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private VehicleReservation reservation;

    private BigDecimal amount;
    private String reason;
    private Instant createdAt = Instant.now();
}
```

---

## 8 — Repository Interfaces

Create under `com.example.carrental.domain.repository`

### Example: VehicleRepository.java

```java
package com.example.carrental.domain.repository;

import com.example.carrental.domain.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    // add custom queries for search by type, model, availability
}
```

**Repeat for:**
- `VehicleReservationRepository extends JpaRepository<VehicleReservation, UUID>`
- `AccountRepository extends JpaRepository<Account, UUID>`
- `PaymentRepository`, `BranchRepository`, `ParkingStallRepository`, `EquipmentRepository`, `ServiceRepository`, `VehicleLogRepository`

---

## 9 — Spring Boot Main Application

### CarRentalApplication.java

```java
package com.example.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarRentalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarRentalApplication.class, args);
    }
}
```

---

## 10 — Useful Dev Utilities & Helpers (Notes)

### ID Generation
- Use `UUID.randomUUID()` for IDs on creation
- Consider `@PrePersist` to set id automatically

### Security
- Store passwords hashed (BCrypt) — we'll add Security in later steps

### Search and Availability
Implement query that checks reservations overlapping with requested time window:

```sql
NOT (existing.endTime <= requested.start OR existing.startTime >= requested.end)
```

Use this to filter out vehicles with conflicting reservations.

### Concurrency / Double-Booking Protection

**Option 1:** Use optimistic locking (`@Version`) on Vehicle or Reservation rows and handle `OptimisticLockException`

**Option 2:** For guaranteed atomic allocation, use DB-level `SELECT ... FOR UPDATE` in a transaction to lock candidate vehicle rows while creating reservation

Later we'll implement a `ReservationService` that runs in a `@Transactional` boundary and obtains locks when necessary.

### Payment Workflow

- Support partial payments and holds
- Model `Payment.status` with enum values: `PENDING`, `COMPLETED`, `FAILED`, `REFUNDED`
- Use idempotency keys for payment endpoints to avoid duplicate charges

### Auditing

Add `createdBy`, `createdAt`, `updatedAt` fields with Spring Data JPA auditing.

---

## 11 — Run Steps (Dev)

1. **Start Docker:**
   ```bash
   docker compose up -d
   ```
   (starts postgres & pgadmin)

2. **Build & Run App:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Flyway runs migrations; app connects to DB**

---

## 12 — Edge-Case Checklist (Covered or To Implement Next)

These are things we must handle in later steps:

- ✅ Double booking / concurrency (lock strategies)
- ✅ Partial payments / deposits / refunds
- ✅ Overlapping reservations across branches (vehicle transfer)
- ✅ Late returns → fines & automated notifications (schedulers)
- ✅ Maintenance & cleaning workflows (post-return)
- ✅ Penalties for damage & insurance claims
- ✅ Payment gateway integration with idempotency and webhooks
- ✅ Audit logs & soft deletes
- ✅ Multi-currency/pricing strategies (seasonal/dynamic)
- ✅ Search scalability (CQRS / read replicas / caching)
- ✅ Pagination, rate limiting, and validation
- ✅ Security (authn/authz), RBAC for receptionist vs customer
- ✅ Tests: unit, integration, contract tests

**We'll implement and test these in later steps.**

---

## 13 — What I Delivered in Step 1 (Summary)

✅ Full project skeleton decisions  
✅ `pom.xml` with essentials  
✅ `docker-compose.yml` for Postgres  
✅ Flyway V1 migration creating base tables  
✅ `application.yml` for dev  
✅ Key JPA entities and enums (Address, Account, Vehicle, Branch, Reservation, Payment, Equipment, Service, Fine, ParkingStall, VehicleLog)  
✅ Repo interfaces and main app boot class  
✅ Run instructions and edge-case checklist

---

## 📊 Entity Relationship Overview

```
Account (Customer/Receptionist)
    ↓ 1:N
VehicleReservation
    ↓ N:1
Vehicle
    ↓ N:1
CarRentalBranch
    ↓ 1:N
ParkingStall

VehicleReservation
    ↓ 1:1
Payment

VehicleReservation
    ↓ 1:1
Fine

VehicleReservation
    ↓ N:N
Equipment, Service
```

---

**Foundation complete! Ready for Step 2. 🚀**