# LLD Chapter — Design Approach & Design Patterns

---

## 1. WHAT is the Design Approach?

The design approach describes **how you plan to structure and build the chess system**.

It shows the interviewer your overall architecture vision before diving into classes or code.

### Your approach is bottom–up, where you:

1. Start with small building blocks (pieces, board squares, players)
2. Build larger structures (board, moves, game)
3. Add rule enforcement (validators, engines)
4. Add UX-like features (turns, interactions)
5. Add scalability and extensions (multiplayer, history, undo/redo)

---

## 2. WHY This Approach?

### Chess is not a CRUD system — it's a state machine + rules engine.

**Bottom-up works best because:**

- Each piece has special movement behavior
- Board state calculations are complex
- Move validation needs board + piece logic
- Checkmate/stalemate depends on move-generation
- Many edge cases must be handled cleanly

### Starting from the smallest entities makes the final system:

- ✅ More modular
- ✅ Easier to test
- ✅ Easier to extend
- ✅ Avoids giant God-classes

> **Interviewer loves when you show reasoning behind your structure.**

---

## 3. HOW the Approach Works (Step-by-Step Explanation)

### Step 1 — Core Entities (Building Blocks)

**Entities:**

| Entity | Purpose |
|--------|---------|
| `ChessPiece` | Abstraction for all pieces |
| `BoardSquare` | Holds piece + coordinate |
| `Player` | Color, name, captured pieces |

**LLD Angle:**  
These represent your most atomic models. Everything else will build on them.

---

### Step 2 — Composite Components (Larger Units)

#### ChessBoard

- Holds 8×8 squares
- Manages piece placement
- Knows current state

#### Move

- Source square
- Destination square
- Move type (normal, capture, castling, en passant, promotion)
- Time-stamp (optional)

#### Game

- Turn management
- Game state (in progress, check, checkmate, draw)
- Rule invocation

---

### Step 3 — Rule Enforcement Modules

#### MoveValidator

- Checks legal moves
- Detects pins
- Detects king safety

#### RulesEngine

- Check, checkmate, stalemate
- Special rules
- End-game detection

---

### Step 4 — Future Scalability

Your design must support:

- Move history
- Undo/redo
- Online multiplayer
- Custom boards or variants
- Time-based rules
- Persistence

> **A bottom-up modular design makes adding these extremely easy.**

---

## 4. WHAT other things to add? (Interview Value)

Interviewer expects you to mention:

- ✅ SOLID principles
- ✅ Clean separation of concerns
- ✅ Extensibility and maintainability
- ✅ Proper use of interfaces and inheritance
- ✅ Avoiding hard-coded logic
- ✅ Reusability of movement rules

**These are signal words that show architectural maturity.**

---

## 5. WHY Design Patterns Matter in Chess LLD

Design patterns tell the interviewer:

| What It Shows | Why It Matters |
|---------------|----------------|
| You understand abstraction | Clean code principles |
| You understand where to put logic | Separation of concerns |
| You know how to avoid duplication | DRY principle |
| You know how to improve flexibility | Extensibility |
| You understand extensibility and maintainability | Production-ready thinking |

> **Just mentioning the correct patterns immediately elevates your design.**

---

## 6. HOW to Discuss Design Patterns for a Chess System

This section is incredibly important for your interview.

### 1. Strategy Pattern ⭐

| Aspect | Details |
|--------|---------|
| **Where used** | Piece movement behaviors |
| **Why** | Each piece has different movement rules |
| **How** | • Each piece implements `getLegalMoves()` differently<br>• Movement logic becomes pluggable<br>• Avoids massive if-else chains<br>• Helps extension (custom variants) |

---

### 2. Factory Pattern

| Aspect | Details |
|--------|---------|
| **Where** | To create pieces in initial board setup |
| **Why** | Board should ask a factory to create pieces instead of manually instantiating |
| **How** | `PieceFactory.create(PieceType, Color, Position)` |

---

### 3. Composite Pattern

