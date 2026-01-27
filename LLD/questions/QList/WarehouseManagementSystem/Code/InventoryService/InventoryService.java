package questions.QList.WarehouseManagementSystem.Code.InventoryService;

public interface InventoryService {
    void restock(RestockRequest request);
    AllocationResult allocateForOrder(String productId, int requiredQty);
    int getTotalAvailable(String productId);
}
