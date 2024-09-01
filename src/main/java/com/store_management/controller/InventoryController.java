package com.store_management.controller;

import com.store_management.dto.AddProductToInventoryDTO;
import com.store_management.entity.Inventory;
import com.store_management.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventories")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
public class InventoryController {

    private final InventoryService inventoryService;

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
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long id, @RequestBody Inventory inventory) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventory));
    }

    @PutMapping("/add-product-to-inventory")
    public ResponseEntity<Inventory> addProductToInventory(@Valid @RequestBody AddProductToInventoryDTO addProductToInventoryDTO) {
        return ResponseEntity.ok(inventoryService.addProductToInventory(addProductToInventoryDTO));
    }

}
