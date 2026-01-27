# Evaluation of a Distributed Cache's Design

Let's evaluate our design in the context of our requirements and understand how to handle high-concurrency scenarios.

## Table of Contents

- [Requirements Compliance](#requirements-compliance)
- [High Performance](#high-performance)
- [Scalability](#scalability)
- [High Availability](#high-availability)
- [Consistency](#consistency)
- [Affordability](#affordability)
- [Handling Concurrent Requests in LRU Cache](#handling-concurrent-requests-in-lru-cache)
- [Common Questions and Answers](#common-questions-and-answers)
- [Summary](#summary)

---

## Requirements Compliance

Let's evaluate our proposed design according to the design requirements.

| Requirement | Status | Grade |
|-------------|--------|-------|
| High Performance | ✅ Achieved | A+ |
| Scalability | ✅ Achieved | A |
| High Availability | ⚠️ Partially Achieved | B+ |
| Consistency | ⚠️ Trade-off Required | B |
| Affordability | ✅ Achieved | A+ |

---

## High Performance

Here are some design choices we made that will contribute to overall good performance:

### Design Choices for Performance

#### 1. Consistent Hashing
**Complexity**: Finding a key under this algorithm requires a time complexity of **O(log(N))**, where N represents the number of cache shards.

**Process:**
```
Key lookup with consistent hashing:
1. Hash the key: O(1)
2. Binary search on sorted hash ring: O(log N)
Total: O(log N)

Example:
10 cache shards → log₂(10) ≈ 3.3 comparisons
100 cache shards → log₂(100) ≈ 6.6 comparisons

Very efficient even with many shards!
```

---

#### 2. Hash Table Lookup
**Complexity**: Inside a cache server, keys are located using hash tables that require **constant time on average**: **O(1)**.

**Operation Times:**
```
Hash table operations:
- Insert: O(1) average
- Lookup: O(1) average
- Delete: O(1) average

Real-world: ~50-100 nanoseconds
```

---

#### 3. LRU Eviction
**Complexity**: The LRU eviction approach uses a **constant time** to access and update cache entries in a doubly linked list: **O(1)**.

**Operations:**
```
LRU operations (all O(1)):
- Move node to head: O(1)
- Remove from tail: O(1)
- Insert at head: O(1)

No iteration needed!
```

---

#### 4. Fast Network Protocols
The communication between cache clients and servers is done through **TCP and UDP protocols**, which is also very fast.

**Latency Comparison:**
```
Protocol overhead:
TCP: ~1-2ms (reliable)
UDP: ~0.5-1ms (fast, less reliable)

Compared to:
Database query: 10-100ms
Disk read: 5-10ms
```

---

#### 5. Read Replicas
Since we added more replicas, these can reduce the performance penalties that we have to face if there's a high request load on a single machine.

**Load Distribution:**
```
Without replicas:
Single server: 100,000 req/s → OVERLOAD

With 1 primary + 2 replicas:
Primary: 33,000 req/s
Replica 1: 33,000 req/s
Replica 2: 34,000 req/s
Total: 100,000 req/s → Balanced!
```

---

#### 6. RAM-Based Storage
An important feature of the design is adding, retrieving, and serving data from the **RAM**. Therefore, the latency to perform these operations is quite low.

**Storage Latency:**
```
RAM:  ~100 nanoseconds
SSD:  ~150 microseconds (1,500x slower)
HDD:  ~10 milliseconds (100,000x slower)

RAM is essential for cache performance!
```

---

### Critical Parameter: Eviction Algorithm

**Note**: A critical parameter for high performance is the selection of the eviction algorithm because the number of cache hits and misses depends on it. The higher the cache hit rate, the better the performance.

---

### Performance Calculation Example

To get an idea of how important the eviction algorithm is, let's assume the following:

**Assumptions:**
- Cache hit service time (99.9th percentile): **5 ms**
- Cache miss service time (99.9th percentile): **30 ms** (this includes time to get the data from the database and set the cache)

**Scenario Comparison:**

| Algorithm | Cache Hit Rate | Cache Miss Rate |
|-----------|----------------|-----------------|
| MFU (Most Frequently Used) | 90% | 10% |
| LRU (Least Recently Used) | 95% | 5% |

---

### Effective Access Time Formula

```
EAT = (Ratio_hit × Time_hit) + (Ratio_miss × Time_miss)
```

**Where:**
- **EAT**: Effective access time
- **Ratio_hit**: The percentage of times a cache hit will occur
- **Ratio_miss**: The percentage of times a cache miss will occur
- **Time_hit**: Time required to serve a cache hit
- **Time_miss**: Time required to serve a cache miss

---

### Calculation for MFU Algorithm

```
EAT = (0.90 × 5 ms) + (0.10 × 30 ms)
    = 4.5 ms + 3.0 ms
    = 7.5 ms
```

**Result**: Average request takes **7.5 milliseconds** with MFU

---

### Calculation for LRU Algorithm

```
EAT = (0.95 × 5 ms) + (0.05 × 30 ms)
    = 4.75 ms + 1.5 ms
    = 6.25 ms
```

**Result**: Average request takes **6.25 milliseconds** with LRU

---

### Impact Analysis

```
Performance improvement with LRU:
7.5 ms (MFU) → 6.25 ms (LRU)

Improvement: (7.5 - 6.25) / 7.5 = 16.7% faster!

For 100,000 requests/second:
MFU: Can handle ~133,000 req/s
LRU: Can handle ~160,000 req/s

27,000 more requests/second just by choosing better eviction!
```

---

### Key Insight

**The numbers above highlight the importance of the eviction algorithm to increase the cache hit rate.**

Each application should conduct an **empirical study** to determine the eviction algorithm that gives better results for a specific workload.

**Recommendation:**
```
Test different algorithms with real workload:
1. Run A/B test with LRU vs LFU
2. Measure cache hit rate
3. Measure latency (P50, P99, P99.9)
4. Choose algorithm with best metrics
```

---

## Scalability

Our design supports excellent scalability through several mechanisms.

### Horizontal Scaling with Sharding

We can create shards based on requirements and changing server loads.

**Example:**
```
Current load: 100,000 req/s with 3 shards
Each shard: ~33,000 req/s

Load increases to 300,000 req/s:
Add 6 more shards → 9 shards total
Each shard: ~33,000 req/s

Linear scalability!
```

---

### Consistent Hashing Benefits

While we add new cache servers to the cluster, we also have to do a **limited number of rehash computations**, thanks to consistent hashing.

**Traditional Hashing (Bad):**
```
3 servers → 4 servers:
Keys to remap: ~75% of all keys
Data movement: MASSIVE

Example:
hash(key) % 3 → Server index changes for most keys
hash(key) % 4 → Different server!
```

**Consistent Hashing (Good):**
```
3 servers → 4 servers:
Keys to remap: ~25% of keys (only from affected ranges)
Data movement: MINIMAL

With virtual nodes:
Even better distribution!
```

---

### Handling Hot Shards

Adding replicas reduces the load on hot shards.

**Before Replication:**
```
Shard A: 10,000 req/s (normal)
Shard B: 100,000 req/s (HOTKEY!)
Shard C: 10,000 req/s (normal)

Shard B is overloaded!
```

**After Adding Replicas:**
```
Shard B Primary: 33,000 req/s
Shard B Replica 1: 33,000 req/s
Shard B Replica 2: 34,000 req/s

Total: 100,000 req/s → Load distributed!
```

---

### Handling Single Hotkey Problem

Another way to handle the hotkeys problem is to do **further sharding within the range of those keys**.

**Problem:**
```
Single key "trending_video:12345":
- 100,000 requests/second
- All go to one shard
- Single server bottleneck
```

**Solutions:**

#### Solution 1: Dynamic Replication
```
Detect hotkey:
  If key receives > 10,000 req/s:
    Replicate to 10 additional servers
    Distribute reads across replicas
```

#### Solution 2: Local Caching
```
Cache client intelligence:
  For detected hotkeys:
    Cache locally in application memory
    Refresh every 1 second
    99% of requests served locally
```

#### Solution 3: Key Suffix Randomization
```
Instead of: "trending_video:12345"
Write to:   "trending_video:12345_0"
            "trending_video:12345_1"
            "trending_video:12345_2"
            
Read from random suffix:
  Distributes load across multiple keys
```

---

### Important Note

Although the scenario where a single key will become hot is rare, it's possible for the cache client to devise solutions to avoid the single hotkey contention issue.

**Example Solutions:**
- Cache clients can intelligently avoid such a situation that a single key becomes a bottleneck
- We can use dynamic replication for specific keys
- And so on

Nonetheless, the solutions are complex and beyond the scope of this lesson.

---

## High Availability

We have improved the availability through redundant cache servers. Redundancy adds a layer of reliability and fault tolerance to our design.

### Current Availability Architecture

We used the **leader-follower algorithm** to conveniently manage a cluster shard.

**Structure:**
```
Shard A:
  Leader (Primary)
  Follower (Replica 1)
  Follower (Replica 2)
  
Writes: Leader only
Reads: Leader + Followers
```

---

### Current Limitations

However, we **haven't achieved high availability** because we have two shard replicas, and at the moment, we assume that the replicas are within a data center.

**Single Data Center Risk:**
```
All servers in Data Center A:
  Leader: DC-A
  Replica 1: DC-A
  Replica 2: DC-A
  
If Data Center A fails:
  ❌ Complete shard unavailable
  ❌ All replicas lost
```

**Availability Calculation:**
```
Single data center availability: 99.9%
Downtime: ~8.7 hours/year

Even with replicas in same DC:
Still limited by DC availability!
```

---

### Achieving Higher Availability

It's possible to achieve higher availability by **splitting the leader and follower servers among different data centers**.

**Multi-Data Center Setup:**
```
Shard A:
  Leader: Data Center A (US-East)
  Follower 1: Data Center B (US-West)
  Follower 2: Data Center C (EU-West)
  
If DC-A fails:
  ✅ Promote Follower 1 to Leader
  ✅ System continues
  ✅ Only ~5 seconds of disruption
```

**Improved Availability:**
```
Multi-DC availability: 99.99%+
Downtime: <1 hour/year

Massive improvement!
```

---

### The Trade-off: Consistency vs Availability

But such high availability comes at a price of **consistency**.

**The Problem:**

We assumed **synchronous writes** within the same data center:
```
Same DC (low latency):
  Write to Leader: 1ms
  Replicate to Follower: 2ms
  Total: 3ms ✅ Acceptable
```

But **synchronous writing for strong consistency** in different data centers has a serious performance implication:
```
Cross-DC (high latency):
  Write to Leader (US-East): 1ms
  Replicate to Follower (EU-West): 100ms (network latency)
  Total: 101ms ❌ Too slow for cache!
```

This isn't welcomed in caching systems.

---

### Solution: Asynchronous Replication Across Data Centers

We usually use **asynchronous replication** across data centers.

**Process:**
```
Write operation:
1. Write to leader (Data Center A)
2. Acknowledge to client immediately
3. Asynchronously replicate to other DCs

Client sees: 5ms latency ✅
Replication happens in background
```

**Trade-off:**
```
Performance: ✅ Excellent (5ms writes)
Availability: ✅ High (multi-DC)
Consistency: ⚠️ Eventual (replicas may lag by 100-500ms)
```

---

### Replication Strategy Summary

#### Within Data Center
For replication within the data center, we can get **strong consistency** with good performance.

```
Synchronous replication:
- Low latency (1-3ms)
- Strong consistency
- All replicas up-to-date
✅ Best for same-DC replicas
```

#### Across Data Centers
We can compromise strong consistency across data center replication to achieve better availability.

```
Asynchronous replication:
- Low write latency (5ms)
- High availability
- Eventual consistency (100-500ms lag)
✅ Best for cross-DC replicas
```

---

### CAP and PACELC Theorems

See **CAP** and **PACELC** theorems for more on these trade-offs.

**CAP Theorem:**
```
Can only have 2 of 3:
- Consistency
- Availability
- Partition tolerance

Distributed cache chooses:
✅ Availability
✅ Partition tolerance
⚠️ Eventual Consistency
```

**PACELC Theorem:**
```
In case of Partition: Choose A or C
Else (normal operation): Choose Latency or Consistency

Distributed cache:
- Partition: Choose Availability
- Normal: Choose Latency (async replication)
```

---

## Consistency

It's possible to write data to cache servers in a synchronous or asynchronous mode.

### Asynchronous Mode (Favored)

In the case of caching, the **asynchronous mode is favored** for improved performance.

**Characteristics:**
```
Write operation:
1. Write to primary
2. Return success immediately
3. Replicate asynchronously

Latency: 5ms ✅
Consistency: Eventual ⚠️
```

**Consequence**: Our caching system **suffers from inconsistencies**.

**Example of Inconsistency:**
```
Time T0:
  Primary: user:101 = "Mumbai"
  Replica: user:101 = "Delhi" (not yet updated)
  
Client A reads from Primary → "Mumbai" ✅
Client B reads from Replica → "Delhi" ❌ (stale)

Time T0 + 100ms:
  Replication completes
  Both: user:101 = "Mumbai" ✅
```

---

### Synchronous Mode (Strong Consistency)

Alternatively, **strong consistency** comes from synchronous writing, but this increases the overall latency, and the performance takes a hit.

```
Write operation:
1. Write to primary
2. Wait for all replicas to acknowledge
3. Return success

Latency: 50-100ms ❌ (Too slow for cache!)
Consistency: Strong ✅
```

---

### Inconsistency from Faulty Systems

Inconsistency can also arise from **faulty configuration files and services**.

**Problem Scenario:**
```
Timeline:
T0: Cache server goes down during write
T1: Write operation fails on that server
T2: Server recovers (stale data)
T3: Read operation hits recovered server
T4: Returns stale data ❌
```

**Example:**
```
1. Write "user:101 = Mumbai" to 3 servers
2. Server 2 is down, doesn't get update
3. Server 2 recovers with old data "user:101 = Delhi"
4. Client reads from Server 2 → Gets "Delhi" (stale!)
```

---

### Solution: Prevent Serving Until Up-to-Date

We can avoid such scenarios for any joining or rejoining server by **not allowing it to serve requests** until it's reasonably sure that it's up to date.

**Recovery Process:**
```
Server comes online:
1. Mark as "RECOVERING"
2. Sync with leader:
   - Get all keys
   - Get all values
   - Verify checksums
3. Complete sync (may take 1-5 minutes)
4. Mark as "READY"
5. Start serving requests
```

**Configuration Service Role:**
```
Configuration Service:
  Monitors server status
  Only routes traffic to "READY" servers
  Prevents stale reads during recovery
```

---

## Affordability

Our proposed design has a **low cost** because it's feasible and practical to create such a system using **commodity hardware**.

### Cost Breakdown

**Commodity Hardware Approach:**
```
Per Server:
- RAM: 64 GB DDR4 @ $200
- CPU: 8-core @ $300
- Network: 10 Gbps @ $100
- Total: ~$600 per server

10 servers: $6,000
Handles: 1 million req/s
Cost per million req/s: $6,000 ✅ Affordable!
```

**vs. Specialized Hardware:**
```
Per Server:
- High-end RAM: 256 GB @ $2,000
- Enterprise CPU: 32-core @ $3,000
- High-speed network: $1,000
- Total: ~$6,000 per server

2 servers: $12,000
Handles: 1 million req/s
Cost per million req/s: $12,000 ❌ 2x expensive!
```

---

### Horizontal Scaling Economics

**Key Advantage**: Horizontal scaling with commodity hardware is more cost-effective.

```
Need 10x capacity?

Commodity approach:
100 servers × $600 = $60,000
✅ Linear cost scaling
✅ Easy to replace failures
✅ Standard parts

Specialized approach:
20 servers × $6,000 = $120,000
❌ 2x more expensive
❌ Vendor lock-in
❌ Expensive replacements
```

---

## Handling Concurrent Requests in LRU Cache

**Critical Problem**: Assume that a large number of concurrent requests are forwarded to a single shard server. If the server uses the LRU algorithm, how do we handle concurrent access without killing performance?

### Why This Is a Problem

#### What LRU Cache Actually Does

An LRU cache typically has:

**1. HashMap** → O(1) lookup by key

**2. Doubly Linked List** → tracks recency
- **Head** = most recently used
- **Tail** = least recently used

**For every request:**
- **Cache hit** → move node to head
- **Cache miss** → insert new node + evict tail

**➡️ Even reads modify the structure**

This is the key problem.

---

### What Goes Wrong with Naïve Locking

If you do:

```java
synchronized(cache) {
   // read or write
}
```

**Result:**
```
1,000 concurrent requests
Only 1 thread works at a time
999 threads wait

Result:
❌ Terrible throughput
❌ High latency (999ms wait for thread #1000)
❌ CPU underutilized (only 1 core busy)
```

**Example:**
```
Each operation takes 1ms
Without locking: 1,000 req/ms = 1 million req/s
With naïve locking: 1 req/ms = 1,000 req/s

999x slower!
```

So we need **concurrency without killing correctness**.

---

### Core Idea: Not All Operations Need the Same Lock

Different operations touch different parts of the cache.

Think of the cache as **two separate concerns**:

1. **Key → value lookup** (Map)
2. **Recency ordering** (List)

That insight leads to better designs.

---

## Solution 1: Fine-Grained Locking

### Idea
Instead of locking the entire cache, lock **only what you touch**.

### Data Structure

```java
ConcurrentHashMap<Key, Node>  // Thread-safe map
DoublyLinkedList with head/tail

Separate locks:
- Map lock (or lock-free via ConcurrentHashMap)
- List lock
- Or even per-node locks
```

---

### Example Flow: Cache Hit

```java
// Thread reads from ConcurrentHashMap
Node node = cache.get(key);  // No global lock needed

if (node != null) {
    // Found! Now update recency
    listLock.lock();
    try {
        // Move node to head
        removeFromList(node);
        addToHead(node);
    } finally {
        listLock.unlock();
    }
}
```

**Critical Section:**
```
Only the linked list modification is locked
Duration: ~10 microseconds
Map access: Completely concurrent
```

---

### Example Flow: Cache Miss

```java
// Read from map → miss
Node node = cache.get(key);  // Concurrent, no lock

if (node == null) {
    // Fetch from database
    value = database.get(key);
    
    listLock.lock();
    try {
        // Evict tail if full
        if (cache.isFull()) {
            Node tail = removeTail();
            cache.remove(tail.key);
        }
        
        // Insert new node at head
        Node newNode = new Node(key, value);
        addToHead(newNode);
        cache.put(key, newNode);
    } finally {
        listLock.unlock();
    }
}
```

---

### Why This Helps

✔ **Readers don't block each other** (concurrent map reads)
✔ **Writers block only the eviction path** (short list lock)
✔ **Lock duration is very small** (~10 microseconds)

**Performance:**
```
Naïve locking: 1,000 req/s
Fine-grained locking: 100,000+ req/s

100x improvement!
```

**Used By:**
- Redis
- Caffeine Cache (Java)
- Guava Cache (Java)

---

## Solution 2: Offline Eviction (Lazy Updates)

### Idea

**Don't update LRU order on every hit**. This is extremely important in high-throughput systems.

### Observation

In real systems:
- Cache hit rate is usually **90%+**
- Updating LRU on every hit causes **contention**
- **Exact LRU** is often not required

So we **relax correctness slightly**.

---

### How Offline Eviction Works

**Instead of this:**
```
Every hit → move node to head (requires lock)
```

**Do this:**
```
Every hit → mark node as "recently used" (no lock)
```

**Then:**

A background thread periodically:
- Cleans up
- Reorders nodes
- Evicts old entries

---

### Example Strategy

**Each entry has:**
```java
class CacheEntry {
    String key;
    Object value;
    long lastAccessTime;  // Updated without lock!
    // No linked list pointers
}
```

**On read (no lock):**
```java
CacheEntry entry = cache.get(key);
if (entry != null) {
    entry.lastAccessTime = System.currentTimeMillis();
    // No lock needed!
}
```

**On eviction (rare):**
```java
// Sample a few candidates (e.g., 5 random entries)
List<CacheEntry> candidates = cache.sample(5);

// Find least recently used among candidates
CacheEntry lru = candidates.min(e -> e.lastAccessTime);

// Evict it
cache.remove(lru.key);
```

---

### This is Called

- **Approximate LRU**
- **Probabilistic eviction**
- **Sampling-based LRU**

---

### Why This Works Well

✔ **Reads are almost lock-free** (just atomic timestamp update)
✔ **Writes are batched** (eviction happens rarely)
✔ **Slightly inaccurate LRU is acceptable** (90-95% as good as perfect LRU)

**Performance:**
```
Fine-grained locking: 100,000 req/s
Approximate LRU: 500,000+ req/s

5x improvement!
```

**Used In:**
- Memcached
- Redis (approximate LRU)
- Most distributed caches

---

### Redis Approximate LRU Example

**Configuration:**
```
maxmemory-policy: allkeys-lru
maxmemory-samples: 5
```

**How it works:**
```
1. Cache is full
2. Need to evict
3. Sample 5 random keys
4. Evict the one with oldest access time

Result:
- 95% as good as perfect LRU
- 10x faster than exact LRU
```

---

## Solution 3: Lock-Free / Wait-Free Implementations

### Idea

Avoid locks entirely using **atomic operations**.

### How This Is Possible

Instead of:
```java
synchronized { ... }
ReentrantLock.lock()
```

Use:
```java
CAS (Compare-And-Swap)
AtomicReference
Non-blocking linked lists
```

---

### High-Level Idea (Simplified)

Each node update uses:

```java
do {
    oldValue = node.get();
    newValue = computeNewValue(oldValue);
} while (!node.compareAndSet(oldValue, newValue));
```

**Process:**
```
Thread 1: Try to update node
Thread 2: Also tries to update same node
One succeeds immediately
Other retries (CAS fails)
No blocking!
```

---

### Why This Scales Well

✔ **No thread blocking** (threads never wait)
✔ **Excellent under high contention** (retries are fast)
❌ **Very complex to implement** (requires deep expertise)
❌ **Hard to reason about correctness** (ABA problem, memory ordering)

---

### Real-World Examples

**Java's ConcurrentHashMap:**
- Uses lock-free techniques for reads
- Minimal locking for writes
- Excellent scalability

**Caffeine Cache:**
- Lock-free admission policy
- Segmented lock-free eviction
- One of the fastest caches available

---

## What Interviewers REALLY Want to Hear

If this is an interview answer, structure it like this:

> "LRU cache under heavy concurrency becomes a **contention hotspot** because even reads modify the structure. Locking the entire cache reduces throughput. To solve this, systems use:
>
> 1. **Fine-grained locking**, where map access and recency updates are separated and only small critical sections are locked.
>
> 2. **Lazy or offline eviction**, where reads do not immediately update LRU order and eviction happens asynchronously or approximately.
>
> 3. **Lock-free or hybrid approaches**, using atomic operations to allow concurrent reads and writes without blocking.
>
> In practice, most production systems prefer **approximate LRU with minimal locking** for better scalability."

That answer signals **real-world experience**, not just theory.

---

## Quick Mental Model to Remember

```
Exact LRU + locks → ❌ slow (1,000 req/s)

Approximate LRU + minimal locking → ✅ scalable (500,000 req/s)

Lock-free LRU → ✅ fastest but complex (1,000,000+ req/s)
```

**For interviews:**
- Mention all three approaches
- Explain trade-offs
- Recommend approximate LRU for production

---

## Common Questions and Answers

### Q1: What happens if the leader node fails in the leader-follower protocol?

**Answer:** Two possible solutions can be used in that case:

#### Solution 1: Leader Election Algorithm
Use the **leader election algorithm** to elect a new leader among the set of available followers.

**Process:**
```
1. Leader fails (detected by heartbeat timeout)
2. Followers detect leader failure
3. Run election algorithm (e.g., Raft, Paxos)
4. Follower with most up-to-date data becomes leader
5. New leader starts accepting writes
6. Other followers sync with new leader

Downtime: ~5-10 seconds
```

**Popular Algorithms:**
- **Raft**: Simple, easy to understand
- **Paxos**: More complex, battle-tested
- **Bully Algorithm**: Simple for small clusters

---

#### Solution 2: Configuration Service
Use a separate **distributed configuration management service** to monitor and select leaders.

**Process:**
```
1. Leader sends heartbeats to Configuration Service
2. Configuration Service detects leader failure
3. Configuration Service selects new leader
4. Notifies all followers of new leader
5. New leader promoted
6. Cache clients updated automatically

Downtime: ~2-5 seconds (faster!)
```

**Services:**
- **ZooKeeper**: Industry standard
- **etcd**: Cloud-native, used by Kubernetes
- **Consul**: Service mesh friendly

**Recommended:** Use configuration service for production (more robust).

---

### Q2: Is it a good idea to introduce synchronous replication to cache servers?

**Answer:** Synchronous replication on the cache clusters will introduce a performance penalty because it takes time to replicate the same data to all the cache servers. Therefore, there's a trade-off between performance and consistency in the usage of asynchronous versus synchronous replication, respectively.

**The answer depends on the use case.**

#### When Synchronous Replication is Good

**Scenario:** Replicated servers are within close proximity of the primary server.

**Example:**
```
All servers in same data center:
  Primary: Rack A
  Replica 1: Rack B (1ms network latency)
  Replica 2: Rack C (1ms network latency)
  
Synchronous replication:
  Write latency: 1ms + 1ms = 2ms ✅ Acceptable!
  Consistency: Strong ✅
```

**Use synchronous when:**
- Replicas in same data center
- Network latency < 5ms
- Strong consistency required
- Acceptable write latency < 10ms

---

#### When Asynchronous Replication is Better

**Scenario:** Replicas across different data centers.

**Example:**
```
Cross-data-center:
  Primary: US-East
  Replica 1: US-West (80ms latency)
  Replica 2: EU-West (120ms latency)
  
Synchronous replication:
  Write latency: 120ms ❌ Too slow for cache!
  
Asynchronous replication:
  Write latency: 2ms ✅ Fast!
  Consistency: Eventual (replicas lag by 100-200ms)
```

**Use asynchronous when:**
- Replicas geographically distributed
- Network latency > 20ms
- Performance more important than consistency
- Acceptable eventual consistency

---

### Q3: How to handle concurrent requests to LRU cache?

**Question:** Assume that a large number of concurrent requests are forwarded to a single shard server. If the server uses the LRU algorithm, some requests will result in cache miss and write, while some will result in cache hit and read. These concurrent requests may compete with each other. Locking the entire data structure will heavily reduce performance. How do you think such a problem will be


md formate don't download form don;t add anything 
"<function_calls>
<invoke name="artifacts">
<parameter name="command">update</parameter>
<parameter name="id">cache_evaluation_guide</parameter>
<parameter name="old_str">Question: Assume that a large number of concurrent requests are forwarded to a single shard server. If the server uses the LRU algorithm, some requests will result in cache miss and write, while some will result in cache hit and read. These concurrent requests may compete with each other. Locking the entire data structure will heavily reduce performance. How do you think such a problem will be</parameter>
<parameter name="new_str">Question: Assume that a large number of concurrent requests are forwarded to a single shard server. If the server uses the LRU algorithm, some requests will result in cache miss and write, while some will result in cache hit and read. These concurrent requests may compete with each other. Locking the entire data structure will heavily reduce performance. How do you think such a problem will be dealt with?
Answer: Generally, for concurrent access to shared data, some locking mechanisms like Semaphore, Monitors, Mutex locks, and others are good choices. But as the number of users (readers in case of cache hit or writers in case of a cache miss) grows, locking the entire data structure isn't a suitable solution.
In that case, we can consider the following options:

Option 1: Limited Locking
In this strategy, only specific sections of the entire data structure will be locked.
How it works:
Instead of:
  lock(entire_cache) → slow

Do:
  lock(only_linked_list) → fast
  ConcurrentHashMap (no lock needed)
Benefits:

While some threads can read from the data structure simultaneously
Some threads may temporarily block access to specific sections
Critical section is minimal (microseconds)

Example:
java// Many threads can do this concurrently:
Node node = hashMap.get(key);  // No lock!

// Only one thread at a time does this:
synchronized(listLock) {
    moveNodeToHead(node);  // Brief lock
}
```

**Performance:**
- Multiple concurrent reads ✅
- Minimal blocking on updates ✅
- 100x faster than full locking ✅

---

#### Option 2: Offline Eviction

Offline eviction may be a possibility where instead of making actual changes to the data structure, **only the required changes will be recorded** while performing different operations until it's necessary to commit the changes.

**How it works:**
```
On cache hit:
  entry.lastAccessTime = now()  // No lock!
  // Don't modify linked list

Periodically or on eviction:
  // Batch update recency order
  // Or sample candidates for eviction
```

**Why this is desirable:**

This solution is **desirable and easy if the cache hit rate is high** because a change to the data structure will likely be required when a cache miss occurs.

**Example:**
```
Cache hit rate: 95%
Cache miss rate: 5%

With offline eviction:
  95% of requests: No lock (just timestamp update)
  5% of requests: Requires lock (eviction)
  
Result: 95% lock-free operations!
Performance:

Almost lock-free reads ✅
Batched writes ✅
500x faster than full locking ✅


Option 3: Lock-Free Implementation
Multiple solutions have been proposed which suggest that simultaneous reading and writing over doubly linked list is feasible to support large number of concurrent reading and writing.
Techniques:

Compare-And-Swap (CAS) operations
Atomic references
Non-blocking algorithms
Wait-free data structures

Example (simplified concept):
javaclass LockFreeLRU {
    AtomicReference<Node> head;
    ConcurrentHashMap<Key, Node> map;
    
    void access(Key key) {
        Node node = map.get(key);
        
        // Lock-free move to head
        do {
            Node currentHead = head.get();
            node.next = currentHead;
        } while (!head.compareAndSet(currentHead, node));
    }
}
```

**Benefits:**
- No thread blocking ✅
- Maximum concurrency ✅
- Excellent scalability ✅

**Challenges:**
- Very complex to implement correctly ❌
- Requires deep expertise ❌
- ABA problem and memory ordering issues ❌

**Performance:**
- 1000x faster than full locking ✅
- But implementation complexity very high ❌

---

### Solution Comparison

| Approach | Complexity | Performance | Lock-Free | Recommended For |
|----------|------------|-------------|-----------|-----------------|
| **Limited Locking** | Low | Good (100x) | Partially | General use |
| **Offline Eviction** | Medium | Excellent (500x) | Mostly | High-throughput |
| **Lock-Free** | Very High | Best (1000x) | Yes | Expert teams |

**Recommendation:** Start with **limited locking**, graduate to **offline eviction** for high-throughput, consider **lock-free** only if you have expertise and extreme performance needs.

---

## Summary

### Design Evaluation Results

| Requirement | Achievement | Details |
|-------------|-------------|---------|
| **High Performance** | ✅ Excellent | O(log N) server lookup, O(1) cache operations, RAM storage |
| **Scalability** | ✅ Excellent | Horizontal scaling, consistent hashing, load balancing |
| **High Availability** | ⚠️ Good | Multi-replica within DC, can extend to multi-DC |
| **Consistency** | ⚠️ Configurable | Synchronous (same DC) or Asynchronous (cross-DC) |
| **Affordability** | ✅ Excellent | Commodity hardware, linear cost scaling |

---

### Key Performance Metrics

**Achieved:**
```
Latency:
- Cache hit: 5ms (P99)
- Cache miss: 30ms (P99)

Throughput:
- Per server: 100,000+ req/s
- With replicas: 300,000+ req/s

Availability:
- Single DC: 99.9%
- Multi-DC: 99.99%+

Scalability:
- Linear horizontal scaling
- Consistent hashing minimizes data movement
```

---

### Critical Design Choices

#### 1. Eviction Algorithm Impact
- LRU vs MFU: **16.7% performance difference**
- Must empirically test for your workload
- Cache hit rate is critical metric

#### 2. Replication Strategy
- **Same DC**: Synchronous (strong consistency, 3ms latency)
- **Cross DC**: Asynchronous (high availability, eventual consistency)

#### 3. Concurrency Handling
- **Naïve locking**: 1,000 req/s ❌
- **Fine-grained locking**: 100,000 req/s ✅
- **Offline eviction**: 500,000 req/s ✅✅
- **Lock-free**: 1,000,000 req/s ✅✅✅ (but complex)

---

### Production Recommendations

**For Most Systems:**
1. Use **LRU** with approximate eviction
2. **Fine-grained locking** or **offline eviction**
3. **Synchronous replication** within data center
4. **Asynchronous replication** across data centers
5. **Commodity hardware** with horizontal scaling

**For High-Throughput Systems:**
1. Use **approximate LRU** with sampling
2. **Offline eviction** with batched updates
3. Consider **lock-free** implementations if expertise available
4. **Aggressive caching** with local L1 cache
5. **Dynamic replication** for hotkeys

---

### Trade-offs Summary
```
Consistency vs Performance:
  Strong consistency (sync): Higher latency
  Eventual consistency (async): Better performance
  → Choose based on use case

Availability vs Consistency:
  Multi-DC sync: Strong consistency, lower availability
  Multi-DC async: High availability, eventual consistency
  → Follow CAP theorem

Exact LRU vs Approximate LRU:
  Exact: Better hit rate, worse performance
  Approximate: Slightly lower hit rate, much better performance
  → Approximate is usually better for production

Lock-based vs Lock-free:
  Lock-based: Simpler, good enough for most cases
  Lock-free: Complex, best for extreme performance needs
  → Start simple, optimize if needed

Document: Evaluation of Distributed Cache Design - Complete Analysis with Concurrency Solutions</parameter><function_calls>
<invoke name="artifacts">
<parameter name="command">update</parameter>
<parameter name="id">cache_evaluation_guide</parameter>
<parameter name="old_str">Question: Assume that a large number of concurrent requests are forwarded to a single shard server. If the server uses the LRU algpastedmd formate19:57I see you've provided the content that's already in the artifact. The document "Evaluation of Distributed Cache Design" is already complete and in markdown format. Let me know if you'd like me to:

Export it as a downloadable file - I can provide instructions on how to copy it
Add more sections - If there's additional content you'd like included
Create a new related document - For example, comparing Redis vs Memcached, or implementation examples
Reorganize the content - If you want a different structure

The current document includes:

✅ Requirements Compliance
✅ High Performance Analysis with calculations
✅ Scalability evaluation
✅ High Availability discussion
✅ Consistency trade-offs
✅ Affor"