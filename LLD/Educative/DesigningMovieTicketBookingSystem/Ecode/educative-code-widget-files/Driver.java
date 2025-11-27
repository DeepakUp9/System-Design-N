import java.util.*;
import java.text.SimpleDateFormat;

public class Driver {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // ----- SETUP PHASE -----
        System.out.println("=====================================================");
        System.out.println("MOVIE TICKET BOOKING SYSTEM - SCENARIO DEMONSTRATION");
        System.out.println("=====================================================");

        // 1. Create city, cinema, hall, seats
        City city = new City("Seattle", "WA", 98101, new ArrayList<>());
        Cinema cinema = new Cinema(1, new ArrayList<>(), city);
        city.cinemas.add(cinema);

        Hall hall = new Hall(101, new ArrayList<>());
        cinema.halls.add(hall);

        // 2. Create movies
        Movie inception = new Movie(
            "Inception", "Sci-Fi",
            parseDate("2010-07-16"), "English", 148, new ArrayList<>()
        );
        Movie interstellar = new Movie(
            "Interstellar", "Sci-Fi",
            parseDate("2014-11-07"), "English", 169, new ArrayList<>()
        );

        // 3. Create ShowTimes (each with seats)
        List<Seat> seatsShow1 = Arrays.asList(new Platinum(), new Gold(), new Silver());
        for (int i = 0; i < seatsShow1.size(); i++) {
            seatsShow1.get(i).seatNo = "A" + (i+1);
            seatsShow1.get(i).status = SeatStatus.AVAILABLE;
            seatsShow1.get(i).setRate();
        }
        ShowTime show1 = new ShowTime(1001, new Date(), new Date(), 150, seatsShow1);
        hall.shows.add(show1);
        inception.shows.add(show1);

        // 4. Create Catalog and add movies
        Catalog catalog = new Catalog();
        catalog.movieTitles = new HashMap<>();
        catalog.movieTitles.put("Inception", Arrays.asList(inception));
        catalog.movieTitles.put("Interstellar", Arrays.asList(interstellar));

        // 5. Create admin, customer, ticket agent
        Admin admin = new Admin();
        admin.name = "Bob (Admin)";
        admin.email = "bob.admin@cinema.com";

        Customer customer = new Customer();
        customer.name = "Alice (Customer)";
        customer.email = "alice@example.com";
        customer.bookings = new ArrayList<>();

        TicketAgent agent = new TicketAgent();
        agent.name = "Eve (Ticket Agent)";
        agent.email = "eve.agent@cinema.com";

        // ----- SCENARIO 1: CUSTOMER SEARCHES FOR A MOVIE -----
        System.out.println("\n----- SCENARIO 1: Customer Searches For A Movie -----");
        System.out.printf("%s is searching for the movie 'Inception':\n", customer.name);
        List<Movie> found = catalog.searchMovieTitle("Inception");
        for (Movie m : found) {
            System.out.printf("  - Found: %s (%s, Released: %s)\n", m.title, m.genre, sdf.format(m.releaseDate));
        }

        // ----- SCENARIO 2: CUSTOMER BOOKS A TICKET -----
        System.out.println("\n----- SCENARIO 2: Customer Books A Ticket -----");
        Seat chosenSeat = show1.seats.get(0); // Platinum
        if (chosenSeat.isAvailable()) {
            chosenSeat.status = SeatStatus.BOOKED;

            // Create ticket and booking
            MovieTicket ticket = new MovieTicket(2001, chosenSeat, inception, show1);
            List<MovieTicket> tickets = new ArrayList<>();
            tickets.add(ticket);

            // Payment
            CreditCard payment = new CreditCard();
            payment.amount = 15.0;
            payment.nameOnCard = customer.name;
            payment.cardNumber = "1234-5678-9012-3456";
            payment.billingAddress = "123 1st Ave, Seattle, WA";
            payment.code = 123;
            payment.status = PaymentStatus.PENDING;
            payment.timestamp = new Date();
            payment.makePayment();
            payment.status = PaymentStatus.CONFIRMED;

            // Booking
            Booking booking = new Booking(3001, 15, 1, new Date(), BookingStatus.CONFIRMED,
                    payment, tickets, Arrays.asList(chosenSeat));
            customer.bookings.add(booking);

            // Notification
            EmailNotification notification = new EmailNotification();
            notification.content = String.format("Booking confirmed for %s!\nSeat: %s (%s)\nShow ID: %d",
                inception.title, chosenSeat.seatNo, chosenSeat.getClass().getSimpleName(), show1.showId);
            notification.sendNotification(customer);

            System.out.printf("%s successfully booked a ticket for: %s\n", customer.name, inception.title);
            System.out.printf("  - Seat: %s (%s), Show ID: %d\n", chosenSeat.seatNo, chosenSeat.getClass().getSimpleName(), show1.showId);
            System.out.printf("  - Payment: $%.2f by Credit Card ending %s\n",
                payment.amount, payment.cardNumber.substring(payment.cardNumber.length()-4));
        } else {
            System.out.println("Seat is not available!");
        }

