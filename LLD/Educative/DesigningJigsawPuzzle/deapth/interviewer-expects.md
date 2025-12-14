# LLD Chapter: Getting Ready

## Topic: Expectations from the Interviewee (Jigsaw Puzzle)

This section is about **how you think and communicate**, not about writing perfect classes. Interviewers use this part to evaluate design clarity, assumption management, and abstraction skills.

---

## What the interviewer expects here

The interviewer expects you to:

- ✅ Ask the right clarifying questions
- ✅ Separate core requirements from assumptions
- ✅ Identify entities and constraints early
- ✅ Show design intent before design details

> **Even for a simple problem like jigsaw, they want to see disciplined LLD thinking.**

---

## The Puzzle Board

### What (in LLD terms)

**The puzzle board represents:**

- The **container** that holds all puzzle pieces
- The **coordinate system / boundary** for placement
- The **global constraint enforcer** (size, shape, limits)

> **It is a first-class domain object, not just a background.**

---

### Why the interviewer cares

**Board-related clarifications influence:**

| Aspect | Design Impact |
|--------|---------------|
| **How pieces are positioned** | Coordinates vs adjacency |
| **Whether edge and corner pieces exist** | Special case handling |
| **Completion validation logic** | How to check if done |
| **Overall system constraints** | Boundaries and limits |

> **If you skip this, your design becomes vague and error-prone.**

---

### How you should reason and respond

**When you ask:**

#### "Is there a picture or pattern?"

→ You're identifying whether the board has **visual metadata** or is just a **structural container**.

#### "What is the board shape?"

→ You're determining **boundary rules**:
- Rectangular boards imply corners and edges
- Circular boards change adjacency logic

**Interview signal:**

> **You are designing based on constraints, not assumptions.**

---

## The Puzzle Pieces

### What (in LLD terms)

**A puzzle piece is:**

- The **core entity**
- An object with:
  - Shape
  - Edges
  - Matching rules
  - Identity

> **This is where most of the domain logic lives.**

---

### Why this matters

**Clarifying piece properties affects:**

| Property | Design Impact |
|----------|---------------|
| **How pieces are represented** | Edges, connectors |
| **Whether rotation is allowed** | Edge orientation handling |
| **Matching algorithms** | Validation logic complexity |
| **Uniqueness constraints** | Identity and equality |

> **Most poor designs fail because the piece model is unclear.**

---

### How to think about the interviewer's questions

#### "What shape does a piece have? How many sides?"

→ Determines whether edges are **fixed (e.g., 4-sided)** or **variable**.

#### "How do pieces fit together?"

→ Leads to **edge-matching rules** rather than position-based fitting.

#### "Are all pieces unique?"

→ Impacts **identity, equality, and validation logic**.

**Interview signal:**

> **You're thinking in terms of object responsibility, not UI or algorithms.**

---

## Additional points you can proactively mention (high signal)

You can earn extra points by briefly touching on:

| Concept | Why It Matters |
|---------|----------------|
| **Edge types** (flat, concave, convex) | Matching logic |
| **Special pieces** (corner, border, inner) | Categorization |
| **Immutability of pieces once created** | Thread safety, correctness |
| **State of a piece** (unplaced, placed, locked) | State management |

> **Do this only conceptually, without jumping into code.**

---

## How this helps your LLD design later

**These clarifications naturally lead to:**

- ✅ Clean class boundaries
- ✅ Fewer conditionals
- ✅ Better extensibility
- ✅ Easier validation of "puzzle completed"

> **In interviews, this shows you design for correctness first.**

---

## LLD interview takeaway

**This section is about earning trust.**

### A strong candidate:

- ✅ Asks precise questions
- ✅ Explains why the question matters
- ✅ Uses answers to constrain the design
- ✅ Avoids overengineering

---

## Detailed Question Framework

### Board-Related Questions

| Question | Why Ask It | Design Impact |
|----------|------------|---------------|
| **"What are the board dimensions?"** | Defines size constraints | Fixed array size or dynamic |
| **"Is the board rectangular?"** | Shape determines adjacency | Corner/edge logic |
| **"Does the board have a target image?"** | Visual validation needed | Image matching logic |
| **"Can the board be pre-populated?"** | Initial state management | Starting configuration |

---

### Piece-Related Questions

| Question | Why Ask It | Design Impact |
|----------|------------|---------------|
| **"How many sides does each piece have?"** | Edge count per piece | Data structure for edges |
| **"Can pieces be rotated?"** | Orientation handling | Edge rotation logic |
| **"Are all pieces unique?"** | Identity guarantees | No duplicate checking needed |
| **"What defines edge compatibility?"** | Matching rules | Validation algorithm |
| **"Are there special piece types?"** | Categorization | Subclass hierarchy |

---

### Connection-Related Questions

| Question | Why Ask It | Design Impact |
|----------|------------|---------------|
| **"How do edges match?"** | Validation logic | Matching algorithm |
| **"Can pieces overlap?"** | Collision detection | Position validation |
| **"Is there only one correct solution?"** | Uniqueness constraint | Solution verification |
| **"Can placed pieces be moved?"** | Mutability | State transitions |

---

## Edge Types Deep Dive

### Why edge types matter

**Different edge types require different matching rules:**

```
Flat Edge:     ________   (border pieces)
Concave Edge:  \      /   (inward curve)
Convex Edge:   /      \   (outward curve)
```

**Interview-level insight:**

> **"Edge pieces have at least one flat edge. Corner pieces have two adjacent flat edges."**

---

## Special Piece Categories

### Why categorization helps

