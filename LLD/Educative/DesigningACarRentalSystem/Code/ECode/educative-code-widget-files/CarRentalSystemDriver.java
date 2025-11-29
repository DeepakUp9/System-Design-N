import java.util.*;

public class CarRentalSystemDriver {
    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println("        CAR RENTAL SYSTEM DEMO");
        System.out.println("=============================================\n");

        // -------------------------------------------------
        // 1. Branch Setup
        // -------------------------------------------------
        System.out.println("1. Branch Setup");
        CarRentalSystem rentalSystem = CarRentalSystem.getInstance();
        CarRentalBranch branch1 = new CarRentalBranch(
                "Airport Branch",
                new Address("123 Main St", "Seattle", "WA", 98101, "USA"),
                new ArrayList<>());
        rentalSystem.addNewBranch(branch1);
        System.out.println("   -> Branch added: " + branch1.getName() + " (" + branch1.getLocation() + ")\n");

        // -------------------------------------------------
        // 2. Add Vehicles
        // -------------------------------------------------
        System.out.println("2. Adding Vehicles to Inventory");
        Car car1 = new Car();
        car1.setVehicleId("C1001");
        car1.setModel("Toyota Corolla");
        car1.setManufacturingYear(2022);
        car1.setCarType(CarType.ECONOMY);
        car1.setStatus(VehicleStatus.AVAILABLE);

        Van van1 = new Van();
        van1.setVehicleId("V1001");
        van1.setModel("Ford Transit");
        van1.setManufacturingYear(2021);
        van1.setVanType(VanType.PASSENGER);
        van1.setStatus(VehicleStatus.AVAILABLE);

        VehicleCatalog catalog = new VehicleCatalog();
        catalog.addVehicle(car1);
        catalog.addVehicle(van1);

        System.out.println("   -> Vehicles added: ");
        System.out.println("      - " + car1.getModel() + " (ID: " + car1.getVehicleId() + ")");
        System.out.println("      - " + van1.getModel() + " (ID: " + van1.getVehicleId() + ")\n");

        // -------------------------------------------------
        // 3. Customer Registration
        // -------------------------------------------------
        System.out.println("3. Customer Registration & Login");
        Customer customer1 = new Customer();
        customer1.setAccountId("U123");
        customer1.setName("Alice Smith");
        customer1.setEmail("alice@email.com");
        customer1.setLicenseNumber("D1234567");

        Calendar expiryCal = Calendar.getInstance();
        expiryCal.set(2027, Calendar.FEBRUARY, 1);
        customer1.setLicenseExpiry(expiryCal.getTime());
        customer1.setStatus(AccountStatus.ACTIVE);

        System.out.println("   -> [LOGIN] Customer: " + customer1.getName() + " (ID: " + customer1.getAccountId() + ")");
        System.out.println("   -> Driver License #: " + customer1.getLicenseNumber() +
                " (Expires: " + customer1.getLicenseExpiry() + ")\n");

        // -------------------------------------------------
        // 4. Vehicle Search by Customer
        // -------------------------------------------------
        System.out.println("4. Vehicle Search");
        System.out.println("   == Vehicle Inventory Search Results ==");
        List<Vehicle> cars = catalog.searchByType("CAR");
        if (!cars.isEmpty()) {
            System.out.println("   -> Found " + cars.size() + " car(s) in inventory:");
            for (Vehicle v : cars) {
                System.out.println("      -> Model: " + v.getModel() +
                        " | ID: " + v.getVehicleId() +
                        " | Year: " + v.getManufacturingYear() +
                        " | Status: " + v.getStatus());
            }
        } else {
            System.out.println("   -> No cars found in inventory.");
        }
        System.out.println();

        // -------------------------------------------------
        // 5. Make a Reservation
        // -------------------------------------------------
        System.out.println("5. Reservation");
        VehicleReservation reservation = new VehicleReservation();
        reservation.setReservationId(1);
        reservation.setCustomerId(customer1.getAccountId());
        reservation.setVehicleId(car1.getVehicleId());
        reservation.setCreationDate(new Date());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setPickupLocation("Airport Branch");
        reservation.setReturnLocation("Airport Branch");

        Calendar dueCal = Calendar.getInstance();
        dueCal.add(Calendar.DAY_OF_MONTH, 3);
        reservation.setDueDate(dueCal.getTime());

        List<VehicleReservation> allReservations = new ArrayList<>();
        allReservations.add(reservation);

