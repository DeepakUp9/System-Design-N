import java.util.*;

public class Customer extends Account {
    private String licenseNumber;
    private Date licenseExpiry;

    public void setLicenseNumber(String n) { licenseNumber = n; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseExpiry(Date d) { licenseExpiry = d; }
    public Date getLicenseExpiry() { return licenseExpiry; }

    @Override
    public boolean resetPassword() {
        this.setPassword(UUID.randomUUID().toString());
        return true;
    }
}
