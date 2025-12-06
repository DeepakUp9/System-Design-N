# Class Diagram for the Hotel Management System  
Learn to create a class diagram for the hotel management system problem using the bottom-up approach.  
Here, we will create the class diagram for our system based on the requirements we gathered in one of the previous lessons. In the class diagram, we will first design and create the classes, abstract classes, and interfaces for the system, and then we'll identify the relationship between classes in accordance with all the requirements of the hotel management system.

## Components of a hotel management system  
In this section, we'll define the classes for a hotel management system. Following the bottom-up approach to designing a class diagram, we will first create the classes of small components. Next, we will integrate these components and create the class diagram for the hotel management system.

### Address and Account  
The `Address` class is required to store any address. It is a custom data type with attributes like a street address, city, etc. In the hotel management system, this class will be used to specify the addresses of the users and the hotel.

`Account` is a class used to store the user's account information. This class has three members, i.e., account ID, password, and account status. The class representation of `Address` and `Account` classes is as follows:


<img src="./Address-and-Account-classes.png"  width="300" height="300">


### Person 
`Person` is an abstract class used to store information related to a person, such as a name, email, phone number, etc. In this class, an object of the `Address` type specifies the person’s address. The `Person` class specifies the accounts in the system. The system has four types of accounts: housekeeper, receptionist, guest, and server.

There are multiple functions of the `Person` class’s subclasses. First, the `Housekeeper` class will keep track of the maintenance records of a room. Second, the `Receptionist` class represents the hotel receptionist. The methods in this class depict the actions that the receptionist can perform. Moreover, the `Guest` class describes the guests of the hotel. Guests are the hotel’s customers who can search for and book a room.

The relationship diagram for these classes is shown below:

<img src="./Person-and-its-derived-classes.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R1:** The system can have four types of accounts, such as housekeeper, receptionist, guest, or server.
</details>

### Service
`Service` is an abstract class that encapsulates the details of different types of services guests have requested. Three types of services are provided: amenity, room service, and kitchen service.

The `Amenity` class is a subclass of `Service` with two members: name and description. Similarly, `RoomService` is also inherited from the `Service` class. This class stores information about room services, whether these services are chargeable or not, and the request time of the service. Furthermore, the last child class of the `Service` is `KitchenService`. The relationship between these classes is shown in the illustration below.

<img src="./Service-and-its-derived-classes.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R8:** The system should allow customers to add services of their choice, such as room service, food or kitchen service, or amenities.
</details>

### Invoice
The `Invoice` class represents the billing system in the hotel management system. The UML diagram for both classes is presented below:

<img src="./Invoice-class.png"  width="400" height="300">

### Room booking
The `RoomBooking` class is responsible for managing the bookings for a room. This class includes attributes like reservation number, start date, duration, etc. Moreover, this class has a member of the `BookingStatus` type that is used to store the status of the room booking. The UML representation of this class is as follows:

<img src="./RoomBooking-class.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R4:** During room booking, the user enters the check-in date and duration of the stay. The user also has to make an advance payment.
</details>

### Notification
`Notification` is an abstract class responsible for sending notifications to guests whenever the booking is nearing the check-in or check-out date. Every notification has an ID, creation date, and content. The notification can either be an SMS notification or an email notification.

The `SMSNotification` class requires the member’s phone number to send a notification. On the other hand, the `EmailNotification` needs the member’s email address to send a notification. The relationship diagram of these classes is shown here:

<img src="./Notification-class.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R6:** The system should notify customers about the booking status or other information.
</details>

### Room, room key, and room maintenance
The `Room` class is the system’s basic building block. Every room has a room number and a price associated with it. The `Room` class uses the `RoomStyle` and `RoomStatus` enums to specify the rooms’ style and status, respectively.