| Aspect | Details |
|--------|---------|
| **Where** | ChessBoard is composed of 64 BoardSquares, and each BoardSquare optionally contains a Piece |
| **Why** | A natural "has-a" relationship |

---

### 4. Command Pattern

| Aspect | Details |
|--------|---------|
| **Where** | Move execution undo/redo |
| **Why** | • A move is a command<br>• Execute → move piece<br>• Unexecute → revert move<br>• Even if undo is not required now, mentioning this proves strong LLD understanding |

---

### 5. Observer Pattern

| Aspect | Details |
|--------|---------|
| **Where** | UI or event listeners |
| **Why** | Useful if you later add:<br>• Notifying UI when board updates<br>• Notifying log engine<br>• Notifying timer<br>• Mentioning this shows forward thinking |

---

### 6. State Pattern

| Aspect | Details |
|--------|---------|
| **Where** | Game states: InProgress, Check, Checkmate, Stalemate, Draw, Resigned |
| **Why** | Keeps game state transitions clean |

---

### 7. Flyweight Pattern (Optional)

Some chess engines reuse piece instances for memory optimization.

> **Mentioning this shows deep knowledge, even if not needed.**

---

## 7. Additional "WHAT, WHY, HOW" (For Interview Excellence)

### WHAT are we designing?

A complete chess rules engine with piece logic, board state, rule validation, and game phases.

### WHY bottom-up?

Because chess complexity lives in the smallest objects (pieces, squares), and modeling them cleanly avoids tight coupling and chaos later.

### HOW will the system evolve?

Through modular patterns like Strategy, Factory, Observer, and Command, ensuring each new feature attaches cleanly without changing existing logic.

---

## 8. Summary for Interview Speaking

Here is one perfect paragraph you can speak:

> **"I prefer a bottom-up approach for chess because its complexity is rooted in the smallest components like pieces and squares. Once those core models are solid, I build composite components like Board, Move, and Game. I use design patterns like Strategy for movement logic, Factory for piece creation, Composite for board structure, and Command for move execution. This makes the system modular, extensible, and easy to test while ensuring it can support checkmate detection, move validation, special rules, and future additions like history or online multiplayer."**

---

## Quick Reference: Design Pattern Mapping

```
Strategy Pattern     → Piece movement behaviors
Factory Pattern      → Piece creation
Composite Pattern    → Board structure (Board → Squares → Pieces)
Command Pattern      → Move execution & undo/redo
Observer Pattern     → UI/Event notifications
State Pattern        → Game state transitions
Flyweight Pattern    → Memory optimization (optional)
```

---

## Architecture Flow

```
Bottom Layer (Atomic)
├── ChessPiece (abstract)
│   ├── Pawn
│   ├── Rook
│   ├── Knight
│   ├── Bishop
│   ├── Queen
│   └── King
├── BoardSquare
└── Player

Middle Layer (Composite)
├── ChessBoard (8x8 squares)
├── Move (source, destination, type)
└── Game (turn manager)

Top Layer (Logic)
├── MoveValidator
├── RulesEngine
└── GameController

Extension Layer (Future)
├── MoveHistory
├── UndoRedoManager
├── OnlineMultiplayer
└── PersistenceLayer
```

---

## SOLID Principles Application

| Principle | Application in Chess |
|-----------|---------------------|
| **SRP** | Each piece knows only its movement; RulesEngine handles validation |
| **OCP** | New piece types can be added without modifying existing code |
| **LSP** | All pieces extend ChessPiece and can substitute each other |
| **ISP** | Separate interfaces for Movable, Capturable, Promotable |
| **DIP** | Game depends on abstractions (ChessPiece), not concrete pieces |

---

## Key Interview Signals

When you mention these, you stand out:

| What You Say | What Interviewer Hears |
|--------------|------------------------|
| "Bottom-up approach" | Systematic thinking |
| "Strategy for movement" | Pattern knowledge |
| "Command for undo/redo" | Advanced design skills |
| "Observer for events" | Scalability mindset |
| "Composite for board" | Natural modeling |
| "SOLID principles" | Professional experience |

---