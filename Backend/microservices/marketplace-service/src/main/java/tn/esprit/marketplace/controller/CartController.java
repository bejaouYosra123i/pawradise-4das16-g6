package tn.esprit.marketplace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.marketplace.dto.CartItemRequest;
import tn.esprit.marketplace.dto.CartResponse;
import tn.esprit.marketplace.dto.CheckoutRequest;
import tn.esprit.marketplace.dto.OrderResponse;
import tn.esprit.marketplace.service.CartService;
import tn.esprit.marketplace.service.OrderService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping("/{userId}")
    public CartResponse getCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}/items")
    public CartResponse addItem(@PathVariable String userId, @Valid @RequestBody CartItemRequest request) {
        return cartService.addItem(userId, request);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public CartResponse removeItem(@PathVariable String userId, @PathVariable Long itemId) {
        return cartService.removeItem(userId, itemId);
    }

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderResponse> checkout(@PathVariable String userId,
                                                  @Valid @RequestBody CheckoutRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkout(userId, request));
    }
}