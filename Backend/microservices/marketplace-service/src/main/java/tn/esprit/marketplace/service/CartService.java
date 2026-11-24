package tn.esprit.marketplace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.marketplace.dto.CartItemRequest;
import tn.esprit.marketplace.dto.CartItemResponse;
import tn.esprit.marketplace.dto.CartResponse;
import tn.esprit.marketplace.entity.Cart;
import tn.esprit.marketplace.entity.Product;
import tn.esprit.marketplace.entity.enums.CartStatus;
import tn.esprit.marketplace.exception.ResourceNotFoundException;
import tn.esprit.marketplace.repository.CartRepository;
import tn.esprit.marketplace.repository.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartResponse getCart(String userId) {
        return toResponse(getOrCreateActiveCart(userId));
    }

    public CartResponse addItem(String userId, CartItemRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Produit", request.productId()));
        if (!product.isAvailable() || request.quantity() > product.getStockQuantity()) {
            throw new IllegalStateException("Stock insuffisant pour le produit " + product.getName());
        }
        Cart cart = getOrCreateActiveCart(userId);
        cart.addItem(product, request.quantity());
        cartRepository.flush();
        return toResponse(cart);
    }

    public CartResponse removeItem(String userId, Long itemId) {
        Cart cart = getOrCreateActiveCart(userId);
        boolean exists = cart.getItems().stream().anyMatch(i -> itemId.equals(i.getId()));
        if (!exists) {
            throw new ResourceNotFoundException("Ligne de panier", itemId);
        }
        cart.removeItem(itemId);
        cartRepository.flush();
        return toResponse(cart);
    }

    private Cart getOrCreateActiveCart(String userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    return cartRepository.save(cart);
                });
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(i -> new CartItemResponse(i.getId(), i.getProduct().getId(), i.getProduct().getName(),
                        i.getProduct().getPrice(), i.getQuantity(), i.getSubtotal()))
                .toList();
        return new CartResponse(cart.getId(), cart.getUserId(), cart.getStatus(), items, cart.calculateTotal());
    }
}