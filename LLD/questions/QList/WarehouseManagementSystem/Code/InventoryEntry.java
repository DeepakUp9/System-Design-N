package questions.QList.WarehouseManagementSystem.Code;

import java.sql.Date;

import questions.QList.WarehouseManagementSystem.Code.Test.Product;
import questions.QList.WarehouseManagementSystem.Code.Test.StorageLocation;

//InventoryEntry (core entity)
public class InventoryEntry {
     private String entryId;
    private Product product;          // or just productId
    private StorageLocation location; // or locationId
    private int quantity;
    private Date lastUpdated;
    private int version;              // for optimistic locking

    public boolean hasEnough(int qty) {
        return quantity >= qty;
    }

    public void reduce(int qty) {
        if (qty > quantity) throw new InsufficientStockException();
        quantity -= qty;
        lastUpdated = new Date();
        version++;
    }

    // ... getters
}
