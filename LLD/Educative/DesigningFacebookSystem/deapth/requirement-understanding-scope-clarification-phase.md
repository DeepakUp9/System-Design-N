# LLD Chapter: Facebook System — Getting Ready

## Topic: Understanding the Problem & Asking Clarifying Questions

---

## 1. WHAT is this stage?

This is the **requirement-understanding and scope-clarification phase** of any LLD interview.

Here, you show that you understand the product — in this case, **Facebook** — and then narrow it down into a buildable system.

**You do not design anything yet.** You focus on:

- Understanding the platform
- Identifying key features
- Finding scope boundaries
- Asking questions that simplify the design

---

## 2. WHY is this stage important in LLD interviews?

In LLD interviews, candidates often fail because they **jump into classes and methods immediately**.

### Interviewers expect you to show that:

| Expectation | Why It Matters |
|-------------|----------------|
| You understand the business problem | Shows domain knowledge |
| You can break down large systems | Shows decomposition skills |
| You can define constraints & assumptions | Shows practical thinking |
| You can design only what is in scope | Shows focus and priority |
| You can reason like a system designer, not a coder | Shows architectural maturity |

> **This step shows maturity in design thinking.**

---

## 3. HOW to approach the Facebook system problem

### A. Identify the Core Entities

From the problem statement, highlight the **primary domain concepts**:

| Entity | Purpose |
|--------|---------|
| **Users** | People using the platform |
| **Posts** | Content shared by users |
| **Friend Requests** | Connection management |
| **Direct Messages** | Private communication |
| **Pages** | Public profiles (brands, celebrities) |
| **Groups** | Community spaces |
| **Feed / Timeline** | Personalized content stream |
| **Media** | Photos, videos, reels, etc. |

> **Why this matters:** LLD is all about object modeling. These concepts will turn into classes, relationships, and components later.

---

### B. Identify the Primary Actions

**Ask:** What operations will the system support?

**Examples:**

- Creating posts
- Sending friend requests
- Accepting/declining requests
- Following pages
- Joining groups
- Generating personalized feed
- Messaging users

> **These turn into use cases.**

---

### C. Clarify the Scope (Very Important in LLD Interviews)

Facebook is **huge**, so you cannot design everything in one go.

**You must ask:** "Which features do you want me to focus on today?"

#### Common scope options:

- 📱 News Feed system
- 👥 Friend management
- 📝 Posting system
- 💬 Messaging
- 🔗 Groups & pages
- 🔔 Notification system

> **Interviewers love when you show narrowing ability.**

---

### D. Functional Requirements

**What the system should do:**

- ✅ Create and manage user profiles
- ✅ Add and manage friends
- ✅ Create and view posts
- ✅ Create pages and groups
- ✅ Personalised feed generation
- ✅ Message other users

---

### E. Non-Functional Requirements

**These show depth in interviews:**

| Requirement | Target | Importance |
|-------------|--------|------------|
| **High availability** | Feed must load quickly | Critical |
| **Low latency** | Messages deliver instantly | Critical |
| **Scalability** | Billions of posts | High |
| **Consistency model** | Eventual consistency for feed | Medium |
| **Data modeling** | Graph-based relationships | High |

---

### F. Hidden Questions Interviewers expect you to ask

**These questions demonstrate seniority:**

#### 1. Should we support real-time updates?

Feed, chat, notifications — do they need real-time?

#### 2. Do we need to support tagging or reactions?

Adds complexity to data models.

#### 3. Should the system support privacy levels?

Public, friends-only, groups, etc.

#### 4. What about media content?

Images, reels, long videos — storage and metadata.

#### 5. Should messaging be in this scope?

Messaging itself is a separate large system.

#### 6. How large a scale are we designing for?

Millions or billions of users?

> **Even if the interviewer doesn't answer, your questions show mastery.**

---

## 4. Extra Value: How an interviewer evaluates you at this stage

### They assess whether you:

