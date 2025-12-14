# LLD Chapter: Facebook System — Design Approach

## Topic: Bottom-Up Design Strategy & Applicable Design Patterns

---

## 1. WHAT is the Bottom-Up Design Approach?

This is a **component-first design workflow** where you identify the smallest, most atomic building blocks of the system and then slowly assemble them into bigger, composite components.

### Smallest Components (Atoms)

**Examples:**

- `User`
- `Post`
- `Comment`
- `Reaction` (like, love, etc.)
- `FriendRequest`
- `Media` (photo/video)

---

### Larger Components (Molecules)

**Built from smaller ones:**

- `Profile`
- `Page`
- `Group`
- `Timeline`
- `Messenger Conversation`

---

### Largest Components (Ecosystem)

- News Feed
- Notification System
- Entire Facebook Platform

> **This method mirrors how complex systems are built in real life.**

---

## 2. WHY is Bottom-Up Design preferred in LLD interviews?

### Interviewers look for:

- ✅ Strong domain modeling
- ✅ Clean class design
- ✅ Understanding of relationships between entities
- ✅ Ability to extend features later without breaking the system

---

### Bottom-up design gives you:

| Benefit | Description |
|---------|-------------|
| **Clear, reusable components** | Each component has well-defined responsibility |
| **Better handling of complexity** | Break down large problems systematically |
| **Easier relationship thinking** | Natural composition emerges |
| **Structured and scalable design** | Foundation for growth |

> **It avoids the mistake many candidates make:**  
> Starting with the biggest module and then struggling to define the supporting components.

---

## 3. HOW to apply the Bottom-Up Approach for Facebook

### Step 1 — Identify the atomic entities

You start by modeling:

```
Post → content, creator, timestamp, media
Comment → commenter, text, timestamp
Reaction → type, user
User → attributes, settings
FriendRequest → sender, receiver, status
```

> **These are simple and well-defined.**

---

### Step 2 — Combine these to design mid-level components

**Examples:**

#### UserProfile
Built using: `User` + `Posts` + `Friends` + `Photos`

#### Page
Built using: `Posts` + `Admins` + `Followers`

#### Group
Built using: `Members` + `Posts` + `Admins` + `Privacy settings`

> **You take the base components and assemble them into logical units.**

---

### Step 3 — Build full subsystems

**Examples:**

#### Feed Engine
Combines: `Posts` + `Relationships` + `Engagement` + `Ranking`

#### Messaging System
Combines: `Conversations` + `Messages` + `Users`

#### Notification System
Combines: `Events` + `Subscriptions` + `Delivery rules`

> **By now, the design becomes scalable and clear.**

### This is exactly what interviewers want to see:

**Independent layers that build up into a complete product.**

---

## 4. WHAT design patterns apply to the Facebook LLD?

Mentioning design patterns **builds credibility** because it shows deep understanding of OOP modeling.

Here are the relevant patterns:

---

### A. Composite Pattern

#### Why?

- Posts can contain multiple media items
- Comments can contain replies (hierarchical structure)

#### The composite pattern fits for:

- Comment threads
- Nested replies
- Group/page structures

```java
interface Component {
    void display();
}

class Comment implements Component {
    private String text;
    private List<Comment> replies; // Nested structure
    
    public void display() {
        System.out.println(text);
        for (Comment reply : replies) {
            reply.display();
        }
    }
}

class Media implements Component {
    private String url;
    
    public void display() {
        System.out.println("Media: " + url);
    }
}

class Post implements Component {
    private String content;
    private List<Component> components; // Can have media, nested comments
    
    public void display() {
        System.out.println(content);
        for (Component component : components) {
            component.display();
        }
    }
}
```

> **It shows you understand hierarchical modeling.**

---

### B. Observer Pattern ⭐

#### Where?

**Notifications.**

#### When:

- Someone likes your post
- Someone comments
- You receive a friend request

**User is the "observer," activity producers are the "subjects."**