Each room has an electronic key card associated with it. The `RoomKey` class expresses the electronic key card. Each card has its own unique ID and barcode. The `RoomKey` class also has members to store the issue date, to check whether or not the key is active, and to check whether or not a key is a master key. Whereas, `RoomMaintenance` is a class used to keep track of all maintenance records for the rooms. The UML representation of these classes is as follows:

<img src="./Room-RoomKey-RoomMaintenance-classes.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R9:** Every room should have its specific key, and there can be a master key that opens a specific set of rooms.  
**R7:** The system should log and manage all the cleanup tasks.
</details>

### Search interface and catalog 
`Search` is one of the most important components of the hotel management system. In the diagram below, `Search` is the interface that allows guests to search for any room of their choice and pay range. The receptionist can also use this interface to search for any room. The `Catalog` class contains a list of all rooms and implements the `Search` interface.

The following UML diagram shows this relationship:

<img src="./Search-interface-classes.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R3:** The system should allow guests to search for and book any available room..  
</details>

### Bill transaction
After generating an invoice, a customer must pay the bill to confirm the room booking. A `BillTransaction` class is required to store the information of bill payment. Check, cash, and credit card transactions are three ways to pay the bill. We can define the bill payment functionality through any payment method using the diagram below:

<img src="./BillTransaction-classes.png"  width="400" height="300">

### Hotel and hotel branch
In this section, we’ll look at the `Hotel` and `HotelBranch` classes. According to the requirements, there can be multiple branches of the hotel. `HotelBranch` is a class used to represent the location of the hotel branch. This class consists of two members: `name` and `Address`. The string type `name` is used to store the name of the hotel branch, while the complex object `Address` is used to store the complete address of a branch.

The `Hotel` class is the base class of the system, which is used to represent the hotel. The visual representation of these classes is as follows:


<img src="./Hotel-and-HotelBranch-classes.png"  width="400" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R10:** A hotel can have multiple branches. 
</details>

### Enumerations  
Here is the list of enumerations required in the hotel management system:

**`BookingStatus`:** This status describes the booking status, whether the booking is requested, pending, confirmed, canceled, or abandoned.  
**`PaymentStatus`:** This status describes the status of the booking's payment, whether it is unpaid, pending, completed, failed, declined, canceled, abandoned, setting, settled, or refunded.  
**`RoomStatus`:** This status describes the room's status, whether it is available, reserved, occupied, not available, being serviced, or any other possibility.  
**`RoomStyle`:** This describes the style of the room that the user wants to book. The style could be standard, deluxe, family, or business.  
**`AccountStatus`:** This status tells the status of the user account, whether it is active, closed, canceled, or blocklisted.  
**`AccountType`:** The account type tells the type of the user's account, whether it is a member, guest, manager, or receptionist.  

<img src="./Enums-in-the-hotel-management-system.png"  width="500" height="300">

<details>
<summary><strong>Click to view related requirements</strong></summary>

**R2:** The rooms can be of different styles, such as standard, deluxe, family suite, or business suite
</details>



## Relationship between the classes  
Now, we'll discuss the relationships between the classes we have defined above in our hotel management system.

### Association  
The class diagram has the following association relationships:

#### One-way association

* The `Housekeeper` class has a one-way association with `RoomMaintenance`.
* Both `Receptionist` and `Guest` have a one-way association with `RoomBooking`.
* The `RoomBooking` class has a one-way association with `Room`.

<img src="./one-way-association-relationship.png"  width="600" height="300">

#### Two-way association  
The `RoomBooking` class has a two-way association with `Notification`.

<img src="./two-way-association-relationship.png"  width="600" height="300">

### Aggregation 
* The `RoomBooking` class is an aggregate of `Service`.

<img src="./aggregation-relationship.png"  width="600" height="300">

### Composition  
* The `RoomBooking` class is composed of `Invoice`.
* The `Invoice` class is composed of `BillTransaction`.
* The `Person` class is composed of `Account`.
* The `Room` class is composed of `RoomMaintenance` and `RoomKey`.
* The `HotelBranch` class is composed of `Room`.
* The `Hotel` class is composed of `HotelBranch`.

