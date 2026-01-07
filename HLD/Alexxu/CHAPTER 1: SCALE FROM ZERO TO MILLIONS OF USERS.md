# System Design Interview Q&A Guide
## Chapter 1: Scale From Zero to Millions of Users

> **Interview Focus**: This chapter tests conceptual clarity and reasoning, not deep implementation details.

---

## 📌 Quick Reference

**One-Line Interview Summary:**
> Stateless web tier + Load balancer + Cache + CDN + Replication + Sharding + Monitoring = scalable system

---

## 1️⃣ Basics & Request Flow

### Q1. How does a request flow from a user to a web server?

**Answer:**
When a user enters a domain name, DNS resolves it to an IP address. The browser then sends an HTTP request to that IP. The web server processes the request and returns an HTML page or JSON response.

### Q2. Why is DNS usually handled by third-party providers?

**Answer:**
DNS needs to be highly available and globally distributed. Third-party DNS providers already have that infrastructure, so hosting DNS ourselves would be expensive and unreliable.

---

## 2️⃣ Single Server Limitations

### Q3. What are the problems with a single-server setup?

**Answer:**
It creates a single point of failure, cannot handle high traffic, and scaling is limited by hardware constraints.

### Q4. Why do we separate web tier and database tier?

**Answer:**
Separating tiers allows independent scaling. Web servers can scale based on traffic, and databases can scale based on data and query load.

---

## 3️⃣ Database Choices

### Q5. When would you choose SQL over NoSQL?

**Answer:**
When data is relational, requires joins, and strong consistency is needed—such as financial or transactional systems.

### Q6. When is NoSQL a better choice?

**Answer:**
When the application needs low latency, high scalability, flexible schema, or handles large volumes of unstructured data.

---

## 4️⃣ Vertical vs Horizontal Scaling

### Q7. What is vertical scaling?

**Answer:**
Vertical scaling means adding more CPU, RAM, or disk to a single server.

### Q8. Why is vertical scaling not suitable for large systems?

**Answer:**
It has hardware limits, creates a single point of failure, and becomes very expensive.

### Q9. What is horizontal scaling?

**Answer:**
Horizontal scaling means adding more servers and distributing traffic across them.

### Q10. Why is horizontal scaling preferred?

**Answer:**
It improves availability, supports failover, and allows the system to grow incrementally.

---

## 5️⃣ Load Balancer

### Q11. Why do we need a load balancer?

**Answer:**
To distribute traffic evenly across multiple servers, improve availability, and handle server failures gracefully.

### Q12. What happens if one web server goes down?

**Answer:**
The load balancer stops routing traffic to that server and redirects traffic to healthy servers.

### Q13. Why do backend servers use private IPs?

**Answer:**
For security. Private IPs are not accessible from the internet, so only the load balancer can communicate with them.

### Q14. What are sticky sessions and why are they avoided?

**Answer:**
Sticky sessions bind a user to one server, which hurts scalability and failover. Stateless design is preferred instead.

---

## 6️⃣ Database Replication

### Q15. Explain master–slave replication.

**Answer:**
The master database handles writes, and slave databases replicate data from the master and handle read operations.

### Q16. Why are there usually more read replicas than masters?

**Answer:**
Most applications are read-heavy, so distributing reads improves performance.

### Q17. What happens if the master database fails?

**Answer:**
A slave is promoted to master, and a new slave is created for replication.

---

## 7️⃣ Caching

### Q18. What is caching and why is it important?

**Answer:**
Caching stores frequently accessed data in memory to reduce database load and improve response time.

### Q19. What is a read-through cache?

**Answer:**
The application first checks the cache. If data is missing, it fetches from the database, stores it in cache, and returns it.

### Q20. What data should be cached?

**Answer:**
Frequently read and infrequently updated data.

### Q21. Why do we need cache expiration?

**Answer:**
To prevent stale data and free up memory.

### Q22. What is cache eviction?

**Answer:**
When cache is full, old data is removed using policies like LRU or LFU.

---

## 8️⃣ CDN (Content Delivery Network)

### Q23. What is a CDN?

**Answer:**
A CDN is a network of geographically distributed servers that deliver static content closer to users.

### Q24. What content should be served via CDN?

**Answer:**
Static assets like images, CSS, JavaScript, and videos.

