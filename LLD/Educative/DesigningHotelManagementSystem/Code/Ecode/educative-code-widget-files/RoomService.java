import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class RoomService extends Service {
    private boolean isChargeable;
    private Date requestTime;

    // Getters and Setters
    public boolean isChargeable() {
        return isChargeable;
    }

    public void setChargeable(boolean chargeable) {
        isChargeable = chargeable;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }
}