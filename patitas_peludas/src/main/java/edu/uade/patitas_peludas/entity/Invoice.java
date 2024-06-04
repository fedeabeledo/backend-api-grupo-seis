package edu.uade.patitas_peludas.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@Table(name = "invoice")
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Short discount;

    @Column(name = "shipping_cost")
    private Short shippingCost;

    @JsonProperty("payment_method")
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    public Invoice(User user, Short discount, Short shippingCost, PaymentMethod paymentMethod) {
        this.user = user;
        this.discount = discount;
        this.shippingCost = shippingCost;
        this.paymentMethod = paymentMethod;
    }
}
