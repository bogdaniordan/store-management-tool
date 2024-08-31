package com.store_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store_management.entity.Category;
import com.store_management.entity.Product;
import com.store_management.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    //todo revise naming conventions for tests

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    private final Product product = new Product(1L, "Lego", "Kids building toy", 22.0, 1, null);

    @BeforeEach
    public void before() {
        Mockito.reset(productService);
    }

    @Test
    public void test_get_product_by_id() throws Exception {
        //arrange & act
        Mockito.when(productService.getProductById(any())).thenReturn(product);
        ResultActions result = mockMvc.perform(get("/api/v1/products/{id}", product.getId()));

        //assert
        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.name").value("Lego"));
    }

    @Test
    public void test_create_product() throws Exception {
        //arrange & act
        Mockito.when(productService.createProduct(any())).thenReturn(product);
        ResultActions result = mockMvc.perform(
                        post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)));

        //assert
        result.andExpect(status().isCreated());
    }

    @Test
    public void test_update_product() throws Exception {
        //arrange & act
        Mockito.when(productService.updateProduct(any(), any())).thenReturn(product);
        ResultActions result = mockMvc.perform(
                put("/api/v1/products/update/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)));

        //assert
        result.andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(product.getId()))
               .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    public void test_add_category_to_product() throws Exception {
        //arrange
        product.setCategory(new Category(2L, "Toys", null));

        //act
        Mockito.when(productService.addProductToCategory(any(), any())).thenReturn(product);
        ResultActions result = mockMvc.perform(
                put("/api/v1/products/add-category-to-product/{productId}/{categoryId}", product.getId(), 2L)
                        .contentType(MediaType.APPLICATION_JSON));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.category").value(product.getCategory()));
    }
}
