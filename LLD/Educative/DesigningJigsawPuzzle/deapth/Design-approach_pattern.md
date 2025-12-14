# LLD Chapter: Getting Ready

## Topic: Design Approach (Jigsaw Puzzle)

This section evaluates **how you structure a design**, not whether you know many classes. The interviewer wants to see a methodical approach that scales from simple objects to the full system.

---

## What: Bottom-Up Design Approach

**Bottom-up design means:**

- Start with the **smallest, most stable units**
- Build larger abstractions by **composing these units**
- Reach the full system **only after the core building blocks are solid**

> **In this problem, the smallest meaningful unit is not the board — it's the edge of a puzzle piece.**

---

## Why Bottom-Up is the right choice here

In an interview, choosing bottom-up shows that:

| Quality | Demonstration |
|---------|---------------|
| **You understand composition over inheritance** | Building up, not breaking down |
| **You design from constraints upward** | Starting with what's fixed |
| **You reduce rework by stabilizing primitives early** | Foundation first |

**A top-down approach would force you to guess details about pieces and edges, which leads to redesign later.**

**Interview signal:**

> **You are minimizing uncertainty early.**

---

## How to explain the approach (verbally, not stepwise)

**You should explain it conceptually like this:**

- A **puzzle edge** defines how two pieces connect
- A **piece** is nothing more than a collection of edges plus identity
- The **board** is an aggregation of pieces with placement rules

> **This shows clear ownership of responsibility at each level.**

---

## Why "edge → piece → board" matters in LLD

| Level | Responsibility |
|-------|----------------|
| **Edges** | Encapsulate matching rules |
| **Pieces** | Encapsulate identity and rotation |
| **Board** | Encapsulate placement, boundaries, and completion logic |

> **Each level depends only on the level below it, creating low coupling and high cohesion.**

---

## Design Patterns: What the interviewer expects you to mention

**You're not expected to force patterns, but recognizing them correctly scores points.**

---

### 1. Composite Pattern

#### What
The puzzle board is composed of pieces; pieces are composed of edges.

#### Why
It models a natural part-whole hierarchy.

#### How to say it in an interview

> **"The board treats individual pieces uniformly as part of a larger structure."**

```java
interface PuzzleComponent {
    boolean isComplete();
}

class Edge implements PuzzleComponent {
    private EdgeType type; // FLAT, IN, OUT
    
    public boolean isComplete() {
        return true; // Edge is always complete
    }
}

class Piece implements PuzzleComponent {
    private List<Edge> edges;
    private boolean placed;
    
    public boolean isComplete() {
        return placed && allEdgesConnected();
    }
}

class Board implements PuzzleComponent {
    private List<Piece> pieces;
    
    public boolean isComplete() {
        return pieces.stream().allMatch(Piece::isComplete);
    }
}
```

---

### 2. Strategy Pattern (Optional but strong signal)

#### What
Different edge-matching rules or validation strategies.

#### Why
Allows flexibility without modifying core entities.

#### How
Matching logic is separated from the piece itself.

```java
interface EdgeMatchingStrategy {
    boolean matches(Edge edge1, Edge edge2);
}

class ShapeMatchingStrategy implements EdgeMatchingStrategy {
    public boolean matches(Edge edge1, Edge edge2) {
        // IN matches OUT, FLAT matches FLAT
        return edge1.getType().isCompatibleWith(edge2.getType());
    }
}

class PatternMatchingStrategy implements EdgeMatchingStrategy {
    public boolean matches(Edge edge1, Edge edge2) {
        // Also check visual pattern alignment
        return shapeMatches(edge1, edge2) && 
               patternAligns(edge1, edge2);
    }
}

class PuzzleValidator {
    private EdgeMatchingStrategy matchingStrategy;
    
    public boolean canConnect(Piece piece1, Piece piece2) {
        return matchingStrategy.matches(
            piece1.getConnectingEdge(), 
            piece2.getConnectingEdge()
        );
    }
}
```

---

### 3. Factory Pattern

