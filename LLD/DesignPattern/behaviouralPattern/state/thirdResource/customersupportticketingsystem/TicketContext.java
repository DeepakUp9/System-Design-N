package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket.NewState;
import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket.TicketState;

public class TicketContext {
    private TicketState currentState;

    public TicketContext() {
        this.currentState = new NewState(); // Initial state
    }

    public void setState(TicketState state) {
        System.out.println("Transitioned to: " + state.getStateName());
        this.currentState = state;
    }

    public void assign() {
        currentState.assign(this);
    }

    public void hold() {
        currentState.hold(this);
    }

    public void resolve() {
        currentState.resolve(this);
    }

    public void close() {
        currentState.close(this);
    }

    public String getCurrentState() {
        return currentState.getStateName();
    }
}
