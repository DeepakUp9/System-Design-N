package com.stackclonell.stackclone.repository;

import com.stackclonell.stackclone.core.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Pagination for the user's notification list (Scalability)
    Page<Notification> findByRecipientIdOrderByCreatedDateDesc(Long recipientId, Pageable pageable);
}