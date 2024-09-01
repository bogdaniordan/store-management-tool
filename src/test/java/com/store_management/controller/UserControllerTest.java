package com.store_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashSet;

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

    private final User user = new User(1L, "Cole", "Palmer",
            "cole.palmer@gmail.com", "colepalmer123", ADMIN, new HashSet<>());

    @BeforeEach
    public void before() {
        Mockito.reset(userService);
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void test_get_user_by_id() throws Exception {
        //arrange & act
        Mockito.when(userService.getUserById(any())).thenReturn(user);
        ResultActions result = mockMvc.perform(get("/api/v1/users/{id}", user.getId()));

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
        //arrange & act
        Mockito.when(userService.createUser(any())).thenReturn(user);
        ResultActions result = mockMvc.perform(post("/api/v1/users/create")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(user)));

        //assert
        result.andExpect(status().isCreated())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void test_update_user() throws Exception {
        //arrange & act
        Mockito.when(userService.updateUser(any(), any())).thenReturn(user);
        ResultActions result = mockMvc.perform(
                put("/api/v1/users/update/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)));

        //assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Cole"))
                .andExpect(jsonPath("$.email").value("cole.palmer@gmail.com"))
                .andExpect(authenticated());
    }
}
