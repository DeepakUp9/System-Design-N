# Requirements for the ATM System

Learn about all the requirements of the ATM design.

In this lesson, we outline the functional and operational requirements for the ATM System. Identifying and understanding requirements is essential to defining the system's scope and ensuring a robust, user-friendly design.

We'll use the notational convention to identify each requirement with a unique label, "Rn," where "R" is short for requirement, and "n" is a natural number.

## Requirement collection

* **R1:** Each user has a single account at the bank that they can access by inserting their card into the ATM.
* **R2:** The ATM system consists of the following main components to facilitate user interaction and transaction processing:
   * Card Reader: Reads the user's ATM card.
   * Keypad: The user can enter their PIN and transaction details.
   * Screen: Displays prompts, messages, and transaction information.
   * Cash Dispenser: Dispenses cash to the user for withdrawal transactions.
   * Printer: Prints transaction receipts for the user.
   * Network Infrastructure: Securely connects the ATM to the bank's central system to process transactions and access account information.
* **R3:** The ATM must authenticate users by verifying the PIN entered, ensuring that only authorized customers can access account services.
* **R4:** All transaction options become available only after successfully authentication the card and PIN.
* **R5:** Users may have either or both of the following account types:
   * Current account
   * Savings account
* **R6:** The ATM must allow the following operations for each account type:
   * Balance inquiry
   * Cash withdrawal
   * Funds transfer
* **R7:** After completing a transaction, the user should have the option to perform another transaction or end their session and retrieve their card.

<span style="background-color: yellow; color: blue;">in depth(Requirements for the ATM System), <a href="./deapth/requirements.md">click here</a></span>