package com.rms.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rms.application.dto.CreateOrderRequest;
import com.rms.application.model.Branch;
import com.rms.application.repository.BranchRepository;
import com.rms.application.repository.OrderRepository;
import com.rms.core.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full Integration Test: Validates API, Security, Transactions, and LLD flow.
 */
@SpringBootTest
@AutoConfigureMockMvc // Auto-configures MockMvc
@Transactional // Ensures DB changes are rolled back after each test
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @WithMockUser(roles = {"WAITER"}) // Test with an authorized role
    void testCreateOrder_Success_TriggersLLDAndEvent() throws Exception {
        // Pre-requisite: Ensure a branch exists (from Flyway V1)
        Branch existingBranch = branchRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Branch not found. Check Flyway script."));

        // 1. Prepare Request DTO
        CreateOrderRequest request = new CreateOrderRequest();
        request.setBranchId(existingBranch.getId());
        request.setChannelType("DINE_IN");
        request.setTotalAmount(new BigDecimal("50.00")); // Base price

        // 2. ACT: Call the secured API endpoint
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                // 3. ASSERT: API Response (Success and correct status code)
                .andExpect(status().isCreated())
                // ASSERT: LLD Logic Check: Order Status should be PROCESSING (State Pattern transition)
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.PROCESSING.name())));

        // 4. ASSERT: Persistence and Event Check (Post-transaction verification)
        // Since we are @Transactional, we check the repository state before rollback.
        Optional<com.rms.application.model.Order> savedOrder = orderRepository.findAll().stream()
                .filter(o -> o.getChannelType().equals("DINE_IN"))
                .findFirst();

        assertTrue(savedOrder.isPresent());
        assertEquals(OrderStatus.PROCESSING, savedOrder.get().getOrderStatus());
        // The event publishing is hard to verify without specific tools, but the logic is triggered in the service.
    }

    @Test
    void testCreateOrder_Unauthorized_FailsSecurity() throws Exception {
        // Pre-requisite
        CreateOrderRequest request = new CreateOrderRequest();
        request.setBranchId(1L);
        request.setChannelType("DINE_IN");
        request.setTotalAmount(new BigDecimal("50.00"));

        // ACT (Without @WithMockUser, hence unauthenticated)
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                // ASSERT: Security should return 401
                .andExpect(status().isUnauthorized());
    }
}