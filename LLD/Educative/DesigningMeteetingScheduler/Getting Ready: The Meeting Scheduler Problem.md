# Getting Ready: The Meeting Scheduler Problem  
Understand the meeting scheduler design and learn the questions to simplify this problem.

## Problem definition  
A meeting scheduler software enables organizations to efficiently arrange and manage meetings for multiple participants, ensuring optimal use of meeting rooms and everyone's time. The system helps find a suitable time and location by checking participants' availability, room capacity, and preferences. Organizers and participants can book, update, or cancel meetings; add or remove attendees; and receive real-time notifications for all meeting changes. When participants respond to invites or remove themselves from a meeting, their calendars are automatically updated to reflect their participation status.

In this LLD interview case study, you'll focus on:

* Assigning available meeting rooms based on room capacity, schedule, and participant count.
* Determining optimal meeting times by analyzing the availability of all required attendees.
* Managing the full meeting life cycle: creation, updates, cancellations, and participant management.
* Sending notifications for all meeting-related events, and updating calendars automatically when meetings are accepted, declined, or modified.
* Tracking each participant's invite/response status in the meeting record, and handling changes such as removals, cancellations, and overlapping bookings.

<span style="background-color: yellow; color: blue;">in depth(Meeting Scheduler), <a href="./deapth/problem-understanding.md">click here</a></span>

# Expectations from the interviewee. 
It is important to narrow down the components you will include in your design of a meeting scheduler. The following section provides an overview of some of the main expectations the interviewer will want to hear you discuss in more detail during the interview.

## Room assignment 
A meeting scheduler assigns a meeting room to a scheduled meeting. Make sure to ask the following questions of the interviewer to figure out how this assignment works:

* How does the system determine available rooms?
* How important is the capacity of a room when assigning a room for a meeting?

## Availability of attendees  
Multiple attendees are in a meeting, and all attendees have different schedules. You may ask the following questions to understand how the scheduler works:

* How does the system check the availability of the attendees?
* How does the system access the meeting information of all attendees?

<span style="background-color: yellow; color: blue;">in depth(Expectations from the Interviewee), <a href="./deapth/Expectations-from-Interviewee.md">click here</a></span>


# Design approach  
We will design this meeting scheduler system using the bottom-up design approach. For this purpose, we will follow the steps below:

* First, we'll identify the simple core entities such as `MeetingRoom`, `Interval`, `Participant`, and `Calendar`, and define their responsibilities and relationships.
* Next, we'll model how meetings are created, how the scheduler checks room and participant availability, assigns suitable rooms, and sends notifications for bookings, cancellations, and updates.
* We'll ensure the design gracefully handles conflicts, last-minute updates, recurring meetings, and edge cases like overlapping events or room shortages.
* This approach will incorporate SOLID principles and design patterns, ensuring the system is scalable, maintainable, and adaptable to future needs.

Diagrams and sample code will illustrate the main workflows, class structures, and collaboration between entities.

## Design pattern  
During an interview, discussing the design patterns the meeting scheduler falls under is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.