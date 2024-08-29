package com.store_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private PaymentType paymentType;

    @OneToMany
    private List<Product> purchasedProducts = new ArrayList<>();

    public Order(Long id, PaymentType paymentType, List<Product> purchasedProducts) {
        this.id = id;
        this.paymentType = paymentType;
        this.purchasedProducts = purchasedProducts;
    }
}
