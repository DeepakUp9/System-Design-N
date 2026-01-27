package questions.QList.WarehouseManagementSystem.Code;
import java.util.*;
import java.util.stream.Collectors;
public class Test {
 

// Enums
enum OrderStatus { PENDING, ALLOCATED, PARTIAL_FULFILLED, SHIPPED, CANCELLED }
enum OrderType   { INBOUND, OUTBOUND }

// Domain Entities (same as before, abbreviated for brevity)
// Immutable Product class with getters only
class Product {
    private final String id;
    private final String name;

    public Product(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
class StorageLocation {
    private String id;
    private int capacity;
    private int occupied;
    
    public StorageLocation(String id, int capacity) {
        this.id = id;
        this.capacity = capacity;
        this.occupied = 0;
    }
    
    public String getId() {
        return id;
    }
    
    public boolean hasSpace(int qty) {
        return occupied + qty <= capacity;
    }
    
    public void occupy(int qty) {
        occupied += qty;
    }
    
    public void release(int qty) {
        if (qty > occupied) {
            throw new IllegalArgumentException("Cannot release more than occupied");
        }
        occupied -= qty;
    }
}
class InventoryEntry {
    private Product product;
    private StorageLocation location;
    private int quantity;
    
    public InventoryEntry(Product product, StorageLocation location, int quantity) {
        this.product = product;
        this.location = location;
        this.quantity = quantity;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void addStock(int qty) {
        this.quantity += qty;
    }
    
    public boolean reduceStock(int qty) {
        if (this.quantity >= qty) {
            this.quantity -= qty;
            return true;
        }
        return false;
    }
    
    public String getLocationId() {
        // Assuming StorageLocation has a method getId()
        return location.getId();
    }
    
    /* ... other methods ... */
}

class Order {
    // Add fields as needed, e.g., List<OrderItem> items;
    public void addItem(OrderItem item) {
        // Implementation goes here
    }

    public boolean isFullyFulfilled() {
        // Implementation goes here
        return false;
    }
}

class OrderItem {
    // Example fields
    private String productId;
    private int quantity;
    private List<Allocation> allocations = new ArrayList<>();

    // Method signature for addAllocation
    public void addAllocation(Allocation allocation) {
        allocations.add(allocation);
    }

    // Other methods and constructors can be added as needed
}
record Allocation(String locationId, int qty) {}

// Strategy Pattern
interface PickingStrategy {
    List<InventoryEntry> prioritize(List<InventoryEntry> entries);
}

class HighestStockFirstStrategy implements PickingStrategy {
    @Override
    public List<InventoryEntry> prioritize(List<InventoryEntry> entries) {
        return entries.stream()
                .sorted(Comparator.comparingInt(InventoryEntry::getQuantity).reversed())
                .collect(Collectors.toList());
    }
}

// Repository Pattern (abstraction for data access)
interface ProductRepository {
    Optional<Product> findById(String productId);
    void save(Product product);
}

interface LocationRepository {
    Optional<StorageLocation> findById(String locationId);
    void save(StorageLocation location);
}

interface InventoryRepository {
    Optional<InventoryEntry> findByProductAndLocation(String productId, String locationId);
    List<InventoryEntry> findByProductId(String productId);
    void save(InventoryEntry entry);
}

// Service (core business logic)
interface InventoryService {
    void restock(String productId, Map<String, Integer> locationToQty);
    List<Allocation> allocate(String productId, int requiredQty);
    int getTotalStock(String productId);
}

class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepo;
    private final ProductRepository productRepo;
    private final LocationRepository locationRepo;
    private final PickingStrategy strategy;

    public InventoryServiceImpl(InventoryRepository inventoryRepo, ProductRepository productRepo,
                                LocationRepository locationRepo, PickingStrategy strategy) {
        this.inventoryRepo = inventoryRepo;
        this.productRepo = productRepo;
        this.locationRepo = locationRepo;
        this.strategy = strategy;
    }

    @Override
    public void restock(String productId, Map<String, Integer> locationToQty) {
        final Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        for (var entry : locationToQty.entrySet()) {
            String locId = entry.getKey();
            int qty = entry.getValue();
            if (qty <= 0) continue;

            StorageLocation loc = locationRepo.findById(locId)
                    .orElseThrow(() -> new IllegalArgumentException("Location not found"));
            if (!loc.hasSpace(qty)) throw new IllegalStateException("Insufficient capacity at location: " + locId);

            if (!loc.hasSpace(qty)) throw new IllegalStateException("No capacity");

            InventoryEntry inv = inventoryRepo.findByProductAndLocation(productId, locId)
                    .orElse(new InventoryEntry(product, loc, 0));

            inv.addStock(qty);
            inventoryRepo.save(inv);
        }
    }

    @Override
    public List<Allocation> allocate(String productId, int requiredQty) {
        List<InventoryEntry> entries = inventoryRepo.findByProductId(productId);

        int total = entries.stream().mapToInt(InventoryEntry::getQuantity).sum();
        if (total < requiredQty) throw new RuntimeException("Insufficient stock");

        List<InventoryEntry> prioritized = strategy.prioritize(entries);

        List<Allocation> allocations = new ArrayList<>();
        int remaining = requiredQty;

        for (InventoryEntry entry : prioritized) {
            if (remaining <= 0) break;
            int canTake = Math.min(remaining, entry.getQuantity());
            if (canTake > 0 && entry.reduceStock(canTake)) {
                allocations.add(new Allocation(entry.getLocationId(), canTake));
                remaining -= canTake;
                inventoryRepo.save(entry);
            }
        }

        // This check handles possible concurrent modifications that may have reduced stock after the initial check.
        if (remaining > 0) throw new RuntimeException("Allocation incomplete due to concurrent modification");
        return allocations;
    }

    @Override
    public int getTotalStock(String productId) {
        return inventoryRepo.findByProductId(productId)
                .stream().mapToInt(InventoryEntry::getQuantity).sum();
    }
}
}
