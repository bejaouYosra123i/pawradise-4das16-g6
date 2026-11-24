package tn.esprit.marketplace.dto;

import tn.esprit.marketplace.entity.enums.OrderStatus;
import tn.esprit.marketplace.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(Long id, String userId, OrderStatus status, BigDecimal totalAmount,
                            String shippingAddress, LocalDateTime createdAt,
                            List<OrderItemResponse> items,
                            PaymentStatus paymentStatus, String trackingNumber) {
}