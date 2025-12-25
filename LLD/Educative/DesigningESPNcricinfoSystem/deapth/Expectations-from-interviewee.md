# ESPNcricinfo System - Expectations from the Interviewee

## 📋 Overview
This document outlines what interviewers expect from candidates when tackling the ESPNcricinfo LLD problem. It covers the **WHAT**, **WHY**, and **HOW** of each expectation.

---

## WHAT the Interviewer is Really Testing Here

This section is **not about coding**. It evaluates whether you can:

* ✅ Read a **vague problem** and convert it into **concrete design decisions**
* ✅ Identify **actors, ownership, and boundaries**
* ✅ Ask **correct clarifying questions** before drawing classes

### Interview Signal
> "I understand that LLD starts with **responsibility clarity**, not UML."

---

## WHY This Section Matters in LLD Interviews

Many candidates jump straight to entities like `Match` or `Player`.

This section checks whether you:

* ✅ Understand **who owns data**
* ✅ Know **when data changes**
* ✅ Can separate **configuration vs runtime behavior**

### Interviewer Expectation
> A good interviewer expects you to **pause here, ask questions**, and only then proceed.

---

## HOW to Explain Each Expectation (Interview Framing)

### 1. Player, Team, and Match Statistics

#### WHAT
Statistics are **derived data** representing match outcomes and player performance.

**Typical examples:**

| Entity | Statistics |
|--------|-----------|
| **Player** | Runs, wickets, strike rate, economy |
| **Team** | Total score, run rate, wins/losses |
| **Match** | Result, margin, Man of the Match (MOM) |

#### WHY This Needs Clarification
Statistics define:

* ⏱️ **Update frequency** - Real-time vs batch
* 👤 **Data ownership** - Who can modify
* 🔒 **Consistency requirements** - Strong vs eventual

**Without clarity, you risk:**
* ❌ **Over-updating** - Recomputing every ball
* ❌ **Under-updating** - Stale data
* ❌ **Inconsistent state** - Mismatch between live and historical data

#### HOW to Reason in Interview

**Good interview response:**
> "Ball-by-ball events are the **source of truth**. Statistics are derived either **incrementally per ball** or **aggregated at logical checkpoints** like end of over, innings, or match."

**Safe LLD Assumption:**
* Core stats updated **per ball** (runs, wickets)
* Summary stats finalized at **innings or match end** (average, strike rate)
* You don't implement analytics—just **model the flow**

**Code Example:**
```java
public class BallEvent {
    private Ball ball;
    private int runs;
    private boolean isWicket;
    
    // This triggers incremental updates
    public void recordBall() {
        updateLiveScore();        // Immediate
        updatePlayerStats();       // Incremental
        // Summary stats computed at innings end
    }
}
```

---

### 2. Match Formats and Match Management

#### WHAT
The system supports:

* 🏏 **Test** - Unlimited overs, 2 innings per team
* 🏏 **ODI** - 50 overs, 1 innings per team
* 🏏 **T20** - 20 overs, 1 innings per team

**Each format affects:**
* Number of innings
* Overs per innings
* Match duration
* Fielding restrictions

#### WHY This is Critical in LLD

Formats change **rules**, not **entities**.

**❌ Bad Design:**
```java
class TestMatch extends Match { }
class ODIMatch extends Match { }
class T20Match extends Match { }
// Duplication everywhere!
```

**✅ Better Design:**
```java
class Match {
    private MatchFormat format;  // Enum or Strategy
    private MatchRules rules;    // Format-specific rules
}
```

**Benefits:**
* One `Match` entity
* Format-specific behavior via:
  * Configuration
  * Strategy pattern (mention lightly)

#### HOW to Express It in Interview

**Strong interview statement:**
> "The match format influences **validation rules and flow**, not the core structure. So I would avoid duplicating entities and instead keep format-specific rules **configurable**."

This shows **clean abstraction thinking**.

**Code Example:**
```java
public enum MatchFormat {
    TEST(Integer.MAX_VALUE, 2),
    ODI(50, 1),
    T20(20, 1);
    
    private final int oversPerInnings;
    private final int inningsPerTeam;
}

public class Match {
    private MatchFormat format;
    
    public boolean isInningsComplete(Innings innings) {
        return innings.getOvers() >= format.getOversPerInnings();
    }
}
```

---

### 3. Squad Submission for Tournaments

#### WHAT
A **tournament squad** is a pre-approved list of players for a team.

**Key characteristics:**
* 📝 **Tournament-scoped** - Valid for entire tournament
* 🔒 **Different from Playing XI** - 15-player squad, 11-player team
* ⏰ **Time-bound** - Submission deadline before tournament

#### WHY This Distinction Matters

This introduces:

* ⏱️ **Temporal rules** - Before vs after tournament start
* 🔐 **Controlled mutability** - Can't change mid-tournament without approval
* 🚫 **Illegal state prevention** - Player can't play without being registered

**Design implications:**
* Validation at multiple levels
* Audit trail (who changed what, when)
* State-dependent behavior

#### HOW to Reason Clearly

