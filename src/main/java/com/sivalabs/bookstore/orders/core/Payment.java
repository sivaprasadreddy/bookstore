package com.sivalabs.bookstore.orders.core;

import com.sivalabs.bookstore.common.model.BaseEntity;
import com.sivalabs.bookstore.orders.core.models.PaymentStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import org.jspecify.annotations.NullUnmarked;

@Entity
@Table(name = "payments")
@NullUnmarked
class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_generator")
    @SequenceGenerator(name = "payment_id_generator", sequenceName = "payment_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "payment_reference", nullable = false, unique = true)
    private String paymentReference;

    @Column(name = "amount", nullable = false)
    private long amount;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    protected Payment() {}

    private Payment(Order order, long amount, String currency) {
        this.order = order;
        this.paymentReference = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.status = PaymentStatus.INITIATED;
    }

    static Payment initiated(Order order, long amount, String currency) {
        return new Payment(order, amount, currency);
    }

    void completeSuccessfully() {
        this.status = PaymentStatus.SUCCESS;
        this.completedAt = LocalDateTime.now();
    }

    void completeWithFailure(String description) {
        this.status = PaymentStatus.FAILED;
        this.errorDescription = description;
        this.completedAt = LocalDateTime.now();
    }

    PaymentStatus getStatus() {
        return status;
    }

    String getErrorDescription() {
        return errorDescription;
    }
}
