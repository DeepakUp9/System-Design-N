# LLD Chapter — Expectations From the Interviewee

This chapter focuses on what interviewers expect you to demonstrate while discussing the chess game system.

An excellent LLD answer isn't only drawing classes — it's about **understanding requirements, constraints, rules, and asking the right clarifying questions**.

---

## 1. WHAT does the interviewer expect?

The interviewer wants to see that you:

- ✅ Understand the domain (chess rules, gameplay, edge cases)
- ✅ Can convert domain rules into software models
- ✅ Think in terms of objects, behaviors, and interactions
- ✅ Consider constraints and special rules
- ✅ Ask clarifying questions
- ✅ Reason about design choices
- ✅ Prioritize correctness and maintainability

> **Note:** This is a logic-heavy LLD problem — more complex than common problems like Parking Lot or ATM.

---

## 2. WHY is this important?

### Chess is NOT a CRUD problem. It's a rules engine.

This test reveals:

| Capability | What It Shows |
|------------|---------------|
| Design state machines | Your ability to model complex states |
| Enforce constraints correctly | Attention to business rules |
| Handle exceptions and edge cases | Real-world thinking |
| Organize logic cleanly | Code quality mindset |
| Think in models + rules + state transitions | Architecture skills |

### A good chess LLD also shows if you can:

- Separate data (`Board`, `Pieces`) from logic (`RulesEngine`)
- Use polymorphism properly
- Avoid massive if-else spaghetti code

---

## 3. HOW interviewers judge your design?

They evaluate:

1. Your requirement gathering
2. Your accuracy about chess rules
3. Your class identification
4. Separation of responsibilities
5. Attention to detail in special rules
6. Handling of weird edge cases
7. Clean structure of interactions

> **Key Insight:** If you get this step right, the rest of the design becomes straightforward.

---

## 4. EXPANDED EXPECTATION AREAS

### A) Chess Pieces (Expectations)

The interviewer expects that you understand the **static structure** of the game:

- Number of pieces
- Types of pieces
- Movement rules
- Strength
- Constraints

#### WHAT?
They expect you to clarify the entities of the game — this forms the **data model**.

#### WHY?
Each piece will become a class or subclass with behavior for legal move generation.

#### HOW?
You must ask clarifying questions like these:

---

#### 1. How many chess pieces are there in the game?

**Answer:**  
32 pieces total — 16 per player.

| Piece Type | Count Per Player |
|------------|------------------|
| Pawns | 8 |
| Rooks | 2 |
| Knights | 2 |
| Bishops | 2 |
| Queen | 1 |
| King | 1 |

**In LLD terms:**  
This forms the initial setup for the `Board` object.

---

#### 2. What are the different pieces, and what are their respective moves?

**Answer:**  
Describe each along with rules:

| Piece | Movement Rules |
|-------|----------------|
| **Pawn** | Forward move, diagonal capture, en passant, promotion |
| **Rook** | Horizontal/vertical |
| **Knight** | L-shape |
| **Bishop** | Diagonal |
| **Queen** | Rook + Bishop |
| **King** | One square, castling |

**LLD Insight:**  
Each movement rule will be implemented either through:
- **Polymorphism** → each class overrides `getLegalMoves()`
- **Strategy pattern** → pieces use movement strategies

---

#### 3. Which piece is the weakest?

**Answer:** Pawn

**Why:** Limited movement, only forward, special capture rules.

**LLD Insight:**  
Pawn movement is the most logic-heavy class → many conditions.

---

#### 4. Which piece is the strongest?

**Answer:** Queen

**Why:** Enemy of 8-direction unlimited movement.

**LLD Insight:**  
Queen movement = reuse rook + bishop logic.

---

### B) Gameplay (Expectations)

This deals with **dynamic rules** — state transitions and win/lose conditions.

You must show command over these rules.

---

#### 1. Which player takes the first turn?

**White always moves first.**

**Why in LLD:**  
`GameState` needs a `currentPlayer` flag.

