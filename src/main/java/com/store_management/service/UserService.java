package com.store_management.service;

import com.store_management.entity.Role;
import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.exception.UserAlreadyExistsException;
import com.store_management.exception.UserDoesNotExistException;
import com.store_management.repository.StoreRepository;
import com.store_management.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Hibernate.initialize(user.getStores());
        return user;
    }

    public User createUser(User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException("User with id " + user.getId() + " already exists");
        }
        User newUser = new User();
        String password = user.getPassword();
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(Role.RoleType.EMPLOYEE);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) throws ResourceNotFoundException {
        return userRepository.findById(id).map(existingUser -> {
            user.setId(id);
            return userRepository.save(user);
        }).orElseThrow(() -> new UserDoesNotExistException("User with id " + id + " does not exist"));
    }

    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with id " + id + " does not exist");
        }
        userRepository.deleteById(id);
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

    public User removeStoreFromUser(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Hibernate.initialize(user.getStores());

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));
        Hibernate.initialize(store.getInventories());

        user.removeStore(store);
        return userRepository.save(user);
    }
}
