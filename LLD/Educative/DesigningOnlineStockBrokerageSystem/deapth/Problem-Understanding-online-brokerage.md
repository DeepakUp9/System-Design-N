# LLD Chapter: Getting Ready – Online Stock Brokerage System

## Topic: Problem Definition & System Understanding

---

## What is this system?

An **online stock brokerage system** is a transactional platform that sits between:

- Buyers
- Sellers
- Stock exchanges / markets

**Its core responsibility** is executing trades on behalf of users, while abstracting away market complexity.

### In interview terms:

> **This is a financial transaction system with strong requirements around consistency, security, real-time data, and reliability.**

---

## Why does this system exist?

### From a business perspective

| Benefit | Description |
|---------|-------------|
| **Removes manual trading overhead** | Automated order processing |
| **Reduces operational costs** | Self-service model |
| **Increases transaction speed** | Electronic execution |
| **Enables self-service trading** | User empowerment |

---

### From a system design perspective

**Centralizes:**
- Trade execution
- Portfolio tracking
- Market data consumption

**Provides:**
- Controlled access to financial markets
- Auditable transaction history
- Secure handling of sensitive data

### In interviews, this justifies:

- ✅ Strong domain modeling
- ✅ Clear separation of responsibilities
- ✅ Careful handling of money + state transitions

---

## How does the system work (high level)?

Without going into steps, logically the system does **four major things**:

### 1. Trade facilitation

**Accepts buy/sell requests**

Validates:
- User eligibility
- Available balance / holdings

**Routes trades** to the market or matching engine

This introduces stateful entities like:
- `Orders`
- `Trades`
- `Positions`

---

### 2. Portfolio & performance tracking

**Maintains user holdings**

Displays:
- Current valuation
- Profit / loss
- Performance graphs

**From LLD lens:**
- Portfolio is a **derived view** over transactions
- Data consistency matters more than raw speed

---

### 3. Market data & alerts

- Consumes real-time stock prices
- Tracks thresholds set by users
- Sends notifications when conditions are met

**This naturally leads to:**
- Event-driven thinking
- Observer / Publisher–Subscriber patterns (mentionable in interview)

---

### 4. Funds management

**Supports:**
- Deposits
- Withdrawals

**Through:**
- Checks
- Wire transfers
- Electronic bank transfers

**This clearly separates:**
- Trading domain
- Payments / funds domain

> **In LLD interviews, this separation is a strong design signal.**

---

## Key characteristics interviewers expect you to notice

Even though they are not explicitly stated, this problem implies:

### High consistency requirements

**Incorrect balance or duplicate trades are unacceptable**

---

### Security-first design

**Because:**
- Money
- Personal data
- Regulatory implications

---

### Near real-time behavior

**Especially for:**
- Prices
- Alerts
- Order status

---

### Auditability

**Every action must be traceable**

> **Mentioning these shows system maturity, not just coding skill.**

---

## Questions you are expected to ask next (interview framing)

After stating this problem, a good LLD candidate naturally asks clarifying questions like:

1. **Are we designing for retail users only or also institutions?**

2. **Do we need real-time execution or delayed/batched trades?**

3. **Is order matching in scope or handled by an external exchange?**

4. **What types of orders are supported (market, limit, stop-loss)?**

5. **Do we need to handle partial fills?**

> **You don't answer them yet — you ask them to narrow scope.**

---

## Why this chapter matters in LLD interviews

This chapter is **not about classes or diagrams yet**.

It's about proving that:

| What You Show | Why It Matters |
|---------------|----------------|
| **You understand the domain** | Shows business context |
| **You can identify system boundaries** | Shows scoping ability |
| **You know what is in scope vs out of scope** | Shows priority judgment |

**Interviewers often reject designs not because of bad classes, but because:**

> **The candidate misunderstood the problem.**

---

# Extended Analysis

## WHAT (Interview Interpretation)

### What kind of system is this?

This is a **distributed, transaction-heavy financial system** that combines:

- User-facing workflows (placing trades, viewing portfolios)
- Backend transactional processing (orders, balances, settlements)
- Real-time data consumption (market prices, trends)
- Notification & alert mechanisms

### In LLD terms, this is NOT a CRUD system.

