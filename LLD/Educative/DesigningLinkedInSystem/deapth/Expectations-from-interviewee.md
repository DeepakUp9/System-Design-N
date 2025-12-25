# LinkedIn System - Expectations from the Interviewee

## 📋 Overview
This document outlines what interviewers expect from candidates when tackling the LinkedIn LLD problem. It focuses on **design maturity** and **scoping ability**, not just coding skills.

---

## 1. WHAT is the Interviewer Really Evaluating Here?

### What This Section Represents in LLD Terms

| Evaluation Area | What They're Looking For |
|-----------------|-------------------------|
| **Domain Identification** | Your ability to identify core domains |
| **Clarifying Questions** | Your skill in asking the right questions |
| **Behavioral Rules** | Your understanding of business logic, not UI |
| **Scoping Ability** | Your judgment on what to include/exclude |

### What Interviewers Expect Implicitly

✅ You **won't design "everything"**  
✅ You'll convert **vague product features** into **clear object responsibilities**  
✅ You'll understand **relationships between entities**  
✅ You'll separate **concerns** appropriately

### Strong Signal to Send

> **"I treat each feature as a domain, not as a screen."**

This shows you think in terms of **bounded contexts** and **object responsibilities**, not UI flows.

---

## 2. Discoverability (Search)

### WHAT

Ability to find:

* 👤 **Users (profiles)** - By name, headline, skills, company
* 👥 **Groups** - Professional communities
* 🏢 **Company pages** - Organizational profiles
* 💼 **Jobs** - Optional in LLD scope (can be mentioned)

### WHY

| Reason | Impact |
|--------|--------|
| **Entry point** | Search is the gateway to all LinkedIn interactions |
| **Network effect** | Poor discoverability breaks networking entirely |
| **Professional context** | Profession-based search ≠ keyword-only search |
| **Privacy-aware** | Results must respect connection degrees and settings |

### HOW (LLD View)

Search is **read-heavy** and **criteria-driven**.

**Results depend on:**

```java
public class SearchCriteria {
    // Profile attributes
    private String name;
    private String headline;
    private List<String> skills;
    private String currentCompany;
    private String location;
    
    // Privacy context
    private Profile searcher;
    private int maxConnectionDegree; // 1, 2, 3
    
    // Result filtering
    private int limit;
    private int offset;
}
```

**Three key factors:**

1. **Profile attributes** - Name, skills, company, location
2. **Privacy rules** - Who can see what
3. **Connection degree** - 1st, 2nd, 3rd degree or outside network

### LLD Implication

❌ **Don't do this:**
```java
public class User {
    // Bad: Search logic inside entity
    public List<User> search(String query) { ... }
}
```

✅ **Do this instead:**
```java
public class SearchService {
    public SearchResult searchProfiles(SearchCriteria criteria) {
        // 1. Query indexed data
        List<Profile> results = searchIndex.query(criteria);
        
        // 2. Apply privacy filters
        results = applyPrivacyRules(results, criteria.getSearcher());
        
        // 3. Rank by relevance and connection degree
        results = rankResults(results, criteria.getSearcher());
        
        return new SearchResult(results, totalCount);
    }
}
```

**Key points:**
* Search logic should **not live inside User**
* Usually exposed via a **SearchService**
* Entities must be **index-friendly**, not tightly coupled

### Interview Insight

> **"Search operates on indexed projections of profiles rather than full domain objects."**

**What this means:**
* Search doesn't query full Profile objects
* Uses lightweight, indexed representations (Elasticsearch, Solr)
* Privacy filters applied post-query

---

## 3. Connections and Following

### WHAT

| Relationship Type | Description | Directionality |
|-------------------|-------------|----------------|
| **Connection** | Mutual relationship (professional trust) | Bidirectional |
| **Following** | One-way relationship (interest-based) | Unidirectional |

### WHY

| Reason | Explanation |
|--------|-------------|
| **Professional trust model** | Connections imply mutual endorsement |
| **Visibility control** | Drives what content you see |
| **Feed relevance** | Connected users' content prioritized |
| **Notification triggers** | Connection accepts, follows, etc. |

