package com.store_management.controller;

import com.store_management.entity.Store;
import com.store_management.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StoreService storeService;

    @Autowired
    public AdminController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/get-store/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @PostMapping("/create-store")
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        return new ResponseEntity<>(storeService.createStore(store), HttpStatus.CREATED);
    }

    @PutMapping("/update-store/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store store) {
        return ResponseEntity.ok(storeService.updateStore(id, store));
    }
}
