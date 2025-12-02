package com.atm.machine.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

/**
 * Entity to store immutable audit records for system events (e.g., login attempts,
 * card status changes, administrative actions).
 */
@Entity
@Table(name = "audit_log")
@Data
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @Column(name = "timestamp", nullable = false)
    private ZonedDateTime timestamp = ZonedDateTime.now();

    @Column(name = "entity_type", nullable = false, length = 50) // e.g., CARD, CUSTOMER, SYSTEM
    private String entityType;

    @Column(name = "entity_id")
    private Long entityId; // The ID of the affected entity (e.g., card_id, customer_id)

    @Column(name = "action_type", nullable = false, length = 50) // e.g., LOGIN_SUCCESS, LOGIN_FAIL, STATUS_CHANGE
    private String actionType;

    @Column(name = "actor_id")
    private Long actorId; // ID of the user or system component performing the action

    @Column(name = "details", columnDefinition = "TEXT")
    private String details; // Detailed JSON or text message about the action

    @Column(name = "ip_address", length = 45) // IP address of the request origin
    private String ipAddress;
}