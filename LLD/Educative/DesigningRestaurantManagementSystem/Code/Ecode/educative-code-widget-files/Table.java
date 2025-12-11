import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Table {
    private int tableID;
    private TableStatus status;
    private int maxCapacity;
    private int locationIdentifier;
    private int numberOfSeats;

    public Table(int tableID, TableStatus status, int maxCapacity, int locationIdentifier, int numberOfSeats) {
        this.tableID = tableID;
        this.status = status;
        this.maxCapacity = maxCapacity;
        this.locationIdentifier = locationIdentifier;
        this.numberOfSeats = numberOfSeats;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getLocationIdentifier() {
        return locationIdentifier;
    }

    public void setLocationIdentifier(int locationIdentifier) {
        this.locationIdentifier = locationIdentifier;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public boolean isTableFree() {
        return status == TableStatus.Free;
    }

    public boolean addReservation() {
        if (status == TableStatus.Free) {
            status = TableStatus.Reserved;
            return true;
        }
        return false;
    }

    public static List<Table> search(int capacity, Date startTime) {
        return new ArrayList<>(); // placeholder
    }
}
