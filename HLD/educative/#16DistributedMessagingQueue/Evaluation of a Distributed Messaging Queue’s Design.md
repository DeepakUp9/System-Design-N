# Evaluation of a Distributed Messaging Queue's Design

Evaluate the proposed system based on the functional and non-functional requirements of a distributed messaging queue.

## Table of Contents

- [Introduction](#introduction)
- [Functional Requirements Compliance](#functional-requirements-compliance)
- [Non-Functional Requirements Compliance](#non-functional-requirements-compliance)
- [Special Scenarios](#special-scenarios)
- [Summary](#summary)

---

## Introduction

We completed the process of designing a distributed messaging queue. Now, let's analyze whether the design met the functional and non-functional requirements of a distributed messaging queue.

---

## Functional Requirements Compliance

### 1. Queue Creation and Deletion

#### Queue Creation

When a request for a queue is received at the front-end, the queue is created with all the necessary details provided by the client after undergoing some essential checks.

**Process:**

**Step 1: Request Reception**
- Front-end receives queue creation request
- Validates all parameters

**Step 2: Cluster Assignment**
- Corresponding cluster manager assigns servers to newly created queue

**Step 3: Metadata Update**
- Updates information in metadata stores and caches
- Uses metadata service for coordination

**Example Flow:**
```
Client request: Create queue "order-processing"
  ↓
Front-end: Validate parameters
  ↓
Cluster Manager: Assign servers
  - Primary: backend-5
  - Replicas: backend-7, backend-9
  ↓
Metadata Service: Update
  - Metadata store ✓
  - Cache ✓
  ↓
Response: Queue created successfully
```

---

#### Queue Deletion

Similarly, the queue is deleted when the client doesn't need it anymore.

**Process:**

**Step 1: Deletion Request**
- Client requests queue deletion

**Step 2: Space Deallocation**
- Responsible cluster manager deallocates space occupied by the queue

**Step 3: Metadata Cleanup**
- Deletes data from all metadata stores
- Deletes data from all caches

**Ensuring Clean Deletion:**
```
Delete queue "order-processing":
  1. Stop accepting new messages
  2. Wait for pending messages to be processed
  3. Deallocate storage space
  4. Remove from metadata store
  5. Invalidate cache entries
  6. Confirm deletion to client
```

---

### 2. Send and Receive Messages

#### Sending Messages

**Producers can deliver messages to specific queues once they are created.**

**At the back-end:**
- Receiving messages are **sorted based on timestamps** to preserve their order
- Messages are placed in the queue

**Process:**

When a message is received from a producer for a specific queue:

**Step 1: Identification**
- Front-end identifies the primary host or cluster (depending on replication model) where the queue resides

**Step 2: Forwarding**
- Request is forwarded to corresponding entity

**Step 3: Queueing**
- Message is put in the queue

**Complete Flow:**
```
Producer sends message:
  "Order #12345 created"
  Queue: "order-processing"
  
Front-end:
  1. Validate message
  2. Check metadata for queue location
  3. Identify primary: backend-5
  
Forward to backend-5:
  1. Receive message
  2. Sort by timestamp
  3. Place in queue
  4. Replicate to replicas
  5. ACK to front-end
  
Front-end → Producer: Success
```

---

#### Receiving Messages

**Consumers can retrieve messages from a specified queue.**

**Process:**
```
Consumer requests message from "order-processing":
  ↓
Front-end: Check metadata
  ↓
Forward to primary: backend-5
  ↓
Backend-5: Retrieve next message in order
  ↓
Return to consumer: "Order #12345 created"
```

---

### 3. Message Deletion

Primarily, **two options** are used to delete a message from a queue:

#### Option 1: Consumer-Tracked Deletion

**The first option is not to delete a message after it's consumed.**

However, in this case, the **consumer is responsible for keeping track** of what's consumed.

**Requirements:**

For this, we need to:
- Maintain the order of messages in the queue
- Keep track of a message within a queue

**Cleanup:**

A job can then delete the message when the **expiration conditions are met**.

**Example: Apache Kafka**

Apache Kafka mostly uses this idea where **multiple processes can consume a message**.

---

**How It Works:**
```
Queue: [Msg1, Msg2, Msg3, Msg4, Msg5]

Consumer A offset: 3 (consumed up to Msg3)
Consumer B offset: 5 (consumed up to Msg5)
Consumer C offset: 1 (consumed up to Msg1)

Messages remain in queue
Each consumer tracks own offset
Background job deletes expired messages (e.g., after 7 days)
```

**Advantages:**
- ✅ Multiple consumers can read same message
- ✅ Consumers can replay messages
- ✅ No complex visibility logic needed

**Use Case:**
- Event streaming
- Log aggregation
- Multiple consumer groups

---

#### Option 2: Visibility Timeout Deletion

**The second approach also doesn't delete a message after it's consumed.**

However, it's made **invisible for some time** via an attribute—for example, `visibility_timeout`.

**How It Works:**

This way, the **other consumers are unable to get messages** that have already been consumed.

The message is then **deleted by the consumer via an API call**.

---

**Process Flow:**
```
1. Consumer A requests message
   Message: "Order #12345"
   visibility_timeout: 30 seconds
   
2. Message becomes invisible to other consumers
   Queue view for Consumer B: [Msg2, Msg3, Msg4]
   (Msg1 is invisible)
   
3. Consumer A processes message
   
4a. Success case:
    Consumer A calls delete API
    Message permanently deleted
    
4b. Failure case:
    Consumer A crashes
    After 30 seconds: Message becomes visible again
    Consumer B can now retrieve it
```

**Advantages:**
- ✅ Automatic retry on failure
- ✅ At-least-once delivery guarantee
- ✅ No duplicate processing (during visibility window)

**Use Case:**
- Task queues
- Job processing
- Single consumer per message

---

#### Why Messages Are Not Immediately Deleted

**In both cases, the message being retrieved by the consumer is only deleted by the consumer.**

**The reason behind this is to provide high durability** if a consumer can't process a message due to some failure.

**Failure Scenario:**
```
Consumer retrieves message
  ↓
Consumer starts processing
  ↓
Consumer crashes (network issue, server failure)
  ↓
Message NOT deleted (still in queue)
  ↓
Consumer restarts
  ↓
Consumer retrieves same message again
  ↓
Consumer processes successfully
  ↓
Consumer deletes message
```

**In such a case, in the absence of a delete call, the consumer can retrieve the message again when it comes back.**

---

#### At-Least-Once Delivery Semantic

Moreover, this approach also provides **at-least-once delivery semantic**.

**Example:**

When a worker fails to process the message, another worker can retrieve the message after it becomes visible in the queue.

**Scenario:**
```
Worker 1: Retrieves message, crashes
  ↓
Message becomes visible after timeout
  ↓
Worker 2: Retrieves same message
  ↓
Worker 2: Processes successfully
  ↓
Message delivered at least once ✓
```

---

#### Question: Visibility Timeout Expiration

**Question:** What happens when the visibility timeout of a specific message expires and the consumer is still busy processing the message?

**Answer:**

The message **becomes visible**, and **another worker can receive the message**, thereby **duplicating the processing**.

**Problem Scenario:**
```
T0: Consumer A gets message (visibility: 30s)
T10: Consumer A still processing...
T20: Consumer A still processing...
T30: Visibility timeout expires
     Message becomes visible
T31: Consumer B gets same message
     Now TWO consumers processing same message!
T40: Consumer A completes (duplicate!)
T45: Consumer B completes (duplicate!)

Result: Message processed twice ❌
```

---

**Solution:**

To avoid such a situation, we ensure that the **application sets a safe threshold for visibility timeout**.

**Best Practices:**
```
Estimate processing time: 10 seconds
Safety factor: 3x
Recommended visibility timeout: 30 seconds

If processing takes longer:
  - Consumer extends visibility timeout
  - Or: Consumer acknowledges progress
```

**Safe Implementation:**
```
while processing:
  if elapsed_time > (visibility_timeout * 0.8):
    extend_visibility_timeout()
    
After processing:
  delete_message()
```

---

### 4. Dead-Letter Queue (DLQ)

**Question:** How do we handle messages that can't be processed—here meaning consumed—after maximum processing attempts by the consumer?

**Answer:**

A special type of queue, called a **dead-letter queue**, can be provided to handle messages that aren't consumed after the maximum number of processing attempts have been made by the consumer.

---

#### What Gets Sent to DLQ

This type of queue is also used for keeping messages that can't be processed successfully due to the following factors:

**1. Non-Existent Queue**
- The messages intended for a queue that doesn't exist anymore

**Example:**
```
Message sent to queue "old-orders"
Queue "old-orders" was deleted yesterday
Message → DLQ
```

---

**2. Queue Length Limit Exceeded**
- The queue length limit is exceeded
- Although this would rarely occur with our current design

**Example:**
```
Queue "notifications" max size: 1,000,000
Current size: 1,000,000
New message arrives → DLQ
```

---

**3. Message Expiration**
- The message expires due to per-message **time to live (TTL)**

**Example:**
```
Message created: 10:00 AM
TTL: 1 hour
Current time: 11:05 AM
Message expired → DLQ
```

---

**4. Repeated Processing Failures**
```
Attempt 1: Failed (network error)
Attempt 2: Failed (network error)
Attempt 3: Failed (network error)
Max retries reached (3)
Message → DLQ
```

---

#### Why DLQ is Important

A dead-letter queue is also important for:

**1. Determining the Cause of Failure**
- Analyze failed messages
- Identify patterns
- Debug issues

**2. Identifying Faults in the System**
- Systematic failures
- Configuration issues
- Integration problems

**Example Analysis:**
```
DLQ Analysis:
  Total messages: 1,000
  
  Failure reasons:
  - 500: Malformed JSON (40%)
  - 300: Database timeout (30%)
  - 200: Invalid schema (20%)
  
  Action: Fix JSON validation in producer
```

---

#### DLQ Architecture

```
Main Queue: "orders"
    ↓
Consumer attempts processing
    ↓
  Success? ──Yes──→ Delete message ✓
    ↓ No
  Retry?
    ↓ Yes (attempts < 3)
  Return to queue
    ↓ No (attempts = 3)
Dead-Letter Queue
    ↓
Manual inspection/reprocessing
```

---

## Non-Functional Requirements Compliance

### 1. Durability

**Goal:** Ensure no data loss

**How We Achieve It:**

To achieve durability:

**Metadata Replication:**
- Queues' metadata is **replicated on different nodes**

**Message Replication:**
- When a message is received, it's **replicated in the queues that reside on different nodes**

**Failure Handling:**

Therefore, if a node fails, other nodes can be used to deliver or retrieve messages.

---

**Example:**
```
Message arrives at Primary (Node A)
  ↓
Replicate to Node B ✓
Replicate to Node C ✓
  ↓
Node A crashes ❌
  ↓
Node B promoted to primary
System continues operating ✓
Message not lost ✓
```

**Durability Guarantees:**
- ✅ Messages survive server crashes
- ✅ Metadata survives node failures
- ✅ No single point of failure
- ✅ Data replicated across failure domains

---

### 2. Scalability

**Goal:** Handle increasing load efficiently

**Our design components are horizontally scalable:**
- Front-end servers
- Metadata servers
- Caches
- Back-end clusters
- And more

**We can add to or remove their capacity to match our needs.**

---

#### Two Dimensions of Scalability

The scalability can be divided into **two dimensions**:

##### A. Increase in the Number of Messages

**Trigger:**

When the number of messages touches a specific limit—say, **80%**—the specified queue is expanded.

**Scale Down:**

Similarly, the queue is **shrunk** when the number of messages drops below a certain threshold.

**Example:**
```
Queue: "order-processing"
Capacity: 1,000,000 messages
Current: 800,000 messages (80%)
  ↓
Trigger: Auto-expand
  ↓
New capacity: 2,000,000 messages
Add partitions or increase storage

Later...
Current: 400,000 messages (20%)
  ↓
Trigger: Auto-shrink
  ↓
New capacity: 1,000,000 messages
Remove unused partitions
```

---

##### B. Increase in the Number of Queues

**Demand Growth:**

With an increasing number of queues, the demand for more servers also increases.

**Responsibility:**

In which case, the **cluster manager is responsible for adding extra servers**.

**Performance Isolation:**

We **commission nodes** so that there is **performance isolation between different queues**.

**Key Principle:**

An **increased load on one queue shouldn't impact other queues**.

**Example:**
```
Initial State:
  10 queues
  5 servers
  
Growth:
  100 queues created
  ↓
Cluster manager adds 45 more servers
  Total: 50 servers
  Each queue isolated
  
Queue A under heavy load:
  Queue A performance: Impacted
  Queue B performance: Normal ✓
  Queue C performance: Normal ✓
  
Performance isolation maintained!
```

---

**Scalability Mechanisms:**

| Mechanism | Purpose | Example |
|-----------|---------|---------|
| **Horizontal Scaling** | Add more servers | 5 → 50 servers |
| **Partitioning** | Distribute load | 6 partitions per queue |
| **Auto-expansion** | Handle message growth | 1M → 2M capacity |
| **Auto-shrinking** | Optimize resources | 2M → 1M capacity |
| **Performance Isolation** | Prevent interference | Separate resources per queue |

---

### 3. Availability

**Goal:** System remains operational during failures

**How We Achieve It:**

Our data components are properly replicated:

**Metadata:**
- Replicated inside or outside the data center

**Actual Messages:**
- Replicated inside or outside the data center

**Traffic Management:**

The **load balancer routes traffic around failed nodes**.

**Result:**

Together, these mechanisms make sure that our **system remains available for service under faults**.

---

**Failure Scenarios:**

**Scenario 1: Single Node Failure**
```
Node A fails
  ↓
Load balancer detects failure
  ↓
Routes traffic to Node B and Node C
  ↓
System continues operating ✓
Availability maintained
```

**Scenario 2: Data Center Failure**
```
Data Center 1 fails (entire DC down)
  ↓
Replicas in Data Center 2 and 3 available
  ↓
External cluster manager redirects traffic
  ↓
System continues operating ✓
Availability maintained
```

**Scenario 3: Multiple Node Failures**
```
2 out of 5 nodes fail
  ↓
3 nodes still operational
  ↓
Quorum maintained (3 > 5/2)
  ↓
System continues with reduced capacity
  ↓
Auto-scaling adds replacement nodes
  ↓
Full capacity restored
```

---

**Availability Measures:**

| Component | Availability Strategy | Result |
|-----------|----------------------|---------|
| **Front-end** | Multiple load balancers | 99.99% |
| **Metadata** | Multi-DC replication | 99.99% |
| **Back-end** | Primary-secondary model | 99.95% |
| **Overall System** | Combined strategies | 99.9%+ |

---

### 4. Performance

**Goal:** Low latency and high throughput

**For better performance we use:**

#### A. Caches

**Benefit:** Reduces data read time

**Implementation:**
- Front-end caches metadata
- Reduces database queries
- Sub-millisecond response times

**Example:**
```
Without cache:
  Every request → Database query (50ms)
  
With cache:
  95% requests → Cache hit (2ms)
  5% requests → Database query (50ms)
  
Average: 0.95×2 + 0.05×50 = 4.4ms
89% improvement!
```

---

#### B. Data Replication

**Benefit:** Reduces read latency

**Implementation:**
- Messages replicated across nodes
- Read from nearest replica
- Parallel reads possible

**Example:**
```
Single node:
  All reads → Single node
  Bottleneck at 10,000 req/s
  
With 3 replicas:
  Reads distributed across 3 nodes
  Each handles 3,333 req/s
  Total: 10,000 req/s with room to spare
```

---

#### C. Partitioning

**Benefit:** Reduces write time and enables parallelism

**Implementation:**
- Queue split into partitions
- Each partition independent
- Parallel processing

**Example:**
```
Single partition:
  1,000 writes/second max
  
6 partitions:
  Each: 1,000 writes/second
  Total: 6,000 writes/second
  6x throughput improvement!
```

---

#### D. Best-Effort Ordering

**Benefit:** Increases throughput and lowers latency when strict ordering isn't necessary

**Implementation:**
- Messages placed as received
- No sorting overhead
- Higher throughput

**Comparison:**
```
Best-effort ordering:
  Throughput: 100,000 msg/s
  Latency: 5ms
  
Strict ordering:
  Throughput: 20,000 msg/s
  Latency: 25ms
  
5x throughput improvement!
```

---

#### E. Time-Window Based Sorting

**Benefit:** Reduces latency for strict ordering scenarios

**Implementation:**
- Sort messages within time window
- Bounded delay
- Balance between ordering and performance

**Example:**
```
Without time-window (full sorting):
  Wait for all messages
  Latency: Variable (could be seconds)
  
With time-window (100ms):
  Sort messages within 100ms window
  Latency: Fixed 100ms
  Good enough for most use cases
```

---

**Performance Summary:**

| Optimization | Improvement | Trade-off |
|--------------|-------------|-----------|
| **Caching** | 89% latency reduction | Memory usage |
| **Replication** | 3x read capacity | Storage cost |
| **Partitioning** | 6x write capacity | Complexity |
| **Best-effort ordering** | 5x throughput | No strict order |
| **Time-window sorting** | Bounded latency | Slight delay |

---

## Special Scenarios

### Online Multiplayer Game Use Case

**Question:** Describe how a distributed messaging queue would be used in an online multiplayer game to manage real-time player actions (movement, attacks, etc.). What are the crucial requirements for this scenario?

---

#### Game Architecture with Messaging Queue

**Use Case:**
```
Game: Multiplayer Battle Royale
Players: 100 per match
Actions: Movement, attacks, item pickups, chat
Update rate: 60 times per second per player
Total messages: 6,000 msg/s per match
```

---

#### How Messaging Queue Is Used

**1. Player Actions as Messages**
```
Player A presses "Move Forward":
  ↓
Client sends message:
  {
    "player_id": "A",
    "action": "move",
    "direction": "forward",
    "timestamp": "10:30:45.123",
    "position": {"x": 100, "y": 200}
  }
  ↓
Message queue: "game-match-12345-actions"
```

**2. Game Server Consumes Actions**
```
Game Server subscribes to queue
  ↓
Receives all player actions
  ↓
Processes in order (by timestamp)
  ↓
Updates game state
  ↓
Broadcasts state to all players
```

**3. Broadcasting Game State**
```
Game Server publishes to:
  "game-match-12345-updates"
  ↓
All clients subscribed
  ↓
Receive state updates
  ↓
Render on screen
```

---

#### Queue Structure

**Multiple Queues Per Match:**
```
Match 12345:
  - actions-queue (input from players)
  - updates-queue (output to players)
  - chat-queue (separate for chat)
  - events-queue (game events: kills, items)
```

---

#### Crucial Requirements

**1. Ultra-Low Latency**
```
Requirement: < 50ms end-to-end
Why: Real-time gameplay feel
Solution:
  - Best-effort ordering
  - In-memory queues
  - Minimal hops
  - Regional data centers
```

**2. High Throughput**
```
Requirement: 6,000+ msg/s per match
Why: 100 players × 60 updates/s
Solution:
  - Partitioned queues
  - Parallel processing
  - No synchronous replication
```

**3. Ordering (Per Player)**
```
Requirement: Player A's actions in order
Why: Movement sequence matters
Solution:
  - Partition by player_id
  - Within partition: strict ordering
  - Across partitions: no ordering needed
```

**4. Message Loss Tolerance**
```
Requirement: Some loss acceptable
Why: Next update will override
Solution:
  - Async replication
  - No persistence
  - Fast, not durable
```

**5. Fairness**
```
Requirement: No player advantage from latency
Why: Competitive integrity
Solution:
  - Server-authoritative
  - Timestamp validation
  - Lag compensation
```

**6. Scalability**
```
Requirement: Support 1000s of matches simultaneously
Why: Popular game
Solution:
  - Queue per match
  - Auto-scaling
  - Performance isolation
```

---

#### Queue Configuration for Gaming

```
Configuration:
  - Replication: Async (performance > durability)
  - Ordering: Per-player partition
  - Persistence: None (in-memory only)
  - TTL: 1 second (old messages irrelevant)
  - Latency: < 10ms (P99)
  - Throughput: 10,000+ msg/s per queue
```

---

#### Trade-offs for Gaming

| Requirement | Choice | Rationale |
|-------------|--------|-----------|
| **Durability** | Low | Next update overrides |
| **Consistency** | Eventual | Server reconciles |
| **Latency** | Critical | Real-time feel |
| **Throughput** | High | Many players |
| **Ordering** | Partial | Per-player only |

---

## Summary

### Functional Requirements Met

✅ **Queue Creation and Deletion**
- Fully supported with cluster manager coordination
- Metadata properly updated
- Clean resource deallocation

✅ **Send and Receive Messages**
- Messages sorted by timestamp
- Proper routing to primary/cluster
- Efficient retrieval for consumers

✅ **Message Deletion**
- Two options supported (consumer-tracked, visibility timeout)
- At-least-once delivery guaranteed
- Failure recovery mechanisms

✅ **Dead-Letter Queue**
- Handles failed messages
- Supports debugging and analysis
- Multiple failure scenarios covered

---

### Non-Functional Requirements Met

✅ **Durability**
- Metadata replicated across nodes
- Messages replicated across nodes
- Survives node failures

✅ **Scalability**
- Horizontal scaling of all components
- Two dimensions: message volume and queue count
- Performance isolation between queues
- Auto-expansion and auto-shrinking

✅ **Availability**
- Multi-level replication
- Load balancer traffic management
- Survives multiple failure scenarios
- 99.9%+ uptime

✅ **Performance**
- Caching reduces latency
- Replication improves read capacity
- Partitioning enables parallelism
- Flexible ordering strategies
- Optimized for different use cases

---

### Key Design Strengths

**Flexibility:**
- Multiple replication models
- Configurable ordering strategies
- Adaptive scaling

**Resilience:**
- Multiple failure handling mechanisms
- Dead-letter queue for edge cases
- At-least-once delivery guarantee

**Performance:**
- Multiple optimization techniques
- Trade-offs clearly defined
- Suitable for diverse workloads

---

### Real-World Applicability

The design successfully addresses:
- Traditional message queuing (task queues, job processing)
- Event streaming (log aggregation, analytics)
- Real-time applications (gaming, chat)
- High-throughput systems (payment processing)
- Mission-critical systems (financial transactions)

---

