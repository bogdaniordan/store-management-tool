package com.store_management.service;

import com.store_management.entity.User;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.exception.UserAlreadyExists;
import com.store_management.exception.UserDoesNotExist;
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

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        Optional<User> foundUser = userRepository.findById(user.getId());
        if (foundUser.isPresent()) {
            throw new UserAlreadyExists("User with id " + user.getId() + " already exists");
        }
        User newUser = new User();
        String password = user.getPassword();
        newUser.setPassword(new BCryptPasswordEncoder().encode(password));
        return userRepository.save(user);
    }


//    public User updateUserRole(Long id, Role role) {
//        User user = userRepository.findById(id).orElseThrow(() -> new UserAlreadyExists("User with id " + id + " does not exist"));
//        user.addRole(role);
//        return userRepository.save(user);
//    }
    public User updateUser(Long id, User user) throws ResourceNotFoundException {
        return userRepository.findById(id).map(existingUser -> {
            user.setId(id);
            return userRepository.save(user);
        }).orElseThrow(() -> new UserDoesNotExist("User with id " + id + " does not exist"));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserAlreadyExists("User with id " + id + " does not exist"));
        userRepository.deleteById(id);
    }

    //todo add more description exception messages
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Hibernate.initialize(user.getStores());
        return user;
    }
}
