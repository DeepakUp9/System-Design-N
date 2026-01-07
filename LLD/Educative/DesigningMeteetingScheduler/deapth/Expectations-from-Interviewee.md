 Expectations from the Interviewee

> **This section is about showing design maturity, not solutions yet.**

---

## 1. Room Assignment 🏢

### What (LLD Framing)

**Room assignment** is the process of selecting a single meeting room that:
- ✅ Is available for the meeting time
- ✅ Has sufficient capacity
- ✅ Satisfies any implicit constraints (location, type, amenities)

---

### Why Interviewers Focus Here

**Rooms are shared, limited resources**

This tests:
- ✅ Constraint handling
- ✅ Conflict prevention
- ✅ State consistency

#### 🎯 Interviewer is checking:
> *"Does this candidate treat room booking as a resource allocation problem?"*

---

### How (What You Should Clarify Aloud)

#### Q1: How does the system determine available rooms?

**Good LLD Direction:**
- Each room has its own **booking schedule**
- Availability is checked by **time overlap validation**, not by status flags

**💬 What to Say:**
> *"I'd model room availability as a list of booked time slots and validate overlap for the requested window."*

**Example logic:**
```java
class Room {
    List<TimeSlot> bookedSlots;
    
    boolean isAvailable(TimeSlot requested) {
        return bookedSlots.stream()
            .noneMatch(slot -> slot.overlaps(requested));
    }
}
```

---

#### Q2: How important is room capacity?

**Expected Answer:**
- Capacity is a **hard constraint**, not a preference
- A room must satisfy: `room.capacity ≥ confirmed participant count`

**Bonus Clarity:**
Capacity may change dynamically if:
- Participants are added/removed
- Optional attendees are excluded
- Some invitees decline

**💡 Interview Signal:**
> You understand that **capacity checks must be re-evaluated on updates.**

---

### Extra Room-Related Clarifications (Strong Candidates Ask These)

| Question | Why It Matters |
|----------|----------------|
| Are rooms **location-specific**? | Affects room selection algorithm |
| Can rooms be **reassigned on update**? | Tests update operation design |
| Is room selection **automatic or manual**? | Changes validation logic |
| Do rooms have **equipment requirements**? | Additional constraints to model |
| Is there a **booking priority system**? | Conflict resolution strategy |

---

## 2. Availability of Attendees 👥

### What (LLD Framing)

**Attendee availability** determines whether a meeting can exist at a given time.

It's not just "are they free?" — it's:
- Can they attend?
- Are they required?
- What happens if they decline?

---

### Why Interviewers Push on This

**People schedules are independent, mutable**

This exposes:
- ✅ Relationship modeling
- ✅ Read vs write ownership
- ✅ Dependency handling

#### ⚠️ This is where weak designs break.

---

### How (LLD-Level Reasoning)

#### Q1: How does the system check attendee availability?

**Expected Thinking:**
- Availability is **derived from**:
  - Each attendee's calendar events
- It's **not a simple boolean**; it's a **time window validation**

**💬 What to Say:**
> *"Availability is computed by checking for overlapping meetings in each participant's calendar."*

**Example logic:**
```java
class AvailabilityChecker {
    boolean isAvailable(User user, TimeSlot slot) {
        List<Meeting> userMeetings = calendar.getMeetings(user, slot);
        return userMeetings.isEmpty();
    }
    
    List<User> getAvailableUsers(List<User> users, TimeSlot slot) {
        return users.stream()
            .filter(user -> isAvailable(user, slot))
            .collect(Collectors.toList());
    }
}
```

---

#### Q2: How does the system access attendees' meeting info?

**LLD Expectation:**
- **Calendars are the source of truth**
- Meeting scheduler **does not own** calendar data
- Scheduler **queries** calendars, doesn't duplicate them

**🎯 Strong Statement:**
> *"The scheduler queries participant calendars instead of duplicating meeting data."*

**Design principle:**
```
Meeting Scheduler
    ↓ (queries)
Calendar Service
    ↓ (owns)
Calendar Data

NOT:
Meeting Scheduler
    ↓ (duplicates)
Calendar Data ❌
```

---

### Extra Attendee-Related Clarifications Worth Adding

| Clarification | Impact |
|---------------|--------|
| **Required vs optional attendees** | Meeting validity depends on required only |
| **Partial acceptance handling** | What if only 3/10 accept? |
| **Decline impact on room/capacity** | Should room be downsized? |
| **Organizer-only overrides** | Can organizer force-schedule? |
| **Tentative responses** | How does "maybe" affect planning? |

---

## 3. Expectations the Interviewer Implicitly Has (Important)

### 🎯 These are NOT written but expected to be surfaced by you.

---

### A. Consistency Expectations 🔄

**A meeting update must:**
- ✅ Update room booking
- ✅ Update all participant calendars
- ✅ Send notifications
- ✅ Handle rollback on any failure