```java
interface NotificationObserver {
    void onNotification(Notification notification);
}

class User implements NotificationObserver {
    private String name;
    
    public void onNotification(Notification notification) {
        // Display notification to user
        System.out.println(name + " received: " + notification.getMessage());
    }
}

class Post {
    private List<NotificationObserver> observers = new ArrayList<>();
    
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }
    
    public void addLike(User liker) {
        // Like logic
        notifyObservers(new Notification(liker.getName() + " liked your post"));
    }
    
    private void notifyObservers(Notification notification) {
        for (NotificationObserver observer : observers) {
            observer.onNotification(notification);
        }
    }
}
```

> **Mentioning this pattern is a very strong point in design interviews.**

---

### C. Factory Pattern

#### Why?

Facebook has multiple post types:

- Text
- Image
- Video
- Story
- Reel

**Factory can be used to generate appropriate Post objects.**

```java
enum PostType {
    TEXT, IMAGE, VIDEO, STORY, REEL
}

interface Post {
    void display();
}

class TextPost implements Post {
    private String content;
    public void display() { /* implementation */ }
}

class ImagePost implements Post {
    private String content;
    private String imageUrl;
    public void display() { /* implementation */ }
}

class VideoPost implements Post {
    private String content;
    private String videoUrl;
    public void display() { /* implementation */ }
}

class PostFactory {
    public static Post createPost(PostType type, Map<String, Object> data) {
        switch(type) {
            case TEXT:
                return new TextPost((String) data.get("content"));
            case IMAGE:
                return new ImagePost((String) data.get("content"), 
                                    (String) data.get("imageUrl"));
            case VIDEO:
                return new VideoPost((String) data.get("content"), 
                                    (String) data.get("videoUrl"));
            default:
                throw new IllegalArgumentException("Unknown post type");
        }
    }
}
```

> **Shows strong OO thinking.**

---

### D. Strategy Pattern

#### Where?

**Feed ranking algorithms.**

#### Different ranking strategies:

- Most recent first
- Most relevant first
- Engagement-based ranking

**The system can switch ranking strategies.**

```java
interface FeedRankingStrategy {
    List<Post> rank(List<Post> posts, User user);
}

class MostRecentStrategy implements FeedRankingStrategy {
    public List<Post> rank(List<Post> posts, User user) {
        return posts.stream()
            .sorted(Comparator.comparing(Post::getTimestamp).reversed())
            .collect(Collectors.toList());
    }
}

class EngagementBasedStrategy implements FeedRankingStrategy {
    public List<Post> rank(List<Post> posts, User user) {
        return posts.stream()
            .sorted(Comparator.comparing(Post::getEngagementScore).reversed())
            .collect(Collectors.toList());
    }
}

class RelevanceBasedStrategy implements FeedRankingStrategy {
    public List<Post> rank(List<Post> posts, User user) {
        // ML-based relevance scoring
        return rankedPosts;
    }
}

class NewsFeed {
    private FeedRankingStrategy strategy;
    
    public void setStrategy(FeedRankingStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<Post> generateFeed(User user) {
        List<Post> posts = fetchPosts(user);
        return strategy.rank(posts, user);
    }
}
```

> **Highly appreciated by interviewers.**

---

### E. Singleton Pattern

#### Where?

**Resource-heavy components:**

- Feed Ranking Engine
- Cache Manager
- Notification Dispatcher

**Only one instance should coordinate these services.**

```java
class FeedRankingEngine {
    private static volatile FeedRankingEngine instance;
    
    private FeedRankingEngine() {
        // Initialize heavy resources
    }
    
    public static FeedRankingEngine getInstance() {
        if (instance == null) {
            synchronized (FeedRankingEngine.class) {
                if (instance == null) {
                    instance = new FeedRankingEngine();
                }
            }
        }
        return instance;
    }
    
    public List<Post> rankFeed(List<Post> posts, User user) {
        // Ranking logic
    }
}
```

---

### F. State Pattern

#### Where?

**Friend request flow:**

```
Pending → Accepted
Pending → Declined
Pending → Blocked
```

