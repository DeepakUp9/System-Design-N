# What is Distributed Monitoring?

## 1. What is Distributed Monitoring?

Distributed monitoring means keeping track of all parts of a distributed system — multiple services running across different servers, data centers, and even geographical locations. Its goal is to detect problems early, understand dependencies, and maintain system reliability.

## ⚠️ 2. Why do we need monitoring?

In a distributed system, many services depend on each other. If one fails, others can also break — this is called a **cascading failure**.

### Example: YouTube

1. **Service A (UI Service)** – accepts a video upload
2. **Service B** – saves metadata to DB and uploads the video to storage
3. **Service C** – replicates data between databases X and Y for redundancy

Now, suppose:

- Service C fails (replication stops)
- DB X crashes, and DB Y never got the replicated data
- When the user tries to watch the video, YouTube shows "Video not found"

👉 **Without monitoring**, no one knows where it went wrong (Service C? DB X?).

Monitoring could have alerted engineers early about:

- Service C failure
- Missing replication logs
- DB X unavailability

This prevents escalation and reduces downtime.

## 💰 3. Downtime Cost

When large-scale systems fail, the financial impact is massive.

### Examples:

- **Meta (Facebook, Instagram, WhatsApp) outage in Oct 2021** — ~9 hours → ~$13 million loss per hour
- **AWS outage (Dec 2021)** — ~impact of $66,000 per minute

👉 These failures show why early detection and automated alerts (monitoring) are crucial — they reduce downtime, user frustration, and business losses.

## 🌎 4. Global Distributed Systems

Modern infrastructure is spread across multiple regions/data centers. Each data center has thousands of servers, often connected through public or private networks.

- Monitoring these geo-separated systems helps detect regional failures
- **Example:** A single network link failure in the US shouldn't affect Europe if monitoring isolates it quickly

Because human operators can't manually check millions of machines, monitoring systems automate fault detection and alerting.

## 🧩 5. Types of Monitoring

There are mainly two types of monitoring based on where the error originates.

### A. Server-Side Monitoring

These errors happen on your servers, visible to your backend systems.

**Examples:**

- Service crashes
- Database timeout
- High CPU/memory usage
- HTTP 5xx errors (like 500 Internal Server Error)

✅ These can be captured easily by logging, metrics, and alerting systems.

### B. Client-Side Monitoring

These happen on the client's end — sometimes the server doesn't even know they happened.

**Examples:**

- The client's network request never reached the server
- Browser errors (JavaScript crash, bad request)
- HTTP 4xx errors (like 404 Not Found or 400 Bad Request)

🧠 **Challenge:** If a request never reaches your system, how will you know a user had a problem?

✅ **Solutions:**

- Client-side logging tools (like Sentry, Datadog RUM, or New Relic Browser)
- Heartbeat pings or synthetic monitoring to simulate user requests from multiple regions

## 🔍 6. Example Recap

### Educative Example:

1. **Service 1 (A)** – allocates containers for learners
2. **Service 2 (B)** – communicates container info to UI service
3. **Service 3 (C)** – updates the learner's UI

**If Service 2 fails:**

- Learner sees "Cannot connect…"
- Monitoring should detect that service 2 had repeated failures and alert the team

**If the learner's request never even reached Service 1:**

- The system won't see any failure internally
- Client-side monitoring is needed to detect that issue

## ⚙️ 7. Final Takeaway

| Category | Description | Example | Detection |
|----------|-------------|---------|-----------|
| **Server-side Monitoring** | Failures within backend systems | DB crash, 500 error | Logs, metrics, tracing |
| **Client-side Monitoring** | Failures on user devices or networks | 404, no response | Client-side logging, synthetic monitoring |
