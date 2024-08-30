package com.store_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany
    private List<Product> products = new ArrayList<>();

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addProduct(Product product) {
        products.add(product);
    }
}
