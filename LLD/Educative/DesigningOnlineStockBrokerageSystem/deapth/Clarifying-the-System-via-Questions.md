# LLD Chapter: Expectations from the Interviewee

## Topic: Clarifying the System via Questions

---

## What is this section really testing?

**This section is not about features.**

It tests whether you:

- ✅ Can discover missing requirements
- ✅ Understand that LLD starts with ambiguity
- ✅ Know how to narrow scope before designing

> **Interviewers expect you to ask intelligent questions, not assume.**

---

## Discoverability

### What does discoverability mean here?

**Discoverability** refers to how users find stocks inside the system.

This includes:
- Searching by name or symbol
- Browsing categories or trends
- Surfacing relevant results efficiently

### In LLD terms, this points to:

- Query patterns
- Read-heavy workflows
- Index-driven access paths

---

### Why does the interviewer care?

**Because discoverability:**

| Impact | Description |
|--------|-------------|
| **User experience** | Direct product quality driver |
| **System load** | Search is read-intensive |
| **Data modeling** | Influences index design |

**If you ignore this, you might:**
- ❌ Over-design trading
- ❌ Under-design search, which users use most

> **Calling this out shows product + engineering awareness.**

---

### How do you discuss it in an interview?

**You don't design search yet.**  
**You ask:**

1. **Is search keyword-based or symbol-based?**
2. **Should results be ranked or exact-match?**
3. **Is search real-time or cached?**

**This signals:**

> **"I won't design blindly."**

---

### Additional angles worth mentioning

- **Is search read-only or does it affect trading decisions?**
- **Is the stock universe static or dynamic?**
- **Do we need autocomplete or fuzzy matching?**

**Why this matters:**

- Search is often the **highest QPS component**
- Poor discoverability = poor product, regardless of trade engine quality

**Interview signal:**

> **"I'm aware of load concentration points."**

---

## Visibility

### What is visibility in this context?

**Visibility** defines:

- Who can see what data
- At what time
- At what freshness level

**Examples:**
- Stock prices
- Holdings
- Open positions

---

### Why is this important?

**Because visibility controls:**

| Aspect | Impact |
|--------|--------|
| **Data consistency guarantees** | Strong vs eventual |
| **Access control rules** | Security implications |
| **Performance expectations** | Real-time vs delayed |

**For example:**
- Everyone seeing live positions → heavy real-time reads
- Restricted visibility → simpler consistency rules

> **Mentioning this shows you understand data exposure risk.**

---

### How do you frame this in LLD?

**You ask:**

1. **Are positions real-time or delayed?**
2. **Are there role-based views?**
3. **Is data eventually consistent or strongly consistent?**

> **You are shaping read models, not implementing them.**

---

### Hidden complexity interviewers expect you to sense

**Different freshness for different data:**

```
Prices → near real-time
Portfolio → slightly delayed acceptable
Historical trades → immutable
```

**Why this matters:**
- Not all data needs the same consistency guarantees
- Over-consistency leads to performance bottlenecks

**Strong interview phrasing:**

> **"We can relax consistency where correctness isn't impacted."**

---

## Order Type

### What is being clarified here?

**Order type** defines **business behavior**, not UI behavior.

**Examples:**
- Market orders
- Limit orders
- Stop-loss orders

**Each order type:**
- Has different validation rules
- Has different execution logic
- Has different lifecycle states

---

### Why does this matter in LLD?

**Because:**

| Reason | Impact |
|--------|--------|
| **Order types directly influence class design** | Inheritance vs composition |
| **They affect extensibility** | Can new types be added? |
| **They define trade workflow complexity** | State machine design |

**Interview insight:**

> **"Order is not just data; it's behavior."**

---

### How do you talk about it?

**You ask:**

1. **Which order types are in scope?**
2. **Can new types be added later?**
3. **Do order types share common behavior?**

**This naturally leads to:**
- Polymorphism
- Strategy-based thinking (without naming it yet)

---

### What's implicitly being tested

**Do you treat order types as:**

- ❌ Conditional logic?
- ✅ Behavioral variants?

**Interviewers want the latter.**

**Why:**
- New order types are common in trading systems
- Rigid designs break quickly

**Extra question you can ask:**

> **"Can order execution be partially fulfilled?"**

**This shows real-world trading awareness.**

---

## Multiplicity

### What does multiplicity mean here?

**Multiplicity** refers to one-to-many relationships in the system.

**Examples:**

```
One user → many watchlists
One watchlist → many stocks
One stock → many purchases over time
```

---

### Why is this critical?

**Because multiplicity impacts:**

| Aspect | Design Impact |
|--------|---------------|
| **Data modeling** | Schema design |
| **Aggregation logic** | How to compute totals |
| **Historical tracking** | Preserve vs overwrite |

**Ignoring it leads to:**
- ❌ Overwriting data
- ❌ Losing history
- ❌ Incorrect portfolio calculations

> **Interviewers see multiplicity as a design maturity indicator.**

---

### How do you surface this in discussion?

**You ask:**

1. **Can users maintain multiple collections?**
2. **Are past purchases preserved?**
3. **Do lots need individual identity?**

> **This shows you care about long-term correctness, not just current state.**

---

### Non-obvious implications

| Concept | Implication |
|---------|-------------|
| **Multiple lots** | Cost basis calculation |
| **Multiple watchlists** | Personalization |
| **Repeated trades** | Historical analytics |

**Why this matters:**
- LLD is not just about relations
- It's about preserving intent and history

**Interview-level insight:**

