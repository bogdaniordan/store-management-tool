package com.store_management.service;

import com.store_management.entity.Category;
import com.store_management.entity.Product;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.CategoryRepository;
import com.store_management.repository.ProductRepository;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
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
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final Product product = new Product(1L, "Lego", "Kids building toy", 22.0, 1, null);


    @BeforeEach
    public void before() {
        Mockito.reset(productRepository);
        Mockito.reset(categoryRepository);
    }

    @Test
    public void test_get_product_by_id() {
        //arrange
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        //act
        Product expectedProduct = productService.getProductById(product.getId());

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).findById(product.getId());
    }

    @Test
    public void test_get_product_by_category_id() {
        //arrange
        Category category = new Category(2L, "Toys", List.of(product));

        //act
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        List<Product> expectedProducts = productService.getProductsByCategoryId(category.getId());

        //assert
        assertNotNull(expectedProducts);
        assertEquals(1, expectedProducts.size());
        assertEquals(product.getName(), expectedProducts.get(0).getName());
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    public void test_create_product() {
        //arrange
        Mockito.when(productRepository.save(product)).thenReturn(product);

        //act
        Product expectedProduct = productService.createProduct(product);

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void test_update_product_exists() {
        //arrange
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);
        Mockito.when(productRepository.save(product)).thenReturn(product);

        //act
        Product expectedProduct = productService.updateProduct(product.getId(), product);

        //assert
        assertNotNull(expectedProduct);
        verify(productRepository, times(1)).existsById(product.getId());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void test_update_product_does_not_exist() {
        //arrange
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(false);

        //act & assert
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(product.getId(), product));
        verify(productRepository, times(1)).existsById(product.getId());
    }

    @Test
    public void test_delete_product_by_id() {
        //arrange
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(true);

        //act
        productService.deleteProduct(product.getId());

        //assert
        verify(productRepository, times(1)).existsById(product.getId());
        verify(productRepository, times(1)).deleteById(product.getId());
    }

    @Test
    public void test_delete_product_does_not_exist() {
        //arrange
        Mockito.when(productRepository.existsById(product.getId())).thenReturn(false);

        //act & assert
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(product.getId()));
        verify(productRepository, times(1)).existsById(product.getId());
    }

    @Test
    public void test_add_product_to_category() {
        //arrange
        Category category = new Category(2L, "Toys", new ArrayList<>());
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        //act
        productService.addProductToCategory(product.getId(), category.getId());

        //assert
        assertEquals(category.getProducts().size(), 1);
        verify(productRepository, times(1)).save(product);
        verify(categoryRepository, times(1)).save(category);
    }
}
