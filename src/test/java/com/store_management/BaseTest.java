package com.store_management;

import com.store_management.entity.Product;
import com.store_management.entity.Store;
import com.store_management.entity.User;

import java.util.HashSet;

import static com.store_management.auth.Role.ADMIN;

public class BaseTest {

    private User user;

    private Product product;

    private Store store;

    public Product getProduct() {
        if (product == null) {
            product = Product.builder()
                    .id(1L)
                    .name("Lego")
                    .description("Kids building toy")
                    .price(22.0)
                    .quantity(1)
                    .build();
        }
        return product;
    }

    public Store getStore() {
        if (store == null) {
            store = Store.builder()
                    .id(1L)
                    .name("Metro")
                    .location("Victoriei square")
                    .build();
        }
        return store;
    };

    public User getUser() {
        if (user == null) {
            user = User.builder()
                    .id(1L)
                    .firstName("Cole")
                    .lastName("Palmer")
                    .email("cole.palmer@gmail.com")
                    .salary(2000.0)
                    .password("colepalmer123")
                    .role(ADMIN)
                    .stores(new HashSet<>())
                    .build();
        }
        return user;
    }
}