### HOW (LLD View)

#### Connections Need State Management

```
Request → Pending → Accepted / Rejected / Withdrawn
```

**State machine:**
```java
public enum ConnectionStatus {
    PENDING,      // Request sent, awaiting response
    ACCEPTED,     // Both parties connected
    REJECTED,     // Request declined
    WITHDRAWN     // Requester cancelled
}

public class Connection {
    private String connectionId;
    private Profile requester;
    private Profile requestee;
    private ConnectionStatus status;
    private LocalDateTime requestedAt;
    private LocalDateTime respondedAt;
    
    public void accept() {
        if (status != ConnectionStatus.PENDING) {
            throw new IllegalStateException("Can only accept pending requests");
        }
        this.status = ConnectionStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }
    
    public void reject() {
        if (status != ConnectionStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending requests");
        }
        this.status = ConnectionStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }
}
```

#### Following is Stateless

```java
public class Follow {
    private String followId;
    private Profile follower;
    private Profile followedProfile;  // or CompanyPage
    private LocalDateTime followedAt;
    
    // No state management needed - just create or delete
}
```

**Operations:**
* **Follow** - Create Follow entity
* **Unfollow** - Delete Follow entity

### LLD Implication

✅ **Connection should be a separate entity**
* Has lifecycle (status transitions)
* Requires validation (can't connect twice)
* Affects discoverability and privacy

✅ **Status-driven lifecycle** (important interview signal)
* Use state machine pattern
* Validate state transitions

✅ **Following can be modeled as a lightweight relationship**
* Simple create/delete
* No approval flow

### Strong Interview Statement

> **"Connection is a stateful domain object with lifecycle management, while follow is a simple association with create/delete semantics."**

---

## 4. Groups, Pages, and Jobs

### WHAT

| Entity Type | Description |
|-------------|-------------|
| **Groups** | Community-based interactions around topics/interests |
| **Company Pages** | Organizational identity and content hub |
| **Jobs** | Structured opportunities linked to companies |

### WHY

LinkedIn is **not only person-to-person**:

* **Organizations are first-class citizens** - Not just attributes
* **Community matters** - Groups foster professional discussions
* **Jobs tie identity to opportunity** - Core value proposition

### HOW (LLD View)

#### 1. Groups

**Characteristics:**
* Created by users (creator becomes admin)
* Users can join/leave (may require approval)
* Posts are group-scoped
* Has members and moderators

```java
public class Group {
    private String groupId;
    private String name;
    private String description;
    private Profile creator;
    private List<GroupMember> members;
    private GroupPrivacy privacy; // PUBLIC, PRIVATE
    private LocalDateTime createdAt;
}

public class GroupMember {
    private Profile profile;
    private Group group;
    private MemberRole role; // ADMIN, MODERATOR, MEMBER
    private LocalDateTime joinedAt;
}

public class GroupPost {
    private Group group;
    private Profile author;
    private String content;
    // Only visible to group members
}
```

#### 2. Company Pages

**Characteristics:**
* Managed by designated admins
* Users can **follow** (no approval needed)
* Can publish jobs and posts
* Has followers, not members

```java
public class CompanyPage {
    private String companyId;
    private String name;
    private String industry;
    private String description;
    private List<CompanyAdmin> admins;
    private List<Follow> followers; // Users who follow this page
    private List<Job> jobs;
    private LocalDateTime createdAt;
}

public class CompanyAdmin {
    private Profile profile;
    private CompanyPage company;
    private AdminRole role; // OWNER, ADMIN, RECRUITER
}
```

#### 3. Jobs

**Characteristics:**
* Owned by company pages
* Applied by users
* Application has state

```java
public class Job {
    private String jobId;
    private CompanyPage company;
    private String title;
    private String description;
    private String location;
    private JobType type; // FULL_TIME, PART_TIME, CONTRACT
    private LocalDateTime postedAt;
    private JobStatus status; // OPEN, CLOSED
}

public class JobApplication {
    private String applicationId;
    private Job job;
    private Profile applicant;
    private String coverLetter;
    private String resumeUrl;
    private ApplicationStatus status; // APPLIED, REVIEWED, REJECTED, HIRED
    private LocalDateTime appliedAt;
}
```

### LLD Implication

❌ **Group and CompanyPage should NOT inherit from User**
* Different responsibilities
* Different access patterns
* Different visibility rules

✅ **Jobs are entities, not attributes**
* Have their own lifecycle
* Link users to companies
* Track application state

### Design Relationships

```
CompanyPage
├── Jobs (1:N)
│   └── JobApplications (1:N)
├── Followers (N:N via Follow)
└── Admins (N:N via CompanyAdmin)

Group
├── Members (N:N via GroupMember)
├── Posts (1:N via GroupPost)
└── Creator (1:1 with Profile)
```

---

## 5. Alerts / Notifications

### WHAT

System-generated updates about:

| Notification Type | Example |
|-------------------|---------|
| **Messages** | "John sent you a message" |
| **Connection requests** | "Jane wants to connect" |
| **Post interactions** | "Sarah commented on your post" |
| **Job alerts** | "New jobs matching your profile" |
| **Profile views** | "Your profile was viewed by 5 people" |
| **Endorsements** | "Mike endorsed you for Java" |

### WHY

| Reason | Impact |
|--------|--------|
| **Engagement driver** | Brings users back to platform |
| **Asynchronous by nature** | Actions don't block on notification delivery |
| **Multi-channel** | In-app, email, push notifications |
| **Decouples actions from awareness** | User doesn't need to be online |

### HOW (LLD View)

**Triggered by events:**
```java
public interface DomainEvent {
    String getEventId();
    LocalDateTime getOccurredAt();
}

public class ConnectionAcceptedEvent implements DomainEvent {
    private Connection connection;
    private LocalDateTime occurredAt;
}

public class PostCommentedEvent implements DomainEvent {
    private Post post;
    private Comment comment;
    private LocalDateTime occurredAt;
}
```

**Delivered via channels:**
```java
public enum NotificationChannel {
    IN_APP,     // Show in UI
    EMAIL,      // Send email
    PUSH,       // Mobile push notification
    SMS         // Text message (rare)
}

public class Notification {
    private String notificationId;
    private Profile recipient;
    private NotificationType type;
    private String content;
    private String actionUrl; // Deep link
    private boolean isRead;
    private LocalDateTime createdAt;
}

public class NotificationService {
    
    @EventListener
    public void onConnectionAccepted(ConnectionAcceptedEvent event) {
        Connection conn = event.getConnection();
        
        // Notify requester
        Notification notification = new Notification(
            conn.getRequester(),
            NotificationType.CONNECTION_ACCEPTED,
            conn.getRequestee().getName() + " accepted your connection request"
        );
        
        notificationRepository.save(notification);
        deliveryService.deliver(notification, NotificationChannel.IN_APP);
    }
}
```

### LLD Implication

✅ **Notification logic must be decoupled**
* Don't call notification creation in entity methods
* Use event-driven approach

✅ **Usually handled via NotificationService**
* Subscribes to domain events
* Creates and delivers notifications

✅ **Notifications reference entities, not embed them**
* Store entity IDs, not full objects
* Fetch fresh data when notification is viewed

### Interview-Friendly Line

> **"Notifications subscribe to domain events instead of being directly invoked, ensuring loose coupling and asynchronous processing."**

---

## 6. Additional Things Worth Mentioning (Value Add)

### Privacy & Visibility

Different profile sections can have different visibility:

```java
public class ProfilePrivacy {
    private VisibilityLevel emailVisibility;      // CONNECTIONS, NETWORK, PUBLIC
    private VisibilityLevel phoneVisibility;
    private VisibilityLevel connectionListVisibility;
    private VisibilityLevel lastSeenVisibility;
}

public enum VisibilityLevel {
    CONNECTIONS_ONLY,  // Only 1st degree
    NETWORK,           // Up to 3rd degree
    PUBLIC             // Anyone
}
```

**LLD implication:**
* Search results must respe