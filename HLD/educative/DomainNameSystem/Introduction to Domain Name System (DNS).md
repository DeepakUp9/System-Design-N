# Introduction to Domain Name System (DNS)

Learn how domain names get translated to IP addresses through DNS.

---

## The origins of DNS

Let’s consider the example of a mobile phone where a unique number is associated with each user.  
To make calls to friends, we can initially try to memorize some of the phone numbers.  
However, as the number of contacts grows, we’ll have to use a phone book to keep track of all our contacts.  
This way, whenever we need to make a call, we’ll refer to the phone book and dial the number we need.

Similarly, computers are uniquely identified by IP addresses — for example:  

104.18.2.119


We use IP addresses to visit a website hosted on a machine.  
Since humans cannot easily remember IP addresses to visit domain names (e.g., **educative.io**), we need a phone book-like repository that can maintain all mappings of domain names to IP addresses.  

➡️ DNS serves as the Internet’s **phone book**.

![screenshot](dns.png)

---

## What is DNS?

The **Domain Name System (DNS)** is the Internet’s naming service that maps human-friendly domain names to machine-readable IP addresses.  

- The service of DNS is **transparent to users**.  
- When a user enters a domain name in the browser, the browser translates the domain name into an IP address by asking the DNS infrastructure.  
- Once the IP address is obtained, the request is forwarded to the **destination web server**.  

The entire operation is performed **very quickly**, so the user experiences minimal delay.  
Browsers also **cache frequently used mappings** for faster access (covered later).

![screenshot](dnsWorks.png)

---


## Important Details

Here are some important details about DNS:

### 1. Name Servers
- DNS is not a single server.  
- It’s a **complete infrastructure** with numerous servers.  
- DNS servers that respond to queries are called **name servers**.  

### 2. Resource Records (RRs)
- The DNS database stores **domain name → IP mappings** as **resource records (RRs)**.  
- An RR is the smallest unit of information that users request from name servers.  
- Each RR has three main components:  
  - **Type**  
  - **Name**  
  - **Value**  

---

### Common Types of Resource Records

| Type  | Description | Name | Value | Example |
|-------|-------------|------|-------|---------|
| **A** | Provides the hostname → IP address mapping | Hostname | IP address | (A, relay1.main.educative.io, 104.18.2.119) |
| **NS** | Provides the hostname of the authoritative DNS for a domain | Domain name | Hostname | (NS, educative.io, dns.educative.io) |
| **CNAME** | Maps alias → canonical hostname | Hostname | Canonical name | (CNAME, educative.io, server1.primary.educative.io) |
| **MX** | Maps mail server alias → canonical hostname | Hostname | Canonical name | (MX, mail.educative.io, mailserver1.backup.educative.io) |

---

### 3. Caching
- DNS uses **caching at different layers** (browser, OS, ISP, etc.).  
- This reduces request latency for the user.  
- Also reduces the burden on DNS infrastructure since it serves the entire Internet.  

### 4. Hierarchy
- DNS servers follow a **hierarchical structure** (like a tree).  
- This makes DNS **highly scalable** to handle billions of queries daily.  
- In the next lesson, we’ll explore how this tree-like hierarchy manages the entire DNS database.  

---
