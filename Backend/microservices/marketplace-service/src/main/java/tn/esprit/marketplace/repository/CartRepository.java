package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.Cart;
import tn.esprit.marketplace.entity.enums.CartStatus;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserIdAndStatus(String userId, CartStatus status);
}