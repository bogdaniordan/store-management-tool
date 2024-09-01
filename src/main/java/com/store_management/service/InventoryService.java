package com.store_management.service;

import com.store_management.dto.AddProductToInventoryDTO;
import com.store_management.entity.Inventory;
import com.store_management.entity.Product;
import com.store_management.entity.Store;
import com.store_management.exception.InventoryAlreadyExistsException;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.InventoryRepository;
import com.store_management.repository.ProductRepository;
import com.store_management.repository.StoreRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public List<Inventory> getInventoriesByStoreId(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find store")).getInventories();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find inventory"));
    }

    public Inventory updateInventory(Long id, Inventory inventory) {
        Optional<Inventory> foundInventory = inventoryRepository.findById(id);
        if (foundInventory.isPresent()) {
            throw new InventoryAlreadyExistsException("Inventory already exists");
        }
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory addProductToInventory(AddProductToInventoryDTO addProductToInventoryDTO) {
        //todo refactor
        Optional<Inventory> inventory = inventoryRepository.findById(addProductToInventoryDTO.getInventoryId());

        Store store = storeRepository.findById(addProductToInventoryDTO.getStoreId()).orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        Hibernate.initialize(store.getInventories());

        Product product = productRepository.findById(addProductToInventoryDTO.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Inventory savedInventory;
        if (inventory.isEmpty()) {
            savedInventory = new Inventory(store, product, addProductToInventoryDTO.getCount());
        } else {
            inventory.get().addProducts(addProductToInventoryDTO.getCount());
            savedInventory = inventory.get();
        }


        return inventoryRepository.save(savedInventory);
    }
}