![alt text](composition-relationship.png)

### Inheritance

The following classes show an inheritance relationship:

* `Receptionist`, `Guest`, and `Housekeeper` classes extend the `Person` class.
* `Amenity`, `RoomService`, and `KitchenService` classes extend the `Service` class.
* Both `PostalNotification` and `EmailNotification` classes extend the `Notification` class.
* The `Catalog` class implements the `Search` interface.

Note: We have already discussed the inheritance relationship between classes in the component section above one by one.

## Class diagram of the hotel management system

This section outlines the multiplicity (cardinality) relationships between the main classes in our hotel management system. We explain the allowed number of instances on each side for each relationship and the real-world or design rationale behind the connection. Understanding these relationships is key to modeling how different entities interact and collaborate to support key workflows in the system.

| Source | Target | Multiplicity | Reason |
|--------|--------|--------------|--------|
| Hotel | HotelBranch | 1 – 0..* | A single hotel can have multiple branches across different locations. |
| HotelBranch | Room | 1 – 0..* | Each branch contains multiple rooms for booking. |
| Room | RoomBooking | 1 – 0..* | A room can be booked multiple times over its lifetime. |
| Guest | RoomBooking | 1 – 0..* | A guest may have multiple bookings over time. |
| Room | RoomMaintenance | 1 – 0..* | A room may undergo multiple room maintenance sessions. |
| HouseKeeper | RoomMaintenance | 1 – 0..* | A housekeeper may be assigned to multiple room maintenance tasks. |
| RoomBooking | Notification | 1 – 0..* | Each booking can trigger multiple notifications (email/SMS). |
| RoomBooking | Invoice | 1 – 1 | Each booking results in exactly one invoice being generated. |
| Invoice | BillTransaction | 1 – 1..* | One invoice may involve one or more payment transactions. |
| Catalog | Room | 1 – 0..* | A catalog maintains a list of multiple rooms available for booking. |
| Room | RoomKey | 1 – 0..* | A room may have multiple keys (e.g., digital, physical). |
| Account | Person (Guest, etc.) | 1 – 1 | Each account is tied to exactly one person entity and vice versa. |

Here is the complete class diagram for our hotel management system:

![alt text](class-diagram-of-the-hotel-management-system.png)


## Design pattern  
We apply the object-oriented design patterns based on system behavior to implement hotel management systems' core features in a flexible and scalable way.

* The hotel system allows guests to receive notifications (like booking confirmations or reminders). The Observer design pattern can be used to model this behavior.
* The hotel system supports payment methods such as credit card, cash, and check. To handle this flexibly, we can use the Strategy design pattern.
* Booking confirmations may need to trigger different notifications (SMS or Email). We can use the Factory design pattern to manage these interchangeable notification types.
* We know that each payment type (cash, credit, check) follows a similar flow, but with some specific steps. We can use the Template Method design pattern to structure this reusable logic.
* We know that the catalog needs to manage and search through a collection of individual room objects. We can use the Composite design pattern to treat individual and grouped room operations uniformly.
* We know that the catalog or hotel instance should be globally accessible and should not have multiple copies. To enforce a single instance, we can use the Singleton design pattern.
* Room bookings and invoices involve setting multiple details, such as dates, rooms, guests, and status. The Builder design pattern can help us manage this complex construction clearly and safely.
* We know that components like notification services or payment processors might change or need mocking in tests. To make the system more flexible and testable, we can use Dependency Injection.

## Additional requirements
The interviewer can introduce some additional requirements in the given hotel management system, or they can ask some follow-up questions. Let’s see examples of the additional requirements:

Discount: Depending on special events such as the New Year, branch opening, a discount will be applied to the payment. The class diagram provided below shows the relationship of `Discount` with the `BillTransaction` class:

![alt text](Discount-with-BillTransaction.png)