#### What
Creation of puzzle pieces and edges.

#### Why
Encapsulates complex construction logic and enforces validity.

#### How
Prevents invalid pieces from being created.

```java
class PieceFactory {
    public static Piece createCornerPiece(EdgeType top, EdgeType right) {
        validateCornerEdges(top, right);
        
        List<Edge> edges = Arrays.asList(
            new Edge(EdgeType.FLAT),  // Top
            new Edge(right),          // Right
            new Edge(EdgeType.FLAT),  // Bottom
            new Edge(top)             // Left
        );
        
        return new Piece(edges, PieceCategory.CORNER);
    }
    
    public static Piece createBorderPiece(EdgeType[] edges) {
        validateBorderEdges(edges);
        // Ensure exactly one flat edge
        return new Piece(edges, PieceCategory.BORDER);
    }
    
    public static Piece createInteriorPiece(EdgeType[] edges) {
        validateInteriorEdges(edges);
        // Ensure no flat edges
        return new Piece(edges, PieceCategory.INTERIOR);
    }
    
    private static void validateCornerEdges(EdgeType top, EdgeType right) {
        if (top == EdgeType.FLAT || right == EdgeType.FLAT) {
            throw new IllegalArgumentException(
                "Corner pieces must have flat edges only on top and left"
            );
        }
    }
}
```

---

### 4. State Pattern (Conceptual)

#### What
Piece states like unplaced, placed, fixed.

#### Why
Avoids conditional logic scattered across the system.

#### How
Behavior changes based on state rather than flags.

```java
interface PieceState {
    void place(Piece piece, Position position);
    void remove(Piece piece);
    void lock(Piece piece);
}

class UnplacedState implements PieceState {
    public void place(Piece piece, Position position) {
        // Allow placement
        piece.setPosition(position);
        piece.setState(new PlacedState());
    }
    
    public void remove(Piece piece) {
        throw new IllegalStateException("Cannot remove unplaced piece");
    }
    
    public void lock(Piece piece) {
        throw new IllegalStateException("Cannot lock unplaced piece");
    }
}

class PlacedState implements PieceState {
    public void place(Piece piece, Position position) {
        throw new IllegalStateException("Piece already placed");
    }
    
    public void remove(Piece piece) {
        // Allow removal
        piece.clearPosition();
        piece.setState(new UnplacedState());
    }
    
    public void lock(Piece piece) {
        piece.setState(new LockedState());
    }
}

class LockedState implements PieceState {
    public void place(Piece piece, Position position) {
        throw new IllegalStateException("Piece is locked");
    }
    
    public void remove(Piece piece) {
        throw new IllegalStateException("Cannot remove locked piece");
    }
    
    public void lock(Piece piece) {
        // Already locked, no-op
    }
}

class Piece {
    private PieceState state = new UnplacedState();
    
    public void place(Position position) {
        state.place(this, position);
    }
    
    public void remove() {
        state.remove(this);
    }
}
```

---

## Important interview nuance

**You do not need to implement these patterns. You only need to recognize and justify them.**

> **Over-implementation is a common mistake.**

---

## Bottom-Up Design Flow

```
Level 1: Atomic Components
└── Edge
    ├── type (FLAT, IN, OUT)
    └── pattern (visual data)

Level 2: Composite Components
└── Piece
    ├── 4 Edges
    ├── identity
    ├── category (corner/border/interior)
    └── state (unplaced/placed/locked)

Level 3: Container
└── Board
    ├── dimensions
    ├── collection of Pieces
    ├── position grid
    └── validation rules

Level 4: System
└── Puzzle
    ├── Board
    ├── completion detection
    └── placement coordination
```

---

## Design Pattern Mapping

| Pattern | Use Case | Interview Phrasing |
|---------|----------|-------------------|
| **Composite** | Board → Pieces → Edges | "Natural part-whole hierarchy" |
| **Strategy** | Edge matching rules | "Flexible validation without modification" |
| **Factory** | Creating valid pieces | "Encapsulated construction logic" |
| **State** | Piece lifecycle | "Behavior changes with state" |