**It is a state-driven system where:**
- Money changes ownership
- Assets move between states
- Incorrect transitions are catastrophic

**Interview signal:**

> **This system demands strong domain modeling, not just service decomposition.**

---

### What are the core problem domains?

Without designing yet, an interviewer expects you to mentally partition the problem into domains:

| Domain | Purpose |
|--------|---------|
| **User & Account** | Identity, authentication, profiles |
| **Trading** | Orders, executions, settlements |
| **Portfolio** | Holdings, valuations, P&L |
| **Market Data** | Prices, quotes, historical data |
| **Funds / Payments** | Deposits, withdrawals, balances |
| **Notification** | Alerts, order status updates |

> **This tells the interviewer you know how to control complexity.**

---

## WHY (Design Motivation)

### Why is this problem chosen in interviews?

**Because it tests multiple competencies at once:**

- Handling financial correctness
- Managing state transitions
- Designing for real-time updates
- Separating critical vs non-critical flows

**It exposes weak candidates who:**
- ❌ Mix concerns
- ❌ Ignore consistency
- ❌ Treat everything as synchronous

---

### Why does the system need strict boundaries?

**Because failures have different blast radii:**

| Failure Type | Severity |
|--------------|----------|
| **Trade execution failure** | Critical |
| **Notification failure** | Recoverable |
| **Graph rendering delay** | Acceptable |

> **In interviews, explicitly calling this out shows engineering judgment.**

---

### Why not directly connect users to the market?

**Because:**

1. **Exchanges are complex and regulated**
2. **Users need abstraction, validation, and protection**

So the brokerage system acts as:
- Gatekeeper
- Validator
- Risk controller

> **This explains the "intermediary" role mentioned in the problem statement.**

---

## HOW (Logical Working – No Steps)

### How does the system manage trades?

**Conceptually:**

A trade is **not an instant action**

It moves through states:

```
CREATED → VALIDATED → PLACED → EXECUTED → SETTLED
```

Even if you don't mention state machines explicitly, **thinking in states is key**.

**Interviewers look for:**

> **Awareness that trading is a process, not a single method call.**

---

### How are portfolios handled?

**A portfolio is not primary data.**

**It is:**
- A computed view over trades + market prices

**This implies:**
- You don't "update portfolio" directly
- You derive it from authoritative sources

> **Mentioning this avoids a common LLD mistake.**

---

### How does real-time information fit in?

**Market prices:**
- Change frequently
- Come from external sources
- Are eventually consistent

**Design implication:**
- Trading decisions use latest available snapshot
- Historical trades rely on immutable execution data

> **Calling this out shows you understand temporal data.**

---

### How are funds managed safely?

**Funds flow is decoupled from trading intent:**

```
Trade request ≠ money movement
Settlement ≠ order placement
```

**This separation:**
- Prevents double spending
- Enables reconciliation
- Simplifies rollback scenarios

> **Interviewers love this observation.**

---

## Non-obvious Constraints (Strong LLD Signal)

Even though the problem statement doesn't say it explicitly:

| Constraint | Why It Matters |
|------------|----------------|
| **Idempotency is required** | Duplicate trade requests must be handled |
| **Concurrency is unavoidable** | Multiple trades at once |
| **Audit logs are mandatory** | Regulatory compliance |
| **Precision & rounding must be controlled** | Financial accuracy |

> **Bringing these up elevates your answer from "good" to "senior".**

---

## Typical Follow-up Questions Interviewers Ask Here

After you explain this chapter, expect questions like:

1. **Which parts must be strongly consistent?**
2. **Where can we tolerate eventual consistency?**
3. **What happens if market price changes mid-trade?**
4. **How do you prevent overspending?**

> **You're not answering them yet — but recognizing them proves readiness.**

---

## Why this chapter sets the tone for the entire LLD

This chapter establishes:

- ✅ System boundaries
- ✅ Domain separation
- ✅ Critical vs auxiliary flows

> **Everything later (classes, APIs, patterns) depends on this clarity.**

---

## System Boundaries Visualization

