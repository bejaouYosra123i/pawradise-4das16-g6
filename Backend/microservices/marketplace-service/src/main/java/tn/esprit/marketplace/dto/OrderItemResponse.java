package tn.esprit.marketplace.dto;

import java.math.BigDecimal;

public record OrderItemResponse(Long productId, String productName, Long sellerId,
                                BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
}