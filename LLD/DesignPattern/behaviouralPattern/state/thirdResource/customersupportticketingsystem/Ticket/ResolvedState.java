package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.TicketContext;

public class ResolvedState implements TicketState {

    @Override
    public void assign(TicketContext context) {
        System.out.println("Cannot assign. Ticket already resolved.");
    }

    @Override
    public void hold(TicketContext context) {
        System.out.println("Cannot put a resolved ticket on hold.");
    }

    @Override
    public void resolve(TicketContext context) {
        System.out.println("Already resolved.");
    }

    @Override
    public void close(TicketContext context) {
        System.out.println("Ticket closed.");
        context.setState(new ClosedState());
    }

    @Override
    public String getStateName() {
        return "RESOLVED";
    }
}

