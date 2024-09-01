package com.store_management.controller;

import com.store_management.entity.User;
import com.store_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("/create")
        public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/add-store-to-user/{storeId}/{userId}")
    public ResponseEntity<User> addStoreToUser(@PathVariable Long storeId, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.addStoreToUser(storeId, userId));
    }

    @PutMapping("/remove-store-from-user/{storeId}/{userId}")
    public ResponseEntity<User> removeStoreFromUser(@PathVariable Long storeId, @PathVariable Long userId) {
        return ResponseEntity.ok(userService.removeStoreFromUser(storeId, userId));
    }
}
