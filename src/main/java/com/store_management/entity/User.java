package com.store_management.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store_management.auth.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIgnoreProperties(value = {"authorities"}, allowGetters = true)
@Table(name = "_users")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min=3, message="Name should have at least 3 characters")
    private String firstName;

    private String lastName;

    @Column(unique = true, nullable = false)
    @Size(min=7, message="Email should have at least 7 characters")
    private String email;

    @Positive
    private Double salary;

    @NonNull
    @Size(min = 10)
    private String password;

    @Enumerated(EnumType.STRING)
    @NonNull
    private Role role;

    @ManyToMany
    private Set<Store> stores = new HashSet<>();

    public User(String firstName, String lastName, String email, String password, Double salary, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.role = role;
        this.salary = salary;
    }

    public void addStore(Store store) {
        stores.add(store);
    }

    public void removeStore(Store store) {
        stores.remove(store);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
