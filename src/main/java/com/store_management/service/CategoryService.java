package com.store_management.service;

import com.store_management.entity.Category;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id %s does not exist", id)));
    }

    public Category createCategory(Category itemCategory) {
        return categoryRepository.save(itemCategory);
    }


    public Category updateCategory(Long categoryId, Category category) throws ResourceNotFoundException {
        if (categoryRepository.existsById(categoryId)) {
            category.setId(categoryId);
            return categoryRepository.save(category);
        }
        throw new ResourceNotFoundException(String.format("Category with id %s does not exist", categoryId));
    }

    public void deleteCategory(Long id) throws ResourceNotFoundException {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(String.format("Category with id %s does not exist", id));
        }
    }

    public Category updateCategoryName(Long id, String name) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id %s does not exist", id)));
        category.setName(name);
        return categoryRepository.save(category);
    }
}
