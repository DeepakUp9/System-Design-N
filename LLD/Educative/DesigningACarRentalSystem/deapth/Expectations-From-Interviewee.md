# ⭐ Expectations From the Interviewee (Deep Explanation)

In an LLD interview, the interviewer isn't checking if you memorize definitions.  
They check if you can **think like a system designer**.

So here's how to approach each expectation clearly.

---

## 1️⃣ Vehicle Types

### ✅ What types of vehicles will the system support?

The system must support many categories, for example:

* Hatchback
* Sedan
* SUV
* Luxury
* Pickup truck
* Van
* Electric vehicles
* Motorcycles (optional)

#### Why?
Because each branch manages a diverse fleet, and pricing depends on category.

#### How to design for this?
We create a `VehicleType` enum and a `Vehicle` class with fields like:

* `plateNumber` (unique identifier)
* `model`
* `make`
* `year`
* `type`
* `mileage`
* `status` (available, reserved, inService)
* `branchId`

### ✅ How can we identify a specific vehicle?

A specific vehicle must have a **globally unique identifier**.

**Real world uses:**

* License plate
* VIN (Vehicle Identification Number)

**In LLD, we use something like:**

* `vehicleId` (UUID)
* `plateNumber` (string)

#### Why?
To avoid double-booking and to track the exact physical car.

#### How?
The reservation doesn't just store "a sedan"; it stores `vehicleId`.

---

## 2️⃣ Search Interface

### ❓ Is it possible to search by vehicle name/type?

**Yes**, customers search by:

* Location
* Pickup date & time
* Drop-off date & time
* Vehicle type (SUV, Sedan)
* Name (e.g., Creta, Honda City)
* Features (automatic, GPS, AC)

#### Why?
Customers want flexibility. The system must filter available vehicles by:

**location → type → availability → features**

### ❓ Can we search by model number?

Usually yes, but practically:

* People use model name ("i20") more than model number
* Model number is helpful for backend filtering

### ❗ Additional things interviewers expect you to ask:

* Should we show exact cars or just car types first?
* Should we show the price in search results?
* Should the search support multi-location availability?  
  (e.g., "no car in Branch A but available in Branch B nearby")

---

## 3️⃣ Services

### ❓ Does the system assign a driver?

Some car rental systems do:

* Self-drive only
* Driver optional
* Driver only

**So you must ask:**

* Is driver an optional add-on?
* How is the price calculated with a driver?

#### Why?
Driver availability adds new constraints:

* Schedules
* Working hours
* Branch assignment

### ❓ Does the system provide roadside assistance?

Usually yes, as:

* Free service
* Add-on package
* Insurance-based support

#### How do we model it?

* Create a `Service` class
* Attach it to reservation (like optional items)

### ❗ Interviewer also expects you to ask:

**Do we support additional items?**

* Child seat
* Additional insurance
* Unlimited mileage
* Extra driver

These impact price and reservation rules.

---

## 4️⃣ Reservation Cancellation

### ❓ Can the member cancel a reservation?

**Yes.** But there are rules.

### ❓ Which member can cancel and when?

Depends on:

* Cancellation window (24h before pickup)
* Penalty rules
* Whether pre-payment was done
* Whether car is already checked out
* Whether modification is allowed instead of cancellation

#### Why is this important?
Because cancellation affects inventory.  
When a reservation is cancelled:

* The car becomes "available"
* Payments may be recalculated or refunded

#### How to handle?
**Status flow:**

```
Reserved → Cancelled
```

**And logs must capture:**

* who cancelled
* when
* penalty applied

### ❗ Interviewer also expects:

* Can staff cancel on customer's behalf?
* Can we partially modify instead of cancelling?

---

## 5️⃣ Payment Flexibility

### ❓ How can customers pay?

**Multiple methods:**

* Cash
* Card
* Cheque (rare but possible)
* Online wallets
* UPI
* Branch-based payment

#### Why?
Because people pick up from multiple branches, and each branch must capture the payment.

### ❓ How to track payments across branches?

This is a **key design point**.

**You must explain:**

* Each reservation contains payment status
* Payments are centralized (not per branch DB)
* Branch POS systems sync with main server

When customer pays at Branch A, the system updates:

```
Reservation.paymentStatus = PAID
PaymentRecord.branchId = A
```

#### How?
A central payment service:

* Generates invoice
* Tracks deposits
* Tracks refunds
* Syncs across branches

### ❗ Interviewer expects you to ask:

* Does system support partial payments?
* Is payment required before pickup?
* Should deposit be refundable?

---

## 🟦 Extra Expectations (important for interviews)

Here are more points interviewers silently expect you to explore:

### 🟢 1. Concurrency handling

How do we prevent two users from booking the same car at the same timestamp?

### 🟢 2. Vehicle movement between branches

When a car is dropped at a different branch, how do we update inventory?

### 🟢 3. Maintenance & cleaning status

After return, the car must go to cleaning/maintenance — not directly available.

### 🟢 4. Penalty calculations

* Late return fee
* Early return adjustment
* Damage cost

### 🟢 5. User roles

* Admin
* Staff
* Customer

Different permissions.

---

## 📚 Next Steps

If you want, in the next subtopic we can go into:

* ✅ Requirements
* ✅ Use cases
* ✅ Class design
* ✅ Inventory lifecycle
* ✅ Reservation lifecycle

etc.

---

**Keep preparing systematically! 🚀**