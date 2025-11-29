import java.util.*;

public class VehicleLog {
    private int logId;
    private VehicleLogType logType;
    private String description;
    private Date creationDate;

    public VehicleLog(int id, VehicleLogType type, String desc) {
        this.logId = id;
        this.logType = type;
        this.description = desc;
        this.creationDate = new Date();
    }
}
