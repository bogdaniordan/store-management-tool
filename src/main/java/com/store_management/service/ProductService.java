package com.store_management.service;

import com.store_management.entity.Product;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.ProductCategoryRepository;
import com.store_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }


    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + id));
    }


    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    //todo add maybe constant for exception
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
}
