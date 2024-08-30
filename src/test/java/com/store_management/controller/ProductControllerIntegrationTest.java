package com.store_management.controller;

import com.store_management.entity.Product;
import com.store_management.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    //todo revise naming conventions for tests

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void test_get_product_by_id() throws Exception {
        long productId = 1L;
        Mockito.when(productService.findProductById(any())).thenReturn(new Product(productId, "Lego", "Kids building toy", 22.0, 1, null));

        ResultActions result = mockMvc.perform(get("/api/v1/products/{id}", productId));
        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
