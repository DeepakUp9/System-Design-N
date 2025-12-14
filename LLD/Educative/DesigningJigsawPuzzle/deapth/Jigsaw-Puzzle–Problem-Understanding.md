# LLD Chapter: Getting Ready

## Topic: Jigsaw Puzzle – Problem Understanding

**This topic is not about design yet.** In an LLD interview, this is used to check how you understand an ambiguous real-world problem before jumping into classes and code.

---

## What is the jigsaw puzzle problem (in interview terms)?

A jigsaw puzzle represents a **finite, constrained system** where:

- The system has a **fixed number of components** (pieces)
- Each component has **unique properties** (shape, edges)
- Components can connect **only in specific ways**
- The system has **exactly one valid final state** (completed picture)

> **From an LLD perspective, this is a perfect example of a well-bounded object-oriented problem.**

---

## Why interviewers start with this problem

**Interviewers are not testing puzzle-solving skills here.**

They want to see:

| Capability | What They Check |
|------------|-----------------|
| **1. Do you ask clarifying questions before designing?** | Shows thoughtfulness |
| **2. Do you identify constraints early?** | Shows scoping ability |
| **3. Can you convert a real-world object into design abstractions?** | Shows modeling skills |
| **4. Do you avoid overengineering a simple, finite system?** | Shows judgment |

---

### Many candidates fail by:

- ❌ Jumping straight into classes
- ❌ Assuming unnecessary features (AI solver, multiple solutions, rotations, etc.)
- ❌ Missing fixed constraints

---

## Key simplifications you should explicitly clarify

**Before design, an interviewer expects you to mentally lock down these assumptions:**

| Assumption | Why It Matters |
|------------|----------------|
| **Fixed number of pieces** | No dynamic addition/removal |
| **Only one correct solution exists** | Simplifies validation logic |
| **Each piece fits only in specific positions** | Defines matching rules |
| **Pieces are immutable** | Shape doesn't change |
| **Goal is assembly, not generation or solving strategy** | Clarifies scope |

> **These clarifications reduce the problem space, which is exactly what good LLD engineers do.**

---

## How this translates into LLD thinking

This problem helps you practice:

### Entity identification

- `Piece`
- `Edge`
- `Puzzle`
- `Board`

### Relationship modeling

- Which pieces can connect?
- How edges match?

### Constraint-driven design

- Only one valid arrangement
- Fixed dimensions

### State modeling

```
Unassembled → Partially assembled → Completed
```

> **In interviews, explicitly calling out constraints like these signals design maturity.**

---

## Typical interviewer follow-up questions (you should be ready for)

**Questions you should acknowledge:**

1. **How do you represent a puzzle piece?**
2. **How do you ensure only valid connections are allowed?**
3. **How do you check if the puzzle is complete?**
4. **Can a piece be rotated?**
5. **Are corner and edge pieces special?**

> **You don't answer these yet — you acknowledge them as design considerations.**

---

## LLD interview takeaway

**This section is about problem framing, not implementation.**

### A strong candidate shows:

- ✅ Controlled assumptions
- ✅ Clear boundaries
- ✅ Focus on constraints
- ✅ Readiness to design without rushing

---

## Problem Characteristics

### Finite System

| Characteristic | Implication |
|----------------|-------------|
| **Fixed piece count** | No dynamic scaling needed |
| **Known dimensions** | Pre-determined board size |
| **Single solution** | Validation is deterministic |
| **Immutable pieces** | No state changes in pieces themselves |

---

### Constrained Interactions

| Constraint | Design Impact |
|------------|---------------|
| **Pieces only fit specific neighbors** | Edge matching rules needed |
| **Each position has one correct piece** | Unique placement validation |
| **No overlapping allowed** | Position occupancy tracking |
| **Border pieces are distinct** | Special categorization |

---

## Interview Question Framework

### Clarifying Questions to Ask

#### About the Puzzle:

- **What are the dimensions?** (e.g., 10x10, or variable?)
- **Are all pieces unique?** (Can we assume no identical pieces?)
- **Is rotation allowed?** (Do pieces have fixed orientation?)
- **What defines a valid connection?** (Shape matching? Color? Both?)

#### About the Scope:

- **Are we building an assembler or a solver?** (Manual placement vs AI)
- **Do we need to validate correctness?** (Check if placement is right)
- **Is there an undo feature?** (Can pieces be removed?)
- **Do we track assembly progress?** (Partial completion state)

#### About Edge Cases:

- **What happens with incorrect placements?** (Reject? Allow temporarily?)
- **Can multiple players work simultaneously?** (Concurrency)
- **Is there a time limit?** (Performance constraints)

---

## Domain Constraints

### Physical Constraints

```
1. Fixed Inventory
   - N pieces exist
   - No pieces added/removed during assembly

2. Spatial Constraints
   - Each piece occupies exactly one position
   - Positions are fixed on a grid
   - No two pieces occupy same position

3. Connection Rules
   - Edges must match geometrically
   - Usually 4 neighbors max (top, right, bottom, left)
```

---

### Logical Constraints

```
1. Uniqueness
   - Each piece has unique shape/pattern
   - Only one valid arrangement exists

2. Completeness
   - Puzzle is complete when all positions filled
   - All edge connections must be valid

3. Correctness
   - Valid placement means edges align
   - Final state matches target image
```

