# Requirements for the Library Management System

Learn about all requirements of the library management system.

In this lesson, we will list the requirements of our library management system. This is a crucial step, as requirements define the scope of a problem. Getting them right from the interviewer and understanding them well will make the system-design process smooth and easy.

We'll use the notational convention to identify each requirement with a unique label "Rn," where "R" is short for Requirement and "n" is a natural number.

## Requirement collection

For LMS (library management system), the requirements have been defined below:

* R1: The system should store information about books and members, and maintain a complete log of all book borrowing, return, reservation, and renewal transactions.
* R2: Every book must have a unique identification number and detailed information, including rack/location in the library.
* R3: Each book should include ISBN, title, author name, subject, and publication date.
* R4: A book can have multiple copies; each physical copy is a distinct book item with a unique ID.
* R5: The system must support two types of users: librarian and member, each with a library card and unique card number.
* R6: Every user must have a library card with a unique card number.
* R7: Members can borrow a maximum of 10 books at a time.
* R8: A member can borrow a book for a maximum of 15 days.
* R9: Only one member can reserve each book item at a time.
* R10: The system must record who issued, reserved, or renewed a book item and on which date.
* R11: The system must allow members to renew borrowed books, according to policy limits.
* R12: The system should notify members if a book is not returned by the due date or when a reserved book becomes available.
* R13: If a book is unavailable, a member should be able to reserve it for when it becomes available.
* R14: The system should allow users to search for books by title, author, subject, or publication date.

<span style="background-color: yellow; color: blue;">If you want to understand more in depth, <a href="./deapth/requirements.md">click here</a></span>