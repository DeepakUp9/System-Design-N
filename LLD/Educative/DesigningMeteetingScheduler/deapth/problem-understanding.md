# LLD Chapter: Meeting Scheduler – Getting Ready / Problem Framing

> **Comprehensive Study Notes for Low-Level Design Interviews**

---

## 1. What is this problem (LLD framing)?

### What

A **meeting scheduler** is a coordination system that:

- ✅ Aligns people availability
- ✅ Allocates shared resources (meeting rooms)
- ✅ Maintains state consistency across calendars, meetings, and notifications

### From an LLD perspective:

> **This is a state-heavy, rule-driven system with multiple actors modifying the same data.**

### Interview Translation:
> *"This is a multi-entity coordination and consistency problem."*

---

## 2. Why is this problem asked in LLD interviews?

### Why Interviewers Love It

| Reason | Explanation |
|--------|-------------|
| ✅ **Deceptively simple** | Looks easy, but quickly exposes design depth |
| ✅ **Real-world constraints** | Tests ability to model complex business rules |
| ✅ **Conflict handling** | Must manage conflicting schedules |
| ✅ **Data consistency** | Multiple entities must stay in sync |
| ✅ **Extensibility** | Design must accommodate future changes |

---

### What They're NOT Testing ❌

- ❌ UI design
- ❌ Time-zone math
- ❌ Distributed systems scale (unless explicitly asked)
- ❌ Database optimization
- ❌ Front-end implementation

---

### What They ARE Testing ✅

- ✅ **Entity modeling** – Identifying correct domain objects
- ✅ **Responsibility boundaries** – Who owns what logic
- ✅ **State transitions** – How entities change over time
- ✅ **Edge cases** – Overlaps, cancellations, partial updates

---

## 3. Core Capabilities (What the System Must Support)

---

### A. Assigning Meeting Rooms 🏢

#### What

Select a room based on:
- Capacity ≥ participant count
- Availability for the requested time window

#### Why

- Rooms are **shared resources**
- **Double booking is unacceptable**
- Capacity mismatch breaks real-world constraints

#### How (LLD Thinking)

**A room has:**
- **Static attributes**: id, capacity, location, amenities
- **Dynamic state**: booked time slots

**Room assignment is:**
- A **constraint-checking operation**, not just a lookup
- Must validate availability AND capacity

#### 💡 Interview Signal:
> **Do you separate room metadata from room schedule?**

**Good answer structure:**
```
Room (entity) → metadata
RoomSchedule (entity) → time slots
RoomAvailabilityService (service) → checks conflicts
```

---

### B. Determining Optimal Meeting Time ⏰

#### What

Find a time slot where:
- All **required** participants are available
- At least one **suitable room** is free

#### Why

- **People are the bottleneck**, not rooms
- Conflicts are inevitable in real organizations
- Partial availability needs handling

#### How (LLD Thinking)

**Availability is:**
- A function of each participant's calendar
- An **intersection problem**, not a search problem

**Time selection is:**
- Check all calendars → find common free slots
- Filter by room availability

#### 🎯 Key LLD Insight:
> **You don't "book a time" first — you validate feasibility first.**

**Process flow:**
```
1. Gather all participant calendars
2. Find intersection of free slots
3. Check room availability for those slots
4. Propose valid options
5. Book only after confirmation
```

---

### C. Managing the Meeting Lifecycle 🔄

#### What

Operations:
- **Create** meeting
- **Update** (time, room, participants)
- **Cancel** meeting
- **Add/remove** participants
- **Change** time or room

#### Why

- Meetings are **long-lived entities**
- **Changes are more common than creation**
- Each change affects multiple entities

#### How (LLD Thinking)

**A meeting is a stateful aggregate**

**Operations must:**
1. Validate constraints **again** (not just at creation)
2. Propagate changes to dependent entities
3. Handle rollback if any step fails

#### 💡 Interview Signal:
> **Do you treat updates as first-class operations, not special cases?**

**Example: Meeting Update Flow**
```
Update meeting time:
  ├─ Validate new time against all calendars
  ├─ Check room availability
  ├─ Update meeting entity
  ├─ Update all participant calendars
  ├─ Release old room booking
  ├─ Create new room booking
  └─ Send notifications
```

---

### D. Notifications & Calendar Updates 📧

#### What

Notify users when:
- **Invited** to a meeting
- Meeting **updated** (time, room, participants)
- Meeting **cancelled**

Update calendars automatically on:
- Accept/decline responses
- Meeting modifications

#### Why

- Manual syncing leads to **inconsistency**
- Users rely on calendars as the **source of truth**
- Real-time updates improve user experience

#### How (LLD Thinking)

**Notifications are:**
- **Side effects**, not core logic
- Should not block main operations
- Can be async/event-driven

**Calendar updates are:**
- **State synchronization**, not just messages
- Must be transactional with meeting changes

#### 🎯 Strong Answer:
> *"I would decouple notification delivery from meeting logic using an event-driven approach."*

**Design pattern:**
```
Meeting operation completes
    ↓
Publish event (MeetingCreated, MeetingUpdated)
    ↓
NotificationService subscribes
    ↓
Send emails/push notifications asynchronously
```

---

### E. Tracking Invite & Response Status 📋

#### What

