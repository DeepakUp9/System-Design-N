# Getting Ready: The Hotel Management System

Understand the hotel management system problem and learn the questions to further simplify this problem.

## Problem definition

The hotel management system is a software used to manage all hotel activities efficiently and smoothly. Almost all popular hotels have an online management system to digitize room bookings, manage staff, and perform other necessary hotel management features. This system makes the notification and payment process flexible and automated. The hotel usually has a fixed number of rooms, and the booking system has information about all rooms present and their availability.

For the hotel manager, manually controlling all the hotel activities is difficult. With the help of an online hotel management system, the manager can keep track of rooms, customers, and workers through a single portal. The manager can also post available rooms to the system and generate bills. The system also allows customers to search for and book a room of their choice and cost range. The system provides complete information about rooms to the customers so they can view the available rooms and book them online. The guest is charged based on the time the hotel room is booked. In this way, the system facilitates the hotel's customers and managers.

<span style="background-color: yellow; color: blue;">in depth(Hotel Management System), <a href="./deapth/Hotel-Management-System.md">click here</a></span>

# Expectations from the interviewee

Numerous components are present in the hotel management system, each with specific constraints and requirements. The following provides an overview of some of the main things the interviewer will want to hear you discuss in more detail.

## Room booking

Booking a room is an essential part of the hotel management system. The system must ensure that no two users can book the same room for overlapping time slots. The interviewer expects you to ask questions to identify how the system will work in such situations:

* How will the system ensure multiple users do not book the same room?
* What type of users are allowed to book a room in the hotel?
* Can users book a room in advance?

## Payment handling

One of the hotel management system's most significant attributes is its payment structure for its customers. This can vary, so the interviewer would expect you to ask the questions listed below:

* What payment methods can the customer use (for example, credit card or cash)?
* How is the payment performed? Does the customer pay online or through a receptionist at the hotel?
* Will the customer be able to pay in advance for a room booking, or is a just-in-time (JIT) payment system available?

## Price variance

We touched upon the payment methods of the hotel management system. Now, the pricing model needs to be clarified by the interviewer. Therefore, you may ask the questions listed below:

* How will the booking price be calculated? Or which factors affect the price of a room in the hotel?
* How does the location and size of the room affect its price?
* How does the booking duration affect the payment?

## Booking cancellation

There will be many duplicate instances in our system. The interviewer expects you to ask the questions provided below:

* Can the user cancel a room booking?
* Which type of users are allowed to request a room booking cancellation?

<span style="background-color: yellow; color: blue;">in depth(Expectations from the interviewee), <a href="./deapth/Expectations-from-interviewee.md">click here</a></span>

# Design approach

We'll design this hotel management system using the bottom-up design approach. For this purpose, we will follow the steps below:

* Identify and design the smallest components first, such as a room.
* Use these small components to design bigger components, such as building a hotel with multiple rooms.
* Repeat the steps above until we design the whole hotel management system.

## Design pattern

During an interview, discussing the design patterns under which the hotel management system falls is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design approach, Design pattern ), <a href="./deapth/Design_approach_pattern.md">click here</a></span>
