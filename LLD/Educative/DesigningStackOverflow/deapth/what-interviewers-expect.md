# LLD Chapter: Interview Expectations for Stack Overflow

When interviewers give you a design problem like Stack Overflow, they don't just want classes and diagrams.

They want to see if you **deeply understand** the functional rules, user flows, constraints, and behaviour of the system.

This chapter explains **critical areas** where interviewers expect clarity.

---

## 1. Discoverability (Search & Filters)

### What?

Discoverability is the feature that lets users find relevant questions quickly:

- Search bar
- Tags
- Filters
- Sorting

---

### Why is this important?

**Stack Overflow is read-heavy.**  
95% of users browse existing answers instead of creating new ones.

So the success of the platform depends on:
- Fast search
- Accurate ranking
- Proper categorization through tags

> **Interviewers want to see if you understand query-heavy systems.**

---

### How to think about it (LLD angle)?

**Ask the interviewer:**

1. **"How can users search?"**
   - Text search? Title only? Full question body? Tags?

2. **"Can we filter questions based on tags/users?"**
   - Helps determine many-to-many relationship between `Question ↔ Tag`

These questions let you clarify:
- How search indexes should be designed
- What fields need indexing
- How to store tags efficiently
- Whether caching is needed for trending tags

---

### What interviewers expect you to mention:

| Feature | Purpose |
|---------|---------|
| **Full-text search engine** | Elasticsearch, Solr |
| **Tag-based filtering** | Many-to-many relationships |
| **Sorting** | By votes, views, activity |
| **Pagination** | Efficient data retrieval |
| **Indexes** | On (title, tags, score, created_at) |

---

### Additional points you can add:

- 🌟 Recently viewed questions cache
- 🌟 Autosuggest for search
- 🌟 Suggested similar questions
- 🌟 Query throttling for expensive searches

> **These additions make your answer look senior-level.**

---

## 2. Reputation System

### What?

A numerical score reflecting:
- Community trust
- Contribution quality
- Privilege levels

---

### Why?

**Reputation controls:**

- Ability to vote
- Ability to comment
- Ability to edit others' posts
- Ability to flag or close questions
- Privileges of moderators

> **LLD interviewers want to see whether you understand role-based and rule-based flows.**

---

### How? (Logic + workflow)

**Ask the interviewer:**

#### "How are reputation points calculated?"

| Action | Points |
|--------|--------|
| Upvote on answer | +10 |
| Upvote on question | +5 |
| Downvote received | −1 |
| Downvote cast | −2 |
| Accepted answer | +15 |

#### "What reputation threshold gives moderator privileges?"

**For example:**

| Reputation | Privilege |
|------------|-----------|
| 15 | Upvote |
| 50 | Comment everywhere |
| 2,000 | Edit others' posts |
| 3,000 | Close questions |

> **These numbers differ by design, so you must ask.**

---

### LLD notes you should mention:

- ✅ Reputation updates are **event-driven**
- ✅ Each vote triggers a `ReputationEvent`
- ✅ Privileges are checked at **runtime**
- ✅ Badges can be triggered when rep milestones are achieved

---

### Extra points to add:

- 🌟 Rate limiting reputation gains to prevent abuse
- 🌟 Logging rep changes for audit
- 🌟 Reputation rollback for fraud detection

> **This shows you think about system responsibility.**

---

## 3. Voting System

### What?

Users can:
- Upvote
- Downvote
- Vote to close
- Vote to delete
- Vote to reopen

---

### Why?

**Voting is the heart of ranking questions and answers.**

Interviewers expect you to understand:
- Who can vote
- How votes affect score
- When votes become moderation actions

---

### How? (LLD flow)

**Ask the interviewer:**

#### "Which types of votes are allowed?"

- Upvote + Downvote
- Close vote
- Reopen vote
- Delete vote (high rep users)

#### "Who can vote to close/delete?"

Usually users with higher reputation.

#### "Does voting on a closed question behave differently?"

---

### These questions expose:

- Access control
- Role checks
- Vote weightage

---

### LLD elements expected:

