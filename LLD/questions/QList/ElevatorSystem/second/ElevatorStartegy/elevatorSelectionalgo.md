# Elevator Control Strategies Notes

## 1. Even–Odd Elevator Logic

### How it Works
- Splits elevators into **odd-floor and even-floor cars**.
  - Odd elevators → serve odd floors (1,3,5,…)
  - Even elevators → serve even floors (2,4,6,…)
- Hall call is served by the appropriate elevator.
- Inside the cabin:
  - Old/simple systems: only odd/even buttons work.
  - Modern systems: cabin may allow all floors but requests outside its service are reassigned.

### Example
- 20-floor apartment:
  - Lift A → odd floors
  - Lift B → even floors
- Floor 7 calls Up → only Lift A comes.
- Floor 8 calls Down → only Lift B comes.

### Use Case
- High-rise residential apartments (20+ floors)
- Hotels
- Older office towers without smart dispatch

### Pros & Cons
| Pros | Cons |
|------|------|
| Faster trips (fewer stops) | Poor UX if wrong lift is taken |
| Less crowded elevators | Not suitable for offices where flexibility is needed |
| Cost-effective compared to adding lifts | Can confuse users |

---

## 2. Zoning

### How it Works
- Elevators are assigned **floor ranges** (zones):
  - Lift 1 → floors 1–15
  - Lift 2 → floors 16–30
- All floors within the zone are served by the assigned elevators.
- Hall calls are sent only to elevators serving that zone.

### Example
- 30-floor office:
  - Lift A/B → floors 1–15
  - Lift C/D → floors 16–30
- Floor 10 calls Up → Lift A or B comes
- Floor 25 calls Down → Lift C or D comes

### Use Case
- Mid/high-rise office towers & hotels (20+ floors)
- Reduces stops, avoids odd/even confusion

### Pros & Cons
| Pros | Cons |
|------|------|
| Efficient for tall buildings | Slight learning curve for users |
| Reduces waiting and travel time | Less flexible than all-floor elevators |
| Avoids odd/even mismatch | Requires some control logic |

---

## 3. Conventional Group Control (All-Floor Lifts)

### How it Works
- All elevators serve **all floors**.
- Hall button pressed → system chooses **best elevator**.
- Inside cabin, all floor buttons are active.
- Elevator moves sequentially, stopping at requested floors.

### Example
- 10-floor office, 4 elevators
- Floor 3 presses Up → elevator on floor 2 going Up may come
- Floor 7 presses Down → nearest car going Down responds
- Inside elevator, user can press any floor (1–10)

### Nearest Car Algorithm
- Criteria for assignment:
  1. Distance to the calling floor
  2. Current direction of travel
  3. Load / occupancy
  4. Idle elevators
- Scoring:

score = distance_factor + direction_factor + load_factor

- Elevator with lowest score is selected
- Tie-breakers: idle elevators, least used elevator, random selection

### Use Case
- Medium-rise offices
- Buildings where passengers need full flexibility
- Cost-effective, easy to maintain

### Pros & Cons
| Pros | Cons |
|------|------|
| Flexible (any floor) | May be slower if crowded |
| Simple, low maintenance | Not optimized for heavy traffic |
| Works well in medium-rise buildings | Stops at every requested floor |

---

## 4. Destination Dispatch System (DDS)

### How it Works
1. **Call Stage**
 - Passenger enters destination **before entering** the elevator.
 - Example: Ground floor → press 15 on lobby panel
2. **Assignment Stage**
 - System assigns **optimized elevator** based on:
   - Similar destinations (grouping)
   - Current positions and directions
   - Load balancing
3. **Boarding Stage**
 - Enter the assigned elevator
 - Inside cabin usually **no floor buttons**
4. **Travel Stage**
 - Elevator picks up passengers going to nearby floors
 - Optimized sequence to minimize stops

### Example
- 5 passengers on ground want floors 12, 14, 15, 27, 28
- Conventional elevator: all go in same lift → 5 stops
- DDS:
- Group 12,14,15 → Lift A (3 stops)
- Group 27,28 → Lift B (2 stops)
- Result: Faster trips, less crowding

### Use Case
- Modern high-rise offices
- Luxury hotels
- Corporate HQs

### Pros & Cons
| Pros | Cons |
|------|------|
| Minimal stops, fast travel | High cost and complexity |
| Groups passengers efficiently | Requires software and panels |
| Avoids wrong-lift problem | May need user training for lobby panels |
| Adaptive to traffic patterns | Maintenance requires skilled technicians |

---

## 5. Summary Table of Strategies

| Feature | Even–Odd | Zoning | Conventional (All-Floor) | Destination Dispatch |
|---------|----------|--------|---------------------------|-------------------|
| Stops per trip | ~50% fewer | ~50% fewer | May be many | Minimal, optimized |
| Cabin buttons | Limited / sometimes blocked | All within zone | All active | Usually none |
| Hall call assignment | Odd/even elevators only | Elevators in zone | Best elevator based on nearest-car algorithm | Assigned by system based on destination |
| Best for | Old apartments, hotels | Mid/high-rise offices | Medium-rise offices | Modern skyscrapers, luxury buildings |
| User experience | Medium | Good | Flexible but may be slower | Excellent |
| Complexity | Low | Medium | Low | High |
| Modern relevance | Rare | Still common | Very common | Increasingly popular |

---

## 6. Key Takeaways

- **Even–Odd** → old-school optimization, good for tall residential/hotel buildings.
- **Zoning** → smart floor-range grouping, reduces stops, common in offices.
- **Conventional All-Floor** → flexible, simple, good for medium-rise buildings.
- **Destination Dispatch** → future-proof, intelligent, minimal stops, best UX.

---

## 7. Example: Nearest Car Algorithm in Conventional Lifts

**Scenario:** Floor 7 presses Up, elevators at floors 3, 10, 12

| Elevator | Distance | Direction | Score | Selected? |
|----------|---------|-----------|-------|-----------|
| Floor 3 | 4 | Up | 4 | ✅ Candidate |
| Floor 10 | 3 | Down | 5 | ❌ Not going in desired direction |
| Floor 12 | 5 | Down | 6 | ❌ Not suitable |

- Result: Elevator on **floor 3 going Up** serves the request

---

### References / Notes
- Modern elevators combine **group control + traffic prediction + adaptive algorithms**.
- Even–Odd or Zoning are mostly used where **full smart dispatch is not implemented**.
- DDS provides the **best efficiency in tall, high-traffic buildings**.