| Category | Characteristics | Design Benefit |
|----------|----------------|----------------|
| **Corner Pieces** | 2 flat edges (adjacent) | Easy to identify starting points |
| **Edge Pieces** | 1 flat edge | Border assembly strategy |
| **Inner Pieces** | 0 flat edges | Core puzzle logic |

**Interview signal:**

> **"Categorizing pieces simplifies assembly algorithms and validation."**

---

## Piece State Management

### State Transitions

```
┌──────────────┐
│   UNPLACED   │ (Not on board)
└──────┬───────┘
       │ place()
       ▼
┌──────────────┐
│    PLACED    │ (At position, but movable)
└──────┬───────┘
       │ lock() [optional]
       ▼
┌──────────────┐
│    LOCKED    │ (Fixed, cannot be moved)
└──────────────┘
```

**Why this matters:**

> **State management prevents invalid operations and clarifies object lifecycle.**

---

## Immutability Considerations

### What should be immutable?

| Property | Immutability | Rationale |
|----------|--------------|-----------|
| **Piece shape** | Immutable | Physical constraint |
| **Piece edges** | Immutable | Fixed at creation |
| **Piece identity** | Immutable | Unique identifier |
| **Piece position** | Mutable | Changes during assembly |
| **Piece state** | Mutable | Assembly progress |

**Interview insight:**

> **"Immutability of piece properties ensures correctness and thread safety."**

---

## Interview Response Templates

### When discussing the board:

> **"Before modeling the board, I need to understand: Is it rectangular with fixed dimensions? Does it have a target image for validation? This affects whether the board is just a container or also a validation component."**

---

### When discussing pieces:

> **"For pieces, I need to clarify: How many edges does each piece have—typically 4? Can pieces be rotated? Are all pieces unique? This determines the piece data structure and matching logic."**

---

### When discussing connections:

> **"For piece connections, I'd ask: What defines a valid edge match—shape complementarity? Can pieces be placed incorrectly temporarily? This shapes the validation strategy."**

---

## Design Intent Communication

### How to show design thinking

**Instead of saying:**
- ❌ "I'll create a Piece class"

**Say:**
- ✅ "A piece should encapsulate its shape and edges, know which neighbors it can connect to, and maintain its current state"

**Why this is better:**

> **It shows you're thinking about responsibilities, not just structure.**

---

## Constraint-Based Design

### Using answers to constrain design

| Clarification | Design Constraint |
|---------------|-------------------|
| **"Fixed 4-sided pieces"** | Edge array of size 4 |
| **"No rotation allowed"** | No edge orientation tracking |
| **"All pieces unique"** | Simple identity-based equality |
| **"Rectangular board"** | 2D array representation |
| **"One correct solution"** | Deterministic validation |

---

## Red Flags to Avoid

| Mistake | Why It's Bad |
|---------|--------------|
| **Not asking about rotation** | Leads to unclear edge matching |
| **Assuming visual validation needed** | Overcomplicates simple problems |
| **Ignoring edge vs corner pieces** | Misses optimization opportunities |
| **Not clarifying uniqueness** | Affects equality logic |
| **Jumping to algorithms** | Skips domain modeling |

---

## Green Flags That Impress

| What You Say | Why It Impresses |
|--------------|------------------|
| **"Edge pieces are special cases"** | Shows categorization thinking |
| **"Pieces should be immutable"** | Shows design principles |
| **"Matching is about edge compatibility"** | Shows domain understanding |
| **"State transitions should be explicit"** | Shows state management awareness |
| **"Board enforces global constraints"** | Shows responsibility allocation |

---

## Example Clarifying Dialogue

### Candidate:

> "Before designing the pieces, I need to understand their properties. Are all pieces 4-sided? Can they be rotated?"

### Interviewer:

> "Yes, all pieces are 4-sided. No rotation."

### Candidate:

> "Great. That means each piece has exactly 4 edges with fixed orientations. Are there flat edges for border pieces, or are all edges interlocking?"

### Interviewer:

> "Border pieces have flat edges. Corner pieces have two flat edges."

### Candidate:

> "Perfect. So pieces can be categorized by their flat edge count. This helps with both modeling and assembly strategies. One more question: What defines a valid edge match—complementary shapes?"

### Interviewer:

> "Yes, edges must have complementary shapes to connect."

### Candidate:

> "Understood. I'll model pieces with 4 edges, each edge having a type (flat, concave, convex), and validation will check if adjacent edges are complementary."

---

## Why This Chapter Matters

### It demonstrates:

- ✅ Systematic thinking
- ✅ Constraint identification
- ✅ Proper abstraction
- ✅ Communication skills

### It prevents:

- ❌ Unclear requirements
- ❌ Over-complicated design
- ❌ Missing edge cases
- ❌ Weak interview impression

---

## Evaluation Criteria

### What Interviewers Score:

| Criteria | What They Look For |
|----------|-------------------|
| **Question Quality** | Are questions relevant and insightful? |
| **Reasoning** | Do you explain why questions matter? |
| **Constraint Recognition** | Do you identify limits and rules? |
| **Entity Identification** | Do you spot key domain objects? |
| **Design Discipline** | Do you avoid premature implementation? |

---

## How Strong Candidates Close This Section

### Perfect closing statement:

> **"Based on our discussion, I'll design a system where the Board is a 2D grid with fixed dimensions, Pieces are 4-sided immutable objects with typed edges, and placement validation checks edge compatibility. Special cases like corner and edge pieces can be identified by their flat edge count. This constraint-based approach ensures correctness before complexity."**

> **This earns immediate trust.**

---

