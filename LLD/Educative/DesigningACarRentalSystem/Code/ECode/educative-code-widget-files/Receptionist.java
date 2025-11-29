import java.util.*;

public class Receptionist extends Account {
    private Date dateJoined;
    private List<Customer> customerList = new ArrayList<>();

    public Receptionist() {
        this.setStatus(AccountStatus.ACTIVE);
    }

    // Search for customers by (partial) name match
    public List<Customer> searchCustomer(String name) {
        List<Customer> res = new ArrayList<>();
        for (Customer c : customerList) {
            if (c.getName() != null && c.getName().contains(name)) res.add(c);
        }
        return res;
    }

    // Helper: Add a customer to the receptionist's managed list
    public void addCustomer(Customer customer) {
        customerList.add(customer);
    }

    // Since reservations are managed globally, provide static helper methods:

    // Add a reservation to the global reservation list
    public boolean addReservation(List<VehicleReservation> allReservations, VehicleReservation reservation) {
        return allReservations.add(reservation);
    }

    // Cancel a reservation by removing it from the global reservation list
    public boolean cancelReservation(List<VehicleReservation> allReservations, VehicleReservation reservation) {
        return allReservations.remove(reservation);
    }

    @Override
    public boolean resetPassword() {
        this.setPassword(UUID.randomUUID().toString());
        return true;
    }
}
