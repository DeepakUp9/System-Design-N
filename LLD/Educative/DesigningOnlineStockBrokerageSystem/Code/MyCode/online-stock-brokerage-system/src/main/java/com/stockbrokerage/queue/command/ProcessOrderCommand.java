package com.stockbrokerage.queue.command;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.service.OrderExecutionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Concrete Command: Encapsulates the request to process a specific Order.
 */
@Component
@Scope("prototype") // Must be prototype to ensure a new instance is created for each queued order
@RequiredArgsConstructor
public class ProcessOrderCommand implements OrderCommand {

    // Receiver: The service that performs the actual business logic
    private final OrderExecutionService orderExecutionService;

    // Command State: The data needed to execute the request
    @Getter
    private Order targetOrder;

    /**
     * Initializes the command with the order data.
     * @param order The order to be processed.
     */
    public void setOrder(Order order) {
        this.targetOrder = order;
    }

    /**
     * Execution method: Delegates the work to the Receiver.
     */
    @Override
    public void execute() {
        if (targetOrder == null) {
            System.err.println("Command skipped: Target Order is null.");
            return;
        }

        // Production: Add retry logic, failure handling, and Dead Letter Queue logging here.
        System.out.printf("[COMMAND EXECUTE] Processing queued order %s of type %s.%n",
                targetOrder.getOrderReferenceId(), targetOrder.getType());

        // Execution delegation to the main service (which uses Strategy/State patterns)
        orderExecutionService.processOrder(targetOrder);
    }
}