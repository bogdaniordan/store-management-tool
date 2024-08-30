package com.store_management.entity;

import com.store_management.auth.Permission;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role implements GrantedAuthority, Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    @NonNull
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }

    @ElementCollection(targetClass = Permission.class)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name));
        return authorities;
    }

    @Getter
    @RequiredArgsConstructor
    public enum RoleType {
        ADMIN(Set.of(Permission.STORE_CREATE, Permission.STORE_UPDATE, Permission.STORE_DELETE, Permission.USER_MANAGE)),
        EMPLOYEE(Set.of(Permission.INVENTORY_CREATE, Permission.INVENTORY_UPDATE, Permission.INVENTORY_DELETE, Permission.PRODUCT_CREATE, Permission.PRODUCT_UPDATE, Permission.PRODUCT_DELETE));

        private final Set<Permission> permissions;
    }
}
