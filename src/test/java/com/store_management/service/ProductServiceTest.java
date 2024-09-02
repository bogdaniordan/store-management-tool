package com.store_management.service;

import com.store_management.BaseTest;
import com.store_management.entity.Category;
import com.store_management.entity.Product;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.CategoryRepository;
import com.store_management.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest extends BaseTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void before() {
        Mockito.reset(productRepository);
        Mockito.reset(categoryRepository);
    }

    @Test
    public void givenProductId_whenFindProduct_thenReturnProduct() {
        //arrange
        Mockito.when(productRepository.findById(getProduct().getId())).thenReturn(Optional.of(getProduct()));

        //act
        Product expectedProduct = productService.getProductById(getProduct().getId());

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).findById(getProduct().getId());
    }

    @Test
    public void givenCategoryId_whenFindProductsByCategory_thenReturnProducts() {
        //arrange
        Category category = new Category(2L, "Toys", List.of(getProduct()));

        //act
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        List<Product> expectedProducts = productService.getProductsByCategoryId(category.getId());

        //assert
        assertNotNull(expectedProducts);
        assertEquals(1, expectedProducts.size());
        assertEquals(getProduct().getName(), expectedProducts.get(0).getName());
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    public void givenProduct_whenCreateProduct_thenProductPersisted() {
        //arrange
        Mockito.when(productRepository.save(getProduct())).thenReturn(getProduct());

        //act
        Product expectedProduct = productService.createProduct(getProduct());

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).save(getProduct());
    }

    @Test
    public void givenExistingProduct_whenUpdateProduct_thenProductUpdated() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(true);
        Mockito.when(productRepository.save(getProduct())).thenReturn(getProduct());

        //act
        Product expectedProduct = productService.updateProduct(getProduct().getId(), getProduct());

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).existsById(getUser().getId());
        verify(productRepository, times(1)).save(getProduct());
    }

    @Test
    public void givenProductNotExists_whenUpdateProduct_thenThrowResourceNotFoundException() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(false);

        //act & assert
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(getProduct().getId(), getProduct()));
        verify(productRepository, times(1)).existsById(getProduct().getId());
    }

    @Test
    public void givenValidProductId_whenDeleteProduct_thenProductDeleted() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(true);

        //act
        productService.deleteProduct(getProduct().getId());

        //assert
        verify(productRepository, times(1)).existsById(getProduct().getId());
        verify(productRepository, times(1)).deleteById(getProduct().getId());
    }

    @Test
    public void givenInvalidProductId_whenDeleteProduct_thenThrowResourceNotFoundException() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(false);

        //act & assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(getProduct().getId()));
        verify(productRepository, times(1)).existsById(getProduct().getId());
    }

    @Test
    public void givenProductAndCategory_whenAddProductToCategory_thenProductAddedToCategory() {
        //arrange
        Category category = new Category(2L, "Toys", new ArrayList<>());
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findById(getProduct().getId())).thenReturn(Optional.of(getProduct()));

        //act
        productService.addProductToCategory(getProduct().getId(), category.getId());

        //assert
        assertEquals(category.getProducts().size(), 1);
        verify(productRepository, times(1)).save(getProduct());
        verify(categoryRepository, times(1)).save(category);
    }
}
