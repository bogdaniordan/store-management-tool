package com.store_management.repository;

import com.store_management.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private final Product product = new Product(1L, "Lego", "Kids building toy", 22.0, 1, null);

    @Test
    public void find_by_product_id() {
        //arrange
        productRepository.save(product);

        //act
        Optional<Product> expected = productRepository.findById(product.getId());

        //assert
        assertTrue(expected.isPresent());
        assertEquals(product.getName(), expected.get().getName());
    }
}