| Element | Details |
|---------|---------|
| **Vote entity** | (userID, postID, value) |
| **Vote history** | Track all votes |
| **Prevent duplicates** | Same user cannot vote multiple times |
| **Reputation update events** | Triggered by votes |

---

### Additional things to add:

- 🌟 Anti-vote-fraud system
- 🌟 Rate limiting (cannot vote unlimited times)
- 🌟 Instant recalculation of post score

---

## 4. Bounty System

### What?

A **bounty** is extra reputation that a question owner offers to attract better answers.

---

### Why?

Shows interviewers you understand:
- Timed workflows
- Scheduled tasks
- Reputation deductions
- Reward distribution logic

---

### How?

**Ask the interviewer:**

#### "When does a user start a bounty?"

Usually after a question goes unanswered for some time.

#### "How long does a bounty last?"

Common is **7 days**.

#### "How is bounty reputation awarded?"

- Deduct reputation instantly or after bounty ends?
- Award full bounty to selected answer?
- Automatic reward if question owner doesn't select?

---

### LLD requirements here:

| Component | Purpose |
|-----------|---------|
| **Bounty entity** | Track bounty details |
| **StartTimestamp / ExpireTimestamp** | Time tracking |
| **Job scheduler** | To expire bounty |
| **Reputation deduction rules** | Business logic |
| **Auto-award logic** | Fallback mechanism |

---

### Extra senior-level points:

- 🌟 Prevent users from offering bounty without enough reputation
- 🌟 Bounty cancellation rules
- 🌟 Notify users when bounty is near expiry

---

## 5. Additional Critical Areas (You should bring these up)

Interviewers **love** when candidates add what was not explicitly asked.

**These score extra marks.**

---

### A) Rate Limiting

| Aspect | Details |
|--------|---------|
| **What** | Limit how many questions/answers one user can post in a short interval |
| **Why** | Prevent spam |
| **How** | Token bucket per user |

---

### B) Content Moderation Workflow

| Aspect | Details |
|--------|---------|
| **What** | Flagging, reviewing, suspending |
| **Why** | Quality control |
| **How** | Moderators + privilege thresholds |

---

### C) Edit History / Versioning

| Aspect | Details |
|--------|---------|
| **What** | Every edit of Q/A is stored |
| **Why** | Auditing and rollback |
| **How** | Version table with timestamps |

---

### D) Notifications

| Aspect | Details |
|--------|---------|
| **What** | Alerts for answers, comments, accepted answers |
| **Why** | Improve engagement |
| **How** | Event-driven async queue |

---

### E) Abuse Prevention

| Aspect | Details |
|--------|---------|
| **What** | Protect against vote manipulation, spam, bots |
| **How** | • IP checks<br>• Device fingerprint<br>• Vote pattern analysis |

---

### F) High-Level Performance Mention

| Aspect | Details |
|--------|---------|
| **What** | How to handle millions of reads |
| **Why** | Stack Overflow is read-dominant |
| **How** | • Caching<br>• Tag-based indexes<br>• Denormalized search index<br>• Pagination with cursors |

---

## Summary (Interview-Ready Answer)

### If interviewer asks:

**"What do you think about when designing Stack Overflow?"**

### You answer:

> **"I clarify discoverability features like search, filters, and tag-based ranking. I understand the reputation system, because privileges depend on it. I ask about voting flows — upvotes, downvotes, close votes — and who is allowed to perform each. I confirm bounty rules since they affect reputation and involve timed workflows. I also consider moderation, versioning, rate limiting, notifications, and fraud detection."**

---

## Detailed Flow Examples

### Reputation Calculation Flow

```
Vote Cast
    │
    ▼
┌──────────────────┐
│ Validate Vote    │
│ • Not own post   │
│ • Not duplicate  │
│ • Has permission │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Store Vote       │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Update Post Score│
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Calculate Rep    │
│ Change           │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Create           │
│ ReputationEvent  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Update User Rep  │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Check Badge      │
│ Conditions       │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Award Badges     │
│ (if earned)      │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Send Notification│
└──────────────────┘
```

---

### Search Flow

