package com.stockbrokerage.order.mapper;

import com.stockbrokerage.order.dto.OrderRequest;
import com.stockbrokerage.order.model.Order;
import com.stockbrokerage.order.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Production Detail: Interface for converting DTO to Entity using MapStruct.
 * This decouples the Controller from the Domain Model.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    /**
     * Maps the OrderRequest (Builder output) to the core Order entity.
     * @param request The DTO from the API.
     * @return The domain entity ready for persistence.
     */
    @Mapping(target = "id", ignore = true) // Database ID is ignored on creation
    @Mapping(target = "orderReferenceId", ignore = true) // Handled by @PrePersist in Order entity
    @Mapping(target = "status", expression = "java(com.stockbrokerage.order.enums.OrderStatus.NEW)") // Enforce initial state
    @Mapping(target = "placedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "executedAt", ignore = true)
    Order toEntity(OrderRequest request);

    // Reverse method for DTO responses (Order to OrderResponseDTO) would also be here
}