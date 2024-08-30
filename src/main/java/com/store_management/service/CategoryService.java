package com.store_management.service;

import com.store_management.entity.ProductCategory;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public CategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductCategory getCategoryById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found for id " + id));
    }

    public ProductCategory createCategory(ProductCategory itemCategory) {
        return productCategoryRepository.save(itemCategory);
    }


    public ProductCategory updateCategory(Long categoryId, ProductCategory productCategory) throws ResourceNotFoundException {
        if (productCategoryRepository.existsById(categoryId)) {
            productCategory.setId(categoryId);
            return productCategoryRepository.save(productCategory);
        } else {
            throw new ResourceNotFoundException("ItemCategory not found");
        }
    }

    public void deleteCategory(Long id) throws ResourceNotFoundException {
        if (productCategoryRepository.existsById(id)) {
            productCategoryRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Product category not found");
        }
    }
}
