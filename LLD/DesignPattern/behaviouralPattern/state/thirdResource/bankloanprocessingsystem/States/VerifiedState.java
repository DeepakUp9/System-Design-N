package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.LoanContext;

public class VerifiedState implements LoanState {

    @Override
    public void verify(LoanContext context) {
        System.out.println("Already verified.");
    }

    @Override
    public void approve(LoanContext context) {
        System.out.println("Loan approved.");
        context.setState(new ApprovedState());
    }

    @Override
    public void disburse(LoanContext context) {
        System.out.println("Cannot disburse before approval.");
    }

    @Override
    public void reject(LoanContext context) {
        System.out.println("Loan rejected.");
        context.setState(new RejectedState());
    }

    public String getStateName() {
        return "VERIFIED";
    }
}
