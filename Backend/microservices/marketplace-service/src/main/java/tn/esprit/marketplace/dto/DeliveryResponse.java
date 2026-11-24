package tn.esprit.marketplace.dto;

import tn.esprit.marketplace.entity.enums.DeliveryStatus;

public record DeliveryResponse(Long id, Long orderId, DeliveryStatus status,
                               String trackingNumber, String address) {
}