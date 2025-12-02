# LLD Chapter — Requirements for the Chess Game

Understanding requirements is the **backbone of LLD**.

Before designing classes, patterns, or workflows, you must lock the scope.

**Interviewers judge you heavily on how clearly you gather and articulate requirements.**

---

## 1. WHAT are Requirements?

These are the **rules, behaviors, and constraints** the system must support.

In LLD, requirements tell you:

- What the system must do
- What the system must not do
- What external rules must be enforced
- What states and transitions exist
- Which edge cases must be handled

> **Without proper requirements, class design becomes guesswork.**

---

## 2. WHY Requirement Gathering Matters in LLD?

Because chess is:

- A turn-based state machine
- A rules-enforcement engine
- A complex interactive system
- With special edge cases
- And multiple end-game conditions

### If you skip or misunderstand even one rule → Your design will break later.

**Interviewers use this section to check:**

- ✔ Completeness
- ✔ Accuracy
- ✔ Ability to identify tricky rules
- ✔ Understanding of special cases
- ✔ Awareness of boundaries

---

## 3. HOW to Use Requirements in LLD?

A clean approach for interviews:

1. List all **functional requirements** (what the system does)
2. List all **constraints** (rules that must never be violated)
3. List all **operational requirements** (how the system behaves)
4. Translate these into system components later

> **This chapter focuses only on identification and understanding.**  
> Actual modelling comes in future steps.

---

## 4. Requirement Collection (Numbered R1–R9)

Let's break them down with explanation.

### R1 — Two Players (Human or Machine), Online, Real-Time Gameplay

| Aspect | Details |
|--------|---------|
| **What** | The system must support two players playing remotely |
| **Why** | This influences architecture:<br>• Networking<br>• Sync of board states<br>• Turn control<br>• Event-driven updates |
| **How** | Will require:<br>• Player session management<br>• Move broadcasting<br>• Real-time state synchronization |

> Even if the interviewer doesn't require networking implementation, just recognizing this requirement shows maturity.

---

### R2 — Enforce Official Chess Rules

| Aspect | Details |
|--------|---------|
| **What** | System must follow FIDE rules exactly |
| **Why** | Movement logic, board rules, special moves must be accurate |
| **How** | Requires:<br>• Central `RulesEngine`<br>• `MoveValidator`<br>• Piece-specific logic |

---

### R3 — Random Player Color Assignment

| Aspect | Details |
|--------|---------|
| **What** | Players get white or black randomly |
| **Why** | Removes bias and simplifies fairness |
| **How** | `Game.setup()` assigns colors randomly |

---

### R4 — Starting Position Setup

| Aspect | Details |
|--------|---------|
| **What** | Standard 16 pieces per player, arranged in official formation |
| **Why** | This is the initial state for every game |
| **How** | Board initialization uses a `PieceFactory` |

---

### R5 — White Moves First

| Aspect | Details |
|--------|---------|
| **What** | White always begins |
| **Why** | Game turn engine must adhere strictly |
| **How** | `Game.currentPlayer = WHITE` at start |

---

### R6 — No Undo / Retract Move

| Aspect | Details |
|--------|---------|
| **What** | Once played, moves cannot be undone |
| **Why** | Prevents state rollback complexity |
| **How** | Disable undo features in UI and backend |

---

### R8 — Official Game End Conditions

System must detect:

- ✔ **Checkmate**
- ✔ **Stalemate**
- ✔ **Draw Types**
  - Mutual agreement
  - 3-fold repetition
  - 50-move rule
  - Insufficient material
- ✔ **Resignation**
- ✔ **Forfeiture**

#### Why:
`GameState` transitions rely on these.

#### How:
Add modules:
- `CheckmateDetector`
- `DrawDetector`
- `RepetitionTracker`
- `MaterialEvaluator`

---

### R9 — Support Special Moves

- **Castling**
- **En Passant**
- **Pawn Promotion**

#### Why:
Special-case logic requires careful modeling.

#### How:
Piece classes + `RulesEngine` must handle special behavior.

---

## 5. Detailed Rules Breakdown

Your given tables are part of system requirement analysis.

Below is a clean structured representation with LLD reasoning.

### A. Rules for Pieces

#### King

| Rule | Details |
|------|---------|
| **Movement** | Moves 1 square any direction |
| **Constraint** | No self-check allowed |
| **Special** | Castling permitted with constraints |

**LLD Insight:**  
King class will have highest rule-check complexity.

---

#### Queen

| Rule | Details |
|------|---------|
| **Movement** | Moves unlimited in 8 directions |
| **Constraint** | Cannot jump pieces |

**LLD Insight:**  
Can reuse Rook + Bishop movement strategies.

---

#### Rook

| Rule | Details |
|------|---------|
| **Movement** | Unlimited horizontal/vertical |
| **Constraint** | Cannot jump |
| **Special** | Participates in castling |

---

#### Bishop

| Rule | Details |
|------|---------|
| **Movement** | Unlimited diagonal |
| **Constraint** | Cannot jump |

---

#### Knight

| Rule | Details |
|------|---------|
| **Movement** | L-shaped |
| **Special** | Can jump over pieces |