        // ----- SCENARIO 3: ADMIN ADDS A NEW SHOW -----
        System.out.println("\n----- SCENARIO 3: Admin Adds A New Show -----");
        ShowTime newShow = new ShowTime(1002, new Date(), new Date(), 148, new ArrayList<>());
        boolean showAdded = admin.addShow(newShow);
        if (showAdded) {
            hall.shows.add(newShow);
            inception.shows.add(newShow);
            System.out.printf("%s added a new show for: %s (Show ID: %d)\n", admin.name, inception.title, newShow.showId);
        }

        // ----- SCENARIO 4: TICKET AGENT CREATES A BOOKING FOR WALK-IN -----
        System.out.println("\n----- SCENARIO 4: Ticket Agent Creates A Walk-In Booking -----");
        ShowTime showInterstellar = new ShowTime(1003, new Date(), new Date(), 169, new ArrayList<>());
        hall.shows.add(showInterstellar);
        interstellar.shows.add(showInterstellar);

        List<Seat> seatsShow2 = Arrays.asList(new Gold(), new Silver());
        for (int i = 0; i < seatsShow2.size(); i++) {
            seatsShow2.get(i).seatNo = "B" + (i+1);
            seatsShow2.get(i).status = SeatStatus.AVAILABLE;
            seatsShow2.get(i).setRate();
        }
        showInterstellar.seats = seatsShow2;

        Seat walkInSeat = showInterstellar.seats.get(1); // Silver
        if (walkInSeat.isAvailable()) {
            walkInSeat.status = SeatStatus.BOOKED;

            MovieTicket ticket = new MovieTicket(2002, walkInSeat, interstellar, showInterstellar);
            List<MovieTicket> tickets = new ArrayList<>();
            tickets.add(ticket);

            Cash cashPayment = new Cash();
            cashPayment.amount = 10.0;
            cashPayment.status = PaymentStatus.PENDING;
            cashPayment.timestamp = new Date();
            cashPayment.makePayment();
            cashPayment.status = PaymentStatus.CONFIRMED;

            Booking agentBooking = new Booking(3002, 10, 1, new Date(), BookingStatus.CONFIRMED,
                    cashPayment, tickets, Arrays.asList(walkInSeat));
            boolean agentBooked = agent.createBooking(agentBooking);
            if (agentBooked) {
                System.out.printf("%s booked ticket for: %s (walk-in)\n", agent.name, interstellar.title);
                System.out.printf("  - Seat: %s (%s), Show ID: %d\n", walkInSeat.seatNo, walkInSeat.getClass().getSimpleName(), showInterstellar.showId);
                System.out.printf("  - Payment: $%.2f by Cash\n", cashPayment.amount);
            }
        }

        // ----- SCENARIO 5: ADMIN DELETES A MOVIE -----
        System.out.println("\n----- SCENARIO 5: Admin Deletes A Movie -----");
        boolean movieDeleted = admin.deleteMovie(interstellar);
        if (movieDeleted) {
            catalog.movieTitles.remove("Interstellar");
            System.out.printf("%s deleted movie: %s\n", admin.name, interstellar.title);
        }

        System.out.println("\n=====================================================");
        System.out.println("       END OF MOVIE TICKET BOOKING DEMO RUN");
        System.out.println("=====================================================");
    }

    // Helper to parse date without deprecated API
    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception ex) {
            return new Date();
        }
    }
}
