# Movie Ticket Booking System - Package Structure

## Recommended by ChatGPT - Spring Boot Clean Architecture

```
com.example.movieticket
 ├─ api
 │   ├─ controller (REST controllers)
 │   └─ dto (request/response DTOs)
 ├─ domain
 │   ├─ model (entities: Movie, Cinema, Hall, Show, ShowSeat, Booking, Payment, User)
 │   └─ enums (SeatStatus, BookingStatus, PaymentStatus, SeatType)
 ├─ repository (Spring Data JPA repositories)
 ├─ service
 │   ├─ impl (ReservationServiceImpl, BookingServiceImpl, PaymentServiceImpl, NotificationServiceImpl)
 │   └─ interfaces (ReservationService, PaymentService, NotificationService)
 ├─ event (domain events & listeners)
 ├─ util (Idempotency util, Time util)
 └─ config (Redis config, Scheduler config)
```

---

## Detailed Package Breakdown

### 📁 `api` - Presentation Layer
**Purpose:** Handles HTTP requests/responses and API contracts

#### `api/controller`
- `MovieController.java` - Browse movies, search
- `ShowController.java` - Get show times, available seats
- `BookingController.java` - Create/cancel bookings
- `PaymentController.java` - Process payments
- `UserController.java` - User management

#### `api/dto`
**Request DTOs:**
- `BookingRequest.java`
- `PaymentRequest.java`
- `SeatSelectionRequest.java`

**Response DTOs:**
- `BookingResponse.java`
- `ShowDetailsResponse.java`
- `SeatAvailabilityResponse.java`

---

### 📁 `domain` - Business Logic Core
**Purpose:** Core business entities and domain logic

#### `domain/model` - Entities
```java
- Movie.java (id, title, duration, genre, language)
- Cinema.java (id, name, location, halls)
- Hall.java (id, name, totalSeats, seatLayout)
- Show.java (id, movie, hall, startTime, endTime, price)
- ShowSeat.java (id, show, seatNumber, status, seatType)
- Booking.java (id, user, show, seats, status, bookingTime)
- Payment.java (id, booking, amount, status, paymentMethod)
- User.java (id, name, email, phone)
```

#### `domain/enums`
```java
- SeatStatus.java (AVAILABLE, RESERVED, BOOKED, BLOCKED)
- BookingStatus.java (PENDING, CONFIRMED, CANCELLED, EXPIRED)
- PaymentStatus.java (PENDING, SUCCESS, FAILED, REFUNDED)
- SeatType.java (NORMAL, PREMIUM, VIP, RECLINER)
```

---

### 📁 `repository` - Data Access Layer
**Purpose:** Database operations using Spring Data JPA

```java
- MovieRepository.java
- CinemaRepository.java
- HallRepository.java
- ShowRepository.java
- ShowSeatRepository.java
- BookingRepository.java
- PaymentRepository.java
- UserRepository.java
```

**Custom Query Examples:**
```java
List<Show> findByMovieAndCinemaAndDate(Movie movie, Cinema cinema, LocalDate date);
List<ShowSeat> findByShowAndStatus(Show show, SeatStatus status);
Optional<Booking> findByIdAndUserId(Long id, Long userId);
```

---

### 📁 `service` - Business Logic Layer
**Purpose:** Implements core business operations

#### `service/interfaces`
```java
- ReservationService.java
  → reserveSeats(showId, seatIds, userId)
  → releaseExpiredReservations()
  
- BookingService.java
  → createBooking(bookingRequest)
  → confirmBooking(bookingId)
  → cancelBooking(bookingId, userId)
  
- PaymentService.java
  → processPayment(bookingId, paymentDetails)
  → refundPayment(bookingId)
  
- NotificationService.java
  → sendBookingConfirmation(booking)
  → sendPaymentReceipt(payment)
  → sendCancellationNotification(booking)
```

