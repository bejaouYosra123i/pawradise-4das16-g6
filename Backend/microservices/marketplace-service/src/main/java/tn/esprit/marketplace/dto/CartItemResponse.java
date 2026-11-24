package tn.esprit.marketplace.dto;

import java.math.BigDecimal;

public record CartItemResponse(Long id, Long productId, String productName,
                               BigDecimal unitPrice, int quantity, BigDecimal subtotal) {
}