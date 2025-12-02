# Getting Ready: The ATM System

Understand the ATM design problem and learn the questions to simplify this problem further.

## Problem definition

An automated teller machine (ATM) is a self-service terminal that enables bank customers to perform financial transactions without interacting with a human teller or visiting a physical bank branch. Through ATMs, users can perform essential banking operations such as depositing cash, withdrawing money, checking account balances, and transferring funds between accounts. ATMs are typically placed in accessible public locations like banks, malls, airports, and convenience stores to provide round-the-clock banking services.

In this LLD interview case study, you'll focus on:

* Enabling secure card-based access and PIN verification for users.
* Supporting core transactions: cash withdrawal, deposit, balance inquiry, and fund transfer.
* Managing cash inventory within the ATM, including withdrawal limits and handling insufficient funds.
* Ensuring a seamless and secure user experience through well-coordinated hardware and software components.


<span style="background-color: yellow; color: blue;">in depth(problem definition and clarifying questions), <a href="./deapth/requirementClarification.md">click here</a></span>


# Expectations from the interviewee

During the interview, you are expected to discuss key aspects of the ATM system's design, clarifying requirements, handling edge cases, and reasoning about hardware-software interactions. Typical areas to cover include:

## ATM components

To better understand an ATM system, you may ask the interviewer the following questions:

1. What are the components of an ATM?
2. Is the ATM necessarily placed inside a room?
3. Does an ATM have a fingerprint scanner?

## ATM features

Different ATMs may vary in terms of features, which is why it is important to clear the following questions from the interviewer:

1. What is the withdrawal limit of an ATM?
2. Can we check our account balance using an ATM?
3. Can we set a PIN using an ATM?

## ATM processing

The interviewer would expect you to ask a question about processing ATM transactions. Therefore, you may ask the following questions:

1. What happens when the amount the user enters for withdrawal exceeds the user's account balance?
2. What happens when the amount the user enters for withdrawal exceeds the ATM's cash limit?
3. What happens when the amount the user enters exceeds the total cash in the ATM?
4. Can the ATM be used for online transactions?

<span style="background-color: yellow; color: blue;">in depth(Expectations from the interviewee), <a href="./deapth/Expectations-from-interviewee.md">click here</a></span>

# Design approach

We will design this ATM system using a bottom-up approach, following these steps:

* First, we'll identify and model the core hardware components such as `CardReader`, `Keypad`, `Screen`, `CashDispenser`, and `Printer`, defining their responsibilities and interactions.
* Next, we'll compose these components into larger entities representing the ATM's operational state, transaction workflows, and the overall machine.
* We'll model how the ATM interacts with users for authentication, processes various transactions, manages its cash inventory, and ensures secure and reliable operations.
* This approach will allow us to address edge cases, hardware failures, and incorporate SOLID design principles for modularity and maintainability. We'll use diagrams and code examples to illustrate the design at each step.

## Design pattern

During an interview, discussing the design patterns under which the ATM system falls is always a good practice. Stating the design patterns gives the interviewer a positive impression and shows that the interviewee is well-versed in the advanced concepts of object-oriented design.

<span style="background-color: yellow; color: blue;">in depth(Design Approach + Design Patterns), <a href="./deapth/DesignApproach-DesignPatterns.md">click here</a></span>


