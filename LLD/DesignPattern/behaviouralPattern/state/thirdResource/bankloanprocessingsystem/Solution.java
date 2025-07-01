package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem;

public class Solution {
    public static void main(String[] args) {
        LoanContext loan = new LoanContext();

        loan.verify();
        loan.approve();
        loan.disburse();

        System.out.println("Final State: " + loan.getCurrentState());

        // Try invalid transitions
        loan.reject(); // Should not work after disbursed


    }
}
