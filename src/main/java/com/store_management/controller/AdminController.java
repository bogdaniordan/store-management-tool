package com.store_management.controller;

import com.store_management.dto.UpdateSalaryDTO;
import com.store_management.entity.Category;
import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.service.CategoryService;
import com.store_management.service.StoreService;
import com.store_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final StoreService storeService;

    private final UserService userService;

    private final CategoryService categoryService;

    @Autowired
    public AdminController(StoreService storeService, UserService userService, CategoryService categoryService) {
        this.storeService = storeService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/get-store/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @PostMapping("/create-store")
    public ResponseEntity<Store> createStore(@RequestBody Store store) {
        return new ResponseEntity<>(storeService.createStore(store), HttpStatus.CREATED);
    }

    @PutMapping("/update-store/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody Store store) {
        return ResponseEntity.ok(storeService.updateStore(id, store));
    }

    @PutMapping("/update-employee-salary/{id}")
    public ResponseEntity<User> updateUserSalary(@PathVariable Long id, @RequestBody UpdateSalaryDTO updateSalaryDTO) {
        return ResponseEntity.ok(userService.updateEmployeeSalary(id, updateSalaryDTO));
    }

    @PutMapping("/update-category-name/{id}/{categoryName}")
    public ResponseEntity<Category> updateCategoryName(@PathVariable Long id, @PathVariable String categoryName) {
        return ResponseEntity.ok(categoryService.updateCategoryName(id, categoryName));
    }
}
