package tn.esprit.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.marketplace.dto.DeliveryResponse;
import tn.esprit.marketplace.dto.DeliveryStatusRequest;
import tn.esprit.marketplace.service.DeliveryService;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/tracking/{trackingNumber}")
    public DeliveryResponse findByTracking(@PathVariable String trackingNumber) {
        return deliveryService.findByTracking(trackingNumber);
    }

    @GetMapping("/orders/{orderId}")
    public DeliveryResponse findByOrder(@PathVariable Long orderId) {
        return deliveryService.findByOrder(orderId);
    }

    @PutMapping("/orders/{orderId}/status")
    public DeliveryResponse updateStatus(@PathVariable Long orderId,
                                         @Valid @RequestBody DeliveryStatusRequest request) {
        return deliveryService.updateStatus(orderId, request.status());
    }
}