# Getting Ready: An Online Stock Brokerage System  
Understand the online stock brokerage system problem and learn the questions to further simplify this problem.

## Problem definition  
An online stock brokerage system acts as an intermediary between the buyer and seller during the trade of stocks. The system facilitates its users' buying and selling of stocks online. It enables users to monitor and carry out their transactions and displays performance graphs for the various stocks in their portfolios. It also protects client transactions and notifies them when stock changes reach certain levels.

The online stock brokerage system streamlines traditional trading using digital infrastructure, offering faster transactions and reduced operational costs. It also offers users quicker access to stock information, market trends, and real-time prices. Additionally, the system supports deposits and withdrawals through checks, wire transfers, or electronic bank transfers, enhancing overall financial flexibility for users.

<span style="background-color: yellow; color: blue;">in depth(Problem Understanding), <a href="./deapth/Problem-Understanding-online-brokerage.md">click here</a></span>


# Expectations from the interviewee  
An online stock brokerage system consists of multiple components. Each component has its own functionality and constraints. The following section provides an overview of some of the main expectations the interviewer will want to hear you discuss in more detail during the interview.

## Discoverability  
For an online stock brokerage system, discoverability is one of the key features. You can ask the following questions to know more about the system:

* How do the members search the stock inventory?
* How will the search surface result?

## Visibility  
To get a better understanding of how the data is visible to the different users, you may ask the following questions:

* Can every member see the current levels of stock positions at any time?

## Order type  
You may ask the interviewer about the types of orders the system should be able to handle by simply asking the question listed below:

* How many types of stock trade orders can the users place, for example, a market order, loss order, etc?

## Multiplicity  
You may ask the interviewer questions related to the multiplicity of the system. These questions are listed below:

* Can the members have multiple watchlists containing multiple stock quotes?
* Can a member buy multiple lots of the same stock at different times?

<span style="background-color: yellow; color: blue;">in depth(Clarifying the System via Questions), <a href="./deapth/Clarifying-the-System-via-Questions.md">click here</a></span>

# Design approach  
We'll design this online stock brokerage system using the bottom-up design approach. For this purpose, we will follow the steps below:

* Identify and design simple components, like stock and stock position, first.
* Use these small components to design bigger components, such as the order and stock inventory, which can be composed of multiple stock items.
* Repeat the steps above until we design the whole stock brokerage system.

## Design pattern  
During an interview, discussing the design patterns that an online stock brokerage system falls under is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design approach + Design pattern), <a href="./deapth/Design-approach-pattern.md">click here</a></span>