---

#### 2. What are the rules of the game?

Interviewer expects high-level summary:

- Players alternate turns
- Move must be legal
- King cannot be left in check
- No piece may move through others (except knight)
- Moves are final (no undo unless stated)
- Special rules apply

**LLD Perspective:**
- Rules → `RulesEngine`
- Restrictions → `MoveValidator`

---

#### 3. What is checkmate?

**Definition:**  
King is in check and no legal move can remove check.

**LLD Perspective:**
```
Checkmate = kingInCheck && noLegalMovesAvailable
```

This affects:
- Game termination
- Move generation logic

---

#### 4. How does a stalemate happen?

**Definition:**  
Player has no legal move but the king is not in check.

**LLD Perspective:**
```
Stalemate = !kingInCheck && noLegalMovesAvailable
```

Your engine must detect this case.

---

#### 5. Can a player resign/forfeit?

**Yes.**  
Game ends immediately.

**LLD Perspective:**  
`GameState` must handle:
- `resigned`
- `forfeit`
- `drawAgreed`

Game must stop generating moves after that.

---

## 5. Additional Expectations (Very Important in Interviews)

Below are extra items interviewers LOVE to hear:

### A) Special rules must be considered

- ⭐ Castling
- ⭐ En passant
- ⭐ Pawn promotion
- ⭐ Fifty-move rule
- ⭐ Threefold repetition
- ⭐ Insufficient material
- ⭐ Time over (optional)

**Showing these tells the interviewer:**  
> "I understand real-world chess and I'm designing a production-grade engine."

---

### B) Avoid Illegal States

The board must **never** enter an illegal configuration.

The system must block moves that:

- ❌ Expose own king
- ❌ Move through other pieces
- ❌ Place king on an attacked square
- ❌ Move opponent's piece
- ❌ Move wrong player's turn

---

### C) Clear Class Boundaries (Interview Key Point)

Interviewer expects:

| Class | Responsibility |
|-------|----------------|
| `Board` | Stores state |
| `Piece` | Knows its movement logic |
| `RulesEngine` | Validates if a move is legal |
| `Game` | Controls turn flow |
| `MoveHistory` | Records all moves |

> **This separation shows solid LLD skills.**

---

## 6. WHAT / WHY / HOW Summary

Here is the short version you can speak during the interview:

### WHAT is expected?

A detailed understanding of chess rules, piece behavior, game flow, and constraints.

### WHY is it expected?

Because chess is a **rules engine, not a CRUD problem**; proper modeling of rules and states is essential.

### HOW do you approach it?

By asking clarifying questions about:
- Pieces
- Movement
- Gameplay
- Draws
- Special moves
- Game termination

This helps define data models, behaviors, and system boundaries before writing class diagrams.

---

## Interview Response Framework

### When asked "Design a Chess Game", follow this flow:

```
1. Clarify Requirements
   ├── Number and types of pieces
   ├── Movement rules per piece
   ├── Game termination conditions
   ├── Special moves support
   └── Draw conditions

2. Identify Entities
   ├── Board, Square, Piece
   ├── Player, Game, GameState
   └── Move, MoveHistory

3. Identify Behaviors
   ├── Move generation
   ├── Move validation
   ├── Check/Checkmate detection
   └── Turn management

4. Apply Design Principles
   ├── Polymorphism for piece movement
   ├── Strategy for move generation
   ├── State pattern for game flow
   └── Validator for rule enforcement

5. Handle Edge Cases
   ├── Special moves (castling, en passant)
   ├── Draw conditions
   ├── Illegal move prevention
   └── Game state consistency
```

---

## Key Signals Interviewers Look For

| Signal | What It Means |
|--------|---------------|
| Asks about special moves | Deep domain knowledge |
| Mentions draw conditions | Completeness of solution |
| Separates rules from data | Clean architecture thinking |
| Discusses illegal states | Defensive programming mindset |
| Considers edge cases early | Production-ready thinking |

---
