package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.DeliveryResponse;
import tn.esprit.marketplace.entity.Delivery;
import tn.esprit.marketplace.entity.Order;
import tn.esprit.marketplace.entity.enums.DeliveryStatus;
import tn.esprit.marketplace.entity.enums.OrderStatus;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.mapper.OrderMapper;
import tn.esprit.marketplace.repository.DeliveryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderMapper mapper;

    @Transactional(readOnly = true)
    public DeliveryResponse findByOrder(Long orderId) {
        return mapper.toDeliveryResponse(getByOrder(orderId));
    }

    @Transactional(readOnly = true)
    public DeliveryResponse findByTracking(String trackingNumber) {
        Delivery delivery = deliveryRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Livraison introuvable pour le numero " + trackingNumber));
        return mapper.toDeliveryResponse(delivery);
    }

    // Le statut de la livraison fait avancer le statut de la commande
    public DeliveryResponse updateStatus(Long orderId, DeliveryStatus status) {
        Delivery delivery = getByOrder(orderId);
        if (delivery.getStatus() == DeliveryStatus.DELIVERED) {
            throw new IllegalStateException("Cette livraison est deja terminee");
        }
        delivery.updateStatus(status);

        Order order = delivery.getOrder();
        if (status == DeliveryStatus.SHIPPED) {
            order.setStatus(OrderStatus.SHIPPED);
        } else if (status == DeliveryStatus.DELIVERED) {
            order.setStatus(OrderStatus.DELIVERED);
        }
        return mapper.toDeliveryResponse(delivery);
    }

    private Delivery getByOrder(Long orderId) {
        return deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Livraison de la commande", orderId));
    }
}