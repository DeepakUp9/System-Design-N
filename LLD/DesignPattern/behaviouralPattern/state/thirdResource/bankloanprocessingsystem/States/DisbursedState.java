package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.LoanContext;

public class DisbursedState implements LoanState {

    public void verify(LoanContext context) {
        System.out.println("Already disbursed.");
    }

    public void approve(LoanContext context) {
        System.out.println("Already disbursed.");
    }

    public void disburse(LoanContext context) {
        System.out.println("Already disbursed.");
    }

    public void reject(LoanContext context) {
        System.out.println("Cannot reject after disbursal.");
    }

    public String getStateName() {
        return "DISBURSED";
    }
}
