package questions.QList.WarehouseManagementSystem.Code.PickingStrategy;

import java.util.List;

public interface PickingStrategy {
    List<InventoryEntry> sortLocations(List<InventoryEntry> entries);
}