package com.store_management.service;

import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.StoreRepository;
import com.store_management.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find store with id " + id));
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
