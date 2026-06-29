# Maintainability
Learn about maintainability, how to measure it, and its relationship with reliability.

---

## What is Maintainability?
Besides building a system, one of the main tasks afterward is **keeping the system up and running** by:  
- Finding and fixing bugs  
- Adding new functionalities  
- Keeping the system’s platform updated  
- Ensuring smooth operations  

Maintainability defines how easily these tasks can be achieved.  
It can be divided into three key aspects:

- **Operability**: Ease of ensuring smooth system operation under normal circumstances and restoring to normal after a fault.  
- **Lucidity**: Simplicity of the codebase. Simple code is easier to understand and maintain.  
- **Modifiability**: Ability of the system to integrate modified, new, and unforeseen features without hassle.  

---

## Measuring Maintainability
Maintainability (**M**) is the probability that a service will restore its functions within a specified time of fault occurrence.  
It measures how conveniently and swiftly the service regains its normal operating conditions.

**Example**:  
If a component has a maintainability value of 95% for half an hour, the probability of restoring it to fully active form within 30 minutes is **0.95**.

### Key Metric: MTTR
We use **MTTR (Mean Time to Repair)** to measure maintainability.

\[
MTTR = \frac{{Total Maintenance Time}}/{{Total Number of Repairs}}
\]

- MTTR is the **average time required** to repair and restore a failed component.  
- **Goal**: Keep MTTR as **low** as possible.  

---

## Maintainability and Reliability
- **Maintainability**: Focuses on **time-to-repair**.  
- **Reliability**: Focuses on **time-to-failure** + **time-to-repair**.  
- Together, they help in analyzing:  
  - **Availability**  
  - **Downtime**  
  - **Uptime**  

---
