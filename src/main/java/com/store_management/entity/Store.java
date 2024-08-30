package com.store_management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Store implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String location;

    @OneToMany
    private Set<Inventory> inventories = new HashSet<>();
}
