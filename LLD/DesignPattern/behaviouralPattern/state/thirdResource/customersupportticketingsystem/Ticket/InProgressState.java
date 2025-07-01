package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.TicketContext;

public class InProgressState implements TicketState {
    @Override
    public void assign(TicketContext context) {
        System.out.println("Already in progress.");
    }

    @Override
    public void hold(TicketContext context) {
        System.out.println("Putting ticket on hold.");
        context.setState(new OnHoldState());
    }

    @Override
    public void resolve(TicketContext context) {
        System.out.println("Ticket resolved.");
        context.setState(new ResolvedState());
    }

    @Override
    public void close(TicketContext context) {
        System.out.println("Cannot close. Please resolve first.");
    }

    public String getStateName() {
        return "IN_PROGRESS";
    }
}
