package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.marketplace.entity.Product;
import tn.esprit.marketplace.entity.enums.TargetAnimal;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySellerId(Long sellerId);
    List<Product> findByTargetAnimal(TargetAnimal targetAnimal);
    List<Product> findByNameContainingIgnoreCase(String name);

    @Query("select count(p) from Product p where p.stockQuantity <= p.lowStockThreshold")
    long countLowStock();
}