---

## Problem Scope Boundaries

### What IS in Scope

| Feature | Description |
|---------|-------------|
| **Piece representation** | Model physical properties |
| **Connection validation** | Check if pieces fit |
| **Board/grid management** | Track piece positions |
| **Completion detection** | Know when puzzle is done |
| **Basic placement operations** | Put piece at position |

---

### What is NOT in Scope (Unless Specified)

| Feature | Why Excluded |
|---------|--------------|
| **AI solver** | Focuses on modeling, not algorithms |
| **Image generation** | Focuses on assembly, not creation |
| **Multiple solutions** | Problem states one solution |
| **Piece rotation** | Simplifies edge matching |
| **Collaborative editing** | Adds concurrency complexity |
| **Undo/redo history** | Adds state management complexity |

---

## Interview Response Template

### Opening Statement

> **"A jigsaw puzzle is a finite, constrained system with fixed pieces that fit together in exactly one valid configuration. Before designing, I'd like to clarify a few things: Are the dimensions fixed? Can pieces be rotated? Are we building a manual assembly system or an AI solver? Should we validate correct placement or allow temporary incorrect positions?"**

---

### After Scope Clarification

> **"Understood. I'll design a system where pieces have unique identities and edge properties, the board tracks piece positions, and placement validation ensures edges match correctly. The system will detect completion when all positions are filled with valid connections. The design will be simple and focused on the assembly mechanics without overengineering."**

---

## Key Entities Preview (Without Designing Yet)

### Just acknowledging what exists

| Entity | Purpose |
|--------|---------|
| **Piece** | Individual puzzle component |
| **Edge** | Side of a piece that connects |
| **Position** | Location on the board/grid |
| **Board** | Container holding all positions |
| **Puzzle** | Complete system orchestrator |

> **You're not designing these yet—you're acknowledging they exist.**

---

## Constraint-Driven Thinking

### Why constraints matter in LLD

| Constraint | Design Benefit |
|------------|----------------|
| **Fixed piece count** | No need for dynamic collections |
| **One solution** | Simplifies validation |
| **No rotation** | Reduces edge matching complexity |
| **Immutable pieces** | Thread-safe by default |
| **Grid-based** | Natural 2D array representation |

---

## State Transitions

### Puzzle Assembly States

```
┌──────────────┐
│ INITIALIZED  │ (All pieces unplaced)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│ IN_PROGRESS  │ (Some pieces placed)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  COMPLETED   │ (All pieces correctly placed)
└──────────────┘
```

---

### Piece States

```
┌──────────────┐
│  UNPLACED    │ (Not on board)
└──────┬───────┘
       │
       ▼
┌──────────────┐
│   PLACED     │ (At a position on board)
└──────────────┘
```

---

## Red Flags to Avoid

| Mistake | Why It's Bad |
|---------|--------------|
| **Assuming rotation is needed** | Adds unnecessary complexity |
| **Designing an AI solver** | Out of scope without clarification |
| **Allowing multiple solutions** | Contradicts problem nature |
| **Over-abstracting edges** | Premature generalization |
| **Ignoring border pieces** | Missing special cases |

---

## Green Flags That Impress

| What You Say | Why It Impresses |
|--------------|------------------|
| **"Puzzle has fixed constraints"** | Shows scoping awareness |
| **"Each piece fits exactly one position"** | Shows understanding of uniqueness |
| **"Border pieces are special cases"** | Shows edge case thinking |
| **"Validation is deterministic"** | Shows algorithmic thinking |
| **"System is finite and bounded"** | Shows complexity awareness |

---

## Example Clarifying Dialogue

### Candidate:

> "Before I start designing, let me clarify the scope. Is this a standard jigsaw puzzle with fixed dimensions, say N×M pieces? Can pieces be rotated, or do they have fixed orientations?"

### Interviewer:

> "Fixed dimensions, no rotation."

### Candidate:

> "Great. Are we building a system where users manually place pieces, or should the system automatically solve the puzzle?"

### Interviewer:

> "Manual placement."

### Candidate:

> "Understood. Should the system validate that placed pieces are correct, or can users place pieces anywhere temporarily?"

### Interviewer:

> "Validate correctness at placement time."

### Candidate:

> "Perfect. I'll design a system with Piece entities that have edge properties, a Board to track positions, and validation logic to ensure edges match when pieces are placed."

---

## Why This Chapter Matters

### It establishes:

- ✅ Problem boundaries
- ✅ Design scope
- ✅ Constraint awareness
- ✅ Proper interview communication

### It prevents:

- ❌ Overengineering
- ❌ Misunderstanding requirements
- ❌ Wasted time on wrong features
- ❌ Poor interview impression

---

## What Interviewers Evaluate Here

| Criteria | What They Look For |
|----------|-------------------|
| **Problem Understanding** | Do you grasp the core problem? |
| **Clarification Skills** | Do you ask good questions? |
| **Scoping Ability** | Can you identify what's in/out of scope? |
| **Constraint Recognition** | Do you see the natural limits? |
| **Design Readiness** | Are you prepared to design thoughtfully? |

---