#### `service/impl`
```java
- ReservationServiceImpl.java
  → Handles seat locking with Redis
  → Implements timeout mechanism (10-15 mins)
  
- BookingServiceImpl.java
  → Orchestrates booking flow
  → Validates seat availability
  → Manages booking lifecycle
  
- PaymentServiceImpl.java
  → Integrates with payment gateway
  → Handles idempotency for payments
  → Updates booking status
  
- NotificationServiceImpl.java
  → Sends email/SMS notifications
  → Uses async processing
```

---

### 📁 `event` - Event-Driven Architecture
**Purpose:** Decouple operations using domain events

#### Events:
```java
- BookingCreatedEvent.java
- PaymentCompletedEvent.java
- BookingCancelledEvent.java
- ReservationExpiredEvent.java
```

#### Listeners:
```java
- BookingEventListener.java
  → onBookingCreated() - Send confirmation
  → onBookingCancelled() - Release seats
  
- PaymentEventListener.java
  → onPaymentCompleted() - Confirm booking
  → onPaymentFailed() - Release reservation
```

---

### 📁 `util` - Utility Classes
**Purpose:** Reusable helper functions

```java
- IdempotencyUtil.java
  → generateIdempotencyKey()
  → checkDuplicateRequest()
  
- TimeUtil.java
  → isShowTimeValid()
  → calculateExpiryTime()
  → formatDateTime()
  
- SeatUtil.java
  → validateSeatNumbers()
  → calculateTotalPrice()
```

---

### 📁 `config` - Configuration Classes
**Purpose:** Spring Boot configurations

```java
- RedisConfig.java
  → Configure Redis for distributed locking
  → Set TTL for seat reservations
  
- SchedulerConfig.java
  → Enable @Scheduled tasks
  → Configure thread pool
  
- SecurityConfig.java (optional)
  → JWT authentication
  → Role-based access control
  
- AsyncConfig.java
  → Configure async task executor
  → For notifications
```

---

## Key Design Principles Applied

### ✅ Separation of Concerns
- **API Layer** - Only handles HTTP
- **Service Layer** - Contains business logic
- **Repository Layer** - Only database operations
- **Domain Layer** - Core entities & business rules

### ✅ Dependency Flow
```
Controller → Service → Repository
     ↓          ↓
    DTO      Domain
```

### ✅ SOLID Principles
- **Single Responsibility** - Each class has one job
- **Interface Segregation** - Service interfaces are focused
- **Dependency Inversion** - Depend on abstractions (interfaces)

---

## Why This Structure?

| Benefit | Description |
|---------|-------------|
| **Scalability** | Easy to add new features in isolated packages |
| **Testability** | Each layer can be tested independently |
| **Maintainability** | Clear separation makes debugging easier |
| **Team Collaboration** | Different teams can work on different layers |
| **Clean Architecture** | Follows industry best practices |

---

## Additional Recommendations

### For Production Systems, Consider Adding:

```
├─ exception
│   ├─ custom (BookingException, PaymentException)
│   └─ handler (GlobalExceptionHandler)
├─ security
│   ├─ jwt (Token generation, validation)
│   └─ filter (Authentication filter)
├─ aspect
│   └─ LoggingAspect.java (AOP for logging)
├─ mapper
│   └─ BookingMapper.java (Entity ↔ DTO conversion)
└─ validation
    └─ Custom validators
```

---

## Sample Implementation Flow

### Booking Flow Example:
```
1. User selects seats
   ↓
2. Controller receives request → BookingRequest DTO
   ↓
3. ReservationService locks seats in Redis (10 min TTL)
   ↓
4. BookingService creates PENDING booking
   ↓
5. PaymentService processes payment
   ↓
6. On success: BookingService confirms booking
   ↓
7. NotificationService sends confirmation email
   ↓
8. BookingCreatedEvent triggers (async)
```

---

## Technology Stack Implied

- **Framework:** Spring Boot
- **Database:** PostgreSQL / MySQL (JPA)
- **Cache:** Redis (seat locking)
- **Events:** Spring Events / Kafka (optional)
- **Scheduler:** Spring @Scheduled
- **Notifications:** Email/SMS service integration

---

This structure provides a solid foundation for a scalable, maintainable movie ticket booking system! 🎬🎟️



