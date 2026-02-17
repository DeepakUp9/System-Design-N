package com.library.services;

import com.library.models.BookLending;
import com.library.models.Fine;
import com.library.models.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing fines.
 * Calculates and tracks fines for overdue books.
 * 
 * SOLID PRINCIPLE: Single Responsibility Principle
 * - Handles only fine-related operations
 */
public class FineService {
    private final List<Fine> fineHistory;
    private final NotificationService notificationService;
    
    public FineService(NotificationService notificationService) {
        this.fineHistory = new ArrayList<>();
        this.notificationService = notificationService;
    }
    
    /**
     * Calculate and issue fine for overdue book.
     * Uses Fine.DAILY_FINE_RATE for calculation.
     * 
     * @param member The member to be fined
     * @param lending The overdue lending
     * @return Fine object
     */
    public Fine calculateAndIssueFine(Member member, BookLending lending) {
        if (!lending.isOverdue()) {
            throw new IllegalArgumentException("Lending is not overdue");
        }
        
        // Create fine using factory method
        Fine fine = Fine.createOverdueFine(member, lending);
        
        // Add to member's account
        member.addFine(fine.getAmount());
        
        // Record fine
        fineHistory.add(fine);
        
        System.out.println("💰 Fine issued:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Amount: $" + String.format("%.2f", fine.getAmount()));
        System.out.println("   Reason: " + fine.getReason());
        System.out.println("   Fine ID: " + fine.getFineId());
        System.out.println();
        
        return fine;
    }
    
    /**
     * Process fine payment from member.
     * 
     * @param member The member paying the fine
     * @param fine The fine being paid
     * @param amount Amount to pay
     */
    public void payFine(Member member, Fine fine, double amount) {
        if (!fine.getMember().equals(member)) {
            throw new IllegalArgumentException("Fine does not belong to this member");
        }
        
        if (fine.isPaid()) {
            throw new IllegalStateException("Fine is already paid");
        }
        
        if (amount != fine.getAmount()) {
            throw new IllegalArgumentException(
                "Payment amount must equal fine amount. Expected: $" + fine.getAmount()
            );
        }
        
        // Mark fine as paid
        fine.markAsPaid();
        
        // Deduct from member's outstanding fines
        member.payFine(amount);
        
        System.out.println("✅ Fine paid:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Amount: $" + String.format("%.2f", amount));
        System.out.println("   Remaining fines: $" + String.format("%.2f", member.getOutstandingFines()));
        System.out.println();
        
        // Send payment confirmation
        notificationService.sendFinePaymentNotification(member, amount);
    }
    
    /**
     * Waive a fine (librarian action).
     * 
     * @param fine The fine to waive
     * @param member The member whose fine is being waived
     */
    public void waiveFine(Fine fine, Member member) {
        if (!fine.getMember().equals(member)) {
            throw new IllegalArgumentException("Fine does not belong to this member");
        }
        
        if (fine.isPaid()) {
            throw new IllegalStateException("Cannot waive a paid fine");
        }
        
        double amount = fine.getAmount();
        
        // Waive fine
        fine.waive();
        
        // Deduct from member's outstanding fines
        member.payFine(amount);
        
        System.out.println("✅ Fine waived:");
        System.out.println("   Member: " + member.getName());
        System.out.println("   Amount: $" + String.format("%.2f", amount));
        System.out.println("   Reason: " + fine.getReason());
        System.out.println();
    }
    
    /**
     * Get all fines for a member.
     * 
     * @param member The member
     * @return List of fines
     */
    public List<Fine> getMemberFines(Member member) {
        return fineHistory.stream()
                .filter(fine -> fine.getMember().equals(member))
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending fines for a member.
     * 
     * @param member The member
     * @return List of pending fines
     */
    public List<Fine> getPendingFines(Member member) {
        return fineHistory.stream()
                .filter(fine -> fine.getMember().equals(member) && fine.isPending())
                .collect(Collectors.toList());
    }
    
    /**
     * Get all pending fines in the system.
     * 
     * @return List of all pending fines
     */
    public List<Fine> getAllPendingFines() {
        return fineHistory.stream()
                .filter(Fine::isPending)
                .collect(Collectors.toList());
    }
    
    /**
     * Get total amount of pending fines for a member.
     * 
     * @param member The member
     * @return Total pending fine amount
     */
    public double getTotalPendingFines(Member member) {
        return getPendingFines(member).stream()
                .mapToDouble(Fine::getAmount)
                .sum();
    }
    
    /**
     * Get all fines in the system.
     * 
     * @return List of all fines
     */
    public List<Fine> getAllFines() {
        return new ArrayList<>(fineHistory);
    }
}
