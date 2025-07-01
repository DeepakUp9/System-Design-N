package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.LoanContext;

public class RejectedState implements LoanState {

    public void verify(LoanContext context) {
        System.out.println("Loan is rejected. Cannot verify.");
    }

    public void approve(LoanContext context) {
        System.out.println("Loan is rejected. Cannot approve.");
    }

    public void disburse(LoanContext context) {
        System.out.println("Loan is rejected. Cannot disburse.");
    }

    public void reject(LoanContext context) {
        System.out.println("Already rejected.");
    }

    public String getStateName() {
        return "REJECTED";
    }
}
