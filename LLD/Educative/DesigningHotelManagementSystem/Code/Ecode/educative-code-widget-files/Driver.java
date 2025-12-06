import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Driver {
    public static void main(String[] args) {
        // ===============================
        // 🏨 Scenario 1: Hotel and Room Setup
        // ===============================
        System.out.println("=== Scenario 1: Hotel and Room Setup ===");

        // Step 1: Create the hotel
        Hotel hotel = new Hotel("Ocean View Resort");
        System.out.println("Hotel created: " + hotel.getName());

        // Step 2: Add branches to the hotel
        HotelBranch beachBranch = new HotelBranch("Beachside Branch", new Address());
        HotelBranch cityBranch = new HotelBranch("City Center Branch", new Address());

        hotel.addLocation(beachBranch);
        hotel.addLocation(cityBranch);
        System.out.println("Added " + hotel.getLocations().size() + " hotel branches.");

        // Step 3: Create and add rooms
        Room room101 = new Room("101", RoomStyle.STANDARD, RoomStatus.AVAILABLE, 120.0, false);
        Room room202 = new Room("202", RoomStyle.DELUXE, RoomStatus.AVAILABLE, 220.0, true);

        Catalog catalog = new Catalog();
        catalog.getRooms().add(room101);
        catalog.getRooms().add(room202);
        System.out.println("Room catalog initialized with " + catalog.getRooms().size() + " rooms.\n");

        // ===============================
        // 🛏️ Scenario 2: Room Search, Booking & Notification
        // ===============================
        System.out.println("=== Scenario 2: Room Search, Booking & Notification ===");

        // Step 1: Search for STANDARD rooms
        System.out.println("Searching for available STANDARD rooms for 2 nights...");
        List<Room> availableRooms = catalog.search(RoomStyle.STANDARD, new Date(), 2);
        System.out.println("Available STANDARD rooms found: " + availableRooms.size());

        // Step 2: Guest creation and room booking
        Guest guest = new Guest();
        guest.setName("Alice Smith");
        guest.setEmail("alice.smith@example.com");
        guest.setPhone("987-654-3210");

        RoomBooking booking = new RoomBooking();
        booking.setReservationNumber("RS789123");
        booking.setRoom(room101);
        booking.setStartDate(new Date());
        booking.setDurationInDays(2);
        booking.setStatus(BookingStatus.CONFIRMED);

        guest.getBookings().add(booking);
        System.out.println("Booking confirmed for guest: " + guest.getName());
        System.out.println("Reservation Number: " + booking.getReservationNumber());

        // Step 3: Notifications
        EmailNotification email = new EmailNotification();
        email.setNotificationId(101);
        email.setContent("Hello " + guest.getName() + ", your room booking is confirmed!");
        email.setCreatedOn(new Date());
        email.sendNotification(guest);

        SMSNotification sms = new SMSNotification();
        sms.setNotificationId(102);
        sms.setContent("Hi " + guest.getName() + ", booking confirmed. Enjoy your stay!");
        sms.setCreatedOn(new Date());
        sms.sendNotification(guest);

        System.out.println();

        // ===============================
        // 💳 Scenario 3: Payment & Checkout Process
        // ===============================
        System.out.println("=== Scenario 3: Payment & Checkout Process ===");

        // Step 1: Initiate payment
        BillTransaction transaction = new CashTransaction();
        transaction.setAmount(room101.getBookingPrice());
        transaction.setCreationDate(new Date());
        transaction.setStatus(PaymentStatus.PENDING);
        transaction.initiateTransaction();

        System.out.println("Payment of $" + transaction.getAmount() + " initiated using cash.");
        System.out.println("Transaction status: " + transaction.getStatus());

        // Step 2: Room checkout
        room101.checkout();
        System.out.println("Room " + room101.getRoomNumber() + " has been successfully checked out.");
        System.out.println("Room status is now: " + room101.getStatus());

        System.out.println("\n✅ All hotel operations completed successfully!");
    }
}
