# Getting Ready: Amazon Locker Service

Understand the Amazon Locker service problem and learn the questions to further simplify this problem.

## Problem definition

Amazon is an online retailer that allows customers to order products for delivery. Sometimes, customers are unavailable at their usual address to receive packages. In such cases, Amazon provides a secure, automated pickup and return service called Amazon Locker.

Amazon Lockers are available at various locations, each containing multiple lockers of different sizes and operating hours. Customers can select a nearby locker location for delivery or returns. Only packages that fit a locker's size are eligible. When a package arrives, the customer receives a unique code to retrieve it. Packages are stored for a limited period; if uncollected, they are removed, and the customer may be refunded. Customers can also return eligible products by dropping them off at available locker locations, where the logistics team picks them up.

This scenario presents interesting challenges: real-time resource allocation, handling concurrent user requests, operating hour constraints, managing physical locker and package sizes, and supporting delivery and return flows.

In this LLD interview case study, your focus will be on:

* Assigning lockers to orders and returns based on size and availability.
* Handling package pickups, returns, and uncollected items.
* Managing locker states, codes, and access windows.
* Ensuring the system works reliably and securely at scale.

> This system model can be adapted for any retailer providing automated, secure delivery and returns.


<span style="background-color: yellow; color: blue;">in depth(Amazone Locker system), <a href="./deapth/amazonelockerSystem.md">click here</a></span>

# Expectations from the Interviewee

The Amazon Locker service consists of multiple components. Each component has its own functionality and constraints. The following section provides an overview of some of the main expectations that the interviewer will want to hear you discuss in more detail during the interview.

## In the Interview, You Are Expected To:

- Clarify system requirements, constraints, and edge cases by asking relevant questions.
- Identify and design core system components.
- Justify design decisions using OOD and SOLID principles.

## Typical Areas You Should Probe and Discuss

### Locker Size

Every locker is of a specific size in the Amazon Locker system. The interviewer expects you to ask questions listed below:

- Are there different locker sizes? How does package size affect locker assignment?
- Are there restrictions on what can be stored in each locker?

### Locker Selection

The most significant part of the Amazon Locker service is the selection of the locker. The system has to make sure that more than one customer should not be able to access a locker at a single time. The interviewer expects you to ask the questions listed below to identify how the system will work in such situations:

- How will the system make sure that multiple customers do not get the same locker?
- Will the customer choose the locker of his own choice, or will the system assign him a locker based on availability?
- Can a customer get two lockers for different orders at the same time?
- Will the system keep in mind the locker and package sizes while assigning the locker to the customer?

### Locker Status

As this problem revolves around the locker, you may ask the questions listed below about the locker status:

- Is there any time constraint on the package that can be kept in the locker?
- What will happen if the customer does not come to pick up his package within the valid time period?

### Returning an Item

Similar to the order delivery process, the item can also be returned through the Amazon Locker service. Therefore, you may ask the questions listed below:

- Can the customer return an item through the Amazon Locker service?
- If yes, will they get the same locker from which they picked up the item?
- How will the locker be assigned to the customer while returning an item?

<span style="background-color: yellow; color: blue;">in depth(Interviewee expect question from you), <a href="./deapth/ExpectationsfromInterviewee.md">click here</a></span>


# Design Approach

We will design this Amazon Locker service using the bottom-up design approach. For this purpose, we will follow the steps below:

* First, we'll identify simple core entities like Locker, LockerLocation, Package, and Order, and define their responsibilities.
* Next, we'll model how lockers are assigned to packages based on size and availability, and handle package pickups, returns, and uncollected items.
* We'll ensure that locker operations respect time limits, location operating hours, and support secure access via unique codes.

This design approach will address concurrency, edge cases, and follow SOLID principles to ensure scalability and maintainability. Later on, diagrams and code will be used to illustrate major workflows and class structures.

## Design Pattern

During an interview, it is always a good practice to discuss the design patterns that the amazon locker system falls under. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design Approach and Design pattern), <a href="./deapth/DesignApproachPattern.md">click here</a></span>
