package com.store_management.service;

import com.store_management.entity.Store;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.StoreRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public Store getStoreById(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find store with id " + id));
        Hibernate.initialize(store.getInventories());
        return store;
    }

    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    public Store updateStore(Long id, Store updatedStore) {
        Optional<Store> store = storeRepository.findById(id);
        if(store.isEmpty()) {
            throw new ResourceNotFoundException("Could not find store with id " + id);
        }
        updatedStore.setId(id);
        return storeRepository.save(updatedStore);
    }
}
