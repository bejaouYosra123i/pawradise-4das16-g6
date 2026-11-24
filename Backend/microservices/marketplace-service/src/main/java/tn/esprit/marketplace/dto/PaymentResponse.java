package tn.esprit.marketplace.dto;

import tn.esprit.marketplace.entity.enums.PaymentMethod;
import tn.esprit.marketplace.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id, Long orderId, PaymentMethod method,
                              PaymentStatus status, BigDecimal amount, LocalDateTime paidAt) {
}