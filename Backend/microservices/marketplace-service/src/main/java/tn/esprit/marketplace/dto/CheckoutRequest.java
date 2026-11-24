package tn.esprit.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.marketplace.entity.enums.PaymentMethod;

public record CheckoutRequest(@NotBlank String shippingAddress, @NotNull PaymentMethod paymentMethod) {
}