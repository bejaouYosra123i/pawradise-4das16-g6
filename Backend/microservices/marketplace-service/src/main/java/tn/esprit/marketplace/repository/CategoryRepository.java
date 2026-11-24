package tn.esprit.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.marketplace.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}