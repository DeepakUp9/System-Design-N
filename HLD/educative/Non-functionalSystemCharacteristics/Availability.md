# Availability

Learn about availability, how to measure it, and its importance.

---

## What is Availability?

Availability is the percentage of time that a service or infrastructure is accessible to clients and operates under normal conditions.  

- **100% availability** → The service functions and responds as intended all the time.  
- Any downtime reduces availability.

---

## Measuring Availability

Mathematically, availability (**A**) is a ratio:  
A (in %) = (Total Time - Downtime) / Total Time × 100 

- The higher the **A** value, the better.  
- Availability is often expressed in terms of **“number of nines.”**

---

## The Nines of Availability

| Availability % | Downtime per Year | Downtime per Month | Downtime per Week |
|----------------|------------------|-------------------|-------------------|
| **90% (1 nine)**     | 36.5 days        | 72 hours          | 16.8 hours        |
| **99% (2 nines)**    | 3.65 days        | 7.20 hours        | 1.68 hours        |
| **99.5%**            | 1.83 days        | 3.60 hours        | 50.4 minutes      |
| **99.9% (3 nines)**  | 8.76 hours       | 43.8 minutes      | 10.1 minutes      |
| **99.99% (4 nines)** | 52.56 minutes    | 4.32 minutes      | 1.01 minutes      |
| **99.999% (5 nines)**| 5.26 minutes     | 25.9 seconds      | 6.05 seconds      |
| **99.9999% (6 nines)**| 31.5 seconds    | 2.59 seconds      | 0.605 seconds     |
| **99.99999% (7 nines)**| 3.15 seconds   | 0.259 seconds     | 0.0605 seconds    |

---

## Availability and Service Providers

Different providers may calculate availability differently:  

- Some start measuring from when the **service is first offered**, others from when a **client starts using it**.  
- Some may **exclude downtime** if it doesn’t affect all clients.  
- **Planned downtimes** (e.g., scheduled maintenance) are usually excluded.  
- **Downtime from cyberattacks** might or might not be included.  

👉 Always check how a provider **defines and calculates availability** before relying on their SLA numbers.

