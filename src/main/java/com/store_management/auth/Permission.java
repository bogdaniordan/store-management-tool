package com.store_management.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    STORE_MANAGE("store:manage"),
    USER_MANAGE("user:manage"),
    CATEGORY_MANAGE("category:manage"),
    INVENTORY_CREATE("inventory:create"),
    INVENTORY_UPDATE("inventory:update"),
    INVENTORY_DELETE("inventory:delete"),
    PRODUCT_CREATE("product:create"),
    PRODUCT_UPDATE("product:update"),
    PRODUCT_DELETE("product:delete");

    private final String permission;
}
