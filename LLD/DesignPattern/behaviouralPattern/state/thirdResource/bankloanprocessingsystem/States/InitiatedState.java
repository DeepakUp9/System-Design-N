package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.LoanContext;

public class InitiatedState implements LoanState {

    @Override
    public void verify(LoanContext context) {
        System.out.println("Loan verified.");
        context.setState(new VerifiedState());
    }

    @Override
    public void approve(LoanContext context) {
        System.out.println("Cannot approve before verification.");
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
        return "INITIATED";
    }
}

