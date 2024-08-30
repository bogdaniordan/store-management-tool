package com.store_management.service;

import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.StoreRepository;
import com.store_management.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private final StoreRepository storeRepository;

    private final UserRepository userRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public User addStoreToUser(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Hibernate.initialize(user.getStores());

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        Hibernate.initialize(store.getInventories());

        user.addStore(store);
        return userRepository.save(user);
    }
}
