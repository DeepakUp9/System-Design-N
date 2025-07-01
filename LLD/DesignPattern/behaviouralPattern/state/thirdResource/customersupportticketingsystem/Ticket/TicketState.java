package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.Ticket;

import LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem.TicketContext;

public interface TicketState {
    void assign(TicketContext context);
    void hold(TicketContext context);
    void resolve(TicketContext context);
    void close(TicketContext context);
    String getStateName();
}