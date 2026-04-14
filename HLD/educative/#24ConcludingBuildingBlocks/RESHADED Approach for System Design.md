# The RESHADED Approach for System Design

> A structured, systematic framework for tackling open-ended system design problems — ensuring completeness, consistency, and clear progression through every stage of the design process.

---

## Table of Contents

1. [Why a Framework?](#1-why-a-framework)
2. [RESHADED Overview](#2-reshaded-overview)
3. [Each Step in Detail](#3-each-step-in-detail)
   - [R — Requirements](#r--requirements)
   - [E — Estimation](#e--estimation)
   - [S — Storage Schema](#s--storage-schema-optional)
   - [H — High-Level Design](#h--high-level-design)
   - [A — API Design](#a--api-design)
   - [D — Detailed Design](#d--detailed-design)
   - [E — Evaluation](#e--evaluation)
   - [D — Distinctive Component / Feature](#d--distinctive-component--feature)
4. [Advantages of RESHADED](#4-advantages-of-reshaded)
5. [Applying RESHADED: A Quick Example](#5-applying-reshaded-a-quick-example)
6. [Summary Cheat Sheet](#6-summary-cheat-sheet)

---

## 1. Why a Framework?

System design problems are **inherently open-ended** — there is no single correct answer. The same system can be designed in many valid ways, each with different trade-offs. Without structure, it is easy to:

```
Common pitfalls without a framework:

  - Jump straight to components without understanding requirements
    -> Build the wrong system confidently

  - Skip estimation
    -> Miss the scale of the problem, choose wrong technologies

  - Design components in isolation
    -> Miss interactions between components, produce incomplete design

  - Forget non-functional requirements
    -> Build a system that works but fails under load or outages

  - Run out of time during an interview or design session
    -> Cover some aspects deeply, miss others entirely
```

A structured approach solves all of these by providing a **clear roadmap** where the next step is always visible, and by ensuring **all essential ingredients** are covered systematically.

---

## 2. RESHADED Overview

```
R  ->  Requirements
E  ->  Estimation
S  ->  Storage Schema        (optional)
H  ->  High-Level Design
A  ->  API Design
D  ->  Detailed Design
E  ->  Evaluation
D  ->  Distinctive Component / Feature
```

```
Design progression:

  [R] What are we building and for whom?
       |
       v
  [E] What scale must it handle?
       |
       v
  [S] How will we model the data? (if needed)
       |
       v
  [H] What are the major components?
       |
       v
  [A] How do users/services interact with the system?
       |
       v
  [D] How do we make it production-ready?
       |
       v
  [E] Does it actually meet the requirements?
       |
       v
  [D] What's unique or especially hard about this problem?
```

---

## 3. Each Step in Detail

---

### R — Requirements

**Goal:** Gather all requirements and define the scope of the design problem before writing a single component.

```
Two types of requirements:

  Functional requirements (WHAT the system does):
    - Core features and operations
    - User-facing capabilities
    - Example for Twitter: post tweets, follow users, view timeline, like tweets

  Non-functional requirements (HOW WELL the system does it):
    - Availability: how much uptime is required?
    - Scalability: how many users/requests/data?
    - Latency: how fast must responses be?
    - Consistency: how accurate must data be?
    - Durability: can data ever be lost?
    - Security: what access controls are needed?
```

**Why this step is critical:**

```
Without clear requirements:
  You might design a strongly consistent system when eventual consistency is fine.
  You might over-engineer for 1 billion users when the system serves 10,000.
  You might build features no one asked for and miss ones they did.

Good requirements questions to ask:
  "Who are the users of this system?"
  "What is the read/write ratio?"
  "Do we need real-time updates or is eventual consistency acceptable?"
  "What happens if the system is down for 1 hour?"
  "Are there regulatory requirements (GDPR, PCI-DSS)?"
```

**Output:** A clear list of functional requirements + non-functional requirements with specific targets where possible (e.g., "99.99% availability", "response time < 200ms").

---

### E — Estimation

**Goal:** Quantify the scale of the system to drive infrastructure and technology decisions.

```
Three core estimation dimensions:

  1. Servers:
     How many servers are needed to handle peak load?

     Formula:
     Servers = Peak requests per second / RPS per server

     Example:
     500 million DAU, each making 5 requests/day
     = 2.5 billion requests/day
     = 2.5B / 86,400 = ~28,935 requests/second (average)
     Peak = ~3x average = ~87,000 requests/second
     Server RPS = 64,000
     Servers needed = 87,000 / 64,000 ≈ 2 servers (at peak)

  2. Storage:
     How much data is generated per day and must be retained?

     Example:
     125 million tweets/day
     20% contain media (average 1MB per media)
     80% are text only (average 500 bytes per tweet)

     Text storage:  100M × 500B = 50 GB/day
     Media storage: 25M × 1MB  = 25 TB/day
     Total:         ~25 TB/day

  3. Bandwidth:
     How much data is transferred in/out per second?

     Incoming = total uploads per day / 86,400
     Outgoing = read traffic per day × average response size / 86,400
```

**Why estimation matters:**

```
Estimation answers questions like:
  "Should we use a relational DB or a distributed NoSQL store?"
    -> If data is petabytes, NoSQL is likely necessary

  "Do we need CDN caching?"
    -> If outgoing bandwidth is terabits/second, yes

  "How many shards do we need?"
    -> Estimation tells you the write throughput per shard

  "Is a single server enough?"
    -> Estimation tells you if 1 server or 10,000 are needed

  Without estimation, technology choices are guesswork.
```

**Output:** Approximate server count, storage per day, bandwidth in/out, and any other resource-specific numbers relevant to the system.

---

### S — Storage Schema (Optional)

**Goal:** Define the data model — which tables/collections are needed and what fields they contain.

```
When to include this step:
  - When the data model is non-trivial or central to the design
  - When data relationships are complex (e.g., social graphs, dependency chains)
  - When schema design drives technology selection (SQL vs NoSQL vs graph DB)

When to skip this step:
  - When the design is about infrastructure rather than data (e.g., CDN design)
  - When the schema is straightforward and doesn't add insight
  - When time is limited in an interview setting
```

**Example schema snippet (Twitter):**

```
Table: tweets
  tweet_id      BIGINT     PRIMARY KEY
  user_id       BIGINT     FOREIGN KEY -> users.user_id
  content       TEXT       (max 280 chars)
  created_at    TIMESTAMP
  media_url     VARCHAR    (nullable — only for media tweets)
  like_count    INT        (or: reference to sharded counter)

Table: follows
  follower_id   BIGINT     FOREIGN KEY -> users.user_id
  followee_id   BIGINT     FOREIGN KEY -> users.user_id
  created_at    TIMESTAMP
  PRIMARY KEY (follower_id, followee_id)
```

**Output:** Table/collection definitions with column names, data types, and key relationships. Optionally: rationale for technology choice (e.g., "we use a graph database for the social graph because relational recursive queries are expensive").

---

### H — High-Level Design

**Goal:** Identify the major components and building blocks, and show how they connect.

```
This is the first architectural draft — broad strokes, not implementation details.

Inspired directly by the requirements:
  Functional requirements -> what components are needed?
    "Users upload videos" -> need object storage (blob store)
    "Users search for content" -> need an indexer and search service
    "Users follow each other" -> need a social graph service

  Non-functional requirements -> what properties must components have?
    "High availability" -> all components must be replicated
    "Low latency reads" -> need caching layer
    "Millions of writes" -> need a distributed queue
```

**Typical components at the high level:**

```
Client -> [Load Balancer] -> [Application Servers]
                                  |
              +---------+---------+---------+
              |         |         |         |
          [Cache]  [Database] [Queue] [Object Storage]
                                  |
                           [Worker Services]
```

**Output:** A block diagram (or ASCII equivalent) showing major components and their connections. Arrows indicate data flow direction. This is a starting point for iteration.

---

### A — API Design

**Goal:** Define the interfaces through which clients interact with the system.

```
APIs translate functional requirements into concrete contracts.

Each major functional requirement typically becomes one or more API endpoints:
  "Users can post tweets" -> POST /tweets
  "Users can like tweets" -> POST /tweets/{tweet_id}/likes
  "Users can view timeline" -> GET /users/{user_id}/timeline

API definition includes:
  - Endpoint / function name
  - HTTP method (GET, POST, PUT, DELETE) or RPC method
  - Input parameters (name, type, description)
  - Return value (type, description)
  - Error cases
```

**Example (Twitter timeline):**

```
GET /users/{user_id}/timeline

Parameters:
  user_id   (path)   Integer  - The ID of the user requesting their timeline
  cursor    (query)  String   - Pagination token for next page of results
  limit     (query)  Integer  - Number of tweets to return (default: 20, max: 100)

Returns:
  {
    tweets: [ { tweet_id, user_id, content, created_at, like_count }, ... ],
    next_cursor: "abc123..."  (null if no more results)
  }

Errors:
  401: User not authenticated
  404: User not found
  429: Rate limit exceeded
```

**Output:** A complete set of API definitions covering all functional requirements. Include parameters, return types, and notable error cases.

---

### D — Detailed Design

**Goal:** Evolve the high-level design into a production-ready architecture by addressing its limitations.

```
The detailed design starts by asking:
  "What are the weaknesses of my high-level design?"
  "What breaks at scale?"
  "What happens when components fail?"
  "How do I meet the non-functional requirements I stated?"

Then addresses each weakness with specific solutions.
```

**What detailed design covers:**

```
1. Component internals:
   Not just "there's a database" but which database, how it's sharded,
   how many replicas, what consistency model, how failures are handled.

2. Data flow and workflows:
   End-to-end flow for each major operation:
   "When a user posts a tweet, what happens step by step?"

3. Technology choices with justification:
   "We use Cassandra because it handles high write throughput
    and our data model is wide-column friendly."
   "We use Kafka as our queue because it provides durable, ordered delivery
    at high throughput with replay capability."

4. Fault tolerance:
   How does the system behave when each component fails?
   What is the recovery mechanism?

5. Scalability mechanisms:
   Partitioning strategy, replication factor, caching layers,
   auto-scaling policies.
```

**Output:** A detailed architecture diagram with all components, explicit technology choices, end-to-end workflows, and explanations of how non-functional requirements are satisfied.

---

### E — Evaluation

**Goal:** Measure the effectiveness of the design against the requirements defined in step R.

```
Evaluation structure:

  For each non-functional requirement:
    "How does our design satisfy this requirement?"
    "What trade-offs did we make?"
    "What are the remaining limitations?"

  Example:
    Requirement:  "99.99% availability"
    Our design:   "Each component is replicated across 3 AZs.
                   If any one AZ goes down, the other two continue serving.
                   Database uses geo-replication for disaster recovery."
    Trade-off:    "Replication increases storage cost by 3x."
    Limitation:   "Cross-region failover adds ~100ms latency for users
                   who are rerouted to a different region."
```

**Also covers:**

```
Trade-offs made:
  "We chose eventual consistency over strong consistency for the like counter
   because strong consistency would require cross-shard locking, which
   destroys write throughput. For social media likes, being off by a few
   counts for a few seconds is acceptable."

Areas for improvement:
  "The current design does not address cache invalidation for the timeline.
   In a future iteration, we would add a cache invalidation service."
```

**Output:** A requirement-by-requirement compliance mapping, explicit trade-off discussion, and identified areas for future improvement.

---

### D — Distinctive Component / Feature

**Goal:** Identify and deeply discuss the unique challenge that makes this specific design problem interesting and hard.

```
Every system design problem has something distinctive:
  Uber:         Fraud detection + driver-rider matching at scale
  Google Docs:  Concurrency control — multiple users editing the same document simultaneously
  Twitter:      The heavy hitters problem — celebrity tweets with millions of concurrent writes
  YouTube:      Video transcoding pipeline at petabyte scale
  WhatsApp:     Message delivery guarantees across unreliable networks
  Uber Eats:    Real-time order tracking with location updates every second

The distinctive component is NOT just another component to list.
It is the part of the design that requires the deepest thought,
the most novel solution, and the clearest trade-off discussion.
```

**Why this step matters:**

```
A generic "design a messaging app" answer misses what's actually hard.
The hardest part of messaging is NOT storing messages (trivial).
It's: "How do you guarantee exactly-once delivery when the network is unreliable?"
That's the distinctive feature — and addressing it demonstrates real depth.

In an interview: this is where you differentiate yourself from candidates
who give textbook answers and show genuine understanding of the problem domain.
```

**Output:** A focused, deep discussion of the one or two aspects that are uniquely challenging about this system, with specific solutions and trade-offs.

---

## 4. Advantages of RESHADED

```
Advantage 1: GUIDANCE
  At every point in the design process, the next step is clear.
  No staring at a blank whiteboard wondering where to begin.
  No jumping around between components randomly.
  The framework provides a consistent progression:
    Understand -> Quantify -> Model -> Sketch -> Interface -> Detail -> Verify -> Differentiate

Advantage 2: COMPLETENESS
  Following RESHADED ensures no critical ingredient is skipped:
    - Requirements not gathered? The system might solve the wrong problem.
    - Estimation skipped? Technology choices lack justification.
    - API design omitted? The system has no clear contract with clients.
    - Evaluation skipped? No way to know if requirements are actually met.
    - Distinctive feature ignored? The hardest part of the problem is unaddressed.

  RESHADED acts as a checklist that catches omissions before they become design flaws.
```

---

## 5. Applying RESHADED: A Quick Example

**Problem: Design Twitter**

```
R — Requirements:
  Functional: post tweets, follow users, view timeline, like/retweet, search
  Non-functional: high availability (99.99%), low read latency (< 100ms),
                  scalability (500M DAU), eventual consistency for counts

E — Estimation:
  500M DAU, ~10 tweets/day per user = 5B tweet reads/day
  = ~57,900 read requests/second (average), ~175,000 at peak
  Servers needed: 175,000 / 64,000 ≈ 3 servers (oversimplified, real system is much more)
  Storage: 125M new tweets/day × 500 bytes = ~62.5 GB/day text
           25M media tweets × 1MB = 25 TB/day media

S — Storage Schema:
  Tables: users, tweets, follows, likes, retweets
  (as defined above)

H — High-Level Design:
  Client -> Load Balancer -> API Servers -> [Tweet DB, Follow Graph DB, Cache, CDN]

A — API Design:
  POST /tweets          -> create tweet
  GET /timeline         -> read personalized timeline
  POST /tweets/:id/like -> like a tweet

D — Detailed Design:
  Timeline generation: fan-out on write (push tweets to follower caches at publish time)
  Like counters: sharded counters (N shards per tweet, based on follower count)
  Search: inverted index with distributed search
  Storage: Cassandra for tweets (high write throughput), Redis for timeline cache

E — Evaluation:
  Availability: All components replicated, multi-AZ -> meets 99.99% target
  Scalability: Horizontal scaling at every layer
  Latency: Timeline served from Redis cache (sub-millisecond)
  Trade-off: Fan-out on write uses more storage but reduces read latency

D — Distinctive Feature:
  The heavy hitters problem: celebrity tweets with millions of followers.
  Standard fan-out would push to 100M follower caches on every celebrity tweet.
  Solution: Hybrid fan-out — fan-out on write for regular users,
            pull on read for celebrities (fetch from celebrity's tweet list at read time).
```

---

## 6. Summary Cheat Sheet

```
Letter  Step                    Core Question                         Output
------  ----                    -------------                         ------
R       Requirements            What are we building and for whom?    Functional + non-functional requirements list
E       Estimation              What scale must it handle?            Server count, storage/day, bandwidth in/out
S       Storage Schema          How will we model the data?           Table definitions, data types, relationships
H       High-Level Design       What are the major components?        Architecture block diagram, component connections
A       API Design              How do clients use the system?        Endpoint definitions, parameters, return types
D       Detailed Design         How do we make it production-ready?   Detailed architecture, tech choices, workflows
E       Evaluation              Does it meet the requirements?        Requirement compliance mapping, trade-offs, gaps
D       Distinctive Feature     What is uniquely hard about this?     Deep dive into the most challenging sub-problem
```

```
RESHADED in one sentence:

  Understand the problem (R)
  -> Quantify the scale (E)
  -> Model the data (S)
  -> Sketch the architecture (H)
  -> Define the interfaces (A)
  -> Flesh out the details (D)
  -> Verify the solution (E)
  -> Address the hard part (D)
```

---

