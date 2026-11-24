package tn.esprit.marketplace.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import tn.esprit.marketplace.entity.enums.PaymentMethod;

import java.util.List;

public record OrderRequest(
        @NotBlank String userId,
        @NotBlank String shippingAddress,
        @NotNull PaymentMethod paymentMethod,
        @NotEmpty @Valid List<OrderLineRequest> items) {
}