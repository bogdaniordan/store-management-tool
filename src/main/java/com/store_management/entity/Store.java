package com.store_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 15)
    private String name;

    @Column(nullable = false)
    @Size(min = 5, max = 15)
    private String location;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Inventory> inventories = new ArrayList<>();
}
