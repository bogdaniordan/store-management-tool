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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest extends BaseTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void before() {
        Mockito.reset(productRepository);
        Mockito.reset(categoryRepository);
    }

    @Test
    public void test_get_product_by_id() {
        //arrange
        Mockito.when(productRepository.findById(getProduct().getId())).thenReturn(Optional.of(getProduct()));

        //act
        Product expectedProduct = productService.getProductById(getProduct().getId());

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).findById(getProduct().getId());
    }

    @Test
    public void test_get_product_by_category_id() {
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
    public void test_create_product() {
        //arrange
        Mockito.when(productRepository.save(getProduct())).thenReturn(getProduct());

        //act
        Product expectedProduct = productService.createProduct(getProduct());

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).save(getProduct());
    }

    @Test
    public void test_update_product_exists() {
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
    public void test_update_product_does_not_exist() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(false);

        //act & assert
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(getProduct().getId(), getProduct()));
        verify(productRepository, times(1)).existsById(getProduct().getId());
    }

    @Test
    public void test_delete_product_by_id() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(true);

        //act
        productService.deleteProduct(getProduct().getId());

        //assert
        verify(productRepository, times(1)).existsById(getProduct().getId());
        verify(productRepository, times(1)).deleteById(getProduct().getId());
    }

    @Test
    public void test_delete_product_does_not_exist() {
        //arrange
        Mockito.when(productRepository.existsById(getProduct().getId())).thenReturn(false);

        //act & assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(getProduct().getId()));
        verify(productRepository, times(1)).existsById(getProduct().getId());
    }

    @Test
    public void test_add_product_to_category() {
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
