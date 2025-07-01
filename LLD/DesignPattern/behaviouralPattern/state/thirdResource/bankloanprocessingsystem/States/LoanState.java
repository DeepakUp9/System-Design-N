package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.LoanContext;

public interface LoanState {
    void verify(LoanContext context);
    void approve(LoanContext context);
    void disburse(LoanContext context);
    void reject(LoanContext context);
    String getStateName();
}
