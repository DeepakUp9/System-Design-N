import java.util.*;

public class VehicleCatalog implements Search {
    private HashMap<String, List<Vehicle>> vehicleTypes = new HashMap<>();
    private HashMap<String, List<Vehicle>> vehicleModels = new HashMap<>();

    public void addVehicle(Vehicle v) {
        String type = v.getClass().getSimpleName().toUpperCase();
        vehicleTypes.putIfAbsent(type, new ArrayList<>());
        vehicleTypes.get(type).add(v);

        String model = v.getModel();
        vehicleModels.putIfAbsent(model, new ArrayList<>());
        vehicleModels.get(model).add(v);
    }

    @Override
    public List<Vehicle> searchByType(String type) {
        return vehicleTypes.getOrDefault(type.toUpperCase(), new ArrayList<>());
    }

    @Override
    public List<Vehicle> searchByModel(String model) {
        return vehicleModels.getOrDefault(model, new ArrayList<>());
    }
}