#### 💡 Interviewer is checking:
> *"Do you think in terms of side effects?"*

**Transaction flow:**
```
Update Meeting
    ├─ Validate constraints
    ├─ Update Meeting entity
    ├─ Update Room booking
    ├─ Update each Calendar
    ├─ Send Notifications
    └─ Rollback if any step fails
```

---

### B. Conflict Handling Expectations ⚔️

**What happens when:**
- Two meetings try to book same room?
- Attendee accepts after capacity is exceeded?
- Room becomes unavailable after booking?
- Participant double-books themselves?

**💬 Good Phrasing:**
> *"All scheduling operations should be validated atomically."*

**Design considerations:**
```
Optimistic locking for room booking
Validation before commit
Conflict resolution strategy:
  - First-come-first-served?
  - Priority-based?
  - Manual resolution?
```

---

### C. Responsibility Boundaries 🎯

**Clear separation of concerns:**

| Component | Responsibility |
|-----------|----------------|
| **Scheduler** | Coordinates operations |
| **Calendar** | Stores availability |
| **Notification System** | Delivers messages |
| **Room Manager** | Handles room bookings |
| **User Service** | Manages user data |

**💡 This shows clean separation of concerns.**

**Design principle:**
```
Meeting Scheduler
    ↓ (coordinates)
    ├─ Calendar Service
    ├─ Room Service
    ├─ Notification Service
    └─ User Service

Each service owns its domain
```

---

## 4. What NOT to Do in the Interview ❌

### 🚫 Jump Straight To:

| Don't Do | Why It's Bad |
|----------|--------------|
| ❌ Database schema | Shows database-first thinking |
| ❌ APIs | Skips domain modeling |
| ❌ Code implementation | Premature optimization |
| ❌ UI mockups | Not relevant to LLD |
| ❌ Technology choices | Not the focus |

---

### 🚫 Assume:

| Don't Assume | Better Approach |
|--------------|-----------------|
| ❌ Infinite rooms | Ask about capacity limits |
| ❌ Always-available attendees | Model availability explicitly |
| ❌ No rescheduling | Design for change |
| ❌ Single time zone | Clarify scope |
| ❌ No conflicts | Build conflict handling |

---

## 5. High-Impact Closing Line (Optional, But Strong)

### 💬 You Can Say:

> *"Before diving into design, I'd clarify constraints around room assignment and attendee availability because they drive most of the system complexity."*

**Why this works:**
- ✅ Shows you understand priorities
- ✅ Demonstrates structured thinking
- ✅ Signals you won't jump into code
- ✅ Opens discussion with interviewer

---

## 🎯 Interview Strategy Summary

### Phase 1: Clarification Questions (Strong Start)

```
Room Assignment:
  ✅ "How do we handle room capacity constraints?"
  ✅ "Can rooms be reassigned after booking?"
  ✅ "Is room selection manual or automatic?"

Attendee Availability:
  ✅ "How do we access participant calendars?"
  ✅ "What happens with partial acceptance?"
  ✅ "Are there required vs optional attendees?"

Consistency:
  ✅ "How do we ensure atomicity of updates?"
  ✅ "What's the rollback strategy?"
```

---

### Phase 2: Show Design Maturity

**Use these phrases:**

| Phrase | Shows |
|--------|-------|
| *"Availability is computed, not stored"* | Understanding of derived state |
| *"Room booking is a resource allocation problem"* | Systems thinking |
| *"Operations must be atomic"* | Concurrency awareness |
| *"Calendars are the source of truth"* | Ownership clarity |
| *"Side effects must be coordinated"* | Transaction thinking |

---

### Phase 3: Avoid Red Flags

**Don't say:**
- ❌ "I'll just add a status field"
- ❌ "Let's create a meetings table"
- ❌ "We can handle that in the API"
- ❌ "I'll assume that never happens"

**Instead say:**
- ✅ "I'll model this as a state machine"
- ✅ "I'll separate concerns between entities"
- ✅ "I'll design for that edge case"
- ✅ "Let me clarify the constraints first"

---

## 🧠 Key Mindset Shifts

### From → To

| Avoid | Prefer |
|-------|--------|
| "What tables do I need?" | "What entities exist in this domain?" |
| "What APIs should I create?" | "What operations must the system support?" |
| "How do I store this?" | "Who owns this responsibility?" |
| "Let me code this" | "Let me model the relationships first" |

---

## 📋 Pre-Interview Checklist

**Before the interview, review:**

- [ ] Room as a resource concept
- [ ] Time slot overlap validation
- [ ] Calendar as source of truth
- [ ] Participation as relationship entity
- [ ] Atomic operations and consistency
- [ ] Side effects and coordination
- [ ] State machines for status
- [ ] Separation of concerns

---