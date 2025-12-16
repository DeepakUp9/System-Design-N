import java.util.*;

public class Driver {

    public static void main(String[] args) {
        System.out.println("== Initializing system... ==");
        
        // Create addresses
        Address address1 = new Address(12345, "Street 1", "CityA", "StateA", "CountryA");
        Address address2 = new Address(67890, "Street 2", "CityB", "StateB", "CountryB");
        System.out.println("Addresses created.");
        
        // Airports
        Airport airport1 = new Airport("Alpha Airport", "ALP", address1, new ArrayList<>());
        Airport airport2 = new Airport("Beta Airport", "BET", address2, new ArrayList<>());
        System.out.println("Airports created: " + airport1.getName() + " and " + airport2.getName());
        
        // Aircraft and seats
        List<Seat> aircraftSeats = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            aircraftSeats.add(new Seat("A" + i, SeatType.REGULAR, SeatClass.ECONOMY));
        }
        Aircraft aircraft = new Aircraft("Boeing 737", "B737", "737-800", 10, aircraftSeats);
        Airline.getInstance().addAircraft(aircraft);
        System.out.println("Aircraft created and added: " + aircraft.getName());
        
        // Accounts
        Account customerAccount = new Account(101, "jane_doe", "password");
        Account adminAccount = new Account(201, "admin1", "adminpass");
        Account crewAccount = new Account(301, "pilot_guy", "flyhigh");
        System.out.println("User accounts created.");
        
        // People
        Customer customer = new Customer(1, "Jane Doe", address1, "jane@example.com", "111-222-3333", customerAccount);
        Admin admin = new Admin("Admin One", address2, "admin@example.com", "444-555-6666", adminAccount);
        Crew crew = new Crew("Captain Sky", address1, "pilot@example.com", "777-888-9999", crewAccount);
        Airline.getInstance().addCrew(crew);
        System.out.println("Customer, Admin, and Crew created.");
        
        // Flight and flight instance
        Flight flight = new Flight("FL123", 120, airport1, airport2, new ArrayList<>());
        FlightInstance flightInstance = new FlightInstance(flight, new Date(), "G5", FlightStatus.SCHEDULED, aircraft, new ArrayList<>());
        
        for (Seat seat : aircraftSeats) {
            flightInstance.getSeats().add(new FlightSeat(seat.getSeatNumber(), seat.getType(), seat.getSeatClass(), 150.0));
        }
        flight.getInstances().add(flightInstance);
        admin.addFlight(flight);
        System.out.println("Initial flight created and flight instance added.");
        
        System.out.println("System initialized.\n");


        // ========= Scenario 1 =========
        System.out.println("== Scenario 1: Customer books and pays ==");

        Passenger p1 = new Passenger(1, "Jane Doe", "Female", createDate(1990, 5, 5), "P12345");
        FlightSeat selectedSeat = flightInstance.getSeats().get(0);
        selectedSeat.setStatus(SeatStatus.BOOKED);
        selectedSeat.setReservationNumber("RESV001");

        HashMap<Passenger, FlightSeat> seatMap = new HashMap<>();
        seatMap.put(p1, selectedSeat);

        FlightReservation reservation = new FlightReservation("RESV001", flightInstance, seatMap, ReservationStatus.CONFIRMED, new Date());

        List<FlightReservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        List<Passenger> passengers = new ArrayList<>();
        passengers.add(p1);

        Itinerary itinerary = new Itinerary(airport1, airport2, new Date(), reservations, passengers);
        itinerary.makeReservation();

        CreditCard card = new CreditCard(1, selectedSeat.getFare(), "Jane Doe", "4111111111111111");
        customer.makePayment(card);

        System.out.println();

        // ========= Scenario 2 =========
        System.out.println("== Scenario 2: Admin adds a new flight and assigns crew ==");

        Aircraft newAircraft = new Aircraft("Airbus A320", "A320", "A320-200", 5, new ArrayList<>());
        admin.addAircraft(newAircraft);

        Flight newFlight = new Flight("FL456", 180, airport2, airport1, new ArrayList<>());
        FlightInstance newInstance = new FlightInstance(newFlight, new Date(), "H2", FlightStatus.SCHEDULED, newAircraft, new ArrayList<>());
        newFlight.getInstances().add(newInstance);

        admin.addFlight(newFlight);
        admin.assignCrew(crew, newInstance);

        System.out.println();

        // ========= Scenario 3 =========
        System.out.println("== Scenario 3: Crew views schedule ==");

        List<FlightInstance> crewSchedule = crew.viewSchedule();
        for (FlightInstance fi : crewSchedule) {
            System.out.println("Assigned to Flight " + fi.getFlight().getFlightNo() + " at Gate " + fi.getGate());
        }
    }

    // Utility method to safely create a Date from year, month, day
    private static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day); // Java months are 0-based
        return cal.getTime();
    }
}