        car1.reserveVehicle();
        System.out.println("   -> Reservation created for " + customer1.getName() +
                " | Vehicle: " + car1.getModel() +
                " | Pickup: " + reservation.getPickupLocation() +
                " | Return: " + reservation.getReturnLocation() +
                " | Due: " + reservation.getDueDate());

        // -------------------------------------------------
        // 6. Add Equipment and Services
        // -------------------------------------------------
        System.out.println("\n6. Add-ons: Equipment & Services");
        ChildSeat seat = new ChildSeat();
        seat.setEquipmentId(10);
        seat.setPrice(15);
        reservation.addEquipment(seat);

        DriverService driverService = new DriverService();
        driverService.setServiceId(20);
        driverService.setPrice(50);
        driverService.setDriverId(222);
        reservation.addService(driverService);

        System.out.println("   -> Equipment added: Child Seat (ID: " + seat.getEquipmentId() + ", Price: $" + seat.getPrice() + ")");
        System.out.println("   -> Service added: Driver (Driver ID: " + driverService.getDriverId() + ", Price: $" + driverService.getPrice() + ")\n");

        // -------------------------------------------------
        // 7. Payment Processing
        // -------------------------------------------------
        System.out.println("7. Payment Processing");
        reservation.setStatus(ReservationStatus.CONFIRMED);
        Payment payment = new CreditCard();
        payment.setAmount(200);
        payment.setTimestamp(new Date());
        payment.setStatus(PaymentStatus.PENDING);
        System.out.println("   -> Processing payment of $" + payment.getAmount() + " ...");
        boolean paymentSuccess = payment.makePayment();

        if (paymentSuccess) {
            System.out.println("   -> Payment completed successfully for reservation #" + reservation.getReservationId());
        } else {
            System.out.println("   -> Payment failed!");
        }
        System.out.println();

        // -------------------------------------------------
        // 8. Notification (Email)
        // -------------------------------------------------
        System.out.println("8. Notification");
        Notification notify = new EmailNotification();
        notify.setContent("Your reservation is confirmed!");
        notify.sendNotification(customer1);
        System.out.println();

        // -------------------------------------------------
        // 9. Vehicle Pickup
        // -------------------------------------------------
        System.out.println("9. Vehicle Pickup");
        System.out.println("   -> " + customer1.getName() + " picked up the vehicle: " +
                car1.getModel() + " (" + car1.getVehicleId() + ")\n");

        // -------------------------------------------------
        // 10. Vehicle Return
        // -------------------------------------------------
        System.out.println("10. Vehicle Return");
        Calendar returnCal = Calendar.getInstance();
        returnCal.add(Calendar.DAY_OF_MONTH, 3); // Returned on due date
        car1.returnVehicle();
        reservation.setReturnDate(returnCal.getTime());
        reservation.setStatus(ReservationStatus.COMPLETED);

        System.out.println("   -> Vehicle returned on: " + reservation.getReturnDate());
        System.out.println("   -> Reservation status is now: " + reservation.getStatus() + "\n");

        // -------------------------------------------------
        // 11. Overdue Fine Check
        // -------------------------------------------------
        System.out.println("11. Fine Calculation");
        Date expectedReturn = reservation.getDueDate();
        Date actualReturn = reservation.getReturnDate();
        if (actualReturn.after(expectedReturn)) {
            Fine fine = new Fine();
            fine.setAmount(100);
            fine.setReason("Late return");
            System.out.println("   -> [FINE] Fine imposed for late return: $" + fine.getAmount());
            Notification fineNotify = new SmsNotification();
            fineNotify.setContent("You have been fined for late return.");
            fineNotify.sendNotification(customer1);
        } else {
            System.out.println("   -> [FINE] No fine. Vehicle was returned on time.");
        }
        System.out.println();

        // -------------------------------------------------
        // 12. Reservation History for Customer
        // -------------------------------------------------
        System.out.println("12. Reservation History for Customer: " + customer1.getName());
        System.out.println("   -------------------------------------");
        for (VehicleReservation r : allReservations) {
            if (r.getCustomerId().equals(customer1.getAccountId())) {
                System.out.println("   -> Reservation ID: " + r.getReservationId() +
                        " | Vehicle: " + r.getVehicleId() +
                        " | Status: " + r.getStatus() +
                        " | Pickup: " + r.getPickupLocation() +
                        " | Returned: " + r.getReturnDate());
            }
        }
        System.out.println("=============================================");
        System.out.println("             END OF DEMO");
        System.out.println("=============================================");
    }
}
