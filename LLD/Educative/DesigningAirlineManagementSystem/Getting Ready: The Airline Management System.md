# Getting Ready: The Airline Management System  
Understand the airline management system problem and learn the questions to further simplify this problem.

## Problem definition  
An airline management system is a software used to efficiently manage all activities within an airline's system. Nowadays, every airline has a management system to digitize the process of scheduling flights, managing staff, ticket reservations, and performing other necessary airline management tasks. Moreover, the system tracks the number of aircraft, pilots, and their availability at airports. The system provides customers the complete flight information to view the available flights and schedule them online. The front desk officer should be able to reserve tickets, create itineraries, and process flight payments for customers, providing a smoother booking experience for those who prefer or require in-person assistance. Similarly, the admin can manage all the airline activities with the help of this system. Therefore, the airline management system facilitates its customers and administrators, controlling all operations of the airline company.

<span style="background-color: yellow; color: blue;">in depth(Problem Definition), <a href="./deapth/ProblemDefinition-AirlineManagementSystem.md">click here</a></span>

# Expectations from the interviewee  
The airline management system comprises several components, each with its own specific constraints and requirements. The following provides an overview of some of the main things that the interviewer will want to hear you discuss in more detail during the interview.

## Flight reservation  
Flight reservation is a crucial component of an airline's management system. The system must ensure that no two people are assigned to the same seat. The interviewer expects you to ask questions to identify how the system will complete flight reservations:

* How will the system ensure that multiple users do not have the same seat on the aircraft?
* Can one itinerary reserve multiple flights?
* Can the customer reserve the whole aircraft?

## Payment handling  
One of the airline management system's most significant attributes is its payment structure for its customers. This can vary, so the interviewer would expect you to ask the questions listed below:

* What payment methods are accepted, such as credit cards or cash?
* How does the customer make the payment? Does the customer pay online or in person?
* Will the customer be able to pay in advance for a flight booking, or is a just-in-time (JIT) payment method available?

## Price variance  
Now that we've discussed the payment methods of the airline management system, let's ask the interviewer about the pricing model. You may ask the questions listed below:

* Is the price set manually, or does the system calculate the price for each flight?
* Do all seats have the same price, or is it calculated based on the seat type?
* Do weekdays and weekends affect the price of the flight?
* Is the price of the flight affected by an increase in demand?
* How does the duration of the flight affect the payment?

## Flight cancellation  
In the airline management system, customers may want to cancel a flight after booking it. Therefore, you can ask the following questions:

* Can the customer cancel a flight?
* What is the time limit to cancel the flight?
* Which type of users are allowed to request a flight cancellation?

## Flight scheduling  
Flight scheduling is crucial for ensuring timely and conflict-free operations. The interviewer might expect you to ask:

* How is a flight schedule created and managed?
* Can a flight be delayed or rescheduled, and how is this handled in the system?
* Is there any buffer time considered between flights for aircraft and crew?

## Staff and crew management  
Managing pilots and crew availability is vital for operations. You may want to ask:

* How is crew availability tracked in the system?
* Can crew members be assigned to multiple flights in a day?
* What happens if a crew member becomes unavailable suddenly?

<span style="background-color: yellow; color: blue;">in depth(Expectations from the interviewee), <a href="./deapth/Expectations-from-interviewee.md">click here</a></span>

## Design approach
We’ll design this airline management system using the bottom-up approach. For this purpose, we will follow the steps below:

* Identify and design the smallest components first, including the seat, flight, etc.  
* Use these small components to design larger components, such as an airport or an aircraft that can be composed of multiple seats.  
* Repeat the steps above until the entire airline management system is designed.  

## Design pattern 
During an interview, it is always a good practice to discuss the design patterns that an airline management system falls under. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design approach + Design pattern), <a href="./deapth/Design-approach-pattern.md">click here</a></span>
