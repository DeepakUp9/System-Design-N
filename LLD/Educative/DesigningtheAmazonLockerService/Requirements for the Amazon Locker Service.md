# Requirements for the Amazon Locker Service

Learn about all requirements for the Amazon locker service.

## Overview

In this lesson, we outline the functional and operational requirements for the Amazon Locker service. Clearly identifying and understanding requirements is essential to define the system's scope and ensure a robust, user-friendly design.

We'll use the notational convention to identify each requirement with a unique label "Rn," where "R" is short for Requirement and "n" is a natural number.

## Requirement Collection

The requirements for the Amazon Locker service are defined below:

* **R1:** A customer can select a preferred locker location for order pickup during checkout.

* **R2:** An order may contain one or more items. Based on locker size availability, items are packaged together if possible.

* **R3:** Locker locations contain multiple lockers of various sizes (extra small, small, medium, large, extra large, double extra large).

* **R4:** Only packages that fit fully within the locker's interior dimensions are eligible for locker delivery.

* **R5:** When a package is delivered to the selected locker, the customer receives a unique code (e.g., a 6-digit PIN) to open the locker.

* **R6:** Packages are held in the locker for a maximum of three days.

* **R7:** Each locker location has defined opening and closing hours; customers must pick up packages within the 3-day window and the location's operating hours.

* **R8:** If a package is not picked up within three days, it is removed from the locker, the locker is released, and the customer is refunded.

* **R9:** Multiple lockers are available at every locker location; each locker can only be assigned to one customer/package at a time.

* **R10:** Once a package is collected, the locker is closed and locked; the provided access code is invalidated and cannot be reused.

* **R11:** Customers may return eligible items by selecting a nearby locker location. Based on package size and location, an available locker is assigned. A new, unique code is sent to the user to open the locker and place the return package.

* **R12:** The logistics team stores returned items in lockers for pickup; the logistics team uses a unique code to collect the returned package. The customer is notified once the return is processed, and the refund policy is applied per product eligibility.

---

We've identified our requirements for the problem, and in the next lesson, we will define different use cases for the amazon locker system.

<span style="background-color: yellow; color: blue;">in depth( Requirements for the Amazon Locker Service), <a href="./deapth/requirements.md">click here</a></span>