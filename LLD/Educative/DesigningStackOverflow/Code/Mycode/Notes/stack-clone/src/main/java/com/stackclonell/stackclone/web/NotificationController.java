package com.stackclonell.stackclone.web;

import com.stackclonell.stackclone.core.model.Notification;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.NotificationRepository;
import com.stackclonell.stackclone.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationController(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint to fetch paginated notifications for the logged-in user.
     * (Core Feature / Scalability)
     */
    @GetMapping
    public ResponseEntity<Page<Notification>> getNotifications(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        User currentUser = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found."));

        PageRequest pageable = PageRequest.of(page, size);

        Page<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedDateDesc(
                currentUser.getId(),
                pageable
        );

        return ResponseEntity.ok(notifications);
    }

    // Production: Would also need a PUT endpoint to mark notifications as 'read'
}