- ✅ Identify core components
- ✅ Show logical structuring
- ✅ Avoid over-engineering too early
- ✅ Ask the right scoping questions
- ✅ Understand functional vs non-functional requirements
- ✅ Demonstrate product thinking
- ✅ Communicate clearly

> **This is a conversation-based phase, not a coding or class-based phase.**

---

## 5. Summary for Interview-Ready Answer

### When given the Facebook problem, start by stating:

1. **What Facebook does**
2. **What the main features are**
3. **What entities exist**
4. **What actions happen**
5. **What the design boundaries could be**
6. **What clarifying questions you need to ask**

> **This earns you early points and sets the direction of the whole design.**

---

## System Overview

```
┌─────────────────────────────────────────────────────┐
│                  Facebook System                     │
└─────────────────────────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
   ┌────▼────┐    ┌────▼────┐   ┌────▼────┐
   │  User   │    │  Post   │   │  Social │
   │  Mgmt   │    │  System │   │  Graph  │
   └─────────┘    └─────────┘   └─────────┘
        │              │              │
   ┌────┴────┐    ┌────┴────┐   ┌────┴────┐
   │Profile  │    │Feed     │   │Friends  │
   │Settings │    │Timeline │   │Groups   │
   │Privacy  │    │Media    │   │Pages    │
   └─────────┘    └─────────┘   └─────────┘
```

---

## Core Feature Breakdown

### User Management

| Feature | Description |
|---------|-------------|
| **Profile Creation** | Name, bio, photo, personal info |
| **Authentication** | Login, signup, password recovery |
| **Privacy Settings** | Control who sees what |
| **Profile Updates** | Edit information, photos |

---

### Social Graph

| Feature | Description |
|---------|-------------|
| **Friend Requests** | Send, accept, decline, cancel |
| **Friend Lists** | Manage connections |
| **Following** | Follow without friendship |
| **Blocking** | Prevent interactions |

---

### Content System

| Feature | Description |
|---------|-------------|
| **Create Posts** | Text, images, videos |
| **Edit Posts** | Modify after publishing |
| **Delete Posts** | Remove content |
| **Privacy Control** | Public, friends, custom |
| **Reactions** | Like, love, angry, etc. |
| **Comments** | Nested conversations |
| **Shares** | Redistribute content |

---

### Feed System

| Feature | Description |
|---------|-------------|
| **Personalized Feed** | Algorithm-driven content |
| **Timeline** | User's own posts |
| **Stories** | Temporary 24-hour content |
| **Ranking** | Relevance-based sorting |

---

### Groups & Pages

| Feature | Description |
|---------|-------------|
| **Create Group** | Public or private communities |
| **Join Group** | Request or direct join |
| **Group Posts** | Content within groups |
| **Create Page** | Public profiles for entities |
| **Follow Page** | Subscribe to updates |
| **Page Posts** | Official announcements |

---

### Messaging System

| Feature | Description |
|---------|-------------|
| **Direct Messages** | One-on-one chat |
| **Group Chats** | Multiple participants |
| **Media Sharing** | Photos, videos, files |
| **Read Receipts** | Message status |
| **Typing Indicators** | Real-time feedback |

---

## Scope Definition Table

| Feature | Priority | Complexity | Interview Focus |
|---------|----------|------------|-----------------|
| **User Profile** | High | Low | Usually included |
| **Friend Management** | High | Medium | Core feature |
| **Posts** | High | Medium | Core feature |
| **News Feed** | High | High | Often main focus |
| **Messaging** | Medium | High | Sometimes separate |
| **Groups** | Medium | Medium | Optional |
| **Pages** | Low | Medium | Optional |
| **Stories** | Low | Medium | Rarely asked |
| **Live Video** | Low | High | Usually skipped |

---

## Clarifying Questions Template

### When asked to design Facebook, structure your questions like this:

```
"Before I start, let me clarify the scope:

1. Core Features:
   - Should I focus on the news feed, or include 
     profile management and friend connections too?
   - Are we including messaging in this design?

2. Scale & Performance:
   - What's the expected user base? Millions or billions?
   - What are the latency requirements for feed generation?

3. Content Types:
   - Do we support text, images, videos, or all?
   - Should we handle live streaming or stories?

4. Social Features:
   - Friend relationships only, or also following?
   - Do we need groups and pages?
   - Should we support reactions beyond likes?

5. Privacy & Security:
   - Different privacy levels for posts?
   - Blocking and reporting features?

6. Real-time Requirements:
   - Real-time feed updates?
   - Instant messaging delivery?
   - Live notifications?

Based on your answers, I'll design accordingly."
```

---

## Interview Response Framework

### Opening Statement:

> **"Facebook is a social networking platform with several core subsystems. The main entities are Users, Posts, Friendships, Pages, Groups, and Messages. The key operations include creating posts, managing friend connections, generating personalized feeds, and facilitating communication. Given the scope, I'd like to clarify which features to prioritize—should I focus on the news feed algorithm, the social graph, or the posting system?"**

---

### After Scope Clarification:

> **"Understood. I'll design a system that supports [confirmed features]. The main entities will be User, Post, Friendship, and Feed. I'll ensure the design handles [scale requirements] and maintains [consistency/availability requirements]. I'll use [relevant patterns] to keep the system extensible and maintainable."**

---

## Key Design Considerations

### Data Model

- **Graph-based relationships** for social connections
- **Time-series data** for posts and feed
- **Hierarchical structure** for comments
- **Many-to-many relationships** for groups and pages

---

### Scalability Challenges

| Challenge | Consideration |
|-----------|---------------|
| **Billions of users** | Partitioning strategy needed |
| **High read volume** | Caching essential |
| **Feed generation** | Pre-computation vs real-time |
| **Media storage** | CDN and object storage |
| **Real-time updates** | WebSocket infrastructure |

---

### Consistency vs Availability

| Component | Consistency Model | Rationale |
|-----------|------------------|-----------|
| **User Profile** | Strong | Critical identity data |
| **Friendships** | Strong | Accurate connections |
| **Posts** | Eventual | Slight delay acceptable |
| **Feed** | Eventual | Personalization over consistency |
| **Messages** | Strong | Delivery guarantee needed |
| **Reactions** | Eventual | Counts can be approximate |

---

## Common Interview Paths

### Path 1: News Feed Focus

**Deep dive into:**
- Feed generation algorithm
- Ranking and filtering
- Caching strategies
- Real-time updates

---

### Path 2: Social Graph Focus

**Deep dive into:**
- Friend recommendation
- Graph traversal
- Mutual friends
- Network effects

---

### Path 3: Posting System Focus

**Deep dive into:**
- Content creation
- Media handling
- Privacy controls
- Engagement tracking

---

### Path 4: Messaging Focus

**Deep dive into:**
- Real-time delivery
- Read receipts
- Group chats
- Media sharing

---

## Red Flags to Avoid

| What NOT to do | Why It's Bad |
|----------------|--------------|
| **Design everything** | Scope too large |
| **Skip clarification** | Ambiguous design |
| **Ignore scale** | Unrealistic solution |
| **Over-engineer early** | Premature optimization |
| **Forget privacy** | Security vulnerability |

---

## Green Flags That Impress

| What TO do | Why It's Good |
|------------|---------------|
| **Ask scope questions** | Shows planning |
| **Mention scale** | Shows awareness |
| **Clarify requirements** | Shows thoroughness |
| **Identify tradeoffs** | Shows maturity |
| **Start with entities** | Shows OOP thinking |

---

## Non-Functional Requirements Deep Dive

### Performance

- Feed generation: < 500ms
- Post creation: < 200ms
- Message delivery: < 100ms
- Search results: < 300ms

---

### Availability

- 99.99% uptime
- Graceful degradation
- Regional failover
- Data replication

---

### Scalability

- 3+ billion users
- 100+ million concurrent
- Billions of posts per day
- Petabytes of media

---

### Security

- Authentication required
- Authorization per resource
- Data encryption (at rest & transit)
- Privacy controls enforced

---
