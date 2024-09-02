package com.store_management.service;

import com.store_management.dto.UpdateSalaryDTO;
import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.exception.UserAlreadyExistsException;
import com.store_management.exception.UserDoesNotExistException;
import com.store_management.repository.StoreRepository;
import com.store_management.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.store_management.auth.Role.ADMIN;
import static com.store_management.auth.Role.EMPLOYEE;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final StoreRepository storeRepository;

    public UserService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s does not exist", userId)));
    }

    public User createUser(User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isPresent()) {
            throw new UserAlreadyExistsException(String.format("User with id %s already exists", user.getId()));
        }
        User newUser = new User();
        String password = user.getPassword();
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        user.setRole(EMPLOYEE);
        return userRepository.save(user);
    }

    public User updateUser(Long id, User user) throws ResourceNotFoundException {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isEmpty()) {
            throw new UserDoesNotExistException(String.format("User with id %s does not exist", id));
        }
        user.setId(id);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserDoesNotExistException(String.format("User with id %s does not exist", id));
        }
        userRepository.deleteById(id);
    }

    public User addStoreToUser(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s does not exist", userId)));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found"));

        user.addStore(store);
        return userRepository.save(user);
    }

    public User removeStoreFromUser(Long userId, Long storeId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with id %s does not exist", userId)));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Store with id %s does not exist", storeId)));

        user.removeStore(store);
        return userRepository.save(user);
    }


    public User updateEmployeeSalary(Long userId, UpdateSalaryDTO updateSalaryDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserDoesNotExistException(String.format("User with id %s does not exist", userId)));
        if (user.getRole() == ADMIN) {
           throw new IllegalArgumentException("Cannot update ADMIN salary");
        }
        user.setSalary(updateSalaryDTO.getSalary());
        return user;
    }
}
