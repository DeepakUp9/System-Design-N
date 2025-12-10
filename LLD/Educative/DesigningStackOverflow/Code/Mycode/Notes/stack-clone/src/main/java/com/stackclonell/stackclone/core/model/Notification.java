package com.stackclonell.stackclone.core.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who receives the notification
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    private User recipient;

    private String message;

    // Link to the content the notification is about (Question/Answer ID)
    private Long contentId;

    private String notificationType; // e.g., "NEW_ANSWER", "COMMENT_ADDED"

    private boolean isRead = false;

    private LocalDateTime createdDate = LocalDateTime.now();
}