# Building Blocks for Modern System Design

System design is like using **Lego pieces** to build bigger systems.  
We identify **common reusable blocks** and use them in different designs.

---

## Why Building Blocks?
- Many system design problems have **similar components**.  
- Learn once → reuse everywhere.  
- Many of these are already available in **AWS, Azure, GCP**.  

---

## 🧱 Key Building Blocks

### 1. Domain Name System (DNS)
- Hierarchical, distributed naming system.  
- Converts human-friendly names → IP addresses.  

### 2. Load Balancers
- Distributes incoming traffic across servers.  
- Provides **fairness, fault-tolerance, and reduced load**.  

### 3. Databases
- Store, retrieve, update, delete data.  
- Covers replication, partitioning, distributed DB concepts.  

### 4. Key-Value Store
- Non-relational database using key → value mapping.  
- Focus on **scalability, durability, configurability**.  

### 5. Content Delivery Network (CDN)
- Stores **videos, images, static content** near users.  
- Reduces latency & offloads datacenters.  

### 6. Sequencer
- Generates **unique IDs** while preserving order/causality.  
- Explains multiple ID generation techniques.  

### 7. Service Monitoring
- Tracks server/client health.  
- Provides early warning for issues.  

### 8. Distributed Caching
- Multiple cache servers coordinate to store frequently used data.  

### 9. Distributed Messaging Queue
- Queue system between **producers & consumers**.  
- Enables **decoupling, scalability, reliability**.  

### 10. Publish-Subscribe (Pub-Sub)
- Async service-to-service communication.  
- Widely used in **microservices & serverless** systems.  

### 11. Rate Limiter
- Throttles incoming requests.  
- Protects services from abuse or overload.  

### 12. Blob Store
- Stores **unstructured data** (videos, images, binaries).  

### 13. Distributed Search
- Handles **crawl → index → search** efficiently.  

### 14. Distributed Logging
- Efficient, scalable way to handle logs from many services.  

### 15. Distributed Task Scheduling
- Assigns tasks to resources.  
- Supports **async background processing**.  

### 16. Sharded Counters
- Efficient counting system for high concurrency (e.g., likes on a tweet).  

---

## Conventions
- Each block/problem will be explained with **Requirements**:  
  - **Functional Requirements** → Features system provides.  
  - **Non-Functional Requirements (NFRs)** → Qualities (availability, latency, scalability).  

---
