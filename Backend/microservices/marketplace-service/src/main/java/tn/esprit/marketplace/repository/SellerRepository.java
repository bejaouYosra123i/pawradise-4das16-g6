package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.Seller;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByUserId(String userId);
}