package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.TicketContext;

public class NewState implements TicketState {
    @Override
    public void assign(TicketContext context) {
        System.out.println("Ticket assigned and work started.");
        context.setState(new InProgressState());
    }

    @Override
    public void hold(TicketContext context) {
        System.out.println("Cannot put NEW ticket on hold.");
    }

    @Override
    public void resolve(TicketContext context) {
        System.out.println("Cannot resolve a NEW ticket.");
    }

    @Override
    public void close(TicketContext context) {
        System.out.println("Cannot close a NEW ticket.");
    }

    public String getStateName() {
        return "NEW";
    }
}
