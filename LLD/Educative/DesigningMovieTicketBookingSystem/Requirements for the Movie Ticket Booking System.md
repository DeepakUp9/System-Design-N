# Requirements for the Movie Ticket Booking System

Learn about all requirements of the movie ticket booking system.

## Overview

This lesson outlines the functional and operational requirements for the movie ticket booking system. Identifying and understanding requirements is essential to defining the system's scope and ensuring a robust, user-friendly design.

We'll use a notational convention to identify each requirement with a unique label "Rn," where "R" is short for Requirement and "n" is a natural number.

## Requirement Collection

The following are the requirements that we have defined for the movie ticket booking problem:

* **R1**: The system supports multiple cinemas, each potentially located in different cities. Each cinema contains multiple halls.
* **R2**: A cinema can schedule multiple shows for each movie, but a hall can host only one show at a time.
* **R3**: The system must display all available showtimes for any selected movie, across all cinemas.
* **R4**: Users can search for movies by title, language, genre, or release date.
* **R5**: Users can book seats for any available showtime at any cinema hall.
* **R6**: Customers may make bookings online or via a ticket agent.
* **R7**: Online customers can pay by credit card only; in-person customers may pay by cash or credit card through a ticket agent.
* **R8**: Users can select multiple available seats for a show in a single booking transaction.
* **R9**: Seats are categorized into three fixed-cost types: Silver, Gold, and Platinum.
* **R10**: Each seat may have only one ticket allocated.
* **R11**: The system must prevent multiple users from booking the same seat for the same show.
* **R12**: Admin users must be able to add, delete, or update shows and movies.
* **R13**: The system visually distinguishes between available and booked seats.
* **R14**: The system must generate notifications for relevant events, such as:
   * When a new movie is released, notify users who opted in.
   * When a booking is made, notify the customers.
   * When a booking is canceled, notify the customers.
* **R15**: The system must send a notification to the customer whenever a booking is modified, informing them of the updated booking details.


<span style="background-color: yellow; color: blue;">in depth(Design Approach + Design Patterns), <a href="./deapth/Requirements.md">click here</a></span>

