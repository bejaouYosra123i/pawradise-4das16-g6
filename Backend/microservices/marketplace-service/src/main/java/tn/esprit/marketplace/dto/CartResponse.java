package tn.esprit.marketplace.dto;

import tn.esprit.marketplace.entity.enums.CartStatus;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(Long id, String userId, CartStatus status,
                           List<CartItemResponse> items, BigDecimal total) {
}