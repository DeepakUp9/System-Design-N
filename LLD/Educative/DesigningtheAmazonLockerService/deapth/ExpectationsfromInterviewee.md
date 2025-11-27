# ✅ Step 2: Expectations from You (Interviewee) — Explained Simply

In an Amazon Locker LLD interview, the interviewer is **NOT** judging your coding skills. They are testing:

1. How you explore the problem
2. How you identify components
3. How you remove ambiguity
4. How you justify your design choices

So you must **ask the right clarifying questions** before designing anything.

Below is the meaning behind each expectation and what exactly you should ask.

---

## ✅ 1. Locker Size — What the interviewer expects

Amazon lockers are **physical hardware**, not a virtual thing. So **size matters**.

The interviewer wants to see if you think about:
- ✔ Package dimensions
- ✔ Locker dimensions
- ✔ Compatibility rules
- ✔ Constraints

### 🔶 What YOU should ask (exact expected questions):

1. **How many locker sizes exist?** (Small, medium, large?)
2. **Does each package come with length/width/height measurements?**
3. **Should the system reject orders if no locker can fit the package?**
4. **Is weight a factor?**
5. **Are hazardous items allowed?**

**Why these matter?** Because locker assignment changes completely depending on size.

---

## ✅ 2. Locker Selection — What the interviewer expects

This is the **heart of the system**. You must show you understand conflict cases, concurrency, and fairness.

The interviewer expects questions like:

### 🔶 Key questions to ask:

1. **Does the customer choose a locker location or system chooses automatically?**
   - Usually: customer chooses the location → system chooses the locker.

2. **How do we prevent two users from getting the same locker?**
   - Hint: Locks, transactions, atomic assignment.

3. **Can the same customer get multiple lockers for multiple orders?**
   - Usually yes, but may depend on capacity.

4. **Should the system try to minimize distance inside locker bank?**
   - Example: prefer closest locker, or any?

5. **Must locker size match package size?**
   - Example: small package → do we allow putting it in a large locker? (Important design question.)

These questions show you can **think like a real engineer**.

---

## ✅ 3. Locker Status — What the interviewer expects

Lockers change status:
- Empty
- Reserved
- Occupied
- Expired
- Out of service

The interviewer wants to see if you ask about **time constraints, expired items, and handling failures**.

### 🔶 Expected questions:

1. **How long can a package remain in a locker?** (24 hrs? 48 hrs?)

2. **What happens when the customer does NOT pick up?**
   - Refund?
   - Package returned to warehouse?
   - Locker freed automatically?

3. **Does operating hours matter?**
   - Some locker locations close at night.

4. **Can lockers go out of service?**
   - Hardware issues?

5. **Who empties expired lockers?**
   - Field agent?
   - Logistics team?

These influence your **state machine and class diagram**.

---

## ✅ 4. Returning an Item — What the interviewer expects

Return flow is **different from delivery flow**.

The interviewer expects you to ask:

### 🔶 Must ask questions:

1. **Can customers return items using lockers?** (Yes in real Amazon.)

2. **Is the locker same as pickup locker?** 
   - Usually: No — system assigns a new locker based on availability.

3. **Does return also require size matching?** 
   - Yes — the returned package must fit a locker.

4. **Is there a time window for dropping off return items?** 
   - Example: Return must be done within 3 days.

5. **Is a new code generated for returns?** 
   - Usually: yes.

These questions show that you understand the system supports **two workflows**:
- **Deliver → Pickup**
- **Return → Collect by logistics team**

---

## 🔥 How Interviewers Evaluate You

They check:

| Skill | How they check it |
|-------|------------------|
| Understanding constraints | Your questions |
| Thought process | How you reason |
| Object-oriented thinking | Your class choices |
| System correctness | Your edge case handling |
| Scalability thinking | Concurrency, lock management |
| Practical mindset | Real-world behavior |

---

## ✔ If you ask these questions properly…

You will look:
- **Strong**
- **Structured**
- **Senior**
- **Interview-ready**

---

## 📝 Quick Reference: Key Questions Checklist

### Locker Size
- [ ] How many locker sizes exist?
- [ ] Package dimensions available?
- [ ] Reject orders if no fit?
- [ ] Weight considerations?
- [ ] Hazardous items allowed?

### Locker Selection
- [ ] Customer chooses location vs system?
- [ ] Concurrency handling?
- [ ] Multiple lockers per customer?
- [ ] Distance optimization?
- [ ] Size matching rules?

### Locker Status
- [ ] Package retention time?
- [ ] Expired package handling?
- [ ] Operating hours impact?
- [ ] Out of service scenarios?
- [ ] Who clears expired items?

### Returns
- [ ] Return functionality supported?
- [ ] Same locker for return?
- [ ] Size matching for returns?
- [ ] Return time window?
- [ ] New code generation?

---

**Remember:** The goal is not to have all the answers, but to ask the **right questions** that demonstrate deep understanding of the problem domain.