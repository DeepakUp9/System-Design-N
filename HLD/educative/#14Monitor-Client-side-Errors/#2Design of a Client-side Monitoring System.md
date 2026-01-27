# 🧭 Design of a Client-side Monitoring System

## 🎯 Goal

Detect errors that never reach your servers — e.g., DNS failure, ISP routing issue, or CDN malfunction — before customers flood social media or sites like Downdetector complaining "Is X down?"

Since your backend cannot directly see these problems, you need a separate monitoring system that works from the client's perspective.

---

## ⚙️ 1. Why We Need Client-side Monitoring

### 🧩 The Problem

Your monitoring tools (like Prometheus, Grafana, CloudWatch) only track server metrics — CPU, latency, 500 errors, etc.

But if users cannot reach your service due to network or DNS issues, your backend will appear 100% healthy.

Users, however, experience service unavailability.

This creates a **visibility gap** — you can't detect real-world downtime early enough.

### 💡 Example

Let's say Google's DNS goes down in a region. You'll still see your app's backend metrics as normal, but millions of users can't connect. Those users might:

- Complain on Twitter/X
- Check Downdetector
- Wait for others to confirm the issue

That's slow feedback — you, as the service provider, find out too late.

👉 Hence, we design a **Client-side Monitoring System** to detect these errors automatically.

---

## 🧱 2. Initial Design: Active Probers

### 🔹 Idea

Act like a synthetic client from multiple regions and ISPs. Each "prober" sends requests periodically to check:

- Can the service be reached?
- How fast is it responding?
- What HTTP status codes are returned?

### 🔹 Architecture

```
[ Prober A ] —\
[ Prober B ] —>  Monitoring System
[ Prober C ] —/
```

These probers are deployed in various vantage points around the world (like AWS regions or ISP networks). They continuously test the reachability of your service and report failures.

---

## ⚠️ 3. Problems with Active Probers

Although simple, active probers have two main weaknesses:

### (a) 🧭 Incomplete Coverage

- The Internet has **100,000+ autonomous systems (ASNs)** (i.e., ISPs or network regions)
- It's impossible or too expensive to place probers in all of them
- Some countries restrict external probes
- Regular maintenance and network changes make coverage inconsistent

So, even if your probers report "OK," many real users might still face issues in unprobed regions.

### (b) 👥 Lack of Real User Behavior

Probers only test simple availability (like pinging your `/health` endpoint). But real users do complex actions:

- Login
- Fetch personalized data
- Stream media

These aren't captured by simple pings.

So, probers don't reflect actual user experience.

---

## 🚀 4. Improved Design: Embedded Client-side Monitoring

To overcome those limitations, we make every real user a mini-prober. This is called **"passive client-side monitoring."**

### 🧩 Two Components

| Component | Role |
|-----------|------|
| Agent | Embedded inside the client app (web, mobile, etc.). Detects and reports failures. |
| Collector | Independent backend system that receives and aggregates these error reports. |

### 🔹 How It Works

1. The **Agent** detects if the main service fails to respond (timeout, DNS failure, etc.)
2. Instead of trying to send the error report to the main backend (which might be unreachable), it sends it to a separate **Collector** service hosted independently
3. The **Collector** aggregates data from thousands or millions of agents
4. The monitoring team visualizes trends — e.g., sudden spikes in errors from India or from a specific ISP

### 🔹 Architecture Diagram (Conceptually)

```
     ┌───────────────┐
     │   Client App   │
     │ (with Agent)   │
     └──────┬────────┘
            │
   Main Service Unreachable
            │
            ▼
     ┌───────────────┐
     │   Collector   │  ← independent infrastructure
     └───────────────┘
            │
            ▼
     ┌──────────────────┐
     │ Analytics / Alert │
     └──────────────────┘
```

---

## ⚙️ 5. Collector Design Details

- Collectors are **independent of the main service** (different IPs, domains, ASNs)
- This ensures that even if your main service goes down, collectors remain reachable
- Collectors can be distributed globally to collect local data
- The data is processed in near real-time using stream processing systems like **Kafka, Flink, or Spark Streaming**
- Some reports can be lost (system tolerates partial data) — the goal is statistical detection, not 100% accuracy

---

## 🧩 6. Key Design Challenges

### (a) 🔘 Activation and Deactivation

- Users must consent to client-side reporting
- The service adds a custom HTTP header (e.g., `X-Monitoring-Enabled`) that instructs the client to send reports
- Browsers or apps that support it include this header in each request
- The client can turn it on/off anytime
- If your app owns both client and backend (like YouTube, Chrome, Gmail), this is easier to implement and standardize

### (b) 🌐 Reaching Collectors Under Faulty Conditions

Collectors must exist outside the **failure domain** of your main service. Let's look at various fault types and solutions:

| Failure Type | Mitigation |
|--------------|------------|
| IP unreachable | Host collector on different IP |
| DNS failure (example.com not resolving) | Collector on different domain, e.g., example.net |
| BGP/ASN hijack | Collector hosted on different autonomous system |
| CDN outage | Collector on different CDN or no CDN |
| Last-mile user issues | Hard to mitigate; cache locally and report later |

This ensures that agents can always reach at least one collector, even when the main service is down.

This concept is known as being **outside the blast radius** — i.e., collectors are insulated from main-service failures.

### (c) 🧠 Protecting User Privacy

This is crucial — since you're collecting network-level information from real users, privacy must be preserved.

#### 🔒 Best Practices

**User control:**
- Users can see, enable, or disable monitoring anytime

**Minimal data collection:**
- Only collect what's necessary (like HTTP status, endpoint, timestamp)

**Avoid sensitive network data:**
- ❌ No traceroute hops (leaks user's physical location)
- ❌ No DNS resolver info (reveals ISP)
- ❌ No RTT (Round Trip Time) or packet loss (location inference possible)

**Collect only what web logs already capture, e.g.:**
- Request URL
- Status code
- Error type (timeout, connection refused, DNS failure)

**Use encryption (HTTPS)** so ISPs or middleboxes cannot alter reports.

**Designated collectors only** should receive this data — no third-party routing.

---

## ⚖️ 7. Example Summary Table

| Component | Role | Design Principle |
|-----------|------|------------------|
| Agent | Embedded in client app | Detect & report connectivity errors |
| Collector | Independent service | Receive and aggregate reports |
| Processing System | Stream processor (Kafka/Flink) | Detect real-time spikes |
| Privacy | Limit data, ensure consent | Respect user control |
| Fault Tolerance | Host collectors in other domains/ASNs | Stay reachable under failures |

---

## ✅ 8. Conclusion

- Traditional monitoring fails when the client can't reach your service
- Client-side monitoring bridges that gap by making users or synthetic agents report failures
- A separate collector infrastructure ensures reachability even when the main service is down
- Proper privacy and fault-tolerance design makes the system both safe and effective

### 🧠 In short:

**Build monitoring outside your blast radius.**

The best observability is the one that sees the world like your users do.
