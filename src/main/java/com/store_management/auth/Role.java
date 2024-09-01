package com.store_management.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN(Set.of(Permission.ROLE_MANAGE, Permission.STORE_MANAGE, Permission.USER_MANAGE)),
    EMPLOYEE(Set.of(Permission.INVENTORY_CREATE, Permission.INVENTORY_UPDATE, Permission.INVENTORY_DELETE, Permission.PRODUCT_CREATE, Permission.PRODUCT_UPDATE, Permission.PRODUCT_DELETE));

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
