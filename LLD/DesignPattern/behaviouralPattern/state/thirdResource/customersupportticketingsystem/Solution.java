package LLD.DesignPattern.behaviouralPattern.state.thirdResource.customersupportticketingsystem;

public class Solution {
    public static void main(String[] args) {
        TicketContext ticket = new TicketContext();

        ticket.assign();   // NEW → IN_PROGRESS
        ticket.hold();     // IN_PROGRESS → ON_HOLD
        ticket.assign();   // ON_HOLD → IN_PROGRESS
        ticket.resolve();  // IN_PROGRESS → RESOLVED
        ticket.close();    // RESOLVED → CLOSED

        System.out.println("Final State: " + ticket.getCurrentState());
    }
}