**LLD Insight:**  
The only piece ignoring block checks.

---

#### Pawn

| Rule | Details |
|------|---------|
| **Movement** | 1-step forward |
| **First Move** | 2-step on first move |
| **Capture** | Diagonal capture |
| **Special 1** | En passant |
| **Special 2** | Promotion |

**LLD Insight:**  
The most complex movement rules → must be isolated cleanly.

---

### B. Rules for Situations (State-Based Requirements)

Each situation affects game state transitions.

#### Check

| Aspect | Details |
|--------|---------|
| **Definition** | King under attack |
| **Requirement** | Player must remove check |
| **LLD** | `RulesEngine.isKingInCheck()` |

---

#### Checkmate

| Aspect | Details |
|--------|---------|
| **Definition** | King in check + no escape |
| **LLD** | `check && noLegalMoves → GameState = CHECKMATE` |

---

#### Stalemate

| Aspect | Details |
|--------|---------|
| **Definition** | No legal moves + not in check |
| **LLD** | Edge-case heavy → must test thoroughly |

---

#### Draw

**Types:**

1. Stalemate
2. 3-fold repetition
3. 50-move rule
4. Insufficient material
5. Mutual agreement

**LLD:**  
`DrawDetector` module is required.

---

#### Forfeiture

| Aspect | Details |
|--------|---------|
| **Definition** | Player not present or abandoned |
| **LLD** | Game ends immediately |

---

#### Resignation

| Aspect | Details |
|--------|---------|
| **Definition** | Voluntary loss |
| **LLD** | Set `GameState=RESIGNED` |

---

#### Castling Rules

Must ensure:

- ✅ King and rook unmoved
- ✅ No pieces between
- ✅ King not in check
- ✅ King does not pass through check
- ✅ King does not end in check

**LLD:**  
`CastlingValidator` module.

---

#### En Passant

Requires:

- Opponent pawn moves 2 steps
- Capture must happen next move
- Only diagonal immediate square

**LLD:**  
Track last move.

---

#### Pawn Promotion

- Pawn reaches last rank
- Player chooses piece

**LLD:**  
`PromotionManager`.

---

## 6. Additional Requirements You Should Add (Interview Boost)

Interviewers expect MORE than what's stated.

### Non-Functional Requirements

#### N1 — Performance

Game engine must generate legal moves within milliseconds.

#### N2 — Scalability

Support for:
- Move history
- Multiple concurrent games (if online)
- Spectators / Observers (optional)

#### N3 — Security

Prevent illegal move injection (for multiplayer).

#### N4 — Reliability

State must never become inconsistent.

---

### Constraints

| ID | Constraint |
|----|------------|
| **C1** | No illegal board positions allowed |
| **C2** | Turn must alternate strictly |
| **C3** | Moves must be atomic (no partial execution) |

---

### Operational Requirements

| ID | Requirement |
|----|-------------|
| **O1** | Board should always reflect current state |
| **O2** | Move should be validated before committed |
| **O3** | System must notify end-game |

---

## 7. WHAT / WHY / HOW Summary (Interview Answer)

### WHAT are the requirements?

All rules, behaviors, and constraints needed to run a complete online chess game according to official standards.

### WHY are they needed?

To define scope, create correct models, and ensure the engine enforces accurate gameplay without inconsistencies.

### HOW will they be used in LLD?

They map directly into:
- Entities
- Behaviors
- Validators
- State transitions
- Interaction workflows

> **These requirements form the foundation for diagrams and class design.**

---

## Requirements Classification Table

| Type | Examples |
|------|----------|
| **Functional** | Move validation, Check detection, Castling, En passant |
| **Non-Functional** | Performance (<50ms), Scalability, Security |
| **Constraints** | No illegal states, Turn alternation, Atomic moves |
| **Operational** | State consistency, Pre-commit validation, End-game notification |

---

## Requirements → Design Mapping

```
Requirements Layer
├── R1: Online gameplay → SessionManager, NetworkSync
├── R2: Official rules → RulesEngine, MoveValidator
├── R3: Random colors → Game.setup()
├── R4: Starting position → PieceFactory, Board.initialize()
├── R5: White first → Game.currentPlayer
├── R6: No undo → Immutable MoveHistory
├── R8: End conditions → CheckmateDetector, DrawDetector
└── R9: Special moves → CastlingValidator, EnPassantHandler, PromotionManager

Rules Layer
├── Piece rules → King, Queen, Rook, Bishop, Knight, Pawn classes
├── Situation rules → Check, Checkmate, Stalemate, Draw detectors
└── Special rules → Castling, EnPassant, Promotion handlers

Non-Functional Layer
├── Performance → Optimized move generation
├── Scalability → Multiple game support
├── Security → Move validation
└── Reliability → State consistency checks
```

---

## Interview Checklist

Before moving to class design, ensure you've covered:

- [ ] All piece movement rules
- [ ] All special moves (castling, en passant, promotion)
- [ ] All end-game conditions (checkmate, stalemate, draws)
- [ ] Turn management
- [ ] State consistency constraints
- [ ] Performance requirements
- [ ] Edge cases identification

---
