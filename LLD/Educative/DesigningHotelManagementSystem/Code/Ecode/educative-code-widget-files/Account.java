import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Account {
    private String id;
    private String password;
    private AccountStatus status;

    public boolean resetPassword() {
        // Password reset logic
        System.out.println("Password reset.");
        return true;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}