```
┌─────────────────────────────────────────────────────┐
│         Online Stock Brokerage System                │
└─────────────────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
   ┌────▼────┐   ┌────▼────┐   ┌───▼────┐
   │Trading  │   │Portfolio│   │Market  │
   │Engine   │   │Manager  │   │Data    │
   └────┬────┘   └────┬────┘   └───┬────┘
        │             │             │
   ┌────┴────┐   ┌────┴────┐   ┌───┴────┐
   │Orders   │   │Holdings │   │Prices  │
   │Trades   │   │P&L      │   │Quotes  │
   │Positions│   │Valuation│   │Alerts  │
   └─────────┘   └─────────┘   └────────┘
```

---

## Domain Separation Table

| Domain | Primary Entities | Critical? | Consistency Model |
|--------|------------------|-----------|-------------------|
| **Trading** | Order, Trade, Execution | ✅ Yes | Strong |
| **Portfolio** | Holding, Position, P&L | Medium | Eventual |
| **Market Data** | Price, Quote, Alert | Medium | Eventual |
| **Funds** | Account, Transaction, Balance | ✅ Yes | Strong |
| **Notification** | Alert, Message | Low | Eventual |
| **User** | User, Profile, Settings | Medium | Strong |

---

## Trade Lifecycle States

```
┌─────────┐
│ CREATED │
└────┬────┘
     │
     ▼
┌─────────────┐
│  VALIDATED  │
└────┬────────┘
     │
     ▼
┌─────────────┐
│   PLACED    │
└────┬────────┘
     │
     ▼
┌─────────────┐
│  EXECUTED   │
└────┬────────┘
     │
     ▼
┌─────────────┐
│   SETTLED   │
└─────────────┘

Side paths:
CREATED → REJECTED
PLACED → CANCELLED
EXECUTED → FAILED
```

---

## Interview Response Template

### Opening Statement:

> **"An online stock brokerage system is a financial intermediary that facilitates trades between users and markets. It's not a simple CRUD system—it's transaction-heavy with strong consistency requirements. The core domains are trading, portfolio management, market data, and funds management. Each has different criticality and consistency needs. Before designing, I'd like to clarify the scope: Are we handling order matching internally or delegating to an exchange? What order types should we support? What's the scale we're designing for?"**

---

### After Scope Clarification:

> **"Understood. I'll design a system that separates trading logic from portfolio views and market data. Trading operations will be strongly consistent with proper state management. Portfolio will be a derived view. Market data will be eventually consistent. I'll ensure idempotency for trade requests and proper audit trails. The design will use state machines for order lifecycle and event-driven architecture for notifications."**

---

## Key Design Principles

### Separation of Concerns

| What | Why |
|------|-----|
| **Trading ≠ Portfolio** | Different consistency needs |
| **Orders ≠ Funds** | Independent lifecycles |
| **Prices ≠ Execution** | Temporal decoupling |

---

### Consistency Requirements

| Component | Consistency | Rationale |
|-----------|-------------|-----------|
| **Order Placement** | Strong | Money at stake |
| **Trade Execution** | Strong | Cannot be wrong |
| **Account Balance** | Strong | Critical for trading |
| **Portfolio Valuation** | Eventual | Derived data |
| **Market Prices** | Eventual | External source |
| **Notifications** | Eventual | Non-critical |

---

### Auditability Requirements

**Every operation must be traceable:**

- Who placed the order?
- When was it executed?
- What price was used?
- Why was it rejected?

**Audit log should capture:**
- User actions
- System decisions
- State transitions
- External events

---

## Red Flags to Avoid

| Mistake | Why It's Bad | Correct Approach |
|---------|--------------|------------------|
| **Treating portfolio as primary data** | Data inconsistency | Derive from trades |
| **Synchronous everything** | Poor performance | Async where safe |
| **Mixing trading and payments** | Tight coupling | Separate domains |
| **No state management** | Bugs in lifecycle | Use state machines |
| **Ignoring idempotency** | Duplicate trades | Use idempotency keys |

---

## Green Flags That Impress

| What You Say | Why It Impresses |
|--------------|------------------|
| **"Trading is a process, not a method call"** | Shows state thinking |
| **"Portfolio is a derived view"** | Shows data modeling maturity |
| **"We need different consistency models"** | Shows distributed systems knowledge |
| **"Idempotency is critical for financial operations"** | Shows production experience |
| **"Audit trails are mandatory"** | Shows compliance awareness |

---

