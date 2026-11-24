package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.Delivery;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    Optional<Delivery> findByOrderId(Long orderId);
}