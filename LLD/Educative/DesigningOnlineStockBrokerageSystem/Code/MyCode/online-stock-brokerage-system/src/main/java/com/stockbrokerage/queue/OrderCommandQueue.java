package com.stockbrokerage.queue;

import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.queue.command.ProcessOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Invoker Component: Manages the queue of Commands for asynchronous execution.
 */
@Service
@RequiredArgsConstructor
public class OrderCommandQueue {

    // Production Detail: A simple in-memory queue for this exercise.
    // In real life, this would be a Kafka or RabbitMQ producer/consumer structure.
    private final Queue<ProcessOrderCommand> commandQueue = new ConcurrentLinkedQueue<>();

    // Production Detail: Use ObjectFactory for prototype beans (ProcessOrderCommand)
    // Spring manages the creation and injection of dependencies for each new command instance.
    private final ObjectFactory<ProcessOrderCommand> commandFactory;

    /**
     * LLD Core: Adds a new Order processing request to the queue.
     * @param order The order entity to be processed.
     */
    public void submitOrder(Order order) {
        ProcessOrderCommand command = commandFactory.getObject(); // Get a new prototype instance
        command.setOrder(order);
        commandQueue.add(command);
        System.out.printf("[COMMAND QUEUE] Order %s submitted to queue. Queue size: %d%n",
                order.getOrderReferenceId(), commandQueue.size());
    }

    /**
     * Production Detail: A scheduler or a dedicated worker thread would poll this method.
     */
    // @Scheduled(fixedDelay = 100) // Example scheduler annotation
    public void pollAndExecute() {
        ProcessOrderCommand command = commandQueue.poll();
        if (command != null) {
            command.execute(); // Executes the command logic
        }
    }
}