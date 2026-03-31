# Evaluation of a Blob Store's Design

> Examining how each design decision maps back to the non-functional requirements — and answering the open security question.

---

## Table of Contents

1. [Availability](#1-availability)
2. [Durability](#2-durability)
3. [Scalability](#3-scalability)
4. [Throughput](#4-throughput)
5. [Reliability](#5-reliability)
6. [Consistency](#6-consistency)
7. [Security (Open Question)](#7-security-open-question)
8. [Requirements Compliance Summary](#8-requirements-compliance-summary)

---

## 1. Availability

**Goal:** The system must remain accessible for reads and writes even during partial or regional failures.

### For Read Requests

```
Strategy: 4 replicas per blob

  Primary Node ──── serves reads
  Replica 1    ──── failover + load distribution
  Replica 2    ──── failover + load distribution
  Replica 3    ──── failover + load distribution

If primary node fails → any replica can serve the read request instantly
```

Replica placement is designed to survive multiple failure scenarios:

| Failure Scenario | How it's Handled |
|---|---|
| Single node/disk failure | Other replicas in same data center take over |
| Entire data center failure | Replica in another DC within same region serves requests |
| Regional disaster | Replica in a geographically separate region takes over |

The **monitoring service** continuously checks replica counts. If failed replicas exceed a defined threshold, it triggers creation of new replicas — maintaining the required count at all times.

---

### For Write Requests

```
Write arrives
    │
    ▼
Data replicated synchronously within the storage cluster
(fault-tolerant, parallel replication)
    │
    ▼
Success returned to client ✅
(fast, because replication is local within the cluster)
```

Writes complete quickly because intra-cluster replication uses nearby nodes with redundant network paths.

---

### For the Manager Node

```
Manager Node state is continuously backed up (checkpointing)

If Manager Node fails:
  → New instance is started
  → Initialized from last saved state
  → Replays operation log to recover in-progress operations
  → System resumes without data loss
```

---

## 2. Durability

**Goal:** Once uploaded, data must not be lost unless explicitly deleted.

### Two-Layer Protection

```
Layer 1 — Synchronous Replication (immediate):
  Data written → replicated to all nodes in the cluster before confirming success
  → If one node loses data, other nodes in the cluster have a copy

Layer 2 — Monitoring Service (ongoing):
  Continuously watches all storage disks
  │
  Disk fails?
    → Alert sent to administrator (replace the disk)
    → Message sent to Manager Node
    → Manager Node copies data from failed disk to a healthy/new disk
    → Mapping updated to reflect the new location
```

```
Disk failure timeline:

  t=0:   Disk D3 fails
  t=1:   Monitoring service detects failure
  t=2:   Alert → Administrator + Manager Node notified
  t=3:   Manager Node copies D3's chunks to healthy disk D7
  t=4:   Metadata mappings updated: D3 → D7
  t=5:   Data fully recovered ✅
```

The combination of synchronous replication and active monitoring means data survives disk failures, node crashes, and even hardware replacements without any data loss.

---

## 3. Scalability

**Goal:** Handle billions of blobs and grow storage capacity on demand.

### How Blobs Scale

```
Blobs split into chunks
    │
    ▼
Chunks distributed across partition servers via partition map
    │
    ▼
Each partition server handles a specific range of blob paths
    │
    ▼
Automatic load balancing between partition servers as traffic grows
```

### How Storage Scales

```
Need more storage? → Add more data nodes horizontally
  No downtime required
  Manager Node updates partition maps automatically
```

---

### The Manager Node Bottleneck

A single Manager Node can handle approximately **10,000 QPS (queries per second)**. At very high scale, this becomes a ceiling.

> ❓ **Q: How do we scale further when the Manager Node hits its computational limit and vertical scaling is no longer an option?**

**A:** Deploy **two (or more) independent instances** of the entire system — each with its own Manager Node and its own set of data nodes.

```
Instance 1:
  Manager Node A  +  Data Nodes [D1...D500]
  → Serves blob range: account IDs 000000–499999

Instance 2:
  Manager Node B  +  Data Nodes [D501...D1000]
  → Serves blob range: account IDs 500000–999999
```

Each instance has been demonstrated to scale up to **a few petabytes**. Adding more instances allows near-linear horizontal scaling beyond that.

> For further scaling *within* a single instance, a more sophisticated multi-manager design would be needed — but the multi-instance approach covers most real-world needs.

---

## 4. Throughput

**Goal:** Sustain high data transfer rates for gigabyte-scale blobs.

### Two Mechanisms Drive High Throughput

**1. Parallel Chunk Fetching**

```
Blob video.mp4 split into 4 chunks across 4 data nodes

  Without parallelism:
    Chunk 1 → Chunk 2 → Chunk 3 → Chunk 4  (sequential)
    Total time = T1 + T2 + T3 + T4

  With parallelism:
    Chunk 1 ┐
    Chunk 2 ├── fetched simultaneously
    Chunk 3 │
    Chunk 4 ┘
    Total time ≈ max(T1, T2, T3, T4)  ← much faster
```

Distributing chunks across data nodes transforms what would be a sequential read into a **parallel read** — significantly improving throughput for large blobs.

**2. Multi-Layer Caching**

```
Cache hit at client      → zero network latency, no Manager Node involved
Cache hit at front-end   → fast routing, no partition lookup needed
Cache hit at Manager Node → no disk I/O, chunk served from memory
Cache hit at CDN         → served from edge server near user (milliseconds)
```

Each caching layer reduces both latency and the load on downstream components — boosting effective throughput across the system.

---

## 5. Reliability

**Goal:** Detect and recover from failures promptly with minimal user impact.

### Heartbeat Protocol

```
Data Nodes → periodic heartbeat signals → Manager Node

  Heartbeat received?   → Node is healthy ✅
  Heartbeat missing?    → Node may be failing ⚠️
                          Manager Node stops routing requests to it
                          Manager Node triggers creation of a new replica
```

This keeps the Manager Node always aware of which nodes are reliable, so it only routes requests to healthy nodes.

### Proactive Disk Space Monitoring

```
Manager Node tracks available disk space on all data nodes

  Free space > threshold:  Normal operation ✅
  Free space < threshold:  Alert sent to administrator → add new disks
```

This prevents the system from silently running out of storage — a failure mode that could cause write errors or data loss.

### Administrator Alerts for Hardware Failures

The monitoring service alerts administrators to replace:
- Failed or degraded disks
- Broken network links
- Faulty switches

Proactive hardware management prevents small failures from escalating into data loss or outages.

---

## 6. Consistency

**Goal:** All users must see the same, up-to-date view of a blob at all times.

### Two-Phase Consistency Strategy

```
Phase 1 — Strong Consistency (synchronous, intra-cluster):

  Write request arrives
       │
       ▼
  Data synchronously replicated to ALL nodes in the storage cluster
  (happens on the critical path — before success is returned to the user)
       │
       ▼
  Success returned to client ✅

  → All subsequent reads from this cluster return the latest data immediately
  → Strong consistency guaranteed within the cluster


Phase 2 — Availability (asynchronous, cross-region):

  After write success is confirmed:
       │
       ▼
  Data asynchronously replicated to other data centers and regions
       │
       ▼
  Reads from those regions are NOT served until replication is complete
  (no stale reads from remote regions)
```

### Why This Design is Correct

```
Strong consistency within the cluster:
  → Satisfies the requirement that all users see the same view of a blob

Async cross-region replication:
  → Achieves availability without sacrificing consistency
  → Remote regions only become readable once they have the latest data
```

This is a carefully balanced design — strong consistency where it matters (serving live requests), and async replication where latency tolerance allows it (geographic redundancy).

---

## 7. Security (Open Question)

**Q: What techniques would you use to make the blob store secure — protecting data at rest and in transit?**

### A: Four-Layer Security Model

---

### 7.1 Encryption at Rest (Data Stored on Disk)

```
Algorithm: AES (Advanced Encryption Standard) — typically AES-256

All blob chunks stored on disk are encrypted.
Without the decryption key, the raw disk data is unreadable.

  Disk failure or physical theft → attacker sees only ciphertext ✅
  Keys managed separately (key management service)
```

---

### 7.2 Encryption in Transit (Data Moving Over the Network)

```
Protocol: HTTPS (HTTP over SSL/TLS)

All communication between:
  - Client ↔ Front-End Servers
  - Front-End Servers ↔ Data Nodes
  - Manager Node ↔ Data Nodes

...is encrypted via TLS.

Benefits:
  - Confidentiality: data can't be read if intercepted
  - Integrity: data can't be tampered with in transit
  - Authentication: client verifies it's talking to the real server
```

---

### 7.3 Access Control (Role-Based Access Control — RBAC)

```
RBAC model:

  Role: Owner
    → Full access: read, write, delete, manage containers

  Role: Contributor
    → Read + write blobs, but cannot delete containers

  Role: Reader
    → Read-only access to specific containers or blobs

  Role: Public (no auth)
    → Access only to explicitly public blobs

Enforcement: Manager Node checks access privileges on every read/write request
  → Private blobs: accessible only by the owning account
  → Public blobs: accessible by anyone with the URL
```

---

### 7.4 Data Integrity Verification (Checksums)

```
On upload:
  Checksum (e.g., MD5, SHA-256) computed for each chunk
  Stored alongside chunk metadata in the Manager Node

On read:
  Chunk retrieved from data node
  Checksum recomputed and compared against stored value

  Match?     → Data is intact ✅
  Mismatch?  → Data corrupted ⚠️ → serve from replica, alert monitoring
```

Checksums protect against silent data corruption — a real failure mode in large-scale distributed storage systems.

---

### Security Summary

| Threat | Protection |
|---|---|
| Data stolen from disk | AES encryption at rest |
| Data intercepted in transit | HTTPS / TLS encryption |
| Unauthorized access | Role-Based Access Control (RBAC) |
| Data corruption (silent) | Checksums / hash verification |
| Physical hardware theft | Encryption at rest (ciphertext is useless without key) |

---

## 8. Requirements Compliance Summary

| Requirement | Design Mechanism | Status |
|---|---|---|
| **Availability** | 4 replicas per blob, multi-region placement, Manager Node checkpointing | ✅ Met |
| **Durability** | Synchronous replication + monitoring-triggered disk recovery | ✅ Met |
| **Scalability** | Partitioning, horizontal data node scaling, multi-instance Manager Node | ✅ Met |
| **Throughput** | Parallel chunk fetching across data nodes + multi-layer caching | ✅ Met |
| **Reliability** | Heartbeat protocol, disk space monitoring, proactive hardware alerts | ✅ Met |
| **Consistency** | Synchronous intra-cluster replication + gated async cross-region reads | ✅ Met |
| **Security** | AES at rest, TLS in transit, RBAC, checksums | ✅ Met |

---

### Design Decisions That Enable Multiple Requirements Simultaneously

```
Replication     → Availability + Durability + Throughput (read distribution)
Partitioning    → Scalability + Throughput (parallel access)
Monitoring      → Durability + Reliability + Availability
Caching         → Throughput + Availability (reduced load on core components)
Checksums       → Durability + Reliability + Security
Sync replication→ Consistency + Availability (writes succeed fast, reads are consistent)
```

---

