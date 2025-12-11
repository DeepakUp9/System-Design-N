import java.util.Date;
import java.util.List;

public class Reservation {
    private int reservationID;
    private Date timeOfReservation;
    private int peopleCount;
    private ReservationStatus status;
    private String notes;
    private Date checkInTime;
    private Customer customer;
    private Table[] tables;
    private List<Notification> notifications;

    public Reservation(int reservationID, Date timeOfReservation, int peopleCount, ReservationStatus status,
                       String notes, Date checkInTime, Customer customer, Table[] tables, List<Notification> notifications) {
        this.reservationID = reservationID;
        this.timeOfReservation = timeOfReservation;
        this.peopleCount = peopleCount;
        this.status = status;
        this.notes = notes;
        this.checkInTime = checkInTime;
        this.customer = customer;
        this.tables = tables;
        this.notifications = notifications;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public Date getTimeOfReservation() {
        return timeOfReservation;
    }

    public void setTimeOfReservation(Date timeOfReservation) {
        this.timeOfReservation = timeOfReservation;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Table[] getTables() {
        return tables;
    }

    public void setTables(Table[] tables) {
        this.tables = tables;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public boolean updatePeopleCount(int count) {
        this.peopleCount = count;
        return true;
    }
}
