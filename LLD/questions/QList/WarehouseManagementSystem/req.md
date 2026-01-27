
###  1. Requirements (Quick Recap – What Interviewer Expects You to State)Functional:Add/update products
 **Functional**:
 - Add/update products
 - Admins can add/update products and define storage locations (bins/shelves/zones with capacity).  
 - Define storage locations (bins/shelves) with capacity
 - Restock inventory (inbound) → add quantity to specific locations
 - Process customer orders (outbound) → check availability, allocate stock from locations (strategy:   most - stock first), reduce quantity
 - Get total stock / low-stock alerts
 - Support partial fulfillment for orders
 - Basic order status tracking.


 **Non-Functional / Scope:**
  - In-memory for simplicity (can extend to DB | mention can extend to DB with repositories) 
  - Thread-safe basics (mention optimistic locking / version)  
  - Extensible (add new picking strategies, multi-warehouse later)  
  - Focus: correctness over ultra-high scale. 



### 2. Class Diagram (Text UML Style + Relationships)
link : https://drive.google.com/file/d/1hAd0LpeIcPi3za7_s8HfAyo7913CK-Zv/view?usp=sharing

### 3. Key Design Principles & Patterns Applied

 **SOLID:**
  - **SRP**: Product only holds product data; InventoryService handles allocation logic; Repositories handle persistence.
  - **OCP**: Picking strategy injectable (e.g., MostStockStrategy, NearestLocationStrategy).  
  - **LSP**: Not heavily used yet (no big inheritance).
  - **ISP**: Small interfaces (e.g., separate InventoryQuery vs InventoryUpdate if grows).  
  - **DIP**: Services depend on Repository interfaces, not concrete impl.  
 
 **Patterns:**
  - **Repository** → data access abstraction.
  - **Service Layer** → business orchestration.  
  - **Strategy** → for picking/allocating (e.g., different ways to choose locations).
  - **DTO** → OrderRequest, RestockRequest (avoid leaking entities).
  - **Enum** → OrderType (INBOUND/OUTBOUND), OrderStatus (PENDING, ALLOCATED, PARTIAL, SHIPPED).

### 4. Code 


### 5. Quick Interview Wrap-up Points  
  - Extensibility: Add multi-warehouse? → Add Warehouse entity, Location belongsTo Warehouse.
  - Concurrency: Use version field + optimistic lock (or pessimistic in DB).
  - Future: Observer for low-stock notifications, Factory for different order types.



































