package tn.esprit.marketplace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.marketplace.entity.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// "order" est un mot reserve en SQL : la table s'appelle "orders"
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private String shippingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Delivery delivery;

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

    // Copie les infos du produit au moment de la commande (prix, nom, vendeur)
    public void addItem(Product product, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("La quantite doit etre superieure a 0");
        }
        OrderItem item = new OrderItem();
        item.setOrder(this);
        item.setProduct(product);
        item.setProductName(product.getName());
        item.setSellerId(product.getSeller().getId());
        item.setUnitPrice(product.getPrice());
        item.setQuantity(qty);
        items.add(item);
        calculateTotal();
    }

    public BigDecimal calculateTotal() {
        this.totalAmount = items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.totalAmount;
    }

    public void confirmPayment() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Seule une commande en attente peut etre payee");
        }
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED
                || status == OrderStatus.CANCELLED || status == OrderStatus.REFUNDED) {
            throw new IllegalStateException("Cette commande ne peut plus etre annulee");
        }
        this.status = OrderStatus.CANCELLED;
    }
}