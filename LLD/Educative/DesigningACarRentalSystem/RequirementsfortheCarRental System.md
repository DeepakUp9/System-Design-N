# Requirement Collection

The set of requirements for the car rental system is listed below:

## User Roles

- **R1:** The system supports two main user roles: Customers and Receptionists.

## Vehicle Management

- **R2:** The system manages multiple types of vehicles, including cars, trucks, vans, and motorcycles.

- **R3:** Each vehicle type may have multiple subtypes, such as:
  - **Cars:** Economy, Luxury, Standard, Compact, Intermediate, Full size, Premium
  - **Vans:** Passenger, Cargo
  - **Motorcycles:** Standard, Cruiser, Touring, Sports, Off-road, Dual purpose
  - **Trucks:** Light-duty, Medium-duty, Heavy-duty

## Reservation Management

- **R4:** The system must record every reservation, including the customer details and the date/time a vehicle is issued.

- **R5:** The system can track and report the number of vehicles each customer has rented, including rental history and active reservations.

- **R6:** Customers can cancel their reservations at any time before the pickup date, subject to company policies.

- **R7:** The system maintains a vehicle log to track all significant events related to each vehicle (e.g., maintenance, repairs, accidents, assignments, or status changes).

## Additional Services and Equipment

- **R8:** Customers can add equipment to their reservations, such as a ski rack, child seat, or navigation system.

- **R9:** Customers can add extra services to their reservations, including a driver, Wi-Fi, or roadside assistance.

## Notifications and Penalties

- **R10:** If a vehicle is not returned by the due date, the system must notify the customer and automatically generate a fine according to company policy.

## Search Functionality

- **R11:** Users can search for vehicles by type, model, features, or availability at specific locations and dates.

## Branch and Location Management

- **R12:** The system must support the management of multiple branches in different cities and locations.

- **R13:** Each branch must maintain a record of parking stalls for vehicles at that location, including current status (occupied, available, reserved).

## Payment Processing

- **R14:** The system should securely process payments, refunds, and fines using multiple payment methods (cash, card, online).

- **R15:** The Customer shall be able to make payment for the car rental in both scenarios: when making the reservation and when returning the vehicle. The system should validate both.
