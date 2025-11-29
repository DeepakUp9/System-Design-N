# Getting Ready: The Car Rental System
Understand the car rental system problem and learn the questions to further simplify this problem.

A car rental system enables customers to rent vehicles for short periods, ranging from a few hours to several weeks. Such systems are commonly operated by companies with multiple offices across cities, often near airports or commercial centers. The platform allows customers to reserve, pick up, and return vehicles at different locations, depending on availability and convenience.

Each branch manages its fleet, consisting of various vehicle types and models, and tracks reservations, customer information, payments, and the logistical aspects of vehicle movement between locations. Customers can search for available vehicles by location, type, or features and reserve or cancel rentals subject to company policies. The system supports multiple payment methods and handles cases such as late returns, reservation modifications, and optional services (e.g., driver assignment and roadside assistance).

This scenario presents several design challenges, including distributed inventory management, concurrent reservations, flexible pickup and drop-off options, dynamic pricing, and ensuring a seamless and reliable customer experience across all locations.

In this LLD interview case study, you'll focus on:

* Managing vehicle inventory and supporting multiple vehicle types and features.
* Handling reservations, cancellations, and modifications across branches.
* Facilitating vehicle search, pickup, and drop-off at different locations.
* Tracking customer information, rental history, and payments.
* Supporting optional services and flexible payment methods.
* Ensuring system reliability, data consistency, and scalability.

> This system model can be adapted for any rental business managing distributed assets and flexible reservations.

<span style="background-color: yellow; color: blue;">in depth(Understanding the Car Rental System (Problem Overview)), <a href="./deapth/Understanding-the-Car-Rental-System.md">click here</a></span>

# Expectations from the interviewee
The car rental system consists of multiple components with functionality and constraints. This section provides an overview of some of the main expectations that the interviewer will want to hear you discuss in more detail during the interview.

## Vehicle types
An interviewer would expect you to discuss the different vehicle types and ask the following questions:

* What types of vehicles will that system support?
* How can we identify the specific vehicle?

## Search interface
Members will use the application and add the location and the reservation date. They will receive several options to select the vehicle. Therefore, an interviewer would expect you to ask the questions listed below:

* Is it possible to search for a vehicle using its name or type?
* Can we search for a vehicle by its model number?

## Services
An interviewer would also expect you to discuss the services of the car rental system and may ask the following questions:

* Does a car rental system assign a driver to its customer?
* Does a car rental system provide roadside assistance to its customers?

## Reservation cancellation
There will be many duplicate instances in our system. The interviewer expects you to ask the questions listed below:

* Can the member be able to cancel a reservation?
* Which member is allowed to request a vehicle reservation cancellation and when?

## Payment flexibility
One of the car rental system's most significant attributes is its customer payment structure. The payment depends on the vehicle type and the time stamp. Therefore, an interviewer would expect you to ask the questions listed below:

* How can customers pay at different branch locations and by different methods (cash, credit, or cheque)?
* If there are multiple branches of the car rental system, how will the system keep track of customers who have already paid at a particular branch?

<span style="background-color: yellow; color: blue;">in depth(Expectations From the Interviewee), <a href="./deapth/Expectations-From-Interviewee.md">click here</a></span>

# Design approach
We'll design this car rental system using the bottom-up design approach. For this purpose, we will follow the steps below:

* **Identify core components:** Start with basic entities like Vehicle, Customer, Reservation, Payment, etc.
* **Compose subsystems:** Build larger modules for inventory management, reservation management, and payment processing.
* **Integrate subsystems:** Assemble the complete application, ensuring the design is modular and adheres to SOLID principles.
* **Document assumptions:** Clearly state and justify your assumptions where requirements are unclear.

This design approach will address concurrency and edge cases and follow SOLID principles to ensure scalability and maintainability. Later, diagrams and code will illustrate major workflows and class structures.

## Design pattern
During an interview, discussing the design patterns the car rental system falls under is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design approach and Design pattern), <a href="./deapth/Design-approach_pattern.md">click here</a></span>