> **"We should never lose past state in financial systems."**

---

## Additional Expectations (Worth Adding)

### Reliability & Failure Handling

Even though not mentioned explicitly, **interviewers expect you to ask:**

1. **What happens if a trade request is duplicated?**
2. **What happens if price changes mid-execution?**
3. **What if notification delivery fails?**

**Why this is powerful:**
- ✅ Shows defensive thinking
- ✅ Shows production readiness

---

### Security & Trust (Implicit Expectation)

Without going into auth details, simply acknowledging:

- Secure transactions
- Data isolation
- Auditability

**…is enough to score points.**

> **You don't design security here—you acknowledge responsibility.**

---

## Meta Insight (Very Important)

This entire section proves one thing:

> **LLD interviews are as much about asking the right questions as designing the solution.**

### A strong candidate:

- ✅ Asks before building
- ✅ Narrows scope
- ✅ Avoids assumptions

### A weak candidate:

- ❌ Jumps straight to classes
- ❌ Designs features no one asked for

---

## How interviewers evaluate you here

### They are listening for:

- Domain awareness
- Boundary thinking
- Controlled curiosity

### They are NOT scoring you on:

- Perfect answers
- Exhaustive feature lists

---

## Extended Interview Perspective

### What interviewers are really evaluating here

Beyond the visible questions (discoverability, visibility, order type, multiplicity), the interviewer is silently checking:

1. **Can you control ambiguity?**
2. **Do you understand system stress points?**
3. **Can you separate core vs supporting features?**

> **This is where many candidates fail—not at design, but at framing.**

---

## How a strong candidate summarizes this section

### In an interview, a good closing line would sound like:

> **"Before designing, I want to clarify discoverability, visibility, order behavior, and multiplicity so the system boundaries are clear and the design remains extensible."**

> **That statement alone signals LLD maturity.**

---

## Question Framework Summary

### Discoverability Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| Keyword vs symbol search? | Query complexity | Index strategy |
| Ranked vs exact-match? | Algorithm choice | Ranking logic |
| Real-time vs cached? | Performance | Cache layer |
| Autocomplete needed? | User experience | Typeahead service |
| Static vs dynamic universe? | Update frequency | Refresh strategy |

---

### Visibility Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| Real-time positions? | Consistency model | Read replicas needed |
| Role-based views? | Access control | Permission system |
| Strong vs eventual consistency? | Performance tradeoff | Data architecture |
| Data freshness SLA? | System requirements | Update frequency |

---

### Order Type Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| Which order types? | Feature scope | Class hierarchy |
| Extensibility needed? | Future growth | Design flexibility |
| Shared behavior? | Code reuse | Base class design |
| Partial fills supported? | Execution complexity | State management |
| Order modification allowed? | Lifecycle complexity | Versioning |

---

### Multiplicity Questions

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| Multiple watchlists? | Data relationships | Many-to-many design |
| Preserve history? | Temporal data | Historical tables |
| Individual lot tracking? | Cost basis calculation | Granular records |
| Portfolio aggregation? | Performance | Pre-computation |

---

## Interview Flow Template

### Step 1: Opening (30 seconds)

```
"A stock brokerage system is complex with multiple domains. 
Before designing, I'd like to clarify a few critical areas 
that will shape the architecture."
```

---

### Step 2: Structured Questions (2-3 minutes)

```
Discoverability:
- "How do users find stocks? Symbol-based or keyword search?"
- "Is search cached or real-time?"

Visibility:
- "Are portfolio values real-time or can they be delayed?"
- "What consistency guarantees do we need for different data types?"

Order Types:
- "Which order types are in scope—market, limit, stop-loss?"
- "Should the design support adding new order types easily?"

Multiplicity:
- "Can users maintain multiple watchlists?"
- "Do we preserve historical trade data or just current positions?"

Reliability:
- "How do we handle duplicate trade requests?"
- "What happens if price changes during order placement?"
```

---

### Step 3: Scope Confirmation (30 seconds)

```
"Based on your answers, I'll focus on [confirmed scope]. 
I'll ensure the design handles [specific requirements] 
while maintaining extensibility for [future needs]. 
Does this align with expectations?"
```

---

## Red Flags to Avoid

| What NOT to Do | Why It's Bad |
|----------------|--------------|
| **Assume all data is real-time** | Over-engineering |
| **Treat orders as simple records** | Missing behavior |
| **Ignore historical data** | Losing audit trail |
| **Skip failure scenarios** | Production blindness |
| **Design search last** | Wrong priorities |

---

## Green Flags That Impress

| What TO Say | Why It Impresses |
|-------------|------------------|
| **"Search is likely highest QPS"** | Shows load awareness |
| **"Different data needs different consistency"** | Shows distributed systems knowledge |
| **"Order types are behaviors, not just data"** | Shows OOP maturity |
| **"Financial systems must preserve history"** | Shows domain knowledge |
| **"We need idempotency for trades"** | Shows production experience |

---

## Final Interview Insight

**This section exists to filter candidates who:**

- ❌ Design first → fail
- ✅ Ask first → pass

> **You're on the right track.**

---

## Evaluation Criteria

### What Gets You Points:

| Criterion | What Interviewer Looks For |
|-----------|---------------------------|
| **Scoping** | Can you identify what matters most? |
| **Prioritization** | Do you know which features are core vs nice-to-have? |
| **Awareness** | Do you understand system stress points? |
| **Communication** | Can you ask clear, structured questions? |
| **Defensiveness** | Do you think about failure scenarios? |

---
