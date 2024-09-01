package com.store_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store_management.entity.Store;
import com.store_management.entity.User;
import com.store_management.service.UserService;
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

import static com.store_management.auth.Role.ADMIN;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void before() {
        Mockito.reset(userService);
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void test_get_user_by_id() throws Exception {
        //arrange
        Mockito.when(userService.getUserById(any())).thenReturn(getUser());

        //act
        ResultActions result = mockMvc.perform(get("/api/v1/users/{id}", getUser().getId()));

        //assert
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Cole"))
                .andExpect(jsonPath("$.email").value("cole.palmer@gmail.com"))
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void test_create_user() throws Exception {
        //arrange
        Mockito.when(userService.createUser(any())).thenReturn(getUser());

        //act
        ResultActions result = mockMvc.perform(post("/api/v1/users/create")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(getUser())));

        //assert
        result.andExpect(status().isCreated())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void test_update_user() throws Exception {
        //arrange
        Mockito.when(userService.updateUser(any(), any())).thenReturn(getUser());

        //act
        ResultActions result = mockMvc.perform(
                put("/api/v1/users/update/{id}", getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getUser())));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Cole"))
                .andExpect(jsonPath("$.email").value("cole.palmer@gmail.com"))
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void test_delete_user_by_id() throws Exception {
        // act
        ResultActions result = mockMvc.perform(
                delete("/api/v1/users/delete/{id}", getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON));

        // assert
        result.andExpect(status().isOk())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void add_store_to_user() throws Exception {
        //arrange
        Mockito.when(userService.addStoreToUser(any(), any())).thenReturn(getUser());

        //act
        ResultActions result = mockMvc.perform(
                put("/api/v1/users/add-store-to-user/{storeId}/{userId}", getStore().getId(), getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON));

        //assert
        result.andExpect(status().isOk())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void remove_store_from_user() throws Exception {
        //arrange
        Mockito.when(userService.removeStoreFromUser(any(), any())).thenReturn(getUser());

        //act
        ResultActions result = mockMvc.perform(
                put("/api/v1/users/remove-store-from-user/{storeId}/{userId}", getStore().getId(), getUser().getId())
                        .contentType(MediaType.APPLICATION_JSON));

        //assert
        result.andExpect(status().isOk())
                .andExpect(authenticated());
    }

    private Store getStore() {
        return Store.builder()
                .id(2L)
                .name("Metro")
                .location("Victoriei square")
                .build();
    };

    private User getUser() {
        return User.builder()
                .id(1L)
                .firstName("Cole")
                .lastName("Palmer")
                .email("cole.palmer@gmail.com")
                .salary(2000.0)
                .password("colepalmer123")
                .role(ADMIN)
                .build();
    }
}
