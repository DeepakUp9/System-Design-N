package com.atm.machine.service;

import com.atm.machine.entity.AuditLog;
import com.atm.machine.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for logging non-transactional system events.
 * It uses its own transaction to ensure logs are written even if the main operation fails.
 *
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Records an audit event asynchronously.
     * Uses propagation REQUIRES_NEW to commit the audit log independently of the main transaction.
     * @param entityType The type of entity involved (e.g., CARD, CUSTOMER)
     * @param entityId The ID of the affected entity
     * @param actionType The action performed (e.g., LOGIN_FAIL, PIN_CHANGE)
     * @param actorId The ID of the actor (e.g., customerId or 0 for system)
     * @param details Additional details (JSON string or message)
     * @param ipAddress The source IP address
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logEvent(String entityType, Long entityId, String actionType, Long actorId, String details, String ipAddress) {
        AuditLog logEntry = new AuditLog();
        logEntry.setEntityType(entityType);
        logEntry.setEntityId(entityId);
        logEntry.setActionType(actionType);
        logEntry.setActorId(actorId);
        logEntry.setDetails(details);
        logEntry.setIpAddress(ipAddress);

        try {
            auditLogRepository.save(logEntry);
            // log.debug("Audit event logged: {}", actionType);
        } catch (Exception e) {
            log.error("Failed to save audit log for action {}: {}", actionType, e.getMessage());
            // Critically, log to system console if DB fails
        }
    }
}