```
User Query
    │
    ▼
┌──────────────────┐
│ Parse Query      │
│ • Keywords       │
│ • Tags           │
│ • Filters        │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Check Cache      │
└────────┬─────────┘
         │
    Hit  │  Miss
    ┌────┴────┐
    │         │
    ▼         ▼
Return   Search Index
Result   (Elasticsearch)
         │
         ▼
    ┌──────────────────┐
    │ Apply Filters    │
    │ • Tags           │
    │ • Date range     │
    │ • User           │
    └────────┬─────────┘
             │
             ▼
    ┌──────────────────┐
    │ Sort Results     │
    │ • Votes          │
    │ • Activity       │
    │ • Relevance      │
    └────────┬─────────┘
             │
             ▼
    ┌──────────────────┐
    │ Paginate         │
    └────────┬─────────┘
             │
             ▼
    ┌──────────────────┐
    │ Cache Result     │
    └────────┬─────────┘
             │
             ▼
    Return to User
```

---

### Bounty Lifecycle

```
Question Owner
    │
    ▼
┌──────────────────┐
│ Offer Bounty     │
│ • Specify amount │
│ • Min: 50 rep    │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Validate         │
│ • Has enough rep │
│ • Question age   │
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Deduct Reputation│
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Create Bounty    │
│ • Start time     │
│ • Expire: +7 days│
└────────┬─────────┘
         │
         ▼
┌──────────────────┐
│ Mark Question    │
│ "Featured"       │
└────────┬─────────┘
         │
    ┌────┴────┐
    │ Wait 7  │
    │ Days    │
    └────┬────┘
         │
    ┌────┴────────────┐
    │                 │
    ▼                 ▼
Owner Awards    Auto-Award
Manually        (scheduler)
    │                 │
    └────────┬────────┘
             │
             ▼
    ┌──────────────────┐
    │ Transfer Rep     │
    │ to Winner        │
    └────────┬─────────┘
             │
             ▼
    ┌──────────────────┐
    │ Send Notification│
    └──────────────────┘
```

---

## Key Interview Signals

### What Impresses Interviewers

| What You Mention | Why It Impresses |
|------------------|------------------|
| **Event-driven reputation** | Shows understanding of async processing |
| **Rate limiting** | Shows awareness of abuse prevention |
| **Search optimization** | Shows performance thinking |
| **Privilege checks** | Shows security mindset |
| **Edit versioning** | Shows data integrity awareness |
| **Anti-fraud measures** | Shows real-world experience |
| **Denormalization** | Shows scalability knowledge |

---

## Common Pitfalls to Avoid

| Pitfall | Why It's Bad | Solution |
|---------|--------------|----------|
| **Calculating score on-the-fly** | Too slow for read-heavy system | Cache score on post entity |
| **No vote fraud prevention** | System can be gamed | Implement rate limiting + IP tracking |
| **Synchronous reputation updates** | Blocks user actions | Use async event processing |
| **No privilege checking** | Security vulnerability | Check reputation before actions |
| **Hard deletes** | Lose audit trail | Use soft deletes + versioning |

---

## Interview Response Templates

### When discussing Search:

> **"For search, I'd implement full-text search using Elasticsearch. Questions would be indexed by title, body, and tags. We'd support filtering by tags, users, and date ranges, with sorting by votes, activity, or relevance. For performance, we'd cache popular searches and use pagination with cursor-based navigation."**

---

### When discussing Reputation:

> **"The reputation system is event-driven. Each vote triggers a ReputationEvent that updates the user's score and checks for badge conditions. Different actions have different point values—answer upvotes give +10, question upvotes give +5, accepted answers give +15. Reputation thresholds control privileges like voting, editing, and moderating."**

---

### When discussing Voting:

> **"The voting system needs to prevent abuse. Users can't vote on their own content, can't vote multiple times on the same post, and have rate limits. Each vote updates the post score immediately and triggers reputation calculations asynchronously. Higher reputation unlocks additional vote types like close votes and delete votes."**

---

### When discussing Bounties:

> **"Bounties are timed workflows that incentivize answers. Users offer reputation (minimum 50) that's immediately deducted. Bounties last 7 days. The asker can manually award it to any answer, or after expiration, a scheduled job auto-awards it to the highest-voted answer posted during the bounty period. This requires a job scheduler and reputation transfer logic."**

---

