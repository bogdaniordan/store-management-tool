package com.store_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store_management.BaseTest;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest extends BaseTest {

    //todo revise naming conventions for tests

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void before() {
        Mockito.reset(productService);
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void get_product_by_id() throws Exception {
        //arrange & act
        Mockito.when(productService.getProductById(any())).thenReturn(getProduct());
        ResultActions result = mockMvc.perform(get("/api/v1/products/{id}", getProduct().getId()));

        //assert
        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.name").value("Lego"))
              .andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void create_product() throws Exception {
        //arrange & act
        Mockito.when(productService.createProduct(any())).thenReturn(getProduct());
        ResultActions result = mockMvc.perform(
                        post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getProduct())));

        //assert
        result.andExpect(status().isCreated())
              .andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void create_product_without_correct_permission() throws Exception {
        //arrange & act
        Mockito.when(productService.createProduct(any())).thenReturn(getProduct());
        ResultActions result = mockMvc.perform(
                post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getProduct())));

        //assert
        result.andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void update_product() throws Exception {
        //arrange & act
        Mockito.when(productService.updateProduct(any(), any())).thenReturn(getProduct());
        ResultActions result = mockMvc.perform(
                put("/api/v1/products/update/{id}", getProduct().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getProduct())));

        //assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(getProduct().getId()))
              .andExpect(jsonPath("$.name").value(getProduct().getName()))
              .andExpect(authenticated());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    public void add_category_to_product() throws Exception {
        //arrange
        Product product = getProduct();
        product.setCategory(new Category(2L, "Toys", new ArrayList<>()));

        //act
        Mockito.when(productService.addProductToCategory(any(), any())).thenReturn(product);
        ResultActions result = mockMvc.perform(
                put("/api/v1/products/add-category-to-product/{productId}/{categoryId}", product.getId(), 2L)
                        .contentType(MediaType.APPLICATION_JSON));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.category").value(product.getCategory()))
                .andExpect(authenticated());
    }
}
