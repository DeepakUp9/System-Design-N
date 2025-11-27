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

# Calculating Downtime from Availability Percentage

## Core Concept

Downtime is calculated as the inverse of availability.
- **Availability Fraction (A):** The percentage expressed as a decimal (e.g., 99.9% = 0.999).
- **Downtime Fraction:** `(1 - A)`

This fraction is then multiplied by the total time in a given period (year, month, week).

---

## Generic Formulas

Let **`A`** be the availability (expressed as a decimal).

### Downtime per Year
**Formula:** `(1 - A) × 365 days`

### Downtime per Month (Average)
A month is not a fixed length, so an average is used.
**Formula:** `(1 - A) × (365 ÷ 12) days` or `(1 - A) × 730 hours`

### Downtime per Week
**Formula:** `(1 - A) × 7 days`

---

## Step-by-Step Calculation Example (99.9% Availability)

1.  **Convert to Decimal:**
    `A = 99.9% = 0.999`

2.  **Find Downtime Fraction:**
    `Downtime Fraction = 1 - 0.999 = 0.001`

3.  **Apply the Formulas:**
    -   **Per Year:**
        `0.001 × 365 days = 0.365 days`
        `0.365 days × 24 hours/day = 8.76 hours`
    -   **Per Month:**
        `0.001 × 730 hours = 0.73 hours`
        `0.73 hours × 60 minutes/hour = 43.8 minutes`
    -   **Per Week:**
        `0.001 × 168 hours = 0.168 hours`
        `0.168 hours × 60 minutes/hour = 10.08 minutes (~10.1 minutes)`

*Note: Small differences are due to rounding.*

---

## Reference Table of Constants

For calculating downtime in **seconds**, use these constants for each period:

| Period | Total Seconds |
| :--- | :--- |
| **Year** | `365 × 24 × 60 × 60 = 31,536,000` |
| **Month (avg)** | `(365/12) × 24 × 60 × 60 = 2,629,800` |
| **Week** | `7 × 24 × 60 × 60 = 604,800` |

**Ultimate Formula (in seconds):**
`Downtime_in_Seconds = (1 - A) × Total_Seconds_in_Period`

---

## Spreadsheet Implementation (Excel/Google Sheets)

Assuming the availability percentage (as a decimal) is in cell **`A2`**.

| To Calculate: | Formula (Result in Days) | Formula (Result in Hours) |
| :--- | :--- | :--- |
| **Downtime per Year** | `=(1-A2)*365` | `=(1-A2)*365*24` |
| **Downtime per Month** | `=(1-A2)*(365/12)` | `=(1-A2)*730` |
| **Downtime per Week** | `=(1-A2)*7` | `=(1-A2)*168` |

**Formatting Tip:** For automatic unit conversion (to hours, minutes, seconds), calculate the downtime in seconds first and then format the cell as a time duration (`[h]:mm:ss`).



| Availability %         | Downtime per Year | Downtime per Month | Downtime per Week | Example (illustrative) |
|------------------------|-------------------:|-------------------:|------------------:|------------------------|
| **90% (1 nine)**       | 36.5 days         | 72 hours           | 16.8 hours        | Non-critical prototype or dev service where outages are acceptable |
| **99% (2 nines)**      | 3.65 days         | 7.20 hours         | 1.68 hours        | Small business website with low SLA requirements |
| **99.5%**              | 1.83 days         | 3.60 hours         | 50.4 minutes      | Early-stage SaaS service (tolerable short outages) |
| **99.9% (3 nines)**    | 8.76 hours        | 43.8 minutes       | 10.1 minutes      | Typical commercial web service / consumer app |
| **99.99% (4 nines)**   | 52.56 minutes     | 4.32 minutes       | 1.01 minutes      | Payment gateway / ecommerce checkout component |
| **99.999% (5 nines)**  | 5.26 minutes      | 25.9 seconds       | 6.05 seconds      | High-availability core service (telecom, core infra) |
| **99.9999% (6 nines)** | 31.5 seconds      | 2.59 seconds       | 0.605 seconds     | Ultra-critical infrastructure (carrier-grade systems) |
| **99.99999% (7 nines)**| 3.15 seconds      | 0.259 seconds      | 0.0605 seconds    | Safety-critical control systems (extremely rare) |

---

## Availability and Service Providers

Different providers may calculate availability differently:  

- Some start measuring from when the **service is first offered**, others from when a **client starts using it**.  
- Some may **exclude downtime** if it doesn’t affect all clients.  
- **Planned downtimes** (e.g., scheduled maintenance) are usually excluded.  
- **Downtime from cyberattacks** might or might not be included.  

👉 Always check how a provider **defines and calculates availability** before relying on their SLA numbers.

