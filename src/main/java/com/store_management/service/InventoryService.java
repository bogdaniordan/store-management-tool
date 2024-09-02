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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, StoreRepository storeRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    public List<Inventory> getInventoriesByStoreId(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Store with id %s does not exist", id))).getInventories();
    }

    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Inventory with id %s does not exist", id)));
    }

    public Inventory updateInventory(Long id, Inventory inventory) {
        Optional<Inventory> foundInventory = inventoryRepository.findById(id);
        if (foundInventory.isPresent()) {
            throw new InventoryAlreadyExistsException(String.format("Inventory with id %s already exists", id));
        }
        return inventoryRepository.save(inventory);
    }


    public Inventory addProductToInventory(AddProductToInventoryDTO addProductToInventoryDTO) {
        Optional<Inventory> inventory = inventoryRepository.findById(addProductToInventoryDTO.getInventoryId());

        Store store = storeRepository.findById(addProductToInventoryDTO.getStoreId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Inventory with id %s does not exist", addProductToInventoryDTO.getStoreId())));

        Product product = productRepository.findById(addProductToInventoryDTO.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Product with id %s does not exist", addProductToInventoryDTO.getProductId())));

        Inventory savedInventory;
        if (inventory.isEmpty()) {
            savedInventory = new Inventory(store, product, addProductToInventoryDTO.getCount());
        } else {
            inventory.get().addProducts(addProductToInventoryDTO.getCount());
            savedInventory = inventory.get();
            logger.info("Product has been added to inventory with id {}", inventory.get().getId());
        }


        return inventoryRepository.save(savedInventory);
    }
}
