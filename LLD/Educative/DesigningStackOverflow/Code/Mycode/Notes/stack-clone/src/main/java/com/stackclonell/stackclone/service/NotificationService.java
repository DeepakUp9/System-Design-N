package com.stackclonell.stackclone.service;

import com.stackclonell.stackclone.core.event.AnswerSubmittedEvent;
import com.stackclonell.stackclone.core.model.Notification;
import com.stackclonell.stackclone.core.model.User;
import com.stackclonell.stackclone.repository.NotificationRepository;
import com.stackclonell.stackclone.repository.UserRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /**
     * LLD: The Observer method triggered by a new Answer.
     */
    @EventListener
    @Transactional
    public void handleNewAnswer(AnswerSubmittedEvent event) {

        // 1. Get the recipient (the author of the original question)
        User recipient = userRepository.findById(event.getQuestionAuthorId())
                .orElse(null);

        if (recipient != null) {
            // 2. In-App Notification (Synchronous part)
            createInAppNotification(recipient, event);

            // 3. Email Notification (ASYNCHRONOUS part - SCALABILITY)
            // This method is marked @Async and runs in the NotificationExecutor thread pool.
            sendEmailNotification(recipient, event);
        }
    }

    /**
     * Stores the notification in the database for the user's dashboard.
     */
    private void createInAppNotification(User recipient, AnswerSubmittedEvent event) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setContentId(event.getAnswerId());
        notification.setNotificationType("NEW_ANSWER");
        notification.setMessage(
                String.format("%s answered your question: '%s'",
                        event.getAnswerAuthorUsername(),
                        event.getQuestionTitle())
        );
        notificationRepository.save(notification);
        System.out.println("Observer: In-App Notification created for user " + recipient.getId());
    }

    /**
     * Simulates sending a potentially slow external email notification.
     * @Async ensures it doesn't block the main process.
     */
    @Async
    public void sendEmailNotification(User recipient, AnswerSubmittedEvent event) {
        // Production implementation would use JavaMailSender or an external service (SendGrid, AWS SES)
        try {
            Thread.sleep(1000); // Simulate network delay of email service
        } catch (InterruptedException ignored) {}

        System.out.println(
                String.format("Async: Email sent to %s: '%s answered your question.'",
                        recipient.getEmail(),
                        event.getAnswerAuthorUsername())
        );
    }
}