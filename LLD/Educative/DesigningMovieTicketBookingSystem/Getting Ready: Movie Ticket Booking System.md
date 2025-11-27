# Getting Ready: Movie Ticket Booking System

Understand the movie ticket booking system problem and learn the questions to simplify this problem.

## Problem Definition

The movie ticket booking system enables customers to search for movies, browse showtimes across various cinemas in multiple cities, select preferred seats, and securely book tickets online. The system manages comprehensive information about cinemas, locations, cinema halls, movie listings, and scheduled shows. Customers can view a live seating layout for each show, select and reserve available seats, receive booking confirmations, and complete payment.

## Focus Areas

In this LLD interview case study, you'll focus on:

* Modeling cinemas, cinema halls, movies, shows, and seating arrangements across cities.
* Designing secure seat reservation workflows that prevent double bookings and support concurrent access.
* Implementing robust payment flows.
* Ensuring timely notifications and confirmations for all successful bookings.

## Note

> <span style="background-color: #8B4513; color: white; padding: 10px; border-radius: 5px; display: inline-block;">This system model can be adapted for any online ticketing platform—such as concerts, sports, or events—with similar requirements for real-time seat selection and bookings.</span>

<span style="background-color: yellow; color: blue;">in depth(What the system is supposed to do), <a href="./deapth/system-is-supposed.md">click here</a></span>

# Expectations from the Interviewee

A typical movie ticket booking system has numerous components, each with specific constraints and requirements. The following provides an overview of some of the main expectations that the interviewer will want to hear you discuss in more detail during the interview.

## Seat Selection

Selecting a seat is an essential part of the movie ticket booking system. The system must ensure that no two people can book the same seat. The interviewer expects you to ask questions to identify how the system will work in these situations:

* How will the system ensure multiple users do not book the same seat?
* Will there be a timeout session that temporarily reserves seats? Will the system use a first-come, first-served algorithm?
* Will there be transaction locks involved in the system?

## Payment Handling

One of the most significant attributes of the movie ticket booking system is the payment structure that it provides to its customers. This can vary, so the interviewer would expect you to ask the questions listed below:

* What payment methods can the customer use (for example, credit card or cash)?
* How is the payment performed? Does the customer pay themselves online or through a ticket agent at the location?

## Price Variance

We touched upon the payment methods of the movie ticket booking system. Now, the pricing model needs to be clarified by the interviewer, and therefore, you may ask questions like these:

* How will the booking price be calculated? Will it vary based on the popularity of the show?
* Does the seat type affect the pricing?
* Will there be discount coupon codes?

## Duplication

There will be many duplicate instances in our system. The interviewer expects you to ask questions like these:

* How are we handling these instances, such as the same cinema having multiple cinema halls showing different movies simultaneously?
* Is the same movie shown in the same cinema/hall at different times?


<span style="background-color: yellow; color: blue;">in depth(what the interviewer expects you to think about and clarify before jumping into the design), <a href="./deapth/interviewer-expects.md">click here</a></span>


# Design Approach

We will design this movie ticket booking system using the bottom-up design approach. For this purpose, we will follow the steps below:

* First, we'll identify core entities such as `Cinema`, `CinemaHall`, `Movie`, `Show Seat`, and `Booking`, and define their responsibilities.
* Next, we'll model how customers search for movies, view showtimes, and interact with seating layouts to reserve seats.
* We'll implement mechanisms to handle concurrency—preventing double bookings and managing seat reservations—while integrating reliable payment methods.

This design approach will address edge cases (such as simultaneous seat selection), scalability, and SOLID principles for maintainable, extendable architecture. In later lessons, we'll illustrate major workflows with diagrams and code examples.

# Design Pattern

Discussing the design patterns that a movie ticket booking system follows during an interview is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.   



<span style="background-color: yellow; color: blue;">in depth(Design Approach + Design Patterns), <a href="./deapth/Design-Approach-Design-Patterns.md">click here</a></span>