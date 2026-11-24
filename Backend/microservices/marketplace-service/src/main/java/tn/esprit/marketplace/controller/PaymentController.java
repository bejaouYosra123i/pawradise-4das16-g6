package tn.esprit.marketplace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.marketplace.dto.PaymentResponse;
import tn.esprit.marketplace.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/orders/{orderId}/pay")
    public PaymentResponse pay(@PathVariable Long orderId) {
        return paymentService.pay(orderId);
    }

    @GetMapping("/orders/{orderId}")
    public PaymentResponse findByOrder(@PathVariable Long orderId) {
        return paymentService.findByOrder(orderId);
    }
}