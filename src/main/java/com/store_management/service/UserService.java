package com.store_management.service;

import com.store_management.entity.Role;
import com.store_management.entity.User;
import com.store_management.exception.UserAlreadyExists;
import com.store_management.repository.UserRepository;
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

    public void removeUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserAlreadyExists("User with id " + id + " does not exist"));
        userRepository.deleteById(id);
    }
}
