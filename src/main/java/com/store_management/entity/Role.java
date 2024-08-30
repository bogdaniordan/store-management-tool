package com.store_management.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return null;
    }
}