**Interview-safe explanation:**
> "A squad is submitted **once per tournament**. From this squad, the **playing eleven** is chosen per match. Post-deadline modifications are restricted and typically require **admin approval**."

This naturally leads to:
* ✅ Validation rules
* ✅ Auditability (but no need to design it yet)
* ✅ State machine for squad status

**Code Example:**
```java
public class TournamentSquad {
    private Tournament tournament;
    private Team team;
    private List<Player> players;  // Max 15
    private SquadStatus status;    // DRAFT, SUBMITTED, LOCKED
    private LocalDateTime submissionDeadline;
    
    public void submitSquad() {
        if (LocalDateTime.now().isAfter(submissionDeadline)) {
            throw new DeadlinePassedException();
        }
        if (players.size() < 11 || players.size() > 15) {
            throw new InvalidSquadSizeException();
        }
        this.status = SquadStatus.SUBMITTED;
    }
}

public class PlayingXI {
    private Match match;
    private TournamentSquad squad;
    private List<Player> players;  // Exactly 11
    
    public void selectPlayingXI(List<Player> selectedPlayers) {
        // Validate all players are in squad
        if (!squad.getPlayers().containsAll(selectedPlayers)) {
            throw new PlayerNotInSquadException();
        }
        this.players = selectedPlayers;
    }
}
```

**Relationship:**
```
Tournament
└── TournamentSquad (per team)
    └── PlayingXI (per match)
```

---

### 4. Admin Operations

#### WHAT
**Admin** is the system authority with elevated privileges.

**Core responsibilities:**

| Operation | Examples |
|-----------|----------|
| **Entity Creation** | Create tournaments, matches, teams |
| **Data Correction** | Fix scoring errors, update stats |
| **Infrastructure Assignment** | Assign stadiums, umpires, commentators |
| **Approval Management** | Approve squad changes, match cancellations |

#### WHY Admin Logic Must Be Centralized

**❌ If admin logic leaks into entities:**
* Validation becomes inconsistent
* Business rules scatter across codebase
* Hard to maintain and audit

**✅ In good LLD, admin operations live in:**
* Service layer (e.g., `AdminService`)
* Not inside entities

#### HOW to Explain Validations

**Strong interview response:**
> "When creating matches or assigning personnel, validations ensure **referential integrity**, **availability**, and **role correctness** before changes go live."

**Example validations:**
* 🏟️ **Venue availability** - Stadium not double-booked
* 👥 **Umpire assignment** - Umpire available on match date
* 📅 **Match scheduling** - No conflicting fixtures for teams
* ✅ **Squad validation** - All players registered in tournament

**Approval flow:**
* Mention as a **possible extension**
* Not mandatory unless interviewer pushes

**Code Example:**
```java
public class AdminService {
    
    public Match createMatch(
        Tournament tournament,
        Team team1,
        Team team2,
        Venue venue,
        LocalDateTime matchDate
    ) {
        // Validation 1: Tournament exists and is active
        validateTournament(tournament);
        
        // Validation 2: Teams are registered in tournament
        validateTeamsInTournament(tournament, team1, team2);
        
        // Validation 3: Venue is available
        validateVenueAvailability(venue, matchDate);
        
        // Validation 4: No conflicting matches for teams
        validateTeamAvailability(team1, team2, matchDate);
        
        // Create match
        Match match = new Match(tournament, team1, team2, venue, matchDate);
        matchRepository.save(match);
        
        return match;
    }
    
    public void assignUmpires(Match match, List<Umpire> umpires) {
        // Validation 1: Correct number of umpires
        if (umpires.size() < 2 || umpires.size() > 3) {
            throw new InvalidUmpireCountException();
        }
        
        // Validation 2: Umpires are available
        for (Umpire umpire : umpires) {
            validateUmpireAvailability(umpire, match.getMatchDate());
        }
        
        // Assign
        match.setUmpires(umpires);
        matchRepository.update(match);
    }
    
    public void approveSquadChange(
        TournamentSquad squad,
        Player removedPlayer,
        Player addedPlayer,
        String reason
    ) {
        // Validation 1: Tournament allows changes
        if (!squad.getTournament().allowsSquadChanges()) {
            throw new SquadChangesNotAllowedException();
        }
        
        // Validation 2: Valid reason provided
        if (reason == null || reason.trim().isEmpty()) {
            throw new ReasonRequiredException();
        }
        
        // Make change with audit
        squad.replacePlayer(removedPlayer, addedPlayer);
        auditLog.log("SQUAD_CHANGE", squad, reason);
    }
}
```

---

## What Good Clarifying Questions Look Like (Signal Quality)

You **don't need answers**—you need to show you **ask the right ones**:

### Statistics and Data Management
* ❓ Are statistics **immutable** after match completion?
* ❓ How are **historical stats** vs **live stats** stored differently?
* ❓ Should stats be **eventually consistent** or **strongly consistent**?

### Tournament and Squad Management
* ❓ Can squads **change mid-tournament**? Under what conditions?
* ❓ Is there a **deadline** for squad submission?
* ❓ What happens if a player gets **injured** mid-tournament?