**Helps manage transitions cleanly.**

```java
interface FriendRequestState {
    void accept(FriendRequest request);
    void decline(FriendRequest request);
    void cancel(FriendRequest request);
}

class PendingState implements FriendRequestState {
    public void accept(FriendRequest request) {
        request.setState(new AcceptedState());
        // Create friendship
    }
    
    public void decline(FriendRequest request) {
        request.setState(new DeclinedState());
    }
    
    public void cancel(FriendRequest request) {
        request.setState(new CancelledState());
    }
}

class AcceptedState implements FriendRequestState {
    public void accept(FriendRequest request) {
        throw new IllegalStateException("Already accepted");
    }
    
    public void decline(FriendRequest request) {
        throw new IllegalStateException("Already accepted");
    }
    
    public void cancel(FriendRequest request) {
        throw new IllegalStateException("Cannot cancel accepted request");
    }
}

class FriendRequest {
    private FriendRequestState state;
    
    public void accept() {
        state.accept(this);
    }
    
    public void decline() {
        state.decline(this);
    }
}
```

---

## 5. HOW to bring this up in interviews

### After clarifying requirements, say:

> **"I will use a bottom-up design approach, starting with atomic components like Post and Comment, then building larger units like Profiles, Pages, and Groups. Additionally, several design patterns naturally apply here — for example, Observer for notifications, Composite for nested comments, Strategy for feed ranking, and State for friend request transitions."**

> **This single paragraph impresses the interviewer instantly.**

---

## 6. Summary

This chapter establishes:

- ✅ A clear design approach
- ✅ Clean reasoning behind choosing bottom-up
- ✅ Awareness of relevant design patterns
- ✅ A structured path to model Facebook piece by piece

---

## Design Pattern Mapping

| Pattern | Use Case | Benefit |
|---------|----------|---------|
| **Composite** | Nested comments, multi-media posts | Hierarchical structure management |
| **Observer** | Notifications, real-time updates | Event-driven architecture |
| **Factory** | Creating different post types | Centralized object creation |
| **Strategy** | Feed ranking algorithms | Runtime algorithm selection |
| **Singleton** | Feed engine, cache manager | Single global instance |
| **State** | Friend request lifecycle | Clean state transitions |

---

## Bottom-Up Design Flow

```
Level 1: Atomic Components
├── User
├── Post
├── Comment
├── Reaction
├── FriendRequest
└── Media

Level 2: Composite Components
├── UserProfile (User + Posts + Friends)
├── Page (Posts + Admins + Followers)
├── Group (Members + Posts + Privacy)
└── Timeline (Posts + ordering)

Level 3: Subsystems
├── Feed Engine (Posts + Ranking + Filtering)
├── Notification System (Events + Delivery)
├── Messaging System (Conversations + Messages)
└── Search Engine (Indexing + Ranking)

Level 4: Full Platform
└── Facebook System
    ├── All Subsystems Integrated
    ├── API Gateway
    └── Analytics & Reporting
```

---

## Architecture Visualization

```
┌─────────────────────────────────────────────────────┐
│              Facebook Platform                       │
└─────────────────────────────────────────────────────┘
                      │
        ┌─────────────┼─────────────┐
        │             │             │
   ┌────▼────┐   ┌────▼────┐   ┌───▼────┐
   │ Feed    │   │ Social  │   │Notif   │
   │ System  │   │ Graph   │   │System  │
   └────┬────┘   └────┬────┘   └───┬────┘
        │             │             │
   Strategy      Composite      Observer
   Pattern        Pattern       Pattern
        │             │             │
   ┌────▼────┐   ┌────▼────┐   ┌───▼────┐
   │Ranking  │   │Post     │   │Event   │
   │Algos    │   │Comment  │   │Delivery│
   └─────────┘   └─────────┘   └────────┘
```

---

## SOLID Principles Application

### Single Responsibility Principle (SRP)

```java
class Post {
    // Only handles post data
}

class PostService {
    // Only handles post operations
}

class FeedGenerator {
    // Only handles feed generation
}
```

