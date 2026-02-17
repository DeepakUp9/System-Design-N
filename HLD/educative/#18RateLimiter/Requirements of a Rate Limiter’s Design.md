# Requirements of a Rate Limiter's Design

Understand the requirements and important concepts of a rate limiter.

## Requirements

Our focus in this lesson is to design a rate limiter with the following functional and non-functional requirements.

### Functional requirements

* To limit the number of requests a client can send to an API within a time window.
* To make the limit of requests per window configurable.
* To make sure that the client gets a message (error or notification) whenever the defined threshold is crossed within a single server or combination of servers.

### Non-functional requirements

* **Availability:** Essentially, the rate limiter protects our system. Therefore, it should be highly available.
* **Low latency:** Because all API requests pass through the rate limiter, it should work with a minimum latency without affecting the user experience.
* **Scalability:** Our design should be highly scalable. It should be able to rate limit an increasing number of clients' requests over time.

## Types of throttling

A rate limiter can perform three types of throttling.

1. **Hard throttling:** This type of throttling puts a hard limit on the number of API requests. So, whenever a request exceeds the limit, it is discarded.

2. **Soft throttling:** Under soft throttling, the number of requests can exceed the predefined limit by a certain percentage. For example, if our system has a predefined limit of 500 messages per minute with a 5% exceed in the limit, we can let the client send 525 requests per minute.

3. **Elastic or dynamic throttling:** In this throttling, the number of requests can cross the predefined limit if the system has excess resources available. However, there is no specific percentage defined for the upper limit. For example, if our system allows 500 requests per minute, it can let the user send more than 500 requests when free resources are available.

## OS-Level Throttling using cgroups

Linux operating systems provide a kernel feature known as `cgroups` (control groups) that limits, accounts for, and isolates the resources—CPU time, system memory, disk storage, I/O, and network bandwidth—of a collection of processes. By using `cgroups`, the system administrator can monitor, deny access to specific resources, and reconfigure the `cgroups` dynamically on a running system. The `cgroups` feature provides the following benefits through which the system administrator gains a fine-grain control of the system resources:

* **Resource limiting:** Using this feature, a restriction can be imposed on groups not to exceed a configured memory limit and file system cache.
* **Prioritization:** Through this feature, some groups can be prioritized to use a larger share of CPU cycles or disk I/O throughput.
* **Accounting:** This feature is used to measure a group's resource usage, which could also be used for billing purposes.
* **Control:** The system administrator can control groups of processes, their checkpoints, and restart via this feature.

Providing such rich features, `cgroup` can be used at a system (single server) level to limit resource usage not only for a single user but also for groups of users or processes.

We can extend the above concepts by designing a service that takes input from the rate-limiting service and enforces the limits on local nodes of a cluster.

### Deep Dive: OS-Level Throttling with Linux cgroups

Great question. This is a very important but often hand-waved topic in system design interviews and real systems.

I'll explain OS-level throttling using Linux cgroups slowly, deeply, and step-by-step, and then connect it back to rate limiting in distributed systems.

Think of this as:
👉 "How the operating system itself becomes the last line of defense."

#### 1️⃣ First: What does "throttling at OS level" even mean?

**Throttling = intentionally slowing something down so it cannot exceed a limit**

At OS level, this means:

* Not in your app code
* Not in your API gateway
* But inside the Linux kernel

So even if:

* A buggy service goes wild
* A malicious process tries to eat all CPU
* A misconfigured app leaks memory

👉 The OS physically prevents it from harming the system.

#### 2️⃣ The Problem OS-level throttling solves

Without OS throttling:

One process can:

* Take 100% CPU
* Allocate all memory
* Flood disk or network

Other processes starve

Whole machine becomes unusable

This is called: **Noisy Neighbor Problem**

Especially bad in:

* Microservices
* Containers
* Multi-tenant systems
* Kafka brokers, API gateways, DBs

#### 3️⃣ Enter cgroups (Control Groups)

"Linux provides a kernel feature known as cgroups"

**What is a cgroup (simple definition)**

A cgroup is:

A kernel-level container that groups processes and enforces resource limits on them.

Important:

* Enforced by kernel
* Cannot be bypassed by application
* Applies to all threads of a process

#### 4️⃣ What resources can cgroups control?

From the text:

* CPU time
* Memory
* Disk storage
* Disk I/O
* Network bandwidth

Let's go one by one, deeply.

#### 5️⃣ CPU Throttling (Most Common)

**Problem**

One process runs infinite loop:

```
while(true) {}
```

Result:

* 100% CPU
* Other services slow or die

**How cgroups fix this**

Using CPU cgroups, you can say:

"This group of processes can use at most 20% of CPU"

**How kernel enforces it**

Linux scheduler tracks:

* CPU time used by cgroup

If limit exceeded:

* Process is paused
* Resumed later

This is hard throttling, not polite suggestion.

