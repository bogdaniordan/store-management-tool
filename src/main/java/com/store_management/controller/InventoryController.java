package com.store_management.controller;

import com.store_management.dto.AddProductToInventoryDTO;
import com.store_management.entity.Inventory;
import com.store_management.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<Inventory>> getInventoriesByStoreId(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoriesByStoreId(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, Inventory inventory) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventory));
    }

    @PutMapping("/add-product-to-inventory")
    public ResponseEntity<Inventory> updateInventory(@Valid @RequestBody AddProductToInventoryDTO addProductToInventoryDTO) {
        return ResponseEntity.ok(inventoryService.addProductToInventory(addProductToInventoryDTO));
    }

}
