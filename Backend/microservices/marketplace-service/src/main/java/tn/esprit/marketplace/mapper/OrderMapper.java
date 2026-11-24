package tn.esprit.marketplace.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.marketplace.dto.DeliveryResponse;
import tn.esprit.marketplace.dto.OrderItemResponse;
import tn.esprit.marketplace.dto.OrderResponse;
import tn.esprit.marketplace.dto.PaymentResponse;
import tn.esprit.marketplace.entity.Delivery;
import tn.esprit.marketplace.entity.Order;
import tn.esprit.marketplace.entity.Payment;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderItemResponse(i.getProduct().getId(), i.getProductName(), i.getSellerId(),
                        i.getUnitPrice(), i.getQuantity(), i.getSubtotal()))
                .toList();
        return new OrderResponse(
                o.getId(), o.getUserId(), o.getStatus(), o.getTotalAmount(), o.getShippingAddress(),
                o.getCreatedAt(), items,
                o.getPayment() == null ? null : o.getPayment().getStatus(),
                o.getDelivery() == null ? null : o.getDelivery().getTrackingNumber());
    }

    public PaymentResponse toPaymentResponse(Payment p) {
        return new PaymentResponse(p.getId(), p.getOrder().getId(), p.getMethod(),
                p.getStatus(), p.getAmount(), p.getPaidAt());
    }

    public DeliveryResponse toDeliveryResponse(Delivery d) {
        return new DeliveryResponse(d.getId(), d.getOrder().getId(), d.getStatus(),
                d.getTrackingNumber(), d.getAddress());
    }
}