**Example mental model**

```
CPU time slice (100ms)
┌──────────────┐
│ Cgroup A 20% │ → 20ms
│ Cgroup B 80% │ → 80ms
└──────────────┘
```

Even if A tries:

Kernel says ❌ NO

#### 6️⃣ Memory Throttling (Extremely Critical)

**Problem**

A process keeps allocating memory:

```
new byte[1_000_000_000];
```

Result:

* OOM
* Entire system crashes

**How cgroups handle memory**

You can configure:

* Maximum memory limit
* File system cache limit

Example:

* Cgroup memory limit = 512 MB

If process exceeds:

* Kernel tries reclaiming cache
* If still exceeded:
* Process is killed (OOM Killer)

Important:

* Only that cgroup's process dies
* Not the whole system

**Why this is powerful**

Without cgroups:

* Kernel OOM killer randomly kills processes

With cgroups:

* Predictable
* Isolated
* Safe

#### 7️⃣ Disk I/O Throttling

**Problem**

One service:

* Writes logs aggressively
* Runs heavy batch job
* Saturates disk

Result:

* DB latency spikes
* Kafka slows
* Everything breaks

**cgroups solution**

Limit:

* Read IOPS
* Write IOPS
* Bandwidth (MB/s)

Example:

* Service A: max 10 MB/s disk write
* Service B: unlimited

Kernel enforces:

* Delays disk requests
* Schedules fairly

#### 8️⃣ Network Throttling

**Problem**

One service:

* Sends too many responses
* Floods network
* Starves others

**cgroups + tc (traffic control)**

You can:

* Limit bandwidth per process group
* Shape traffic
* Drop excess packets

Example:

* Service X → max 50 Mbps

Even if app tries:

* Kernel enforces the cap

#### 9️⃣ Accounting (Monitoring & Billing)

"Measure a group's resource usage"

This is huge.

cgroups can tell:

* CPU seconds used
* Memory peak
* Disk bytes read/written
* Network bytes sent

Used for:

* Billing (cloud providers)
* Capacity planning
* Debugging

Example:

```
Service A used:
- CPU: 120s
- Memory peak: 450MB
- Disk write: 3GB
```

#### 🔁 10️⃣ Control (Process Lifecycle)

"Control groups of processes, checkpoints, restart"

This means:

* Freeze a group
* Resume it
* Kill all processes in group
* Restart safely

This is how:

* Containers are paused
* Kubernetes restarts pods
* systemd manages services

#### 11️⃣ Why this is OS-level throttling (not app-level)

**App-level rate limiting:**

* Can be bypassed
* Bugs can disable it
* Needs code changes

**OS-level throttling:**

* Enforced by kernel
* Language agnostic
* Crash-safe

👉 This is last safety net

#### 12️⃣ Real-world examples you already use

You may not realize, but you already use cgroups:

**Docker**

* Each container = cgroup
* CPU/memory limits → cgroups

**Kubernetes**

* Pod limits → cgroups
* CPU requests/limits → scheduler + cgroups

**systemd**

* Each service runs in its own cgroup

#### 13️⃣ Connecting this to Rate Limiting (VERY IMPORTANT)

From your text:

"We can extend the above concepts by designing a service that takes input from the rate-limiting service and enforces the limits on local nodes of a cluster."

Let's decode this.

**Step-by-step architecture**

**Step 1: Central Rate Limiter**

Decides:

* User X → 100 req/sec
* Service Y → 2 CPU cores

**Step 2: Send limits to nodes**

Control plane pushes limits

Example:

```json
{
  "service": "payment",
  "cpu": "1 core",
  "memory": "512MB"
}
```

**Step 3: Local Enforcement via cgroups**

On each node:

* Service runs in its own cgroup
* Limits applied dynamically

Even if:

* API gateway fails
* Application misbehaves

👉 OS still enforces limits

#### 14️⃣ Why this is powerful in distributed systems

| Layer | Purpose |
|-------|---------|
| API Gateway | Per-user rate limiting |
| App | Business logic limits |
| OS (cgroups) | Hard safety limits |
| Hardware | Physical ceiling |

cgroups = final guardrail

#### 15️⃣ Final Mental Model (IMPORTANT)

Think of cgroups as:

"Traffic police inside the kernel"

* Applications request resources
* Kernel decides how much they get
* Rules are enforced physically

## Where to place the rate limiter

There are three different ways to place the rate limiter.

1. **On the client side:** It is easy to place the rate limiter on the client side. However, this strategy is not safe because it can easily be tampered with by malicious activity. Moreover, the configuration on the client side is also difficult to apply in this approach.

2. **On the server side:** As shown in the following figure, the rate limiter is placed on the server-side. In this approach, a server receives a request that is passed through the rate limiter that resides on the server.

   *Rate limiter placed at the server side*

