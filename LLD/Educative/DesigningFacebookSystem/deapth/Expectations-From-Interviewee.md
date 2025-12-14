# LLD Chapter: Facebook System — Expectations From the Interviewee

## Topic: Identifying Key Functional Areas & Asking Scope-Defining Questions

This chapter explains **what interviewers expect**, **why these areas matter**, and **how to ask the right questions** that show maturity in system design.

---

## 1. WHAT are "interviewer expectations"?

These are the **critical functional areas** of Facebook that interviewers expect you to clarify and explore before starting the design.

### Each area represents a major subsystem:

- 🔍 User discoverability
- 👥 Friends & following
- 🔗 Groups & pages
- 🔒 Privacy & access control
- 🔔 Alerts & notifications

> **You don't need to implement all of them.**  
> But you must show awareness and ask scoping questions.

**This proves you understand the complexity of a real product like Facebook.**

---

## 2. WHY are these expectations important?

### Because:

| Reason | Impact |
|--------|--------|
| **Facebook is huge** | Interviews test your ability to narrow scope intelligently |
| **Right questions show understanding** | Real-world systems, not just classes and methods |
| **Demonstrates dual skills** | Product sense + technical depth |
| **Evaluates leadership** | Can you lead a design conversation? |

> **When you ask the correct questions, the interviewer often reveals the scope they want.**  
> This helps you avoid designing unnecessary subsystems.

---

## 3. HOW to think about each functional area

---

## A. Discoverability

### What it is

The ability for users to **find other users, pages, groups, or content**.

---

### Why it matters

**Search systems require:**
- Indexing
- Ranking
- Privacy filtering
- Multiple identifiers (name, email, phone)

> So asking questions here shows deep understanding.

---

### How to frame the questions

**Interviewers expect you to ask:**

#### How can users search for other users?

→ Text-based search? Auto-suggestions? Typeahead?

#### Can users search through phone numbers or emails?

→ This impacts data indexing and privacy.

---

### These questions show that you're thinking about:

- ✅ Search index
- ✅ Sensitive data handling
- ✅ Access control
- ✅ Performance of queries

---

## B. Friends & Following

### What it is

Two kinds of relationships:
- **Bidirectional** (friends)
- **Unidirectional** (follow/unfollow)

---

### Why it matters

This affects:

| Aspect | Impact |
|--------|--------|
| **News feed personalization** | Who's content appears |
| **Privacy rules** | Who can see what |
| **Notification triggers** | When to alert users |
| **Graph modeling** | Data structure design |

> Showing that you know both relationship types exist shows depth.

---

### How to ask the questions

**The interviewer expects:**

#### How do users add each other as friends?

→ Request flows, acceptance, rejection.

#### How can users follow others without becoming friends?

→ Helps you define different types of edges in the social graph.

---

### By asking these, you identify:

- `FriendRequest` entity
- Relationship graph
- State transitions (requested, accepted, blocked)
- Effects on feed and alerts

---

## C. Groups & Pages

### What they are

Two community-building features:

| Feature | Purpose |
|---------|---------|
| **Pages** | Public representation (brands, creators) |
| **Groups** | Community spaces |

---

### Why they matter

These introduce:
- Membership logic
- Visibility rules
- Admin roles
- Posting privileges

> **This makes the interview design richer.**

---

### Expected clarifying questions

#### How can users create groups or pages?

→ Additional entities beyond `User`.

#### What are the rules for joining a group?

→ Open, private, closed groups.

#### Does friendship affect group joining?

→ Impacts access control logic.

---

### These questions show:

- ✅ Awareness of multiple entity types
- ✅ Understanding of membership models
- ✅ Handling of join requests
- ✅ Complexity of permissions

---

## D. Privacy

### What it is

The **rules that decide who sees what**.

---

### Why it matters

Privacy is a **core part** of any social network.

It affects:

```
Feed visibility
Profile visibility
Group visibility
Search results
Post sharing
```

---

### Important questions to ask

#### How will the system handle privacy lists?

→ Custom lists like "close friends," "blocked," etc.

#### How do group privacy settings work?

→ Public vs private vs secret groups.

---

### These questions help define:

- Access control layers
- Data filtering logic
- User-specific visibility graphs

> **Asking this shows you think like a backend architect.**

---

## E. Alerts (Notifications)

### What they are

Updates for user actions like:

- Friend request
- Comment
- Tagged post
- Group post
- Message received

---

### Why they matter

Notifications tie together:

| Component | Purpose |
|-----------|---------|
| **Real-time updates** | Instant feedback |
| **Background jobs** | Async processing |
| **User preferences** | Customization |
| **Delivery channels** | Web, mobile, email |

---

### Key questions interviewers expect

#### How will users be notified of events?

→ Push? Pull? Polling?

#### Can users control what alerts they get?

→ Notification preferences subsystem.

---

### These questions demonstrate:

- ✅ Awareness of event-driven design
- ✅ Understanding of user preferences
- ✅ Thinking about scalability (high-frequency events)

---

## 4. Summary Answer You Can Give in an Interview

### When discussing Facebook LLD, say:

> **"Facebook is a large platform, so before diving into the design I want to clarify a few major functional areas — discoverability, user relationships, groups/pages, privacy models, and notification expectations."**

**Then ask the key questions in structured form.**

---

### Interviewers love candidates who:

- ✅ Break problems into subsystems
- ✅ Ask high-impact questions
- ✅ Think about privacy & permissions
- ✅ Understand relationship graphs
- ✅ Avoid over-designing

---

## Detailed Question Framework

### Area 1: Discoverability

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| How can users search for others? | Search complexity | `SearchService`, indexing strategy |
| Can users search by phone/email? | Privacy & data indexing | Access control, PII handling |
| Do we need typeahead/autocomplete? | User experience | Real-time search, caching |
| Should search respect privacy settings? | Security | Visibility filtering logic |

