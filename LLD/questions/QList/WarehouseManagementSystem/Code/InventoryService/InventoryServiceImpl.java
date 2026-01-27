package questions.QList.WarehouseManagementSystem.Code.InventoryService;

public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository repo;
    private final PickingStrategy pickingStrategy;  // injected, e.g. @Qualifier("mostStock")

    @Override
    @Transactional
    public AllocationResult allocateForOrder(String productId, int required) {
        List<InventoryEntry> entries = repo.findByProductId(productId);
        entries = pickingStrategy.sortLocations(entries);  // Strategy pattern

        List<Allocation> allocations = new ArrayList<>();
        int remaining = required;

        for (InventoryEntry entry : entries) {
            if (remaining <= 0) break;
            int canTake = Math.min(entry.getQuantity(), remaining);
            if (canTake > 0) {
                entry.reduce(canTake);
                repo.save(entry);  // in transaction
                allocations.add(new Allocation(entry.getLocationId(), canTake));
                remaining -= canTake;
            }
        }

        if (remaining > 0) {
            // rollback or compensate in real code
            throw new PartialAllocationException("Allocated only " + (required - remaining));
        }

        return new AllocationResult(allocations, required);
    }
}
