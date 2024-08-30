package com.store_management.service;

import com.store_management.entity.Inventory;
import com.store_management.entity.Product;
import com.store_management.entity.Store;
import com.store_management.exception.InventoryAlreadyExists;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.InventoryRepository;
import com.store_management.repository.ProductRepository;
import com.store_management.repository.StoreRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find inventory"));
    }

    public List<Inventory> getInventoriesByStoreId(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find store"));
        return store.getInventories();
    }

    public Inventory updateInventory(Long id, Inventory inventory) {
        Optional<Inventory> foundInventory = inventoryRepository.findById(id);
        if (foundInventory.isPresent()) {
            throw new InventoryAlreadyExists("Inventory already exists");
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory addProductToInventory(Long storeId, Long inventoryId, Long productId, int quantity) {
        //todo refactor
        Optional<Inventory> inventory = inventoryRepository.findById(inventoryId);

        Store store = storeRepository.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        Hibernate.initialize(store.getInventories());

        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Inventory savedInventory = null;
        if (inventory.isEmpty()) {
            savedInventory = new Inventory(store, product, quantity);
        } else {
            inventory.get().addProducts(quantity);
            savedInventory = inventory.get();
        }


        return inventoryRepository.save(savedInventory);
    }
}
