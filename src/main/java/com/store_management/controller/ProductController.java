package com.store_management.controller;

import com.store_management.entity.Product;
import com.store_management.service.CategoryService;
import com.store_management.service.ProductService;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    //todo add DTOs

    private final ProductService productService;

    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @GetMapping("/category/{id}")
    public List<Product> getProductsByCategoryId(@PathVariable Long id) {
        return productService.findProductsByCategory(id);
    }

    @PostMapping("/create")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PostMapping("/add-category-to-product/{productId}/{categoryId}")
    public Product addCategoryToProduct(@PathVariable Long productId, @PathVariable Long categoryId) {
        return productService.addProductToCategory(productId, categoryId);
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }
}
