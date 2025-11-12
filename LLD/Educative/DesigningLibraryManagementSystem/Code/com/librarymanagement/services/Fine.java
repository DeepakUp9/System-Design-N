package Educative.DesigningLibraryManagementSystem.Code.com.librarymanagement.services;

public class Fine {
    private static final double FINE_PER_DAY = 1.0;
    
    public static double collectFine(String memberId, int days) {
        double amount = days * FINE_PER_DAY;
        System.out.printf("Collected fine of $%.2f from member %s\n", amount, memberId);
        return amount;
    }
}