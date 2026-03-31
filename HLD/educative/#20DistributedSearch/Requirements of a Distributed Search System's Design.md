# Distributed Search System: Requirements & Resource Estimation

> Defining what the system must do, how well it must do it, and estimating the servers, storage, and bandwidth needed to support YouTube-scale search.

---

## Table of Contents

1. [Functional Requirements](#1-functional-requirements)
2. [Non-Functional Requirements](#2-non-functional-requirements)
3. [Resource Estimation](#3-resource-estimation)
   - [Assumptions](#assumptions)
   - [Number of Servers](#number-of-servers-estimation)
   - [Storage](#storage-estimation)
   - [Bandwidth](#bandwidth-estimation)
4. [Building Blocks](#4-building-blocks)
5. [Summary Cheat Sheet](#5-summary-cheat-sheet)

---

## 1. Functional Requirements

A distributed search system has one core functional requirement:

### Search

Users must receive **relevant content** based on their search queries — quickly and accurately, regardless of how much data the system holds.

```
User types: "machine learning tutorial for beginners"
       │
       ▼
Search system processes query
       │
       ▼
Returns: ranked list of relevant results ✅
```

That's it — the functional contract is deliberately simple. The complexity lives entirely in the non-functional requirements: how fast, how available, how scalable, and at what cost.

---

## 2. Non-Functional Requirements

These define the quality bar the system must meet to be production-worthy.

---

### 2.1 Availability

The system must be **highly available** — users expect search to work at all times, from anywhere in the world.

```
Impact of downtime on search:
  YouTube search down for 1 hour
  → 150M daily users × (1/24) = ~6.25M users affected
  → Massive user frustration, lost engagement, support load spike
```

High availability requires replication, failover, and redundant infrastructure at every layer.

---

### 2.2 Scalability

The system must scale with the **ever-growing amount of data** being indexed.

```
YouTube today:    ~800M videos
YouTube tomorrow: more videos + more metadata + more languages

System must:
  → Index new content continuously as it arrives
  → Handle growing query volume without degradation
  → Add capacity horizontally without redesign
```

---

### 2.3 Fast Search on Big Data

Users expect results in **milliseconds**, regardless of how large the corpus is.

```
Unacceptable: "We have 800M videos, so your search takes 10 seconds."
Required:     "We have 800M videos, and your results appear in < 300ms."
```

This is the hardest requirement to satisfy — it's the primary reason distributed search needs careful indexing design.

---

### 2.4 Reduced Cost

The overall cost of building and operating the search system should be **as low as possible**.

```
Cost drivers to minimize:
  - Number of servers (compute cost)
  - Storage used for indexes (storage cost)
  - Bandwidth consumed per query (network cost)

Design goal: maximize efficiency so the system does more with less
```

---

### Non-Functional Requirements Summary

| Requirement | Goal | Key Challenge |
|---|---|---|
| Availability | Always on, globally | Replication + failover |
| Scalability | Grows with data volume | Horizontal scaling of index |
| Fast search | Results in milliseconds | Efficient index design |
| Reduced cost | Minimize infra spend | Efficient use of servers, storage, bandwidth |

---

## 3. Resource Estimation

We use **YouTube search** as our reference system for all estimations.

### Assumptions

| Parameter | Value |
|---|---|
| Daily active users (search feature) | 150 million |
| RPS per server | 64,000 |
| JSON document size per video | 200 KB |
| Unique terms extracted per document | 1,000 |
| Storage per term in index table | 100 Bytes |
| Videos uploaded per day | 6,000 |
| Search query size | 100 Bytes |
| Suggested results per query | 80 video IDs |
| Size per suggestion | 50 Bytes |

---

### Number of Servers Estimation

Using daily active users as a proxy for peak requests per second:

```
Peak requests/second = 150,000,000

Servers needed = Peak RPS / RPS per server
               = 150,000,000 / 64,000
               = 2,343.75
               ≈ 2,350 servers
```

**Result: ~2,350 servers needed at peak load**

> ⚠️ **Note:** Concurrent requests have a much higher impact on server count than the same number of requests spread over time. Peak load planning must account for bursts, not just daily averages.

---

### Storage Estimation

#### Step 1 — Storage per video

Each video's metadata is stored as a JSON document containing: title, description, channel name, and transcript.

```
Storage per video = Storage/doc + (Terms/doc × Storage/term)
                  = 200 KB     + (1,000     × 100 Bytes)
                  = 200 KB     + 100 KB
                  = 300 KB per video
```

#### Step 2 — Storage per day

```
Storage/day = Videos/day × Storage/video
            = 6,000      × 300 KB
            = 1,800,000 KB
            = 1.8 GB/day
```

**Result: ~1.8 GB/day** to index all new YouTube videos

#### Storage Breakdown Table

| Component | Per Video | Per Day (6,000 videos) |
|---|---|---|
| JSON document | 200 KB | 1.2 GB |
| Index terms (1,000 × 100B) | 100 KB | 0.6 GB |
| **Total** | **300 KB** | **1.8 GB** |

> This is a single-tenant estimate for YouTube only. In a multi-tenant distributed search service (serving multiple platforms), storage requirements scale proportionally with the number of tenants and their content volume.

---

### Bandwidth Estimation

#### Incoming Traffic (Search Queries)

```
Bandwidth = Requests/second × Query size

Requests/second = 150,000,000 / 86,400 = ~1,736 requests/sec
Query size      = 100 Bytes

Incoming bandwidth = 1,736 × 100 Bytes
                   = 173,600 Bytes/sec
                   ≈ 173.6 KB/sec
                   ≈ ~1.4 Mbps incoming
```

#### Outgoing Traffic (Search Results)

Each response contains 80 video ID suggestions, each 50 Bytes:

```
Response size = 80 suggestions × 50 Bytes = 4,000 Bytes per response

Outgoing bandwidth = 1,736 requests/sec × 4,000 Bytes
                   = 6,944,000 Bytes/sec
                   ≈ 6.94 MB/sec
                   ≈ ~55.5 Mbps outgoing
```

#### Bandwidth Summary

| Traffic Type | Per Request | Bandwidth |
|---|---|---|
| Incoming (queries) | 100 Bytes | ~1.4 Mbps |
| Outgoing (results) | 4,000 Bytes | ~55.5 Mbps |
| **Ratio** | — | **~40× more outgoing** |

> **Why bandwidth is modest:** Results are text only — ordered lists of video IDs (50 Bytes each). No thumbnails, no media in the response. This keeps bandwidth low and enables near real-time results. Many production search services return thumbnails and metadata in addition, which would significantly increase outgoing bandwidth.

---

### Resource Estimation Summary

```
Servers:          ~2,350 at peak load
Storage/day:      ~1.8 GB (new videos indexed per day)
Incoming BW:      ~1.4 Mbps
Outgoing BW:      ~55.5 Mbps
Query/response:   100B in → 4,000B out (~40× amplification)
```

---

## 4. Building Blocks

The distributed search system requires one key infrastructure component:

### Distributed Storage

Used for two purposes:

| Purpose | What's Stored |
|---|---|
| Raw data to be indexed | JSON documents (video metadata, transcripts) |
| The index itself | Inverted index tables mapping terms to document IDs |

We use a **blob store** (as designed in the previous chapter) as the underlying distributed storage — referred to generically as "distributed storage" throughout this design.

```
[New video uploaded]
        │
        ▼
[Distributed Storage]
  ├── stores raw JSON document (200 KB)
  └── stores index entries for extracted terms (100 KB)
        │
        ▼
[Search System] reads index from distributed storage to answer queries
```

---

## 5. Summary Cheat Sheet

### Requirements at a Glance

```
Functional:                         Non-Functional:
───────────                         ───────────────────────────────────
✅ Search — return relevant          ✅ Availability  → always on
   results for any query             ✅ Scalability   → grows with data
                                     ✅ Fast search   → ms results on big data
                                     ✅ Reduced cost  → efficient infra use
```

### Estimation Summary (YouTube-scale)

```
DAU (search):        150 million users
Peak RPS:            150 million req/sec
Servers needed:      ~2,350
Storage per video:   300 KB (200 KB doc + 100 KB index terms)
Storage per day:     ~1.8 GB (6,000 new videos)
Incoming bandwidth:  ~1.4 Mbps (100B queries)
Outgoing bandwidth:  ~55.5 Mbps (4,000B results)
```

### Key Design Insight

```
The bandwidth requirements are intentionally low because:
  → Results are text-only (video IDs, no media)
  → Low bandwidth per query = more queries served at lower cost
  → Near real-time response becomes achievable at this scale
```

---

