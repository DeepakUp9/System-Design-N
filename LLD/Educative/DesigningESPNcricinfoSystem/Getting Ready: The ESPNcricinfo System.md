# Getting Ready: The ESPNcricinfo System

Understand the ESPNcricinfo problem and identify the key questions to simplify it further.

## Problem definition  
ESPNcricinfo is a comprehensive cricket management and broadcasting system that offers real-time and historical data for cricket matches, players, teams, and tournaments. It supports various formats, including Test, ODI, and T20 matches. The platform is designed for multiple stakeholders, admins, coaches, umpires, and commentators, each with distinct roles and responsibilities.

The system enables ball-by-ball tracking of match events, including wickets, scores, and live commentary, providing an engaging experience for viewers and analysts. The admin can add or update all statistics related to players, teams, and matches, ensuring data accuracy and completeness.

ESPNcricinfo also handles tournament management, allowing the admin to add and modify tournaments, assign stadiums and match personnel, and manage team participation. For each tournament, coaches can select a squad of players to represent the team. From this squad, the playing eleven can be chosen for each match.

Additionally, the system maintains points tables, match results, and complete historical data for previous tournaments and games. It includes features for publishing cricket news and updates, managed by the admin or coaches, and provides tools for commentators to input or update commentary during live matches.

<span style="background-color: yellow; color: blue;">in depth(Problem understanding ESPNcricinfo System), <a href="./deapth/problem-understanding-ESPNcricinfo-System.md">click here</a></span>

# Expectations from the interviewee  
ESPNcricinfo is a role-based cricket management system that supports match operations, tournament setup, and the handling of real-time data. During the interview, you are expected to clearly understand the system's actors, their responsibilities, and the use cases that drive the system. You should ask relevant questions that help clarify the system's functional and operational behavior.

## Player, team, and match statistics  
The system must accurately track player, team, and match statistics.

* How are player statistics updated—manually by the admin or through automation?
* What statistical detail is required (e.g., runs, wickets, strike rate)?
* Should stats be updated after every ball, inning, or match?

## Match formats and match management  
The system must support all three formats: Test, ODI, and T20.

* Are there format-specific rules that need to be encoded in the system?
* Should match duration or innings logic be handled differently based on format?

## Squad submission for tournaments  
Each team must submit a tournament squad in advance.

* Who is responsible for submitting the squad?
* Can a coach modify the squad after it has been submitted, and under what conditions?

## Admin operations  
The admin oversees the creation and management of all entities in the system.

* What validations are needed when adding matches, players, or teams?
* How are stadiums and match personnel assigned?
* Is there a review or approval step before updates go live?

<span style="background-color: yellow; color: blue;">in depth(Expectations from the interviewee), <a href="./deapth/Expectations-from-interviewee.md">click here</a></span>

# Design approach  
We'll design Cricinfo using the bottom-up approach. For this purpose, we will follow the steps below:

* Identify and design the smallest components, like a ball and a run.
* Use these small components to design larger components, such as an over, a team, and an umpire.
* Repeat the steps above until we design the complete ESPNcricinfo platform.

## Design pattern  
During an interview, discussing the design patterns that ESPNcricinfo falls under is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design approach + Design pattern ), <a href="./deapth/Design-approach-pattern.md">click here</a></span>