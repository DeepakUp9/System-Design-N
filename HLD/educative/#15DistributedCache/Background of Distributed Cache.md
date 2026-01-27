# Background of Distributed Cache

Learn the fundamentals for designing a distributed cache.

## Table of Contents

- [Overview](#overview)
- [Writing Policies](#writing-policies)
- [Eviction Policies](#eviction-policies)
- [Cache Invalidation](#cache-invalidation)
- [Storage Mechanism](#storage-mechanism)
- [Cache Client](#cache-client)
- [Practical Example: How Everything Works Together](#practical-example-how-everything-works-together)

## Overview

The main goal of this chapter is to design a distributed cache. To achieve this goal, we should have substantial background knowledge, mainly on different reading and writing techniques. This lesson will help us build that background knowledge.

### Lesson Structure

| Section | Motivation |
|---------|------------|
| Writing policies | Data is written to cache and databases. The order in which data writing happens has performance implications. We'll discuss various writing policies to help decide which writing policy would be suitable for the distributed cache we want to design. |
| Eviction policies | Since the cache is built on limited storage (RAM), we ideally want to keep the most frequently accessed data in the cache. Therefore, we'll discuss different eviction policies to replace less frequently accessed data with most frequently accessed data. |
| Cache invalidation | Certain cached data may get outdated. We'll discuss different invalidation methods to remove stale or outdated entries from the cache in this section. |
| Storage mechanism | A distributed storage has many servers. We'll discuss important design considerations, such as which cache entry should be stored in which server and what data structure to use for storage. |
| Cache client | A cache server stores cache entries, but a cache client calls the cache server to request data. We'll discuss the details of a cache client library in this section. |

---

## Writing Policies

Often, a cache stores a copy (or part) of data, which is persistently stored in a data store. When we store data to the data store, some important questions arise:

- Where do we store the data first? Database or cache?
- What will be the implication of each strategy for consistency models?

The short answer is: **it depends on the application requirements.**

### 1. Write-Through Cache

**Rule**: Write to cache AND database at the same time (or sequentially).

The write-through mechanism writes on the cache as well as on the database. Writing on both storages can happen concurrently or one after the other. This increases the write latency but ensures strong consistency between the database and the cache.

#### Example Flow

User updates city from Delhi → Mumbai:

```
Client → Cache (update city = Mumbai)
        → Database (update city = Mumbai)
```

**After write:**
```
Cache: user:101 → Mumbai
DB:    user:101 → Mumbai
```

**Read after write:**
```
Client → Cache → Mumbai ✔
```

#### Characteristics

**Pros:**
- Always consistent
- Strong consistency guarantee
- No data loss risk

**Cons:**
- Slower writes (two write operations)
- Cache polluted with rarely-read data
- Higher write latency

#### When to Use

Best for systems where **correctness is more important than speed**:
- Banking balances
- User profiles
- Orders
- Inventory systems
- Financial transactions

---

### 2. Write-Back Cache (Write-Behind)

**Rule**: Write to cache first, database later (asynchronously).

In the write-back cache mechanism, the data is first written to the cache and asynchronously written to the database. Although the cache has updated data, inconsistency is inevitable in scenarios where a client reads stale data from the database. However, systems using this strategy will have small writing latency.

#### Example Flow

User updates city from Delhi → Mumbai:

```
Client → Cache (Mumbai) ✔ Immediate
        → DB (later, async)
```

**Immediate state:**
```
Cache: Mumbai ✔
DB:    Delhi ❌ (OLD!)
```

**Read after write:**
```
Client → Cache → Mumbai ✔
```

**But another service reads DB directly:**
```
Another Service → DB → Delhi ❌ (stale)
```

#### Failure Case (Critical)

If cache crashes before DB update:
```
Update LOST forever ❌
```

#### Characteristics

**Pros:**
- Fast writes
- Low latency
- Reduced database load

**Cons:**
- Eventual consistency only
- Risk of data loss
- Stale reads from database

#### When to Use

Best for systems where **speed is more important than perfect consistency**:
- Metrics collection
- Logging systems
- Counters and analytics
- Page view counts
- Real-time statistics

---

### 3. Write-Around Cache

**Rule**: Write ONLY to database, skip cache entirely.

This strategy involves writing data to the database only. Later, when a read is triggered for the data, it's written to cache after a cache miss. The database will have updated data, but such a strategy isn't favorable for reading recently updated data.

#### Example Flow

User updates city:

```
Client → DB (Mumbai) ✔
Cache → unchanged (or empty)
```

**First read after write:**
```
Client → Cache MISS
       → DB → Mumbai
       → Cache filled (Mumbai)
```

#### Characteristics

**Pros:**
- Avoids cache pollution
- Database always has latest data
- Good for write-heavy workloads

**Cons:**
- Slow read right after write
- Cache miss penalty
- Higher read latency initially

#### When to Use

Best for:
- Write-heavy systems
- Data rarely read immediately after update
- Bulk data imports
- Log aggregation

---

### Writing Policy Comparison

| Policy | Cache Updated on Write? | Consistency | Speed | Use Case |
|--------|------------------------|-------------|-------|----------|
| Write-through | Yes (immediately) | Strong | Slower | Banking, Orders, Critical Data |
| Write-back | Yes (DB later) | Eventual | Fast | Metrics, Logs, Analytics |
| Write-around | No | Strong | Medium | Write-heavy, Bulk Operations |

---

## Eviction Policies

One of the main reasons caches perform fast is that they're small. Small caches mean limited storage capacity. Therefore, we need an eviction mechanism to remove less frequently accessed data from the cache.

**Key Concept**: Eviction happens when cache is FULL and we need space for new data.

### Example Setup

Assume:
```
Cache size = 3 users
Currently stored: user:101, user:102, user:103
New request: user:104
```

Someone must be evicted to make room.

---

### 1. Least Recently Used (LRU)

**Rule**: Remove the item that hasn't been accessed for the longest time.

#### Example

Access pattern:
```
101 → 102 → 101 → 103
```

Access timeline:
```
user:103 - accessed 1 second ago
user:101 - accessed 2 seconds ago
user:102 - accessed 5 seconds ago (LEAST RECENT)
```

**Result**: Evict `user:102`

#### Why LRU is Popular

Based on the principle: **"Recently used data is likely to be used again"**

#### When to Use

- Web applications
- API responses
- Session data
- General-purpose caching

#### Used By

- Redis (default)
- Memcached
- Most web caches

---

### 2. Least Frequently Used (LFU)

**Rule**: Remove the item that has been accessed the fewest times.

#### Example

Usage count:
```
user:101 → accessed 10 times
user:102 → accessed 2 times (LEAST FREQUENT)
user:103 → accessed 5 times
```

**Result**: Evict `user:102`

#### When to Use

Best for:
- Hot keys (some keys are always popular)
- Recommendation engines
- Content delivery
- Video streaming

---

### 3. Most Recently Used (MRU)

**Rule**: Remove the item that was most recently accessed.

#### Example

Access pattern:
```
101 → 102 → 103 (MOST RECENT)
```

**Result**: Evict `user:103`

#### When to Use

Useful for:
- Sequential scans (database queries)
- Batch processing jobs
- One-time data processing

---

### 4. Most Frequently Used (MFU)

**Rule**: Remove the item that has been accessed the most times.

#### When to Use

Rare use cases:
- When popular items become obsolete
- Cyclic data patterns

---

### 5. First In First Out (FIFO)

**Rule**: Remove the oldest inserted item, regardless of access pattern.

#### Example

Insertion order:
```
user:101 (inserted first) ← EVICT THIS
user:102
user:103
```

**Result**: Evict `user:101`

#### Characteristics

- Simple to implement
- Often inefficient
- Ignores access patterns

---

### Eviction Policy Comparison

| Policy | Criteria | Best For | Complexity |
|--------|----------|----------|------------|
| LRU | Time since last access | General-purpose | Medium |
| LFU | Access frequency | Hot keys, CDN | High |
| MRU | Most recent access | Sequential scans | Medium |
| MFU | Highest access count | Rare use cases | High |
| FIFO | Insertion order | Simple systems | Low |

---

## Cache Invalidation

**Key Concept**: Eviction ≠ Invalidation

- **Eviction**: Removing data due to lack of space
- **Invalidation**: Removing data because it's wrong or outdated

Apart from the eviction of less frequently accessed data, some data residing in the cache may become stale or outdated over time. Such cache entries are invalid and must be marked for deletion.

### The Stale Data Problem

Example of stale data:
```
Cache: user:101 → Delhi (OLD!)
DB:    user:101 → Mumbai (CORRECT)
```

The cache is WRONG and must be invalidated.

### How to Identify Stale Entries?

Resolution of the problem requires storing metadata corresponding to each cache entry. Specifically, maintaining a **time-to-live (TTL)** value to deal with outdated cache items.

---

### TTL (Time To Live)

Each cache entry stores metadata:

```
Key: user:101
Value: Mumbai
TTL: 10 minutes
Created: 2026-01-16 10:00:00
```

After 10 minutes → entry is expired

---

### Invalidation Approaches

We can use two different approaches to deal with outdated items using TTL:

#### 1. Active Expiration

**Rule**: Background process actively checks and removes expired entries.

```
Background Thread:
  Every 1 second:
    Scan cache
    Delete expired keys
```

**Pros:**
- Frees memory early
- Prevents serving stale data
- Proactive cleanup

**Cons:**
- CPU overhead
- Continuous background processing
- May impact performance

---

#### 2. Passive Expiration (Lazy Expiration)

**Rule**: Check TTL only when an entry is accessed.

```
Client → Cache (request user:101)
  Check TTL
  If expired:
    Delete entry
    Return MISS
  Else:
    Return value
```

**Pros:**
- Simple implementation
- No background threads
- Efficient CPU usage

**Cons:**
- Expired items remain in memory
- Memory not freed immediately
- Requires access to detect expiration

**Most Common**: Used by Redis, Memcached

---

### Cache Invalidation Strategies

| Strategy | When Checked | Memory Efficiency | CPU Usage | Best For |
|----------|--------------|-------------------|-----------|----------|
| Active | Background thread | High | High | Critical systems |
| Passive | On access | Medium | Low | General-purpose |
| Hybrid | Both | High | Medium | Production systems |

---

### Example Client Flow

```
Application → Cache Client Library
                ↓
            Determine server (consistent hashing)
                ↓
            Send request to Cache Server
                ↓
            Receive response
                ↓
            Return to Application
```

### Client Libraries

- **Redis**: redis-py, node-redis, Jedis
- **Memcached**: pylibmc, node-memcached
- **Custom**: Built for specific needs

---

## Practical Example: How Everything Works Together

### Real System: User Profile Service

Let's see how all concepts work together in a real application.

#### System Requirements

- **Service**: User profile management
- **Data**: User profiles with name, city, email
- **Traffic**: 10,000 requests/second
- **Consistency**: Profiles must be accurate

#### Design Decisions

**1. Writing Policy**: Write-through
- Why? Profile data must be correct
- Downside: Slightly slower writes (acceptable)

**2. Eviction Policy**: LRU
- Why? Recently viewed users are hot (active users)
- Size: Cache holds 100,000 profiles

**3. Invalidation**: TTL + Passive expiration
- TTL: 1 hour
- Why? Old profiles expire naturally
- Passive: Check on access (efficient)

#### Complete Flow Example

**Scenario 1: User Updates Profile**

```
1. User changes city: Delhi → Mumbai

2. Write-through cache:
   Client → Cache (user:101 = Mumbai)
          → Database (user:101 = Mumbai)

3. Both updated immediately

4. Next read:
   Client → Cache HIT → Mumbai ✔
```

**Scenario 2: Cache Full, New User Profile Needed**

```
1. Cache at capacity: 100,000 profiles

2. Request for user:200001

3. LRU eviction:
   - Find least recently used: user:500
   - Evict user:500
   - Load user:200001

4. Serve user:200001
```

**Scenario 3: Stale Data After 1 Hour**

```
1. user:101 cached at 10:00 AM

2. Current time: 11:05 AM (65 minutes later)

3. User requests profile:
   Client → Cache
          → Check TTL (expired!)
          → Delete entry
          → Cache MISS
          → Load from DB
          → Cache new entry with fresh TTL

4. Serve fresh data
```

---

### Why This Combination Works

| Decision | Reason | Benefit |
|----------|--------|---------|
| Write-through | Correctness critical | No stale reads |
| LRU eviction | Access patterns predictable | High hit rate |
| Passive invalidation | Low CPU overhead | Efficient resources |
| 1-hour TTL | Profiles don't change often | Balance freshness/performance |

---

## Summary

### Key Takeaways

1. **Writing Policies** determine when and where data is written:
   - Write-through: Strong consistency, slower
   - Write-back: Fast, eventual consistency
   - Write-around: Avoid cache pollution

2. **Eviction Policies** manage limited cache space:
   - LRU: Most common, works well for most cases
   - LFU: Best for predictable hot keys
   - Choose based on access patterns

3. **Cache Invalidation** handles stale data:
   - TTL: Time-based expiration
   - Active: Proactive cleanup
   - Passive: On-access checking

4. **Real Systems** combine all three:
   - Match policies to requirements
   - Monitor and tune based on metrics
   - Balance consistency, speed, and resources

---

# Storage Mechanism for Distributed Cache

A comprehensive guide to understanding how data is stored and managed in distributed cache systems.

## Table of Contents

- [Introduction](#introduction)
- [Core Design Questions](#core-design-questions)
- [Hash Functions in Distributed Cache](#hash-functions-in-distributed-cache)
- [Data Structures for Cache Storage](#data-structures-for-cache-storage)
- [Sharding in Cache Clusters](#sharding-in-cache-clusters)
- [Cache Client](#cache-client)
- [Summary](#summary)

---

## Introduction

Storing data in the cache isn't as trivial as it seems because the distributed cache has multiple cache servers. The storage mechanism is a critical design component that directly impacts the performance of the distributed cache system.

---

## Core Design Questions

When we use multiple cache servers, the following design questions need to be answered:

### Question 1: Data Distribution
**Which data should we store in which cache servers?**

This question addresses:
- How to distribute data across multiple servers
- How to locate data when needed
- How to handle server failures
- How to scale the system

### Question 2: Data Structure
**What data structure should we use to store the data?**

This question addresses:
- How to organize data within each cache server
- How to implement eviction policies efficiently
- How to optimize read/write operations
- How to manage memory effectively

The above two questions are important design issues because they'll decide the performance of our distributed cache, which is our most important requirement.

---

## Hash Functions in Distributed Cache

Hash functions are used in distributed caching systems in two different scenarios:

### Scenario 1: Cache Server Selection

**Purpose**: Identify which cache server should store and retrieve specific data in a distributed cache.

#### Simple Hashing (Not Recommended)

```
server_index = hash(key) % number_of_servers
```

**Problems with Simple Hashing:**
- When a server crashes, all keys need to be remapped
- When adding a new server, massive data redistribution occurs
- Not ideal for dynamic environments

**Example:**
```
Initial: 3 servers
hash("user:101") % 3 = 1 → Server 1
hash("user:102") % 3 = 2 → Server 2

Server 2 crashes, now 2 servers:
hash("user:101") % 2 = 1 → Server 1 (same ✓)
hash("user:102") % 2 = 0 → Server 0 (different! ✗)
```

Almost all keys need to be remapped!

---

#### Consistent Hashing (Recommended)

**Why Consistent Hashing?**

Consistent hashing or its flavors usually perform well in distributed systems because they minimize data movement when servers are added or removed.

**How It Works:**

1. Hash both servers and keys to a hash ring (0 to 2^32-1)
2. Each key is assigned to the next server clockwise on the ring
3. When a server fails, only its keys are redistributed to the next server

**Benefits:**
- **Minimal redistribution**: Only K/N keys need to be remapped (K = total keys, N = servers)
- **Scalability**: Easy to add/remove servers
- **Fault tolerance**: Graceful degradation when servers fail
- **Load balancing**: Virtual nodes can distribute load evenly

**Example:**
```
Hash Ring (simplified):
    0° ────────────── 90° ────────────── 180° ────────────── 270°
    Server A          Server B           Server C

Key "user:101" hashes to 45° → Stored on Server B (next clockwise)
Key "user:102" hashes to 200° → Stored on Server C

If Server B fails:
Key "user:101" → Now stored on Server C
Only keys between Server A and Server B are affected!
```

---

### Scenario 2: Cache Entry Location

**Purpose**: Locate cache entries inside each cache server for read/write operations.

In this scenario, we can use typical hash functions to locate a cache entry to read or write inside a cache server.

```
entry_location = hash(key) % bucket_size
```

**Important Note:**

A hash function alone can only locate a cache entry. It doesn't address:
- How to manage data within the cache server
- How to implement eviction strategies (LRU, LFU, etc.)
- What data structures to use for storing data

This leads us to the next section on data structures.

---

### Comparison: Consistent Hashing vs. Simple Hashing

| Aspect | Simple Hashing | Consistent Hashing |
|--------|----------------|-------------------|
| Formula | `hash(key) % N` | Hash ring with virtual nodes |
| Keys moved when adding server | ~K * (N-1)/N | ~K/N |
| Keys moved when removing server | ~K * (N-1)/N | ~K/N |
| Load balancing | Uneven without careful planning | Good with virtual nodes |
| Scalability | Poor | Excellent |
| Fault tolerance | Poor | Excellent |
| Implementation complexity | Simple | Moderate |

**Key Insight**: Consistent hashing is preferred because it improves scalability and fault tolerance by minimizing data redistribution during server changes.

---

## Data Structures for Cache Storage

### Doubly Linked List

We'll use a **doubly linked list** as the primary data structure for cache storage.

#### Why Doubly Linked List?

**Main Reasons:**
1. **Widespread usage and simplicity**: Well-understood and easy to implement
2. **Constant time operations**: Adding and removing data is O(1)
3. **No iterations required**: Direct access to head and tail

#### How It Works with Cache

```
HEAD                                                    TAIL
(Most Recently Used)                        (Least Recently Used)

[Entry 1] ←→ [Entry 2] ←→ [Entry 3] ←→ [Entry 4] ←→ [Entry 5]
```

**Operations:**

1. **Cache Hit** (accessing existing entry):
   - Remove entry from current position: O(1)
   - Move to head (most recently used): O(1)

2. **Cache Miss** (adding new entry):
   - Add new entry at head: O(1)

3. **Eviction** (when cache is full):
   - Remove entry from tail (LRU): O(1)

#### Combined with Hash Map

For efficient lookups, we combine doubly linked list with a hash map:

```
Hash Map                    Doubly Linked List
┌────────────┐             HEAD ←→ ←→ ←→ ←→ TAIL
│ key:101 ───┼────────────→ [Entry 101]
│ key:102 ───┼────────────→ [Entry 102]
│ key:103 ───┼────────────→ [Entry 103]
└────────────┘
```

**Benefits:**
- Hash map: O(1) lookup
- Linked list: O(1) insertion/deletion
- Combined: O(1) for all operations!

#### Example: LRU Cache Implementation

```
Structure:
- Hash Map: {key → node reference}
- Doubly Linked List: maintains access order

Access "user:101":
1. Hash map lookup: O(1) → find node
2. Remove from current position: O(1)
3. Move to head: O(1)

Total: O(1) operation
```

---

### Bloom Filters (Optional Optimization)

**Bloom filters** are an interesting choice for quickly finding if a cache entry doesn't exist in the cache servers.

#### How Bloom Filters Help Caching

**Use Case**: Before checking the cache server, use a Bloom filter to determine if the key might exist.

```
Request for "user:999"
    ↓
Check Bloom Filter
    ↓
If "definitely NOT present":
    Skip cache lookup → Direct DB query
    Saves network call to cache server!
    
If "may be present":
    Check cache server
```


<span style="background-color: yellow; color: blue;">in depth(BloomFilter), <a href="./deapth/bloomFilter.md">click here</a></span>

#### Benefits in Large Systems

- **Quick negative lookups**: Determine a cache entry definitely doesn't exist
- **Reduced network calls**: Avoid unnecessary cache server queries
- **Memory efficient**: Small overhead for large datasets

#### Characteristics

- **Definite negative**: 100% accurate when saying "not present"
- **Probabilistic positive**: May have false positives when saying "present"

**Note**: Bloom filters are quite useful in large caching or database systems.

---

## Sharding in Cache Clusters

To avoid **SPOF (Single Point of Failure)** and high load on a single cache instance, we introduce **sharding**.

**Sharding**: Splitting up cache data among multiple cache servers.

Sharding can be performed in the following two ways:

---

### 1. Dedicated Cache Servers

In the dedicated cache servers method, we **separate the application and web servers from the cache servers**.

#### Architecture

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│ Application  │     │ Application  │     │ Application  │
│  Server 1    │     │  Server 2    │     │  Server 3    │
└──────┬───────┘     └──────┬───────┘     └──────┬───────┘
       │                    │                    │
       └────────────────────┼────────────────────┘
                            │
       ┌────────────────────┼────────────────────┐
       │                    │                    │
┌──────▼───────┐     ┌──────▼───────┐     ┌──────▼───────┐
│    Cache     │     │    Cache     │     │    Cache     │
│   Server 1   │     │   Server 2   │     │   Server 3   │
└──────────────┘     └──────────────┘     └──────────────┘
```

#### Advantages

1. **Hardware Flexibility**: 
   - Choose optimal hardware for each service
   - Cache servers: RAM-optimized
   - Application servers: CPU-optimized

2. **Independent Scaling**:
   - Scale web/application servers separately from cache servers
   - Add cache capacity without touching application layer

3. **Cache as a Service**:
   - Multiple microservices can use the same cache cluster
   - Centralized caching infrastructure

4. **Specialized Optimization**:
   - Tune cache servers specifically for caching workload
   - Different configurations for different needs

#### Multi-Tenant Considerations

When acting as "Cache as a Service," the caching system must be aware of different applications so that their data doesn't collide.

**Solution**: Use namespace prefixing
```
App1: app1:user:101
App2: app2:user:101
```

---

### 2. Co-located Cache

The co-located cache **embeds cache and service functionality within the same host**.

#### Architecture

```
┌─────────────────────────┐
│      Host 1             │
│  ┌─────────────────┐    │
│  │   Application   │    │
│  └─────────────────┘    │
│  ┌─────────────────┐    │
│  │   Cache         │    │
│  └─────────────────┘    │
└─────────────────────────┘

┌─────────────────────────┐
│      Host 2             │
│  ┌─────────────────┐    │
│  │   Application   │    │
│  └─────────────────┘    │
│  ┌─────────────────┐    │
│  │   Cache         │    │
│  └─────────────────┘    │
└─────────────────────────┘
```

#### Advantages

1. **Cost Efficiency**:
   - Reduced CAPEX (Capital Expenditure): No extra hardware
   - Reduced OPEX (Operational Expenditure): Fewer machines to maintain

2. **Automatic Scaling**:
   - Scaling application automatically scales cache
   - No separate scaling decisions needed

3. **Lower Latency**:
   - In-process or localhost access
   - No network overhead

#### Disadvantages

1. **Coupled Failure**:
   - Failure of one service affects both
   - Loss of cache and application simultaneously

2. **Resource Contention**:
   - Cache and application compete for same resources
   - Memory pressure from both services

3. **Limited Flexibility**:
   - Cannot scale services independently
   - Hardware must suit both workloads

---

### Data Temperature Classification

Data can be classified into three temperature regions depending on the access frequency:

```
HOT                    WARM                   COLD
↑                      ↑                      ↑
High Access            Medium Access          Rare Access
Frequency              Frequency              Frequency

Examples:              Examples:              Examples:
- Popular products     - Recent orders        - Old archives
- Trending posts       - User preferences     - Historical data
- Active sessions      - Recommendations      - Deleted items
```

#### Temperature Characteristics

| Temperature | Access Frequency | Cache Priority | Eviction Risk |
|-------------|-----------------|----------------|---------------|
| **Hot** | Very high | Always cached | Very low |
| **Warm** | Moderate | Often cached | Medium |
| **Cold** | Rare | Rarely cached | Very high |

**Cache Behavior**:
- Cold data frequently gets evicted from the cache
- Gets replaced with hot or warm data
- Natural flow from cold → warm → hot based on access patterns

---

### Sharding Strategy Comparison

| Aspect | Dedicated Cache Servers | Co-located Cache |
|--------|------------------------|------------------|
| Hardware flexibility | High | Low |
| Independent scaling | Yes | No |
| Cost | Higher | Lower |
| Latency | Network overhead | Minimal (localhost) |
| Failure impact | Cache only | Both services |
| Multi-tenancy | Easy | Complex |
| Best for | Large systems, microservices | Small systems, monoliths |

---

## Cache Client

**Cache Client**: A piece of code residing in hosting servers that performs hash computations to store and retrieve data in the cache servers.

### What Does a Cache Client Do?

The cache client is responsible for:
1. **Hash calculations**: Determine which cache server to use
2. **Coordination**: Work with monitoring and configuration services
3. **Protocol handling**: Communicate with cache servers
4. **Consistency**: Ensure same operations return same results

### Architecture

```
┌───────────────────────────┐
│   Application Server      │
│                           │
│  ┌─────────────────────┐  │
│  │   Cache Client      │  │
│  │  - Hash calculation │  │
│  │  - Server selection │  │
│  │  - Protocol handler │  │
│  └──────────┬──────────┘  │
└─────────────┼─────────────┘
              │
      ┌───────┼───────┐
      │       │       │
┌─────▼──┐ ┌──▼────┐ ┌▼──────┐
│ Cache  │ │ Cache │ │ Cache │
│Server 1│ │Server2│ │Server3│
└────────┘ └───────┘ └───────┘
```

---

### Characteristics of Cache Clients

#### 1. Complete Server Knowledge

**Each cache client will know about all the cache servers.**

- Maintains list of all cache servers
- Updates when servers are added/removed
- Uses configuration service for discovery

**Example:**
```
Cache Servers: [
  server1.cache.com:6379,
  server2.cache.com:6379,
  server3.cache.com:6379
]
```

---

#### 2. Standard Protocols

**All clients can use well-known transport protocols like TCP or UDP to talk to the cache servers.**

| Protocol | Characteristics | Use Case |
|----------|----------------|----------|
| **TCP** | Reliable, ordered delivery | Critical data, consistency needed |
| **UDP** | Fast, no guarantees | High throughput, some data loss acceptable |

**Common Choice**: TCP for reliability

---

#### 3. Consistent Behavior

**All cache clients are programmed in the same way so that the same PUT and GET operations from different clients return the same results.**

**Example:**
```
Client 1: PUT("user:101", "data")
Client 2: GET("user:101")

Both clients use same hash function:
hash("user:101") → Server 2

Client 2 successfully retrieves "data" from Server 2
```

---

### Cache Client Operations

#### PUT Operation

```
Application → Cache Client
                ↓
          Hash(key) → Determine server
                ↓
          Serialize data
                ↓
          Send to Cache Server via TCP
                ↓
          Receive acknowledgment
```

#### GET Operation

```
Application → Cache Client
                ↓
          Hash(key) → Determine server
                ↓
          Send request to Cache Server
                ↓
          Receive data
                ↓
          Deserialize
                ↓
          Return to Application
```

---

### Handling Cache Server Failures

**Question**: What will be the behavior of cache clients to an access request if one of the cache servers is dead?

**Answer**:

Since the data within the cache servers will no longer be available, cache clients will mark that access request as a **cache miss**.

#### Failure Handling Flow

```
1. Client requests "user:101"
2. Hash determines Server 2
3. Server 2 is dead
4. Connection fails
5. Mark as CACHE MISS
6. Fetch from database
7. (Optional) Store in next available server
```

#### Strategies for Handling Failures

**1. Mark as Cache Miss (Simple)**
```
If server unavailable:
    Return MISS
    Fetch from database
```

**2. Failover to Next Server (Advanced)**
```
If server unavailable:
    Use consistent hashing to find next server
    Try next server
    If successful: Cache hit
    Else: Cache miss
```

**3. Circuit Breaker Pattern**
```
If server fails multiple times:
    Mark server as down temporarily
    Stop sending requests for X seconds
    Retry after cooldown period
```

---

### Cache Client Configuration

**Example Configuration:**
```json
{
  "cache_servers": [
    "cache1.example.com:6379",
    "cache2.example.com:6379",
    "cache3.example.com:6379"
  ],
  "hash_algorithm": "consistent_hashing",
  "virtual_nodes": 150,
  "protocol": "TCP",
  "timeout_ms": 100,
  "retry_count": 2,
  "connection_pool_size": 50
}
```

---

## Summary

### Key Takeaways

#### 1. Hash Functions

- **Consistent hashing** for server selection (better scalability and fault tolerance)
- **Simple hashing** for entry location within servers
- Minimizes data redistribution during scaling

#### 2. Data Structures

- **Doubly linked list + Hash map** for efficient O(1) operations
- Supports LRU/MRU eviction policies
- **Bloom filters** for quick negative lookups (optional)

#### 3. Sharding Strategies

| Strategy | Best For | Trade-off |
|----------|----------|-----------|
| Dedicated servers | Large systems, flexibility | Higher cost |
| Co-located | Small systems, cost-sensitive | Coupled failures |

#### 4. Cache Client

- Handles hash computations and server selection
- Knows all cache servers
- Uses standard protocols (TCP/UDP)
- Marks dead servers as cache misses

#### 5. Data Temperature

- **Hot**: Frequently accessed, stays in cache
- **Warm**: Moderately accessed
- **Cold**: Rarely accessed, often evicted

---

### Design Decisions Summary

```
Question 1: Which server stores which data?
Answer: Consistent hashing for distribution

Question 2: What data structure to use?
Answer: Doubly linked list + hash map for O(1) operations

Question 3: How to shard?
Answer: Dedicated servers (flexibility) or co-located (cost)

Question 4: How to handle failures?
Answer: Cache client marks as miss, fetches from DB
```

---