### Match Infrastructure
* ❓ Are umpires assigned **per match** or **per tournament**?
* ❓ Can a **venue host multiple matches** simultaneously?
* ❓ Who can **modify** match results after completion?

### Content and Commentary
* ❓ Is commentary **editable post-match**?
* ❓ Who can **add commentary** during a live match?
* ❓ Should commentary be **versioned** or **append-only**?

### Role-Based Access
* ❓ Can a **coach** view opponent team's playing XI before match?
* ❓ Can **commentators** see ball-by-ball data in real-time?
* ❓ What operations require **admin approval**?

**Why these questions matter:**
> Asking these shows **design maturity** and understanding that LLD is about clarifying constraints before coding.

---

## Key Takeaway You Should Convey

**Essential interview statement:**
> "Before designing classes, I want clarity on **ownership**, **update frequency**, and **constraints**—because these directly affect **responsibilities and relationships** in LLD."

This shows you understand:
* 📊 **Data modeling** requires business context
* 🔄 **State management** depends on update patterns
* 🎯 **Responsibility assignment** follows from ownership clarity

---

## Interview Do's and Don'ts

### ✅ Do's

| Action | Why It Matters |
|--------|----------------|
| **Ask clarifying questions first** | Shows structured thinking |
| **Identify data ownership early** | Prevents design confusion |
| **Distinguish configuration vs runtime** | Shows abstraction maturity |
| **Mention validation points** | Shows attention to correctness |
| **Acknowledge temporal constraints** | Shows real-world awareness |

### ❌ Don'ts

| Action | Why It's Bad |
|--------|--------------|
| **Jump straight to coding** | Signals lack of planning |
| **Assume requirements** | May design wrong system |
| **Ignore who can modify what** | Leads to security holes |
| **Over-engineer with patterns** | Adds unnecessary complexity |
| **Skip validation discussion** | Shows weak business logic |

---

## Sample Interview Dialogue

### Scenario 1: Statistics Discussion

**Interviewer:** "How would you handle player statistics?"

**❌ Weak Response:**
"I'll create a `PlayerStats` class with runs and wickets."

**✅ Strong Response:**
"Before designing, I'd clarify: Are statistics updated **incrementally per ball** or **computed at match end**? Also, should **historical stats** be immutable once a match is complete? This affects whether I use **event sourcing** or **direct updates**."

---

### Scenario 2: Squad Management

**Interviewer:** "How do you handle team squads?"

**❌ Weak Response:**
"A team has a list of players."

**✅ Strong Response:**
"I see **two concepts**: a **tournament squad** (15 players, submitted once) and a **playing XI** (11 players, selected per match). The squad has a **submission deadline** and requires **admin approval** for changes. Should I model these as separate entities with validation rules?"

---

### Scenario 3: Format Handling

**Interviewer:** "The system supports Test, ODI, and T20 formats."

**❌ Weak Response:**
"I'll create `TestMatch`, `ODIMatch`, and `T20Match` classes."

**✅ Strong Response:**
"Formats affect **rules**, not **core structure**. I'd use a single `Match` entity with a `MatchFormat` configuration that defines **overs per innings** and **innings count**. This avoids duplication and makes adding new formats easier. Does that approach work?"

---

## Design Maturity Checklist

Before moving to class diagrams, ensure you've covered:

- [ ] **Data Ownership** - Who creates, reads, updates, deletes each entity?
- [ ] **Update Frequency** - Real-time, near real-time, or batch?
- [ ] **Temporal Constraints** - Deadlines, validity periods, state transitions?
- [ ] **Validation Points** - What checks are needed before operations?
- [ ] **Role-Based Access** - Who can perform which operations?
- [ ] **Configuration vs Runtime** - What's static config vs dynamic behavior?
- [ ] **Immutability** - What data never changes after creation?
- [ ] **Audit Requirements** - What changes need tracking?

---

## Connection to Next Steps

After clarifying expectations, you proceed to:

1. **Identify Core Entities** - Based on ownership clarity
2. **Define Relationships** - Based on update frequency understanding
3. **Assign Responsibilities** - Based on validation requirements
4. **Design APIs** - Based on role-based access rules

**Remember:** Good clarification leads to clean design.

---

## Summary Table

| Expectation | What to Clarify | Design Impact |
|-------------|----------------|---------------|
| **Statistics** | Update frequency, ownership | Incremental vs batch updates |
| **Match Formats** | Rule variations | Configuration vs inheritance |
| **Squad Submission** | Temporal constraints, mutability | Validation, state management |
| **Admin Operations** | Validation rules, approvals | Service layer design |

---

## Additional Interview Tips

### Show System Thinking
* Don't just think about happy paths
* Consider error cases and edge conditions
* Think about data consistency and integrity

### Demonstrate Trade-off Awareness
* "We could do X for simplicity or Y for flexibility..."
* "This approach favors read performance over write complexity..."
* "I'm choosing eventual consistency here because..."

### Use Proper Terminology
* "Aggregate root" instead of "main class"
* "Bounded context" instead of "area"
* "Invariant" instead of "rule"

---
