package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.TicketContext;

public class ClosedState implements TicketState {

    @Override
    public void assign(TicketContext context) {
        System.out.println("Cannot assign. Ticket is already closed.");
    }

    @Override
    public void hold(TicketContext context) {
        System.out.println("Cannot hold. Ticket is already closed.");
    }

    @Override
    public void resolve(TicketContext context) {
        System.out.println("Cannot resolve. Ticket is already closed.");
    }

    @Override
    public void close(TicketContext context) {
        System.out.println("Already closed.");
    }

    @Override
    public String getStateName() {
        return "CLOSED";
    }
}
