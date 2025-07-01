package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.TicketContext;

public class OnHoldState implements TicketState {

    @Override
    public void assign(TicketContext context) {
        System.out.println("Resuming work from ON_HOLD.");
        context.setState(new InProgressState());
    }

    @Override
    public void hold(TicketContext context) {
        System.out.println("Ticket is already on hold.");
    }

    @Override
    public void resolve(TicketContext context) {
        System.out.println("Cannot resolve while on hold. Resume first.");
    }

    @Override
    public void close(TicketContext context) {
        System.out.println("Cannot close ticket while it's on hold.");
    }

    @Override
    public String getStateName() {
        return "ON_HOLD";
    }
}
