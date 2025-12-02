package com.atm.machine.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "date_created", nullable = false)
    // ZonedDateTime is preferred for tracking timestamps with timezone (best practice)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    // One-to-Many relationships (optional, but good for convenient fetching)
    // MappedBy refers to the field name in the Account entity
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;


    // --- STEP 12: KYC/AML Fields ---
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 30) // e.g., PASSPORT, DRIVERS_LICENSE
    private DocumentType documentType;

    @Column(name = "document_id", unique = true, length = 50)
    private String documentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false)
    private KycStatus kycStatus = KycStatus.PENDING; // Default: PENDING

    // --- END KYC/AML Fields ---

    @Column(name = "date_registered", nullable = false)
    private ZonedDateTime dateRegistered;

    @Column(name = "date_updated")
    private ZonedDateTime dateUpdated;



    // ✅ Constructor for DataLoader (without KYC fields)
    public Customer(Long customerId, String firstName, String lastName, String email,
                    String phoneNumber, String address, ZonedDateTime dateCreated) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateCreated = dateCreated;
        this.accounts = new ArrayList<>();
        this.kycStatus = KycStatus.PENDING;
    }

    // ✅ Constructor with KYC fields
    public Customer(Long customerId, String firstName, String lastName, String email,
                    String phoneNumber, String address, LocalDate dateOfBirth,
                    DocumentType documentType, String documentId, KycStatus kycStatus,
                    ZonedDateTime dateCreated, ZonedDateTime dateRegistered,
                    ZonedDateTime dateUpdated) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.documentType = documentType;
        this.documentId = documentId;
        this.kycStatus = kycStatus;
        this.dateCreated = dateCreated;
        this.dateRegistered = dateRegistered;
        this.dateUpdated = dateUpdated;
        this.accounts = new ArrayList<>();
    }

}