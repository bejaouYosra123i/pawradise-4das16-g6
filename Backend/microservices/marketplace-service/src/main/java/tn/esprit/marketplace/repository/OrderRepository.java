package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(String userId);
    List<Order> findDistinctByItemsSellerId(Long sellerId);
}