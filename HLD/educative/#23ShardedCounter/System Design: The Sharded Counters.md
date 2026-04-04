# System Design: The Sharded Counters

> Understanding why simple counters break at scale, how the heavy hitters problem creates write bottlenecks, and why sharding is the solution.

---

## Table of Contents

1. [The Problem Statement](#1-the-problem-statement)
2. [Why Counting is Hard at Scale](#2-why-counting-is-hard-at-scale)
3. [The Heavy Hitters Problem](#3-the-heavy-hitters-problem)
4. [Why Traditional Counters Fail](#4-why-traditional-counters-fail)
5. [Design Roadmap](#5-design-roadmap)
6. [Summary](#6-summary)

---

## 1. The Problem Statement

Large-scale social platforms process enormous volumes of concurrent user interactions. Every like, view, comment, and share must be counted — accurately and in real time.

### The Scale of the Problem

**Twitter:**

```
Tweets per second:  ~6,000
Tweets per day:     ~500 million
Likes per day:      Billions (multiple likes per tweet on average)

A single viral tweet can receive:
  - 100,000 likes in the first minute
  - 1,000,000 likes in the first hour
  - All from different users, hitting the same counter simultaneously
```

**YouTube:**

```
Most-viewed videos on YouTube receive:
  - Millions of views within the first 24 hours
  - Each view = a write to the view counter

  Example: A major music video release
  t=0:   Video published
  t=1h:  5,000,000 views
  t=24h: 50,000,000 views

  That is ~2,000 view counter writes per second, sustained for 24 hours.
  For the most popular videos, peaks can reach 100,000+ writes/second.
```

**Facebook:**

```
A post from an account with millions of followers can receive:
  Likes:    Thousands per second immediately after posting
  Comments: Hundreds per second
  Shares:   Hundreds per second

  All three are counter updates hitting the same post record simultaneously.
```

---

## 2. Why Counting is Hard at Scale

### The Single Counter Problem

For a single post with low traffic, a counter is trivial:

```
Single Counter (simple case):

  Database record: { post_id: 123, likes: 0 }

  User A likes post 123:
    Read current value: 0
    Increment: 0 + 1 = 1
    Write back: { post_id: 123, likes: 1 }

  Works perfectly when one user at a time.
```

But the moment concurrent writes arrive, things break:

```
Concurrent Counter Update (the problem):

  Database record: { post_id: 123, likes: 1000 }
  Simultaneously:
    User A reads: 1000 -> increments to 1001 -> writes 1001
    User B reads: 1000 -> increments to 1001 -> writes 1001
    User C reads: 1000 -> increments to 1001 -> writes 1001

  Result: Three users liked the post, but counter shows 1001 (not 1003).
  Two likes were lost. This is a RACE CONDITION.
```

### The Lock Solution and Its Failure

To prevent race conditions, databases use **exclusive locks** on write:

```
Locked Counter (works but breaks at scale):

  Write operation process:
    1. Acquire exclusive lock on the counter record
    2. Read current value
    3. Increment
    4. Write back
    5. Release lock

  With 1 write/second: works fine, lock acquired and released quickly
  With 100 writes/second: small queue forms, acceptable
  With 10,000 writes/second: large queue, writes stall
  With 100,000 writes/second: system spends more time WAITING for locks
                               than UPDATING the counter
```

```
Lock contention growth (non-linear):

  Writes/second    Lock contention    Effective throughput
  10               Near zero          ~10 writes/sec (fast)
  100              Low                ~95 writes/sec (fine)
  1,000            Moderate           ~800 writes/sec (acceptable)
  10,000           High               ~4,000 writes/sec (degraded)
  100,000          Severe             ~500 writes/sec (collapsed)

  Lock contention grows NON-LINEARLY with concurrent writers.
  At extreme concurrency, the system effectively grinds to a halt.
```

---

## 3. The Heavy Hitters Problem

The **heavy hitters problem** describes the scenario where a small number of items (posts, videos, tweets) receive a disproportionately large share of traffic.

```
Distribution of write traffic (typical social platform):

  99% of posts:   < 100 writes/second per post  <- manageable
  0.9% of posts:  100-10,000 writes/second       <- challenging
  0.1% of posts:  > 10,000 writes/second         <- heavy hitters

  The 0.1% creates the bottleneck.
  A single viral tweet, trending video, or celebrity post
  can generate more write traffic than all other posts combined.
```

### Why Heavy Hitters Cause Hot Partitions

```
Database sharding distributes data across multiple nodes:

  Shard 1: posts 1-1,000,000
  Shard 2: posts 1,000,001-2,000,000
  Shard 3: posts 2,000,001-3,000,000

  A viral tweet (post_id=500000) is on Shard 1.
  All 100,000 writes/second hit SHARD 1.
  Shard 2 and Shard 3: mostly idle.

  Result: Shard 1 is a HOT PARTITION.
    - CPU pegged at 100% on Shard 1
    - Other posts on Shard 1 also slow down (collateral damage)
    - Shard 2 and 3 sitting underutilized
    - Adding more shards doesn't help (the hot item moves to one new shard)
```

---

## 4. Why Traditional Counters Fail

### Failure Mode 1: Single Counter with Locking

```
What happens:
  All writes queue up waiting for the lock
  Queue length grows faster than it drains
  Write latency increases from milliseconds to seconds
  Timeouts increase -> clients retry -> makes contention worse
  Cascading failure

Real-world symptom:
  "Like" button spins for 3 seconds before registering
  OR: Like is silently dropped (timeout)
```

### Failure Mode 2: Optimistic Locking (Compare-and-Swap)

```
Attempt to avoid locks using CAS (Compare-And-Swap):

  Read value: 1000
  Compute new value: 1001
  CAS: "Set to 1001 only if current value is still 1000"

  If another writer changed it to 1001 first:
    -> CAS fails -> retry with new read value
    -> Read: 1001, compute 1002, CAS...

  Under high concurrency:
    99% of CAS operations FAIL and must retry
    System loops through failed CAS attempts
    CPU consumed entirely by retries, not useful work
    Throughput collapses (same problem, different mechanism)
```

### Failure Mode 3: Batched Writes (Partial Fix)

```
Approach: Buffer writes in memory, flush periodically to DB

  Buffer: collect 1,000 writes, then do one DB write of +1000

  Pros: Reduces DB write frequency dramatically
  Cons:
    - Counter is stale between flushes (shows wrong value to readers)
    - If server crashes before flush: all buffered writes lost
    - Not suitable for systems requiring exact, real-time counts
    - Buffer itself can become a bottleneck under extreme traffic
```

### Failure Mode 4: Distributed Counter with Single Record

```
Attempt: Store counter in a distributed cache (e.g., Redis) instead of DB

  Redis INCR command is atomic: no race condition
  Redis single-threaded: operations serialized

  Problem: Single Redis key for one counter = single point of serialization
    100,000 writes/second all target one key
    Redis processes them serially
    Redis throughput: ~100,000-200,000 ops/second for INCR
    At peak: Redis becomes the bottleneck
    Even with a fast cache, a single key has a ceiling
```

### The Root Cause

```
All traditional approaches share the same fundamental limitation:

  ONE counter = ONE serialization point

  No matter how fast or distributed the system is,
  if all writes must update the same single value,
  they must be serialized at some point.
  Serialization = throughput ceiling = bottleneck.

  The solution must ELIMINATE the single serialization point.
  -> This is exactly what SHARDED COUNTERS do.
```

---

### Traditional Approaches Comparison

| Approach | Mechanism | Failure Mode | Ceiling |
|---|---|---|---|
| Single DB counter + lock | Exclusive lock per write | Lock queue grows non-linearly | ~1,000 writes/sec |
| Optimistic locking (CAS) | Retry on conflict | 99% retries under high concurrency | ~5,000 writes/sec |
| Memory buffer + batch flush | Collect writes, flush periodically | Stale data + data loss on crash | ~10,000 writes/sec |
| Single Redis key (INCR) | Atomic increment in cache | Single key serialization ceiling | ~200,000 writes/sec |
| **Sharded Counters** | Split into N independent shards | — | N × shard_limit |

---

## 5. Design Roadmap

The sharded counters design is explored in two parts:

---

### Part 1 — High-Level Design

```
Covers:
  High-level architecture:
    How sharding eliminates the single serialization point
    How writes are distributed across shards
    How the total count is aggregated from shards

  API design:
    How clients increment counters
    How clients read the total count
```

---

### Part 2 — Detailed Design and Evaluation

```
Covers:
  Implementation details:
    How many shards to use?
    How are writes routed to shards?
    How is aggregation performed efficiently?
    How are shards managed and rebalanced?

  Evaluation:
    Does the design meet throughput requirements?
    How does it handle failures?
    What are the consistency trade-offs?
```

---

### Design Roadmap at a Glance

```
Part 1                    Part 2
──────────────────────    ──────────────────────────────────
High-Level Design      -> Detailed Design + Evaluation
(architecture + API)      (implementation + trade-offs)
```

---

## 6. Summary

```
THE PROBLEM:
  Social platforms process billions of concurrent write operations
  (likes, views, comments) targeting the same counters simultaneously.

  Heavy hitters: a small number of posts/videos receive
  disproportionately high traffic (100,000+ writes/second to one counter).

  Hot partitions: all writes target one database shard,
  leaving other shards idle while the hot shard collapses.

WHY TRADITIONAL COUNTERS FAIL:
  Locking:          Write queue grows non-linearly -> throughput collapses
  Optimistic CAS:   99% retries under high concurrency -> CPU consumed by retries
  Memory buffering: Stale data + data loss on crash -> not suitable for exact counts
  Single Redis key: Serialization ceiling even with fast cache

ROOT CAUSE:
  One counter = one serialization point
  Any single serialization point has a throughput ceiling
  At high enough concurrency, the system spends more time managing
  contention than doing actual work

THE SOLUTION: SHARDED COUNTERS
  Split one counter into N independent shards
  Each shard accepts a fraction of the writes (no contention between shards)
  Total count = sum of all shard values
  Throughput scales linearly with N (add shards, add capacity)
  Eliminates the single serialization point entirely
```

---

