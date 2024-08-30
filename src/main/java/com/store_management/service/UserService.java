package com.store_management.service;

import com.store_management.entity.Role;
import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.exception.UserAlreadyExists;
import com.store_management.repository.StoreRepository;
import com.store_management.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    //todo add loggers and @valid to payload

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    @Autowired
    public UserService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public User registerUser(User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isPresent()) {
            throw new UserAlreadyExists("User with id " + user.getId() + " already exists");
        }
        User newUser = new User();
        String password = user.getPassword();
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        //todo role addition
        return userRepository.save(user);
    }


    public User updateUserRole(Long id, Role role) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserAlreadyExists("User with id " + id + " does not exist"));
        user.addRole(role);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserAlreadyExists("User with id " + id + " does not exist"));
        userRepository.deleteById(id);
    }

    //todo add more descriptiove exception messages
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Hibernate.initialize(user.getStores());
        return user;
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