Track per participant:
- **Invited** – Initial state
- **Accepted** – Confirmed attendance
- **Declined** – Not attending
- **Tentative** – Maybe attending
- **Removed** – No longer invited

#### Why

- Organizer decisions depend on responses
- Availability changes after responses
- Room size validation depends on accepted count

#### How (LLD Thinking)

**Response is:**
- **Relationship data** between `Meeting ↔ Participant`
- **Not a boolean** — it's a state machine
- Has its own lifecycle

#### 💡 Interview Signal:
> **Do you model participation as an entity, not a flag?**

**Good design:**
```java
class MeetingParticipation {
    Meeting meeting;
    User participant;
    ParticipationStatus status; // INVITED, ACCEPTED, DECLINED
    boolean isRequired;
    DateTime responseTime;
}
```

**State transitions:**
```
INVITED ──accept──▶ ACCEPTED
   │
   └───decline──▶ DECLINED
   │
   └───tentative──▶ TENTATIVE
```

---

## 4. Hidden Constraints Interviewers Expect You to Surface

### 🎯 These are NOT in the problem statement—but mentioning them scores points:

| Constraint | Why It Matters |
|------------|----------------|
| **Overlapping meetings** | Same person invited to conflicting times |
| **Required vs optional attendees** | Meeting can proceed without optional |
| **Organizer vs participant permissions** | Who can edit/cancel? |
| **Partial acceptance** | What if only 3/10 accept? |
| **Last-minute cancellations** | How to handle room release? |
| **Rescheduling ripple effects** | Cascade to dependent meetings? |
| **Recurring meetings** | Weekly/monthly patterns |
| **Buffer time** | 5-min gap between meetings |

### 💬 Saying this line helps:
> *"Before designing, I'd clarify how strict availability and responses are."*

**Good clarifying questions:**
```
"Should the system:
 - Block overlapping meetings for same user?
 - Suggest alternative times automatically?
 - Handle partial acceptance scenarios?
 - Support recurring meetings?
 - Enforce buffer time between meetings?"
```

---

## 5. Design Mindset Expected in the Interview

### ✅ Good LLD Answer Sounds Like:

| Statement | Why It's Good |
|-----------|---------------|
| *"I'll model meetings as aggregates"* | Shows understanding of domain design |
| *"Room booking must be atomic"* | Recognizes concurrency concerns |
| *"Availability checks should be reusable"* | Thinks about abstraction |
| *"Notifications should not block core flow"* | Understands async patterns |
| *"I'll use a state machine for participation"* | Shows design pattern knowledge |

---

### ❌ Bad LLD Answer Sounds Like:

| Statement | Why It's Bad |
|-----------|--------------|
| *"First I'll create tables"* | Database-first, not domain-first |
| *"Then I'll write APIs"* | Skips design, jumps to implementation |
| *"I'll just update the calendar directly"* | Doesn't consider consistency |
| *"I'll add an if-else for each case"* | No design patterns, procedural thinking |
| *"Let's add a status field"* | Over-simplifies state management |

---

## 6. What Comes Next (Awareness, Not Implementation Yet)

### Typical Next Interviewer Steps:

```
Step 1: Identify Core Entities
    ↓
Step 2: Define Relationships
    ↓
Step 3: Decide Ownership of Responsibilities
    ↓
Step 4: Handle Conflicts & Updates
    ↓
Step 5: Apply Design Patterns
    ↓
Step 6: Code Key Classes
```

### 📌 Be Ready For:
- "What are your core entities?"
- "How do you handle double booking?"
- "What if someone declines after accepting?"
- "How do you notify users?"
- "Show me the Meeting class"

---

## 🎯 Quick Reference: Core Entities Preview

| Entity | Responsibility |
|--------|---------------|
| **User** | Participant identity |
| **Calendar** | User's schedule |
| **Meeting** | Meeting aggregate |
| **MeetingParticipation** | User-Meeting relationship |
| **Room** | Physical resource |
| **RoomBooking** | Room-time slot reservation |
| **TimeSlot** | Time range abstraction |
| **Notification** | Communication event |

---

## 💡 Interview Strategy

### Phase 1: Clarification (5 min)
```
✅ Ask about constraints
✅ Confirm scope
✅ Identify edge cases
```

### Phase 2: Entity Modeling (10 min)
```
✅ List all entities
✅ Define attributes
✅ Establish relationships
```

### Phase 3: Behavior Design (10 min)
```
✅ Core operations
✅ State transitions
✅ Validation logic
```

### Phase 4: Patterns & Code (15 min)
```
✅ Apply design patterns
✅ Code key classes
✅ Show extensibility
```

---

## 🧠 Key Takeaways

### Remember These Points:

1. **Meetings are aggregates** – They own their lifecycle
2. **Availability is computed** – Not stored
3. **Rooms are resources** – Need conflict resolution
4. **Participation has state** – Not just a boolean
5. **Notifications are async** – Don't block core logic
6. **Updates are complex** – Treat as first-class operations

### Interview-Winning Phrases:

> ✅ *"This is a coordination and consistency problem"*  
> ✅ *"I'll separate concerns between scheduling and notification"*  
> ✅ *"Room booking requires atomic operations"*  
> ✅ *"I'll model participation as a relationship entity"*  
> ✅ *"Availability checks should be constraint-based"*

---
