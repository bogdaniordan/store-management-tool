package com.store_management.service;

import com.store_management.entity.Category;
import com.store_management.entity.Product;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.CategoryRepository;
import com.store_management.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + id));
    }

    public List<Product> getProductsByCategoryId(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find category")).getProducts();
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Could not find product with id " + id);
        }
        product.setId(id);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Could not find product with id " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public Product addProductToCategory(Long productId, Long categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + productId));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Could not find category with id " + categoryId));
        category.addProduct(product);
        categoryRepository.save(category);
        return productRepository.save(product);
    }
}
