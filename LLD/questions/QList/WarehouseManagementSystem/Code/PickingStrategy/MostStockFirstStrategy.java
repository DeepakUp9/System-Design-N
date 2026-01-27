package questions.QList.WarehouseManagementSystem.Code.PickingStrategy;

public class MostStockFirstStrategy implements PickingStrategy {
    @Override
    public List<InventoryEntry> sortLocations(List<InventoryEntry> entries) {
        return entries.stream()
                .sorted((a, b) -> Integer.compare(b.getQuantity(), a.getQuantity()))
                .collect(Collectors.toList());
    }
}
