package com.store_management.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AddProductToInventoryDTO {

    private Long storeId;

    private Long inventoryId;

    private Long productId;

    private int count;
}
