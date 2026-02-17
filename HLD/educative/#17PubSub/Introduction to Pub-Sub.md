# Introduction to Pub-Sub

Learn about the use cases of the pub-sub system, how to define its requirements, and design the API for it.

## Table of Contents

- [Use Cases of Pub-Sub](#use-cases-of-pub-sub)
- [Pub-Sub vs Queues](#pub-sub-vs-queues)
- [How Producers and Consumers Are Decoupled](#how-producers-and-consumers-are-decoupled)
- [Requirements](#requirements)
- [API Design](#api-design)
- [Pub-Sub in a Real-Time Chat Application](#pub-sub-in-a-real-time-chat-application)
- [Common Confusions Clarified](#common-confusions-clarified)
- [Summary](#summary)

---

## Use Cases of Pub-Sub

Pub-sub messaging offers asynchronous communication. Let's explore the use cases where it is beneficial to have a pub-sub system.

A few use cases of pub-sub are listed below:

### 1. Improved Performance

The pub-sub system enables **push-based distribution**, alleviating the need for message recipients to check for new information and changes regularly. It encourages **faster response times** and **lowers the delivery latency**.

**How It Works:**
```
Without Pub-Sub:
  Consumer: "Any new data?" → Polls every 5 seconds
  Latency: Up to 5 seconds

With Pub-Sub:
  System: Pushes data the instant it's available
  Latency: Milliseconds
```

---

### 2. Handling Ingestion

The pub-sub helps in **handling log ingestion**. The user-interaction data can help us figure out useful analyses about the behavior of users.

**Key Points:**
- We can ingest a **large amount of data** to the pub-sub system
- It can deliver the data to **any analytical system** to understand the behavior patterns of users
- We can also **log the details of the event** that's happening while completing a request from the user

**Real-World Example:**

Large services like **Meta** use a pub-sub system called **Scribe** to:
- Know exactly **who needs what data**
- **Remove or archive** processed or unwanted data

Doing this is necessary to **manage an enormous amount of data**.

**Architecture:**
```
User Interactions
    ↓
Pub-Sub System (Scribe)
    ↓
┌───────────────┬───────────────┬───────────────┐
│  Analytics    │  Monitoring   │  Archival     │
│  System       │  System       │  System       │
└───────────────┴───────────────┴───────────────┘
```

---

### 3. Real-Time Monitoring

**Raw or processed messages** of an application or system can be provided to **multiple applications** to monitor a system in real time.

**Example:**
```
Application Events (Publisher)
    ↓
Pub-Sub Topic: "system-events"
    ↓
┌─────────────┬─────────────┬─────────────┐
│  Dashboard  │  Alerting   │  Logging    │
│  (Monitor)  │  System     │  System     │
└─────────────┴─────────────┴─────────────┘

All receive the same events simultaneously
```

---

### 4. Replicating Data

The pub-sub system can be used to **distribute changes**. Several important scenarios are covered:

**A. Leader-Follower Protocol:**

The leader sends the changes to its followers via a pub-sub system. It allows followers to **update their data asynchronously**.

```
Leader (Publisher)
    ↓ Publishes changes
Pub-Sub Topic
    ↓
┌─────────────┬─────────────┐
│  Follower 1 │  Follower 2 │
│  (Subscriber)│ (Subscriber)│
└─────────────┴─────────────┘
Each updates asynchronously
```

**B. Distributed Caches:**

The distributed caches can also **refresh themselves** by receiving the modifications asynchronously.

```
Database Update (Publisher)
    ↓
Pub-Sub Topic: "cache-invalidation"
    ↓
┌─────────────┬─────────────┬─────────────┐
│  Cache 1    │  Cache 2    │  Cache 3    │
│  (refresh)  │  (refresh)  │  (refresh)  │
└─────────────┴─────────────┴─────────────┘
All caches updated simultaneously
```

**C. Multi-View Applications (e.g., WhatsApp):**

Applications like **WhatsApp** that allow **multiple views** of the same conversation—for example, on a mobile phone and a computer's browser—can elegantly work using a pub-sub, where **multiple views can act either as a publisher or a subscriber**.

```
User sends message on Phone (Publisher)
    ↓
Pub-Sub Topic: "conversation-12345"
    ↓
┌─────────────┬─────────────┐
│  Phone      │  Browser    │
│  (Subscriber)│ (Subscriber)│
│  (also see  │  (updates   │
│   own msg)  │   instantly)│
└─────────────┴─────────────┘
```

---

## Pub-Sub vs Queues

**Question:** What are the similarities and differences between a pub-sub system and queues?

**Answer:**

The pub-sub system and queues are similar because they **deliver information that's produced by the producer to the consumer**.

**The difference** is that:
- **Queue**: Only **one consumer** consumes a message
- **Pub-Sub**: There can be **multiple consumers** of the same message

---

### Comparison Table

| Feature | Pub-Sub | Queue |
|---------|---------|-------|
| **Message Delivery** | One-to-many | One-to-one |
| **Consumers per message** | Multiple | Single |
| **Delete after consume** | No | Yes |
| **Use Case** | Broadcasting, notifications | Task distribution, job queues |
| **Example** | News feed updates | Order processing |

**Visual Comparison:**
```
Pub-Sub:                    Queue:
Publisher → Topic           Producer → Queue
    ↓                            ↓
Consumer 1 ✓               Consumer 1 ✓
Consumer 2 ✓               (only one gets it)
Consumer 3 ✓
```

---

## How Producers and Consumers Are Decoupled

**Question:** How are producers and consumers decoupled from one another in a pub-sub system?

**Answer:**

Producers **don't know who'll end up reading their information**. They just send it to the system, and it is read by the consumer.

**The system acts as a decoupling layer** between the producers and consumers.

**Key Points:**

Producers are **not affected** by:
- Slow consumers
- The count of consumers
- The failure of consumers

**We can scale them independently.**

---

**Decoupling Visualization:**
```
┌──────────────┐                    ┌──────────────┐
│  Producer 1  │                    │  Consumer 1  │
└──────┬───────┘                    └──────────────┘
       │                                   ▲
┌──────┴───────┐    ┌──────────┐          │
│  Producer 2  │───→│  Pub-Sub │──────────┤
└──────────────┘    │  System  │          │
                    │ (Decoupl)│    ┌─────┴────────┐
┌──────────────┐    └──────────┘    │  Consumer 2  │
│  Producer 3  │───→                └──────────────┘
└──────────────┘
                                    ┌──────────────┐
                                    │  Consumer 3  │
                                    └──────────────┘

Producers don't know consumers exist
Consumers don't know producers exist
System handles delivery
```

---

## Requirements

We aim to design a pub-sub system that has the following requirements.

### Functional Requirements

Let's specify the functional requirements of a pub-sub system:

#### 1. Create a Topic

The producer should be able to **create a topic**.

---

#### 2. Write Messages

Producers should be able to **write messages** to the topic.

---

#### 3. Subscription

Consumers should be able to **subscribe to the topic** to receive messages.

---

#### 4. Read Messages

The consumer should be able to **read messages** from the topic.

---

#### 5. Specify Retention Time

The consumers should be able to **specify the retention time** after which the message should be deleted from the system.

---

#### 6. Delete Messages

A message should be **deleted from the topic or system** after a certain retention period as defined by the user of the system.

---

### Functional Requirements Summary

| # | Requirement | Who | Action |
|---|-------------|-----|--------|
| 1 | Create Topic | Producer | Creates a new topic |
| 2 | Write Messages | Producer | Publishes messages to topic |
| 3 | Subscription | Consumer | Subscribes to a topic |
| 4 | Read Messages | Consumer | Reads messages from topic |
| 5 | Retention Time | Consumer | Specifies message lifetime |
| 6 | Delete Messages | System | Auto-deletes after retention |

---

### Non-Functional Requirements

We consider the following non-functional requirements when designing a pub-sub system:

#### 1. Scalable

The system should **scale with an increasing number of**:
- Topics
- Writing load (by producers)
- Reading load (by consumers)

---

#### 2. Available

The system should be **highly available**, so that:
- Producers can add their data **anytime**
- Consumers can read data from it **anytime**

---

#### 3. Durability

The system should be **durable**. Messages accepted from producers **must not be lost** and should be delivered to the intended subscribers.

---

#### 4. Fault Tolerance

Our system should be able to **operate in the event of failures**.

---

#### 5. Concurrent

The system should **handle concurrency issues** where reading and writing are performed simultaneously.

---

### Non-Functional Requirements Summary

| Requirement | Target | Importance |
|-------------|--------|------------|
| **Scalability** | Handle growing topics, producers, consumers | Critical |
| **Availability** | Always accessible for read/write | Critical |
| **Durability** | Zero message loss | Critical |
| **Fault Tolerance** | Operate during failures | High |
| **Concurrency** | Simultaneous read and write | High |

---

## API Design

We'll exclude some parameters from the functions below, such as the producer or consumer's identifier. Let's assume that this information is available from the underlying connection context.

The API design for this problem is as follows:

---

### Create a Topic

The API call to create a topic should look like this:

```
create(topic_ID, topic_name)
```

This function returns an **acknowledgment** if it successfully creates a topic, or an **error** if it fails to do so.

| Parameter | Description |
|-----------|-------------|
| `topic_ID` | It uniquely identifies the topic |
| `topic_name` | It contains the name of the topic |

---

### Write a Message

The API call to write into the pub-sub system should look like this:

```
write(topic_ID, message)
```

The API call will write a `message` into a topic with an ID of `topic_ID`.

**Constraints:**
- Each message can have a **maximum size of 1 MB**

**Returns:**
- An **acknowledgment** if it successfully places the data in the system
- An appropriate **error** if it fails

| Parameter | Description |
|-----------|-------------|
| `topic_ID` | The ID of the topic to write to |
| `message` | The message to be written in the system |

---

### Read a Message

The API call to read data from the system should look like this:

```
read(topic_ID)
```

The topic is found using `topic_ID`, and the call will **return an object containing the message** to the caller.

| Parameter | Description |
|-----------|-------------|
| `topic_ID` | It is the ID of the topic against which the message will be read |

---

### Subscribe to a Topic

The API call to subscribe to a topic from the system should look like this:

```
subscribe(topic_ID)
```

The function **adds the consumer as a subscriber** to the topic that has the `topic_ID`.

| Parameter | Description |
|-----------|-------------|
| `topic_ID` | The ID of the topic to which the consumer will be subscribed |

---

### Unsubscribe from a Topic

The API call to unsubscribe from a topic from the system should look like this:

```
unsubscribe(topic_ID)
```

The function **removes the consumer as a subscriber** from the topic that has the `topic_ID`.

| Parameter | Description |
|-----------|-------------|
| `topic_ID` | The ID of the topic against which the consumers will be unsubscribed |

---

### Delete a Topic

The API call to delete a topic from the system should look like this:

```
delete_topic(topic_ID)
```

The function **deletes the topic** on the basis of the `topic_ID`.

| Parameter | Description |
|-----------|-------------|
| `topic_ID` | The ID of the topic which is to be deleted |

---

### Complete API Summary

| API Call | Purpose | Returns |
|----------|---------|---------|
| `create(topic_ID, topic_name)` | Create a new topic | ACK or error |
| `write(topic_ID, message)` | Publish message to topic | ACK or error |
| `read(topic_ID)` | Read message from topic | Message object |
| `subscribe(topic_ID)` | Subscribe consumer to topic | ACK or error |
| `unsubscribe(topic_ID)` | Remove consumer from topic | ACK or error |
| `delete_topic(topic_ID)` | Delete a topic | ACK or error |

---

## Question
You’re designing a real-time chat application that has channels for different discussions. Explain how you would use a pub-sub system for its messaging component. 

Describe the mapping of the chatting application onto the following pub-sub components:

- Topics  
- Subscribers  
- How message delivery would work

## Pub-Sub in a Real-Time Chat Application

In a real-time chat application, users communicate through channels (for example, #general, #tech, #random). A publish–subscribe (pub-sub) system is a natural fit because it enables **real-time message broadcasting** to multiple users without tightly coupling senders and receivers.

Below is how the chat application maps to pub-sub components.

---

### 1. Topics

Topics represent **chat channels**.

Each chat channel maps to **one topic** in the pub-sub system.

**Example Mappings:**

| Chat Channel | Pub-Sub Topic |
|--------------|---------------|
| #general | `chat.general` |
| #tech | `chat.tech` |
| #sports | `chat.sports` |

Topics **logically isolate** conversations so that messages published to one channel are **not delivered** to users of another channel.

---

### 2. Subscribers

Subscribers represent **users (or user sessions)** connected to a channel.

**How Subscription Works:**
- When a user **joins a channel**, the chat service subscribes that user's session to the corresponding topic
- A single user can **subscribe to multiple topics** if they join multiple channels
- Each active **device or browser tab** can be treated as a separate subscriber

**Example:**
```
User A joins #general and #tech
  → Subscribed to: chat.general, chat.tech

User B joins #general only
  → Subscribed to: chat.general
```

---

### 3. Message Delivery Flow

**Step-by-step message flow:**

**Step 1: User Sends a Message**
- User A types a message in #general

**Step 2: The Chat Server Receives the Message**
- Message arrives at the chat service

**Step 3: Message is Published**
- The chat server publishes the message to the topic `chat.general`
- The publisher **does not need to know** who is online or subscribed

**Step 4: Pub-Sub System Delivers the Message**
- The pub-sub system forwards the message to **all subscribers** of `chat.general`
- This includes User A, User B, and any other users currently in the channel

**Step 5: Clients Receive the Message**
- Each subscribed client receives the message **in real time**
- The chat UI **updates instantly**

---

**Complete Flow Diagram:**
```
User A types "Hello!"
    ↓
Chat Server receives message
    ↓
Publishes to Topic: chat.general
    ↓
Pub-Sub System
    ↓
┌─────────────┬─────────────┬─────────────┐
│   User A    │   User B    │   User C    │
│  (receives) │  (receives) │  (receives) │
│  own msg    │  in real    │  in real    │
│             │  time       │  time       │
└─────────────┴─────────────┴─────────────┘
```

---

### 4. Key Characteristics in This Design

#### Loose Coupling
Publishers (message senders) are **unaware of subscribers** (receivers), which makes the system **scalable and flexible**.

#### Fan-Out Delivery
A single published message is **automatically delivered to multiple subscribers**.

#### Real-Time Communication
Messages are **pushed immediately**, enabling live chat behavior.

#### Scalability
New users can **join or leave channels** without impacting message producers.

---

### 5. Optional Enhancements (Real-World Considerations)

#### Offline Users
Messages can also be **persisted in a database** for later retrieval.

#### Multiple Devices
A user logged in on multiple devices simply has **multiple subscriptions**.

#### Moderation
The publisher can **validate or filter messages** before publishing.

---

## Common Confusions Clarified

These are the points where pub-sub usually feels confusing. Each one is cleared slowly, precisely, and with examples.

---

### Question 1: Is a Channel One-Way Communication?

> "Is a channel one-way—meaning only admin can send messages and other participants can only see the message?"

**Short Answer:** ❌ Not necessarily. A channel is **not inherently one-way**.

---

#### What a Channel Really Is

A channel is just a **topic**.

**Who can publish** is a **business rule**, not a pub-sub rule.

---

#### Two Common Chat Channel Types

**A. Announcement / Broadcast Channel (One-Way)**

**Example:** `#company-announcements`

| Role | Permission |
|------|------------|
| Admins | Publisher (can send) |
| Employees | Subscriber (read-only) |

**How It's Enforced:**
- **Authorization at the chat service** decides who can publish
- **NOT** by the pub-sub system itself

Pub-sub still works the same way:
- Admin publishes
- Everyone subscribed receives

---

**B. Discussion Channel (Two-Way)**

**Example:** `#general`

| Role | Permission |
|------|------------|
| All Members | Publisher AND Subscriber |

Each user:
- **Publishes** messages to the topic
- **Subscribes** to the same topic (receives others' messages)

---

**Key Takeaway:**

> Pub-sub does not decide who can talk. Your application logic does.

---

### Question 2: Is Pub-Sub Push or Pull?

> "Is pub-sub push-based or pull-based?"

**Short Answer:** 👉 Primarily **PUSH**, but often combined with **PULL** in practice.

---

#### PUSH Model (Classic Pub-Sub)

**How It Works:**
- Subscriber registers interest
- System **pushes messages immediately**

**Best For:**
- Real-time chat
- Live notifications
- Low latency requirements

**Example:**
```
Message published → Instantly pushed to all subscribers
```

---

#### PULL Model (Consumer Fetches)

**How It Works:**
- Subscriber actively asks: *"Do you have messages for me?"*

**Used In:**
- Kafka
- Some queue systems

**Example:**
```
Subscriber polls → Receives messages on request
```

---

#### What Chat Apps Usually Do: Hybrid Model

| Scenario | Model Used |
|----------|------------|
| User is **online** | PUSH (WebSocket) |
| User is **offline** | PULL (fetch history on login) |

So:
- **Real-time** → Push
- **History / Recovery** → Pull

---

**System-Level Comparison:**

| System | Model |
|--------|-------|
| Pub-Sub (Redis, SNS) | Push |
| Message Queue (Kafka, SQS) | Pull |
| Chat Applications | Push + Pull (Hybrid) |

---

### Question 3: Who Deletes the Message After Reading?

> "Who deletes the message after reading? Can the producer delete it so subscribers can't see it?"

**Short Answer:**
- ❌ Producers do **NOT** delete messages after sending
- ❌ Subscribers do **NOT** delete messages just by reading

---

#### Pub-Sub Mental Model (Very Important)

A message is **delivered**, not stored for reading later.

Once published:
- Message is **copied to subscribers**
- No single "shared message" exists anymore

---

#### What Actually Happens in Chat Systems

Chat systems have **two layers**:

**Layer 1: Pub-Sub (Delivery)**
- Delivers message to **online users**
- No concept of delete-after-read
- Message disappears once delivered to subscribers

**Layer 2: Storage (History)**
- Message is **stored in database**
- Used for:
  - Offline users
  - Chat history
  - Search
- **Deletion rules apply only here**

---

**Complete Example Flow:**
```
User A sends message:
    ↓
┌─────────────────────────────────┐
│  Layer 1: Pub-Sub (Delivery)    │
│  Published to topic             │
│  Delivered to online users      │
│  (No delete-after-read)         │
└─────────────────────────────────┘
    ↓ (simultaneously)
┌─────────────────────────────────┐
│  Layer 2: Database (Storage)    │
│  Stored for history             │
│  Accessible to offline users    │
│  Deleted after retention period │
└─────────────────────────────────┘

User B reads message:
  → Message is NOT deleted
  → Message remains until:
     - Retention policy expires
     - User manually deletes
     - Admin deletes
```

---

**Important Clarification:**

If a producer deletes a message, subscribers **do not lose it** because delivery **already happened**.

Deletion affects:
- ✅ Future reads from storage
- ❌ Does NOT affect past deliveries

---

### Pub-Sub vs Queue: The Key Difference Explained

This comparison explains most of the confusion between pub-sub and queues:

| Feature | Pub-Sub | Queue |
|---------|---------|-------|
| One message → many users | ✅ Yes | ❌ No |
| Delete after consume | ❌ No | ✅ Yes |
| Consumer tracks offset | ❌ No | ✅ Yes |
| Live chat messages | ✅ Yes | ❌ No |
| Background job processing | ❌ No | ✅ Yes |

---

### Final Answers in One Line Each

**1. Is channel one-way?**
👉 Depends on authorization rules, not on pub-sub

**2. Push or pull?**
👉 Push for live chat, pull for history

**3. Who deletes the message?**
👉 Nobody deletes after read in pub-sub; storage retention rules apply separately

---

## Summary

### What We Covered

#### Use Cases
- ✅ Improved performance (push-based distribution)
- ✅ Handling ingestion (log and data processing)
- ✅ Real-time monitoring (multiple consumers)
- ✅ Replicating data (leader-follower, caches, multi-view apps)

#### Requirements
- ✅ **Functional**: Create topics, write/read messages, subscribe/unsubscribe, retention, deletion
- ✅ **Non-functional**: Scalability, availability, durability, fault tolerance, concurrency

#### API Design
- ✅ Six core APIs covering all functional requirements
- ✅ Clean, simple interface
- ✅ Message size limit of 1 MB

#### Real-World Application
- ✅ Chat application use case fully mapped
- ✅ Topics = Channels
- ✅ Subscribers = Users/Sessions
- ✅ Fan-out delivery for real-time messaging

#### Common Confusions Resolved
- ✅ Channels are not inherently one-way (application logic decides)
- ✅ Pub-sub is primarily push, but hybrid in practice
- ✅ Messages are delivered, not shared; storage handles persistence separately

---

*Document: Introduction to Pub-Sub | Use Cases, Requirements, API Design, and Common Clarifications*