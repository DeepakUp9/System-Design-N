package LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States.InitiatedState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.bankloanprocessingsystem.States.LoanState;

public class LoanContext {
   private LoanState currentState;

    public LoanContext() {
        this.currentState = new InitiatedState(); // Initial state
    }

    public void setState(LoanState state) {
        System.out.println("State changed to: " + state.getStateName());
        this.currentState = state;
    }

    public void verify() {
        currentState.verify(this);
    }

    public void approve() {
        currentState.approve(this);
    }

    public void disburse() {
        currentState.disburse(this);
    }

    public void reject() {
        currentState.reject(this);
    }

    public String getCurrentState() {
        return currentState.getStateName();
    }
}
