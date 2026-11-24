package tn.esprit.marketplace.dto;

import jakarta.validation.constraints.NotNull;
import tn.esprit.marketplace.entity.enums.DeliveryStatus;

public record DeliveryStatusRequest(@NotNull DeliveryStatus status) {
}