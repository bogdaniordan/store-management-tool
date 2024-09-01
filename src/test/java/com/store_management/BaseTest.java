package com.store_management;

import com.store_management.entity.Product;
import com.store_management.entity.Store;
import com.store_management.entity.User;

import static com.store_management.auth.Role.ADMIN;

public class BaseTest {

    public Product getProduct() {
        return Product.builder()
                .id(1L)
                .name("Lego")
                .description("Kids building toy")
                .price(22.0)
                .quantity(1)
                .build();
    }

    public Store getStore() {
        return Store.builder()
                .id(2L)
                .name("Metro")
                .location("Victoriei square")
                .build();
    };

    public User getUser() {
        return User.builder()
                .id(1L)
                .firstName("Cole")
                .lastName("Palmer")
                .email("cole.palmer@gmail.com")
                .salary(2000.0)
                .password("colepalmer123")
                .role(ADMIN)
                .build();
    }
}
