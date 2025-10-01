# Design of a Key-value Store

Learn about the **functional** and **non-functional requirements** and the **API design** of a key-value store.

---

## Requirements

Let’s list the requirements of designing a key-value store to overcome the problems of traditional databases.

---

## Functional Requirements

Typically, key-value stores are expected to offer functions such as **get** and **put**.  
However, what sets this particular key-value store system apart is its distinct characteristics, explained as follows:

### 1. Configurable Service
- Some applications might want to **trade strong consistency for higher availability**.  
- We need to provide a **configurable service** so that different applications can use a range of consistency models.  
- This allows **tight control over trade-offs** between availability, consistency, cost-effectiveness, and performance.

> **Note:**  
> Such configurations can only be performed when **instantiating a new key-value store instance**.  
> They **cannot** be changed dynamically when the system is operational.

---

### 2. Ability to Always Write (Choosing **A** over **C** in CAP)
- Applications should always have the ability to **write** into the key-value storage.  
- If a user wants **strong consistency**, this requirement might not always be fulfilled due to the **CAP theorem**.  

> **Example:**  
> - In Amazon’s **shopping cart application**, the ability to always write (high availability) is considered a **functional requirement**.  
> - In other use cases, high availability may be considered **non-functional**.  
> - Inspired by **Amazon’s Dynamo**, we categorize the ability to always write as **functional** here.

---

### 3. Hardware Heterogeneity
- We should be able to add **new servers with different and higher capacities** seamlessly into the cluster.  
- No need to change or upgrade existing servers.  
- The system should:
  - Handle **different capacity servers**.
  - Ensure correct **get** and **put** functionality.
  - Balance workload distribution **according to each server’s capacity**.  

This calls for a **peer-to-peer design** with no distinguished nodes.

---

## Non-functional Requirements

The non-functional requirements are as follows:

### 1. Scalability
- Key-value stores should run on **tens of thousands of servers** distributed globally.  
- **Incremental scalability** is highly desirable:
  - Add or remove servers as needed.
  - Do so with **minimal to no disruption** in service availability.  
- The system should handle an **enormous number of users** of the key-value store.

---

### 2. Fault Tolerance
- The key-value store should continue to operate **uninterrupted** despite:
  - Server failures
  - Component failures  

---

## Discussion Points

1. **What are the key differences between key-value stores and traditional databases?**  
   - Key-value stores focus on **fast lookups** using a simple key.  
   - Traditional databases support **complex queries, joins, and ACID transactions**.

2. **In what scenarios are key-value stores particularly advantageous?**  
   - When handling **massive data volumes**.  
   - When requiring **fast retrievals**.  
   - When data structures are **flexible** or **frequently changing**.  
   - Use cases:  
     - Shopping carts  
     - User sessions  
     - Customer preferences  
     - Product catalogs  

---

**Question-:**  
Differences between key-value stores and traditional databases  

**ans-:**  
Certainly! Key-value stores are designed for simple retrievals using a unique key, often sacrificing complex querying capabilities and strict consistency to achieve high availability and scalability. They typically handle unstructured data and are optimized for speed and volume. Traditional relational databases, on the other hand, organize data into tables with predefined schemas, supporting complex queries, joins, and ACID transactions, making them suitable for structured data and applications requiring consistency.

Key-value stores are particularly advantageous in scenarios involving massive data volumes, high-speed lookups, or when data structures are flexible or evolving. Keep exploring!   

**Question-:**  
Why do we need to run key-value stores on multiple servers?    

**ans-:**    
A single-node-based hash table can fall short due to one or more of the following reasons:  
  - No matter how big a server we get, this server can’t meet data storage and query requirements.
  - Failure of this one mega-server will result in service downtime for everyone.
So, key-value stores should use many servers to store and retrieve data.   

**Question-:**  
When performing data integrity checks, we often store hashes of values (and sometimes the value along with the associated key) as metadata. Should these hashes be generated before or after data compression or encryption, and why?  

**ans-:**  
In most data integrity use cases, hashes should be generated after compression but before encryption.  

   -  **Hashing before encryption** is recommended because encryption produces ciphertext, which may vary across operations due to salting or IVs, even if the underlying data is the same. Hashing encrypted data would make integrity checks unreliable.  
   - **Hashing after compression** is preferred because compression reduces data size without changing meaning, and it avoids hashing redundant data unnecessarily.  

   So, the typical and most effective sequence is: Compress → Hash → Encrypt. This ensures integrity is validated on the actual content, not the encrypted form, and it supports consistent verification during both storage and retrieval operations.

**Example scenario:** Suppose you store daily database backups in a cloud archive. First, you compress each backup to reduce its size. Then, you generate a hash of the compressed file and store it as metadata. Finally, you encrypt the file before uploading it. Later, when retrieving the backup, you decrypt it and re-hash the compressed file to compare with the stored hash. This way, you verify that the actual content is intact, regardless of encryption changes or storage format.   


---
# Key-Value Store API Design

## Assumptions

We'll assume the following to keep our design simple:

* The data centers hosting the service are trusted (non-hostile).
* All the required authentication and authorization are already completed.
* User requests and responses are relayed over HTTPS.

## API Design

Key-value stores, like ordinary hash tables, provide two primary functions, which are `get` and `put`.

Let's look at the API design.

### The `get` Function

The API call to get a value should look like this:

```
get(key)
```

We return the associated value on the basis of the parameter `key`. When data is replicated, it locates the object replica associated with a specific key that's hidden from the end user. It's done by the system if the store is configured with a weaker data consistency model. For example, in eventual consistency, there might be more than one value returned against a key.

#### Parameters

| Parameter | Description |
|-----------|-------------|
| `key` | It's the `key` against which we want to get `value`. |

### The `put` Function

The API call to put the value into the system should look like this:

```
put(key, value)
```

It stores the `value` associated with the `key`. The system automatically determines where data should be placed. Additionally, the system often keeps metadata about the stored object. Such metadata can include the version of the object.

#### Parameters

| Parameter | Description |
|-----------|-------------|
| `key` | It's the `key` against which we have to store `value`. |
| `value` | It's the object to be stored against the `key`. |


---

# Data Types in a Key-value Store

---

## Key and Value

- The **key** is often treated as a **primary key** in a key-value store.  
- The **value** can be **any arbitrary binary data**, such as:  
  - Text  
  - JSON  
  - Images  
  - Blobs  
  - Application objects  

This flexibility allows key-value stores to handle **structured, semi-structured, or unstructured data** seamlessly.  

---

## Example

| Key          | Value                                   |
|--------------|-----------------------------------------|
| `user:101`   | `{ "name": "Alice", "age": 28 }`        |
| `session:42` | `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9`  |
| `img:202`    | Binary blob representing an image file  |

---

## Dynamo’s Approach

- **Dynamo** (Amazon’s key-value store) uses **MD5 hashing** on the key.  
- The hash function generates a **128-bit identifier**.  
- This identifier helps the system determine:  
  - **Which server node** will store the data  
  - **How to distribute** keys across multiple servers using techniques like **consistent hashing**  

---

## Notes

- This chapter is based on **Amazon Dynamo**, which is an influential system in the domain of key-value stores.  
- Dynamo introduced design ideas that inspired many popular NoSQL databases like **Cassandra**, **Riak**, and **DynamoDB**.  

---






