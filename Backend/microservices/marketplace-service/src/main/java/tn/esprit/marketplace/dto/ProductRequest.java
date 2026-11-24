package tn.esprit.marketplace.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.marketplace.entity.enums.TargetAnimal;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        String imageUrl,
        TargetAnimal targetAnimal,
        @Min(0) int stockQuantity,
        @Min(0) int lowStockThreshold,
        @NotNull Long sellerId,
        @NotNull Long categoryId) {
}