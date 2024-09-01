package com.store_management.repository;

import com.store_management.BaseTest;
import com.store_management.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepositoryTest extends BaseTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void find_by_product_id() {
        //arrange
        productRepository.save(getProduct());

        //act
        Optional<Product> expected = productRepository.findById(getProduct().getId());

        //assert
        assertTrue(expected.isPresent());
        assertEquals(getProduct().getName(), expected.get().getName());
    }
}
