package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.LoanContext;

public class ApprovedState implements LoanState {

    @Override
    public void verify(LoanContext context) {
        System.out.println("Already approved. Cannot go back.");
    }

    @Override
    public void approve(LoanContext context) {
        System.out.println("Already approved.");
    }

    @Override
    public void disburse(LoanContext context) {
        System.out.println("Loan disbursed.");
        context.setState(new DisbursedState());
    }

    @Override
    public void reject(LoanContext context) {
        System.out.println("Loan rejected.");
        context.setState(new RejectedState());
    }

    public String getStateName() {
        return "APPROVED";
    }
}

