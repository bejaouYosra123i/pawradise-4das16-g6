package tn.esprit.marketplace.dto;

import tn.esprit.marketplace.entity.enums.TargetAnimal;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        TargetAnimal targetAnimal,
        int stockQuantity,
        boolean available,
        boolean lowStock,
        Long sellerId,
        String shopName,
        Long categoryId,
        String categoryName) {
}