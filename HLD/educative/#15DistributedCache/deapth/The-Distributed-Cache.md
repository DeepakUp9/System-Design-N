# System Design: The Distributed Cache

Learn the basics of a distributed cache.

## Table of Contents

- [Problem Statement](#problem-statement)
- [What is a Distributed Cache?](#what-is-a-distributed-cache)
- [Why Distributed Cache?](#why-distributed-cache)
- [How Does Distributed Caching Work?](#how-does-distributed-caching-work)
- [Cache Management Best Practices](#cache-management-best-practices)
- [Use Cases for Distributed Caching](#use-cases-for-distributed-caching)
- [Popular Distributed Caching Solutions](#popular-distributed-caching-solutions)
- [Cache Eviction Policies](#cache-eviction-policies)
- [Cache Writing Strategies](#cache-writing-strategies)
- [Common Challenges and Solutions](#common-challenges-and-solutions)
- [Performance Metrics](#performance-metrics)
- [Design Roadmap](#design-roadmap)

## Problem Statement

A typical system consists of these components:

- It has a client who requests the service.
- It has one or more service hosts that entertain client requests.
- It utilizes a database for data storage, which is used by the service.

Under normal circumstances, this abstraction performs fine.

However, as the number of users increases, the number of database queries also increases. As a result, service providers are overburdened, leading to slow performance. In such cases, a cache is added to the system to deal with performance deterioration.

**A cache is a temporary data storage that can serve data faster by keeping data entries in memory.**

Caches store only the most frequently accessed data. When a request reaches the serving host, it retrieves data from the cache (cache hit) and serves the user. However, if the data is unavailable in the cache (cache miss), the data will be queried from the database.

Additionally, the cache is populated with the new value to prevent cache misses in the future.

A cache is a non-persistent storage area used to store data that is repeatedly read and written, providing the end user with lower latency. Therefore, a cache must serve data from a storage component that is fast, has sufficient storage, and is affordable in terms of dollar cost as the caching service scales.

### Performance Comparison

Typical latency numbers every programmer should know:

- L1 cache reference: 0.5 ns
- L2 cache reference: 7 ns
- RAM reference: 100 ns
- SSD read: 150,000 ns
- Database query: 1-10 ms
- Network round trip: 500 ms

This clearly shows why RAM-based caching provides such dramatic performance improvements.

## What is a Distributed Cache?

A distributed cache is a caching system where multiple cache servers coordinate to store frequently accessed data.

Distributed caches are necessary in environments where a single cache server is insufficient to store all the data. At the same time, it's scalable and guarantees a higher degree of availability. Caches are generally small, frequently accessed, short-term storage with fast read time. Caches use the locality of reference principle.

### Benefits of Distributed Caches

Generally, distributed caches are beneficial in these ways:

- They minimize user-perceived latency by precalculating results and storing frequently accessed data.
- They pre-generate expensive queries from the database.
- They store user session data temporarily.
- They serve data from temporary storage even if the data store is down temporarily.
- They reduce network costs by serving data from local resources.
- They can be scaled horizontally by adding more servers, making them ideal for applications with high traffic or large datasets.
- They can reduce the load on databases by offloading frequently accessed data to memory, freeing up database resources for more complex queries and transactions.
- They are typically more highly available than databases, as they are not subject to single points of failure.

## Why Distributed Cache?

As the size of the data required in the cache increases, storing the entire dataset in one system becomes impractical. This is because of the following three reasons:

1. It can be a potential single point of failure (SPOF).
2. A system is designed in layers, and each layer should have its caching mechanism to ensure the decoupling of sensitive data from different layers.
3. Caching at different locations helps reduce the latency of serving at that layer.

### Caching at Different Layers of a System

| System Layer | Technology in Use | Usage |
|--------------|-------------------|-------|
| Web | HTTP cache headers, web accelerators, key-value store, CDNs, and so on | Accelerate retrieval of static web content, and manage sessions |
| Application | Local cache and key-value data store | Accelerate application-level computations and data retrieval |
| Database | Database cache, buffers, and key-value data store | Reduce data retrieval latency and I/O load from database |

Apart from the three system layers above, caching is also performed at DNS and client-side technologies like browsers or end-devices.

## How Does Distributed Caching Work?

Here's an example of how distributed caching can be used in a web application:

1. A web application makes a request to the distributed cache for data.
2. The distributed cache server checks to see if the data is in the cache. If it is, the cache server returns the data to the application.
3. If the data is not in the cache, the cache server retrieves the data from the backend system (e.g., a database) and stores it in the cache for future requests.
4. The cache server then returns the data to the application.

The distributed cache server can be located on the same server as the application or on a separate server. Distributed cache servers are often deployed in a cluster to improve performance and scalability.

By using distributed caching, the web server can avoid retrieving the required data from the database for every request. This can significantly improve the performance of the web application.

### Cache Request Flow Example

```
Client Request → Load Balancer → Application Server
                                        ↓
                                  Check Cache
                                   /         \
                            Cache Hit    Cache Miss
                               /              \
                    Return from Cache    Query Database
                                              ↓
                                        Update Cache
                                              ↓
                                        Return Data
```

## Cache Management Best Practices

To maximize the benefits of distributed caching, adhere to these best practices:

- **Cache eviction**: Implement cache eviction policies, such as Least Recently Used (LRU) or Time to Live (TTL), to maintain a refreshed and relevant cache.
- **Data consistency**: Ensure data consistency between the cache and the primary data source, especially for frequently updated data.
- **Monitoring**: Regularly monitor cache performance metrics, such as hit and miss rates, to identify areas for improvement.
- **Scalability**: Design the cache infrastructure to be scalable, allowing for easy addition of cache nodes as the application grows.

Implementing distributed caching involves selecting the right solution, installing and configuring it on all nodes, defining data partitioning and replication strategies, integrating the cache with the application, and continuously monitoring and fine-tuning performance.

### Additional Best Practices

- **Cache warming**: Pre-populate the cache with frequently accessed data during application startup.
- **Graceful degradation**: Design your application to continue functioning even if the cache is unavailable.
- **Security**: Implement proper authentication and encryption for sensitive cached data.
- **Versioning**: Use cache key versioning to invalidate old data when your data schema changes.
- **Size limits**: Set appropriate memory limits to prevent cache from consuming all available resources.

## Use Cases for Distributed Caching

Distributed caching can be used in a variety of scenarios, including:

- **Web applications**: Distributed caches can be used to store frequently accessed web pages, images, and other resources. This can improve the performance and scalability of web applications.
- **E-commerce applications**: Distributed caches can store product catalogs, shopping carts, and other customer data. This can improve the performance and scalability of e-commerce applications.
- **Content delivery networks (CDNs)**: Distributed caches are often used in CDNs to store static content, such as images, CSS, and JavaScript files. This can improve the performance of websites and web applications.
- **Gaming applications**: Distributed caches can store game state data, including player inventory, map data, and leaderboard information. This can improve the performance and scalability of gaming applications.

### Additional Use Cases

- **Real-time analytics**: Cache aggregated metrics and dashboard data for instant access.
- **API rate limiting**: Store request counts and timestamps to enforce rate limits.
- **Session management**: Store user session data across multiple application servers.
- **Database query results**: Cache expensive JOIN operations and complex queries.
- **Recommendation engines**: Store personalized recommendations for quick retrieval.
- **Social media feeds**: Cache user feeds and timelines for rapid display.

## Popular Distributed Caching Solutions

There are a number of popular distributed caching solutions available, including:

- **Redis**: An open-source in-memory data structure store that can be used as a distributed cache. It is known for its speed and scalability.
- **Memcached**: Another popular open-source distributed cache. It is simple to use and easily scalable.
- **Hazelcast**: A commercial distributed caching solution that offers several advanced features, including data replication and eventing.
- **Apache Ignite**: An open-source distributed caching and computing platform. It offers several features, including in-memory data processing and distributed SQL queries.

### Comparison Table

| Feature | Redis | Memcached | Hazelcast | Apache Ignite |
|---------|-------|-----------|-----------|---------------|
| Data Structures | Rich (strings, lists, sets, hashes) | Simple (key-value) | Rich | Rich |
| Persistence | Yes | No | Yes | Yes |
| Replication | Master-slave | No | Yes | Yes |
| Clustering | Yes (Redis Cluster) | Client-side | Built-in | Built-in |
| Transactions | Yes | No | Yes | Yes |
| Pub/Sub | Yes | No | Yes | Yes |

Distributed caching is a powerful way to improve application performance, scalability, and availability.

To use it effectively, focus on caching data that is frequently accessed and rarely changes, set appropriate expiration times to keep data fresh, monitor cache performance regularly, and consider using a cache management library to handle eviction and synchronization efficiently.

## Cache Eviction Policies

When the cache reaches its capacity, eviction policies determine which data to remove:

### Common Eviction Policies

1. **Least Recently Used (LRU)**: Removes the least recently accessed items first. Best for general-purpose caching where recent access indicates future access.

2. **Least Frequently Used (LFU)**: Removes items that are accessed least often. Useful when access frequency matters more than recency.

3. **First In First Out (FIFO)**: Removes the oldest items first, regardless of access patterns. Simple but may not be optimal for most use cases.

4. **Time To Live (TTL)**: Items expire after a specified duration. Essential for time-sensitive data.

5. **Random Replacement**: Randomly selects items for eviction. Simple and surprisingly effective in some scenarios.

6. **Most Recently Used (MRU)**: Removes the most recently used items. Useful in specific scenarios like sequential scans.

### Choosing the Right Policy

- Use LRU for most web applications
- Use LFU for content with predictable access patterns
- Use TTL for time-sensitive data (sessions, temporary tokens)
- Combine policies for optimal results (e.g., LRU + TTL)

## Cache Writing Strategies

### Write-Through Cache

Data is written to both cache and database simultaneously. This ensures consistency but may increase write latency.

**Pros**: Strong consistency, simple to implement
**Cons**: Higher write latency, cache may contain data that's never read

### Write-Behind (Write-Back) Cache

Data is written to cache first, then asynchronously written to the database. This provides better write performance but risks data loss.

**Pros**: Lower write latency, reduced database load
**Cons**: Risk of data loss, more complex to implement

### Write-Around Cache

Data is written directly to the database, bypassing the cache. The cache is updated only on subsequent reads.

**Pros**: Avoids cache pollution from write-heavy operations
**Cons**: Cache miss on immediate read after write

### Refresh-Ahead Cache

Cache proactively refreshes data before it expires, based on predicted access patterns.

**Pros**: Reduced latency for frequently accessed data
**Cons**: May waste resources refreshing unused data

## Common Challenges and Solutions

### Challenge 1: Cache Stampede

When a popular cache entry expires, multiple requests simultaneously try to regenerate it, overwhelming the database.

**Solution**: Use cache locking or probabilistic early expiration to ensure only one request regenerates the data.

### Challenge 2: Cache Inconsistency

Data in cache becomes out of sync with the database.

**Solution**: Implement proper cache invalidation strategies, use TTL, or employ event-driven cache updates.

### Challenge 3: Cold Start Problem

When the cache is empty (after restart), all requests hit the database.

**Solution**: Implement cache warming strategies to pre-populate frequently accessed data.

### Challenge 4: Hot Key Problem

A single cache key receives disproportionately high traffic, creating a bottleneck.

**Solution**: Replicate hot keys across multiple cache nodes or use local caching in application servers.

### Challenge 5: Network Latency

Network calls to distributed cache add latency.

**Solution**: Use local caching for extremely hot data, implement cache client connection pooling.

## Performance Metrics

### Key Metrics to Monitor

1. **Cache Hit Ratio**: (Cache Hits / Total Requests) × 100
   - Target: >80% for most applications
   - Lower ratios indicate poor cache effectiveness

2. **Cache Miss Ratio**: (Cache Misses / Total Requests) × 100
   - Complement of hit ratio
   - High miss rates suggest cache size or TTL issues

3. **Average Response Time**: Time to retrieve data from cache
   - Should be <5ms for in-memory caches
   - Compare against database query times

4. **Eviction Rate**: Number of items evicted per second
   - High rates may indicate cache is too small

5. **Memory Usage**: Percentage of allocated memory in use
   - Monitor to prevent out-of-memory errors

6. **Network Bandwidth**: Data transfer to/from cache
   - Important for distributed deployments

### Optimization Tips

- Aim for 80-90% cache hit ratio
- Keep cache response time under 5ms
- Maintain memory usage at 70-80% capacity
- Monitor eviction rates to right-size your cache

## Design Roadmap

### How Will We Design a Distributed Cache?

We'll divide the task of designing and reinforcing the learning of major concepts of distributed cache into five lessons:

1. **Background of Distributed Cache**: It's imperative to build the background knowledge necessary to make critical decisions when designing distributed caches. This lesson will revisit some fundamental yet essential concepts.

2. **High-level Design of a Distributed Cache**: We'll build a high-level design of a distributed cache in this lesson.

3. **Detailed Design of a Distributed Cache**: We'll identify some limitations of our high-level design and work toward a scalable, affordable, and performant solution.

4. **Evaluation of a Distributed Cache Design**: This lesson will assess our design against various non-functional requirements, including scalability, consistency, and availability.

5. **Memcached versus Redis**: We'll discuss well-known industrial solutions, namely Memcached and Redis. We'll also go through their details and compare their features to help us understand their potential use cases and how they relate to our design.

## Conclusion

Distributed caching is a critical component of modern scalable systems. By understanding the concepts, strategies, and best practices outlined in this guide, you can effectively implement caching solutions that dramatically improve application performance, reduce database load, and enhance user experience.

Remember that caching is not a silver bullet—it requires careful planning, monitoring, and tuning to achieve optimal results. Start simple, measure everything, and iteratively improve based on real-world usage patterns.

---

