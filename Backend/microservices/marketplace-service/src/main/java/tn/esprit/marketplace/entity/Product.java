package tn.esprit.marketplace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.marketplace.entity.enums.TargetAnimal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private TargetAnimal targetAnimal;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private int lowStockThreshold = 5;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    // Evite que deux commandes simultanees depassent le stock
    @Version
    private Long version;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.signum() <= 0) {
            throw new IllegalArgumentException("Le prix doit etre superieur a 0");
        }
        this.price = newPrice;
    }

    public boolean isAvailable() {
        return stockQuantity > 0;
    }

    public void decreaseStock(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("La quantite doit etre superieure a 0");
        }
        if (qty > stockQuantity) {
            throw new IllegalStateException("Stock insuffisant pour le produit " + name);
        }
        this.stockQuantity -= qty;
    }

    public void increaseStock(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("La quantite doit etre superieure a 0");
        }
        this.stockQuantity += qty;
    }

    public boolean isLowStock() {
        return stockQuantity <= lowStockThreshold;
    }
}
