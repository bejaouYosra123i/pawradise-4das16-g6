package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.PaymentResponse;
import tn.esprit.marketplace.entity.Delivery;
import tn.esprit.marketplace.entity.Order;
import tn.esprit.marketplace.entity.Payment;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.mapper.OrderMapper;
import tn.esprit.marketplace.repository.OrderRepository;
import tn.esprit.marketplace.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper mapper;

    // Paiement simule : valide le paiement, passe la commande en PAID et cree la livraison
    public PaymentResponse pay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Commande", orderId));
        Payment payment = order.getPayment();
        if (payment == null) {
            throw new IllegalStateException("Aucun paiement associe a cette commande");
        }
        payment.process();
        order.confirmPayment();

        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setAddress(order.getShippingAddress());
        delivery.generateTrackingNumber();
        order.setDelivery(delivery);

        orderRepository.flush();
        return mapper.toPaymentResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse findByOrder(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement de la commande", orderId));
        return mapper.toPaymentResponse(payment);
    }
}