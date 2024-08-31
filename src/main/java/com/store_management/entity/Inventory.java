package com.store_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Inventory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Product product;

    @Positive
    private Integer quantity;

    public Inventory(Store store, Product product, Integer quantity) {
        this.store = store;
        this.product = product;
        this.quantity = quantity;
    }

    public void addProducts(int quantity) {
        quantity += quantity;
    }
}
