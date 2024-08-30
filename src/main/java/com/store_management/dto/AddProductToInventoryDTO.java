package com.store_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductToInventoryDTO {

    private Long storeId;

    private Long inventoryId;

    private Long productId;

    private int count;
}
