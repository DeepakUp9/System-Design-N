package com.rms.core.state;

import com.rms.core.context.OrderContext;
import com.rms.core.model.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the NewOrderState (LLD core logic).
 */
@ExtendWith(MockitoExtension.class)
public class NewOrderStateTest {

    private NewOrderState newOrderState;

    @Mock
    private OrderContext mockContext; // Mock the context that holds the state

    @BeforeEach
    void setUp() {
        newOrderState = new NewOrderState();
        // Setup the mock context to return a test ID
        when(mockContext.getOrderId()).thenReturn(1L);
    }

    @Test
    void testProcessOrder_Success() {
        // ACT: Call the method designed to change the state
        newOrderState.processOrder(mockContext);

        // ASSERT: Verify the expected side effect (setting the next status) occurred
        verify(mockContext, times(1)).setStatus(OrderStatus.PROCESSING);
        // In a fully wired test, we would also verify context.changeState() was called
    }

    @Test
    void testFulfillOrder_InvalidAction() {
        // ACT: Call an invalid action for the current state
        newOrderState.fulfillOrder(mockContext);

        // ASSERT: Verify no state change or status update occurred
        verify(mockContext, never()).setStatus(any());
        // We rely on the log message printed in the actual class for simple invalid actions
    }

    @Test
    void testCancelOrder_Success() {
        // ACT
        newOrderState.cancelOrder(mockContext);

        // ASSERT: Verify transition to CANCELLED state
        verify(mockContext, times(1)).setStatus(OrderStatus.CANCELLED);
    }
}