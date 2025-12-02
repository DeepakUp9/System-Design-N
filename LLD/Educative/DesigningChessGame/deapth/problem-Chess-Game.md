# LLD Chapter — Getting Ready: The Chess Game

---

## 1. WHAT is the Problem?

We must design a digital chess engine that models an **8×8 board**, follows all official rules, and supports complete turn-based gameplay.

### The system should:

- ✅ Represent all chess pieces with their movement rules
- ✅ Represent the board and game state
- ✅ Validate legal moves
- ✅ Detect check, checkmate, stalemate, and all draw conditions
- ✅ Allow special moves (castling, en passant, pawn promotion)
- ✅ Prevent illegal moves
- ✅ Record the entire move history
- ✅ Manage turns between two players
- ✅ Ensure no undo once a move is made

> **Note:** This is not about AI — just the rule engine of chess.

---

## 2. WHY is this Step Important?

Before designing any system in LLD, you clarify:

| Aspect | Purpose |
|--------|---------|
| **Boundaries** | What is inside the scope |
| **Actors** | Players, game engine |
| **Rules** | Static rules and dynamic rules |
| **Required behaviors** | What the system must do |
| **Edge cases** | Unusual scenarios |
| **Constraints** | Limitations and restrictions |

### If you don't lock these things, you end up designing a weak or incomplete class-model.

**Interviewers expect you to show:**
- Requirements understanding
- Ability to convert rules into models
- Ability to foresee edge cases
- Reasoning behind design decisions

> This step makes sure we know exactly what we're about to design.

---

## 3. HOW do we Simplify and Break It Down?

### 3.1 Identify the Domain Entities

These will become future classes:

- `Board`
- `Square`
- `Piece`
  - `King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn`
- `Move` (notation, type, special rules)
- `Player`
- `Game` / `Match`
- `GameState`
- `MoveHistory`
- `RulesEngine`

### 3.2 Identify Behaviors

Behaviors will become methods:

- Move generation
- Move validation
- Check detection
- Checkmate detection
- Stalemate detection
- Special rules handling
- Turn switching
- Game termination

### 3.3 Identify Constraints

These guide rule design:

| Constraint | Impact |
|------------|--------|
| Pieces cannot move through others (except knight) | Movement validation logic |
| King cannot move into check | Safety validation required |
| Only current player can move | Turn management needed |
| Move cannot expose your own king | Pin detection required |
| No undo allowed | Immutable move history |
| Move history must be stored correctly | Complete record keeping |
| No illegal board states allowed | Validation at every step |

---

## 4. WHAT Questions an Interviewer Expects You to Ask?

Below are the key clarifying questions, with answers:

### Q1. Is the game always between two human players?

**Answer:** Yes.  
No AI, no network play unless interviewer says otherwise.

### Q2. Do we need a GUI?

**Answer:** No.  
We design only the backend logic and rules engine.

### Q3. Do we need to support chess variants (960, blitz, etc.)?

**Answer:** No.  
Only standard classical chess.

### Q4. Do we need full legality checking?

**Answer:** Yes.  
All the following must be enforced:
- Move legality
- King safety
- Special moves
- Checkmate, stalemate
- Draw rules

### Q5. Are special draw rules needed?

**Answer:** Yes.  
Support:
- Stalemate
- Threefold repetition
- 50-move rule
- Insufficient material
- Mutually agreed draw
- Resignation
- Time forfeiture (optional)

### Q6. Should the system store the entire game history?

**Answer:** Yes.  
Full history stored as `Move` objects.

### Q7. Should moves be undoable?

**Answer:** No.  
Moves are permanent—this affects design of `Board` and `RulesEngine`.

---

## 5. WHAT Additional Points Should Be Added (For Interview Excellence)?

### 5.1 Define Functional Requirements

- ✅ Validate legal moves
- ✅ Enforce turn-taking
- ✅ Detect check and checkmate
- ✅ Detect draw conditions
- ✅ Support special moves
- ✅ Record moves
- ✅ Expose the game state

### 5.2 Define Non-Functional Requirements

| Requirement | Specification |
|-------------|---------------|
| **Accuracy of rules** | 100% correctness |
| **Performance** | Generate legal moves in < 50ms |
| **Maintainability** | Extendable for variants (chess960 etc.) |
| **Testability** | Each rule should be unit-testable |
| **Deterministic behavior** | No randomness |

### 5.3 Identify Edge Cases

Critical for LLD interviews:

- ⚠️ Moving a pinned piece
- ⚠️ Overlapping castling rules (king in check or passing through check)
- ⚠️ Pawn promotion to different pieces
- ⚠️ En passant only one turn validity
- ⚠️ Detect threefold repetition based on position history
- ⚠️ Insufficient material (e.g., K+N vs K is a draw)
- ⚠️ Illegal board states must never appear

---

## 6. HOW to Think in LLD Mindset (Important for You)

### Design must answer:

1. What are my models?
2. What data do they hold?
3. What behaviors do they expose?
4. How do they interact?

### You'll use principles like:

| Principle | Application |
|-----------|-------------|
| **Encapsulation** | Keep board state protected |
| **Composition** | Piece belongs to square, board owns squares |
| **Polymorphism** | Move behavior per piece type |
| **Single Responsibility** | Rules are in `RulesEngine`, not in pieces |
| **State Management** | `GameState` tracks current situation |

> This step ensures the foundation is rock solid before we jump into class design.

---

## 7. Summary (for Interview Answer)

In this stage, we **clarify the chess problem** and define what exactly we need to build.

### What We're Building:

A turn-based chess engine that:
- Models all rules of classical chess
- Supports special moves
- Validates legality
- Detects check/checkmate/draws
- Keeps a full move history

### What We're NOT Building:

- ❌ No undo
- ❌ No AI
- ❌ No GUI

### This Preparation Step:

Identifies entities, behaviors, constraints, edge cases, and required clarifying questions. This ensures that the final class design will be **accurate, complete, and ready to implement in code**.

---

## Quick Reference: Domain Model Overview

```
Game
├── Board
│   └── Square[64]
│       └── Piece (King, Queen, Rook, Bishop, Knight, Pawn)
├── Players[2]
├── GameState (current turn, check status, game result)
├── MoveHistory
└── RulesEngine (validation, check detection, move generation)
```

---

## Checklist Before Moving to Class Design

- [ ] All entities identified
- [ ] All behaviors listed
- [ ] All constraints documented
- [ ] Edge cases noted
- [ ] Clarifying questions answered
- [ ] Functional requirements clear
- [ ] Non-functional requirements defined
- [ ] Design principles selected

---
