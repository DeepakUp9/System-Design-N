import java.util.*;

public class CarRentalBranch {
    private String name;
    private Address address;
    private List<ParkingStall> stalls;

    public CarRentalBranch(String name, Address addr, List<ParkingStall> stalls) {
        this.name = name;
        this.address = addr;
        this.stalls = stalls;
    }

    public Address getLocation() { return address; }
    public List<ParkingStall> getStalls() { return stalls; }
    public String getName() { return name; }
}
