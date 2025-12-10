import java.util.*;

public class Admin extends User {
    public boolean blockUser(User user) {
        user.setStatus(UserStatus.BLOCKED);
        return true;
    }

    public boolean unblockUser(User user) {
        user.setStatus(UserStatus.ACTIVE);
        return true;
    }

    public void awardBadge(User user, Badge badge) {
        user.getBadges().add(badge);
    }
}