---

### Area 2: Friends & Following

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| How do friend requests work? | Relationship management | `FriendRequest` entity, state machine |
| Can users follow without friendship? | Social graph complexity | Two types of edges in graph |
| What happens when users block each other? | Privacy enforcement | Bidirectional relationship removal |
| Can users have friend lists? | Categorization | `FriendList` entity, grouping logic |

---

### Area 3: Groups & Pages

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| How are groups created? | Entity modeling | `Group` entity, admin roles |
| Public vs private vs secret groups? | Privacy layers | Visibility rules, access control |
| Can users be admins/moderators? | Role-based permissions | RBAC system |
| How do pages differ from profiles? | Different entity types | `Page` vs `User` distinction |

---

### Area 4: Privacy

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| What privacy levels exist? | Access control | Public, Friends, Custom, Only Me |
| How do privacy lists work? | Fine-grained control | `PrivacyList` entity |
| Can users control who sees posts? | Per-post privacy | Privacy metadata per post |
| How does privacy affect feed? | Filtering logic | Feed generation algorithm |

---

### Area 5: Notifications

| Question | Why It Matters | Design Impact |
|----------|----------------|---------------|
| What events trigger notifications? | Event catalog | Event types enumeration |
| Real-time or batch notifications? | Architecture choice | Push vs pull, WebSocket vs polling |
| Can users customize preferences? | User control | `NotificationPreferences` entity |
| How to handle high notification volume? | Scalability | Queue system, rate limiting |

---

## Interview Flow Template

### Step 1: Opening (30 seconds)

```
"Facebook is a complex social platform with multiple subsystems. 
Before designing, I'd like to understand the scope by exploring 
five key areas: discoverability, relationships, communities, 
privacy, and notifications."
```

---

### Step 2: Structured Questions (2-3 minutes)

```
Discoverability:
- "How should users find each other? Search by name? Email? Phone?"
- "Do we need real-time search suggestions?"

Relationships:
- "Are we supporting both friendship and following?"
- "How do friend requests work? Can they be customized?"

Groups & Pages:
- "Should I design groups and pages, or focus on core social features?"
- "If yes, what privacy levels do groups support?"

Privacy:
- "What privacy controls exist for posts and profiles?"
- "Do we need custom friend lists for privacy?"

Notifications:
- "Which events trigger notifications?"
- "Real-time push or periodic fetch?"
```

---

### Step 3: Scope Confirmation (30 seconds)

```
"Based on your answers, I'll focus on [confirmed scope]. 
I'll design entities for User, Post, Friendship, and Feed, 
with privacy controls and basic notification support. 
Does this align with what you're looking for?"
```

---

## Design Impact Matrix

| Functional Area | Entities Needed | Complexity | Common Interview Focus |
|-----------------|-----------------|------------|----------------------|
| **Discoverability** | SearchIndex, UserLookup | Medium | Sometimes |
| **Friends/Following** | Friendship, FollowRelation | High | Always |
| **Groups** | Group, Membership, GroupPost | High | Often |
| **Pages** | Page, PageFollower, PagePost | Medium | Sometimes |
| **Privacy** | PrivacySettings, PrivacyList | High | Always |
| **Notifications** | Notification, NotificationPreference | Medium | Often |

---

## Common Interview Paths

### Path 1: Core Social Graph

**Focus on:**
- User relationships
- Friend requests
- Following mechanism
- Mutual friends

**Skip:**
- Groups
- Pages
- Advanced privacy

---

### Path 2: News Feed

**Focus on:**
- Feed generation
- Post ranking
- Privacy filtering
- Real-time updates

**Skip:**
- Groups
- Advanced search
- Messaging

---

### Path 3: Privacy & Permissions

**Focus on:**
- Privacy levels
- Access control
- Visibility rules
- Custom lists

**Skip:**
- Feed algorithm
- Search
- Notifications

---

## Red Flags

| What NOT to Do | Why It's Bad |
|----------------|--------------|
| **Assume everything is in scope** | Shows poor judgment |
| **Skip privacy discussion** | Missing critical feature |
| **Ignore relationships complexity** | Incomplete design |
| **Forget about notifications** | User experience gap |
| **Over-design initially** | Wastes interview time |

---

## Green Flags

| What TO Do | Why It's Good |
|------------|---------------|
| **Ask about bidirectional vs unidirectional relationships** | Shows depth |
| **Mention privacy early** | Shows security awareness |
| **Clarify groups vs pages** | Shows product knowledge |
| **Ask about notification preferences** | Shows user-centric thinking |
| **Structure questions by area** | Shows organized thinking |

---

## Example: Perfect Question Sequence

```
1. "Before I start, let me understand the relationship model. 
   Do we support both friendship (bidirectional) and following 
   (unidirectional)? This affects my graph design."

2. "For discoverability, can users search by email or phone, 
   or just by name? This impacts privacy and indexing."

3. "Should the design include groups and pages, or should I 
   focus on the core user-to-user social features?"

4. "What privacy levels exist? Public, friends-only, custom 
   lists? This is crucial for feed and post visibility."

5. "For notifications, are we doing real-time push or periodic 
   polling? And can users customize what they're notified about?"
```

> **This sequence shows: technical depth, product sense, and systematic thinking.**

---

## Evaluation Criteria

### What Interviewers Score:

| Criteria | What They Look For |
|----------|-------------------|
| **Scoping Ability** | Can you narrow down a massive system? |
| **Product Sense** | Do you understand user needs? |
| **Technical Depth** | Do you know implementation implications? |
| **Communication** | Can you ask clear, structured questions? |
| **Priority** | Can you identify what's most important? |

---
