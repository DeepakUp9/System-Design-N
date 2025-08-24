# Elevator System Low-Level Design

1. Elevators and Floors: The system shall include 3 elevator cars. The elevators will service a building that has up to 15 floors.
2. Elevator Movement: Elevators shall be capable of moving up, moving down, or remaining idle.
3. Elevator Door Operation: Elevator doors shall open only when the elevator is idle and not in motion.
4. Floor Access: Each elevator shall be capable of stopping at every floor in the building.
5. Outside Control Panel: An external panel with up and down buttons shall be available on each floor to call an elevator.
6. Inside Control Panel: Inside each elevator, a control panel with buttons for all floors and door operation (open/close) shall be provided.
7. Displays: Each elevator shall have internal and external displays. External displays shall indicate the current floor and the direction of movement. Internal displays shall show the current floor, the direction of movement, and the elevator’s capacity.
8. Floor Panels and Displays: Each floor shall have a panel with buttons for calling elevators and displays indicating the status of each elevator.
9. Passenger Directions: The system shall accommodate multiple passengers going to different floors and in different directions simultaneously.
10. Elevator System Control: The system shall manage elevator movement, door operations, and monitor elevator statuses.
11. Smart Dispatch System: Upon a passenger calling for an elevator, the system shall intelligently assign the most suitable elevator based on its current location and trajectory.
12. Capacity: Each elevator shall have a maximum capacity of eight people or 680 kilograms.
13. Maximum Number of Elevators: The building shall be equipped with up to three elevators to service all floors.


### Elevator System Requirements

## System Overview
The system shall include **3 elevator cars** servicing a building with **up to 15 floors**.

## Functional Requirements

### 1. Elevator Movement
- Elevators shall be capable of:
  - Moving up
  - Moving down
  - Remaining idle

### 2. Elevator Door Operation
- Doors shall open **only** when:
  - The elevator is idle
  - The elevator is not in motion

### 3. Floor Access
- Each elevator shall stop at **every floor** in the building

### 4. Control Panels
#### Outside Control Panel
- External panel on each floor with:
  - Up button
  - Down button

#### Inside Control Panel
- Control panel in each elevator with:
  - Buttons for all floors
  - Door operation buttons (open/close)

### 5. Displays
#### External Displays
- Current floor
- Direction of movement

#### Internal Displays
- Current floor
- Direction of movement
- Elevator's capacity

### 6. Floor Panels
- Each floor shall have:
  - Call buttons
  - Displays indicating elevator statuses

### 7. Passenger Handling
- The system shall accommodate:
  - Multiple passengers
  - Different destination floors
  - Different directions simultaneously

### 8. System Control
The system shall manage:
- Elevator movement
- Door operations
- Elevator status monitoring

### 9. Smart Dispatch System
- Upon call, assigns most suitable elevator based on:
  - Current location
  - Current trajectory

## Technical Specifications

| Specification               | Value                     |
|-----------------------------|---------------------------|
| Number of Elevators         | 3                         |
| Number of Floors            | Up to 15                  |
| Maximum Capacity per Elevator | 8 people or 680 kilograms |