### Q25. What happens if content is not found in CDN?

**Answer:**
The CDN fetches it from the origin server, caches it, and then serves it to the user.

### Q26. How do you invalidate CDN cache?

**Answer:**
By using CDN APIs or by versioning URLs.

---

## 9️⃣ Stateless vs Stateful Architecture

### Q27. What is a stateful web server?

**Answer:**
A server that stores user session data locally.

### Q28. Why is stateful architecture problematic?

**Answer:**
Requests must go to the same server, making scaling and failover difficult.

### Q29. What is a stateless web tier?

**Answer:**
A design where web servers do not store session data locally. State is stored in shared storage.

### Q30. Why is stateless architecture better for scaling?

**Answer:**
Any request can go to any server, making auto-scaling easy.

---

## 🔟 Multi-Data Center Design

### Q31. Why do we need multiple data centers?

**Answer:**
To reduce latency and improve availability during regional failures.

### Q32. What is GeoDNS?

**Answer:**
A DNS-based routing mechanism that directs users to the nearest data center.

### Q33. What is the biggest challenge in multi-data center systems?

**Answer:**
Data synchronization and consistency across regions.

---

## 1️⃣1️⃣ Message Queue

### Q34. What is a message queue?

**Answer:**
A system that enables asynchronous communication between services using producers and consumers.

### Q35. Why are message queues useful?

**Answer:**
They decouple services, improve reliability, and allow independent scaling.

### Q36. Give a real-world use case of message queues.

**Answer:**
Image processing or email sending, where tasks can be processed asynchronously.

---

## 1️⃣2️⃣ Monitoring & Automation

### Q37. Why are metrics important?

**Answer:**
They help monitor system health, performance, and business KPIs.

### Q38. What metrics would you track?

**Answer:**
CPU, memory, request latency, error rate, and user activity.

### Q39. Why is automation critical at scale?

**Answer:**
Manual processes don't scale. Automation improves reliability and developer productivity.

---

## 1️⃣3️⃣ Database Sharding

### Q40. What is sharding?

**Answer:**
Sharding splits a database into smaller parts to distribute data across multiple servers.

### Q41. What is a sharding key?

**Answer:**
A column used to determine how data is distributed across shards.

### Q42. What is resharding and when is it required?

**Answer:**
Resharding is the process of redistributing data across shards when existing shards become too large or unevenly loaded.

### Q43. Why does uneven data distribution happen in sharding?

**Answer:**
Because some shard keys receive much more traffic or data than others, leading to shard exhaustion.

### Q44. What is the celebrity (hotspot key) problem?

**Answer:**
When a small number of keys receive disproportionately high traffic, causing a single shard to become overloaded.

### Q45. How can the hotspot problem be solved?

**Answer:**
By allocating dedicated shards for hot keys, further partitioning data, or using caching to absorb read traffic.

### Q46. Why are joins difficult in sharded databases?

**Answer:**
Because related data may exist on different shards, making cross-shard joins expensive and slow.

### Q47. What is denormalization and why is it used?

**Answer:**
Denormalization duplicates data across tables to avoid joins and improve read performance in distributed systems.

---

## 1️⃣4️⃣ Advanced CDN Concepts

### Q48. Why should cache expiry not be too long in CDN?

**Answer:**
Because users may receive stale content if updates are made at the origin.

### Q49. Why should cache expiry not be too short?

**Answer:**
It increases requests to origin servers, reducing CDN effectiveness.

### Q50. What is CDN fallback?

**Answer:**
A mechanism where clients fetch content directly from origin servers if the CDN is unavailable.

---

## 1️⃣5️⃣ Cache Reliability & Consistency

### Q51. What consistency issues can occur between cache and database?

**Answer:**
Data can become stale if updates succeed in the database but fail in cache or vice versa.

### Q52. Why is maintaining cache consistency difficult at scale?

**Answer:**
Because updates across distributed systems are not atomic and may happen out of order.

### Q53. How do you reduce cache-related failures?

**Answer:**
By using multiple cache nodes, replication, and over-provisioning memory.

---

## 1️⃣6️⃣ Stateless Architecture & Auto Scaling

### Q54. Why does stateless architecture simplify auto-scaling?

**Answer:**
Because new servers can be added or removed without affecting user sessions.

