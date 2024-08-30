package com.store_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Inventory implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Product product;

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