3. **As middleware:** In this strategy, the rate limiter acts as middleware, throttling requests to API servers as shown in the following figure.

   *Rate limiter as middleware*

**Note:** Many modern services use APIs to provide their functionality to the clients. API endpoints can be a good vantage point to rate limit the incoming client traffic because all traffic passes through them.

Placing a rate limiter is dependent on a number of factors and is a subjective decision, based on the organization's technology stack, engineering resources, priorities, plan, goals, and so on.

## Design Consideration

When designing a system that tracks usage or limits access, how would you choose between a single counter shared by all users and separate counters maintained for each user? What does this choice suggest about tradeoffs between fairness, resource control, and added system complexity?

## Two models for implementing a rate limiter

One rate limiter might not be enough to handle enormous traffic to support millions of users. Therefore, a better option is to use multiple rate limiters as a cluster of independent nodes. Since there will be numerous rate limiters with their corresponding counters (or their rate limit), there are two ways to use databases to store, retrieve, and update the counters along with the user information.

1. **A rate limiter with a centralized database:** In this approach, rate limiters interact with a centralized database, preferably Redis or PostgreSQL. The advantage of this model is that the counters are stored in centralized databases. Therefore, a client can't exceed the predefined limit. However, there are a few drawbacks to this approach. It causes an increase in latency if an enormous number of requests hit the centralized database. Another extensive problem is the potential for race conditions in highly concurrent requests (or associated lock contention).

2. **A rate limiter with a distributed database:** Using an independent cluster of nodes is another approach where the rate-limiting state is in a distributed database. In this approach, each node has to track the rate limit. The problem with this approach is that a client could exceed a rate limit—at least momentarily, while the state is being collected from everyone—when sending requests to different nodes (rate-limiters). To enforce the limit, we must set up sticky sessions in the load balancer to send each consumer to exactly one node. However, this approach lacks fault tolerance and poses scaling problems when the nodes get overloaded.

Aside from the above two concepts, another problem is whether to use a global counter shared by all the incoming requests or individual counters per user. For example, the **token bucket algorithm** can be implemented in two ways. In the first method, all requests can share the total number of tokens in a single bucket, while in the second method, individual buckets are assigned to users. The choice of using shared or separate counters (or buckets) depends on the use case and the rate-limiting rules.

## Frequently Asked Questions

### Can a load balancer be used as a rate limiter?

Load balancers play a critical role in preventing an application server from being overwhelmed by an excessive number of requests. They achieve this by either declining requests based on predefined limits or directing them to a queue for deferred processing. It's essential to emphasize that load balancers treat all incoming requests impartially, without recognizing the diverse complexities and processing durations associated with different operations. For instance, certain operations within a web service may be rapid, while others may be more time-intensive. When there is a need to control the number of requests for specific operations, a more effective approach is to implement this control at the application server level using a rate limiter. The rate limiter excels at understanding the intricacies of individual operations and can selectively impose limitations as required.

### Multi-Region Rate Limiting Scenario

**Question:** Assume a scenario in which a client intends to send requests for a particular service using two virtual machines (VMs), where one is using a VPN to a different region. Suppose that the throttling identifier works based on user credentials. Therefore, the user ID would be the same for both sessions. Moreover, let's assume that requests from different VMs can hit different data centers. How would the throttling work to prevent the user from exceeding the rate limit in this scenario?

**Answer:** To rate limit the incoming requests, we have two different choices to place the rate limiter.

1. **Rate limiter per data center:** One way to throttle the incoming requests from the user is to use rate limiting per data centers. Each data center will have its own rate-limiter, which limits the incoming requests. In this approach, the rate (count or rate limit) is relatively lower. Therefore, a limited number of requests are allowed per unit time. Moreover, this approach provides lower latency since the requests are normally directed to the nearest data centers located geographically. Often, latency within a data center is less than one millisecond and multiple redundant paths are available in case of some link failure.

2. **A shared rate limiter across data centers:** Another approach is to use a shared rate limiter across multiple data centers. This way, requests received from both VMs will be throttled by the single rate limiter. The number of requests allowed in this case is higher. However, this approach is relatively slower, as prior to directing a request to any nearest data center, it will pass through the shared rate limiter. Latency is often high and variable across geographically distributed data centers and not a lot of redundant paths are available.

If rate limiting is applied independently per data center without global coordination, users could potentially exceed the limit by distributing requests across multiple data centers. To prevent this, a globally shared rate limiter (e.g., a distributed token bucket) would be needed to enforce a consistent limit across all data centers.

Building blocks we will use
The design of the rate limiter utilizes the following building blocks that we discussed in the initial chapters.


## Building blocks in the design of a rate limiter

Building blocks in the design of a rate limiter
- **Databases** are used to store rules defined by a service provider and metadata of users using the service.

- **Caches** are used to cache the rules and users’ data for frequent access.

- **Queues** are essential for holding the incoming requests that are allowed by the rate limiter.