---

## Interview Response Template

### When discussing design approach:

> **"I'll use a bottom-up approach, starting with the Edge as the smallest unit that defines connections. Pieces are composed of edges, and the Board aggregates pieces. This ensures each level has clear responsibilities and depends only on the level below it."**

---

### When mentioning patterns:

> **"The design naturally aligns with several patterns: Composite for the part-whole hierarchy, Strategy for flexible edge matching, Factory for creating valid pieces with proper constraints, and State for managing piece lifecycle. These aren't forced—they emerge from the problem structure."**

---

## Why This Approach Works

### Benefits

| Benefit | Description |
|---------|-------------|
| **Low Coupling** | Each level depends only on the level below |
| **High Cohesion** | Each entity has clear, focused responsibility |
| **Easy Testing** | Can test edges, pieces, and board independently |
| **Natural Extension** | New piece types or matching rules fit cleanly |
| **Clear Validation** | Rules are localized at appropriate levels |

---

## What Interviewers Evaluate

### They're checking if you:

- ✅ Start with fundamentals
- ✅ Build through composition
- ✅ Recognize natural patterns
- ✅ Justify design choices
- ✅ Avoid premature complexity

### They're NOT checking if you:

- ❌ Memorized all patterns
- ❌ Implement everything
- ❌ Use the most patterns
- ❌ Write perfect code

---

## Red Flags to Avoid

| Mistake | Why It's Bad |
|---------|--------------|
| **Starting with the board** | Guessing piece details |
| **Forcing design patterns** | Looks artificial |
| **Implementing every pattern** | Over-engineering |
| **Skipping edge abstraction** | Missing the atomic unit |
| **Top-down decomposition** | Leads to rework |

---

## Green Flags That Impress

| Behavior | Why It's Good |
|----------|---------------|
| **Edge as atomic unit** | Shows deep thinking |
| **Justifying patterns naturally** | Shows experience |
| **Composition over inheritance** | Shows OO maturity |
| **Clear responsibility levels** | Shows design discipline |
| **Recognizing without forcing** | Shows judgment |

---

## LLD Interview Takeaway

**This section proves that:**

- ✅ You design from fundamentals
- ✅ You understand object composition
- ✅ You can map real-world problems to OO patterns
- ✅ You think in systems, not classes

> **That's exactly what interviewers look for.**

---

## Composition Principles

### How edges compose into pieces:

```
Edge (atomic)
  - Has type (FLAT, IN, OUT)
  - Has pattern/color data
  - Immutable once created

Piece (composite of 4 edges)
  - Has identity
  - Has category
  - Has state
  - Manages placement

Board (composite of N pieces)
  - Has dimensions
  - Validates placement
  - Tracks completion
  - Enforces boundaries
```

---

## Responsibility Distribution

### Clear separation of concerns:

| Entity | Knows About | Doesn't Know About |
|--------|-------------|-------------------|
| **Edge** | Its type, compatibility | Other edges, pieces |
| **Piece** | Its edges, position | Other pieces, board |
| **Board** | All pieces, grid | Edge matching details |
| **Puzzle** | Board state | Individual piece internals |

---

## Example Interview Dialogue

### Candidate:

> "I'll design from the bottom up. The fundamental unit is an Edge, which has a type like FLAT, IN, or OUT. A Piece is composed of four edges plus metadata like identity and category. The Board aggregates pieces and manages placement. This creates clear layers where each level only depends on the one below it."

### Interviewer:

> "How would you handle different types of pieces like corner and border?"

### Candidate:

> "That's handled at creation time using a Factory pattern. Corner pieces are created with two flat edges, border pieces with one, and interior pieces with none. The factory enforces these constraints so invalid pieces can't exist."

### Interviewer:

> "What about placement validation?"

### Candidate:

> "Edge matching logic can use Strategy pattern—we might validate by shape only, or also check pattern alignment. The strategy is injected into the validation logic, keeping it flexible without modifying the core piece model."

---
