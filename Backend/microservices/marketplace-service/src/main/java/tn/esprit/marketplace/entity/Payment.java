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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.marketplace.entity.enums.PaymentMethod;
import tn.esprit.marketplace.entity.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private LocalDateTime paidAt;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", unique = true)
    private Order order;

    // Paiement simule : aucune donnee bancaire reelle n'est manipulee
    public boolean process() {
        if (status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Ce paiement a deja ete traite");
        }
        this.status = PaymentStatus.SUCCESS;
        this.paidAt = LocalDateTime.now();
        return true;
    }

    public void refund() {
        if (status != PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Seul un paiement reussi peut etre rembourse");
        }
        this.status = PaymentStatus.REFUNDED;
    }
}