---

### Open/Closed Principle (OCP)

```java
// New ranking strategies can be added without modifying existing code
class NewMLBasedStrategy implements FeedRankingStrategy {
    public List<Post> rank(List<Post> posts, User user) {
        // New algorithm
    }
}
```

---

### Liskov Substitution Principle (LSP)

```java
// All Post types can substitute the Post interface
Post post = new TextPost();
post = new ImagePost(); // Works seamlessly
post = new VideoPost(); // Works seamlessly
```

---

### Interface Segregation Principle (ISP)

```java
interface Likeable {
    void like(User user);
}

interface Commentable {
    void addComment(Comment comment);
}

interface Shareable {
    void share(User user);
}

// Posts implement only what they need
class Post implements Likeable, Commentable, Shareable {
    // Implementation
}
```

---

### Dependency Inversion Principle (DIP)

```java
class FeedService {
    private FeedRankingStrategy strategy; // Depends on abstraction
    private PostRepository repository;     // Depends on interface
    
    // Not dependent on concrete implementations
}
```

---

## Advanced Concepts

### Event-Driven Architecture

```
User Action (Like, Comment, Share)
    │
    ▼
┌──────────────┐
│  Event Bus   │
└──────┬───────┘
       │
       ├──► Notification Service
       ├──► Feed Update Service
       ├──► Analytics Service
       └──► Recommendation Engine
```

---

### Caching Strategy

```
Feed Request
    │
    ▼
┌──────────────┐
│ Redis Cache  │
└──────┬───────┘
       │
  Hit  │  Miss
  ┌────┴────┐
  │         │
  ▼         ▼
Return   Generate
         (Expensive)
         │
         ▼
     Cache Result
         │
         ▼
       Return
```

---

### Asynchronous Processing

```java
class NotificationService {
    private ExecutorService executor;
    
    public void sendNotification(Notification notification) {
        // Non-blocking notification delivery
        executor.submit(() -> {
            deliverNotification(notification);
            logNotification(notification);
            updateAnalytics(notification);
        });
    }
}
```

---

## Interview Response Template

### When discussing design approach:

> **"I'll use a bottom-up approach for Facebook. Starting with atomic entities like User, Post, and Comment, I'll build composite structures like UserProfile and Groups. This ensures each component is well-defined before integration. The design naturally supports several patterns: Composite for nested content, Observer for notifications, Strategy for feed ranking, and State for request workflows. This approach ensures the system is maintainable and extensible."**

---

### When discussing specific patterns:

> **"For the news feed, I'll use the Strategy pattern to support different ranking algorithms—chronological, engagement-based, or ML-driven—which can be swapped at runtime. For notifications, the Observer pattern fits perfectly since multiple observers (user devices) need updates when events occur. The Composite pattern handles nested comments elegantly, allowing uniform treatment of single comments and comment threads."**

---

## Interview Bonus Points

| Topic | What to Say | Why It Impresses |
|-------|-------------|------------------|
| **Composite Pattern** | "Nested comments and multi-media posts" | Shows hierarchical thinking |
| **Observer Pattern** | "Real-time notifications and feed updates" | Shows event-driven knowledge |
| **Strategy Pattern** | "Pluggable feed ranking algorithms" | Shows flexibility awareness |
| **SOLID Principles** | "Each service has single responsibility" | Shows OOP maturity |
| **Event-Driven** | "Async event bus for notifications" | Shows scalability thinking |

---

## Common Mistakes to Avoid

| Mistake | Why It's Bad | Solution |
|---------|--------------|----------|
| **Starting with UI** | Wrong layer | Start with domain entities |
| **Monolithic Post class** | God object anti-pattern | Use Factory for different types |
| **Tight coupling** | Hard to extend | Use interfaces and dependency injection |
| **Synchronous notifications** | Performance bottleneck | Use Observer with async delivery |
| **No ranking strategy** | Inflexible feed | Use Strategy pattern |

---