### Q55. Where should session data be stored in stateless systems?

**Answer:**
In shared storage such as Redis, databases, or distributed key-value stores.

### Q56. Why is NoSQL often chosen for session storage?

**Answer:**
Because it scales easily and provides low-latency access.

---

## 1️⃣7️⃣ Multi-Data Center Deep Dive

### Q57. What happens during a complete data center outage?

**Answer:**
Traffic is redirected to a healthy data center using GeoDNS.

### Q58. Why is data replication across data centers complex?

**Answer:**
Because of network latency, conflict resolution, and eventual consistency issues.

### Q59. Why is asynchronous replication preferred across regions?

**Answer:**
To reduce latency and avoid blocking user requests.

### Q60. How do you test multi-data center deployments?

**Answer:**
By running region-specific tests and using automated deployment pipelines.

---

## 1️⃣8️⃣ Message Queue - Failure & Scaling

### Q61. How does a message queue improve fault tolerance?

**Answer:**
Messages are stored durably, so they aren't lost if consumers are temporarily unavailable.

### Q62. What happens when message queue size grows rapidly?

**Answer:**
More consumers are added to process messages faster.

### Q63. Can producers and consumers be scaled independently?

**Answer:**
Yes, which is a key advantage of message queues.

---

## 1️⃣9️⃣ Logging, Metrics & Automation

### Q64. Why is centralized logging important?

**Answer:**
It allows easy debugging and analysis across multiple servers.

### Q65. What are host-level metrics?

**Answer:**
Metrics related to infrastructure health, such as CPU, memory, and disk usage.

### Q66. What are business-level metrics?

**Answer:**
Metrics that reflect user behavior and business performance, like DAU and revenue.

### Q67. Why is CI/CD important in large systems?

**Answer:**
It reduces deployment risk and improves development speed.

---

## 2️⃣0️⃣ End-to-End System Thinking

### Q68. Why should redundancy exist at every tier?

**Answer:**
To eliminate single points of failure and ensure high availability.

### Q69. Why should static assets be separated from dynamic content?

**Answer:**
Static assets can be cached aggressively using CDN, reducing load on web servers.

### Q70. Why is monitoring critical before failures occur?

**Answer:**
It helps detect early warning signs and prevents outages.

---

## 2️⃣1️⃣ Final High-Impact Questions

### Q71. What are the first components you scale in a growing system?

**Answer:**
Web tier using load balancers, followed by caching to reduce database load.

### Q72. How do you reduce database load without changing schema?

**Answer:**
By adding cache layers and read replicas.

### Q73. How do you design for failure?

**Answer:**
By adding redundancy, monitoring, and automated recovery mechanisms.

### Q74. How do you handle sudden traffic spikes?

**Answer:**
Using auto-scaling, load balancers, cache, and CDN.

### Q75. What mindset should you have while designing scalable systems?

**Answer:**
Assume failure will happen and design systems to recover gracefully.

---

## 🎯 Golden Closing Answer

### Q. How do you scale a system from zero to millions of users?

**Answer:**
Start with a simple architecture, then gradually add load balancers, caching, CDN, stateless web servers, database replication, sharding, and monitoring while ensuring redundancy at every layer.

---

## 📊 Component Priority Matrix

| Priority | Component | Purpose |
|----------|-----------|---------|
| 1 | Load Balancer | Traffic distribution & failover |
| 2 | Cache Layer | Reduce DB load |
| 3 | CDN | Serve static content |
| 4 | Stateless Web Tier | Enable horizontal scaling |
| 5 | Database Replication | Read scaling & availability |
| 6 | Sharding | Write scaling |
| 7 | Message Queue | Decouple services |
| 8 | Multi-Data Center | Regional availability |
| 9 | Monitoring | System health |

---

## 🔑 Key Takeaways

- **Always think stateless** - enables scaling
- **Cache everything cacheable** - improves performance
- **Design for failure** - assume components will fail
- **Monitor proactively** - detect issues early
- **Scale gradually** - add complexity only when needed

---

## 📝 Interview Tips

1. Start with the simplest solution
2. Identify bottlenecks explicitly
3. Explain trade-offs clearly
4. Use real numbers when estimating
5. Draw diagrams to visualize architecture
6. Always mention monitoring and redundancy

---
