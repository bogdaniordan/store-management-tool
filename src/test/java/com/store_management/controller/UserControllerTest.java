package com.store_management.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store_management.BaseTest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseTest {

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
    public void givenGetUserRequest_whenFindUser_thenReturnUserOk() throws Exception {
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
    @WithMockUser(authorities = "inventory:create")
    public void givenGetUserWithoutPermission_whenFindUser_thenStatusIsForbidden() throws Exception {
        //arrange
        Mockito.when(userService.getUserById(any())).thenReturn(getUser());

        //act
        ResultActions result = mockMvc.perform(get("/api/v1/users/{id}", getUser().getId()));

        //assert
        result.andExpect(status().isForbidden())
                .andExpect(authenticated());
    }

    @Test
    @WithMockUser(authorities = "user:manage")
    public void giveCreateUserRequest_whenCreateUser_thenUserCreatedOk() throws Exception {
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
    public void givenUpdateUserRequest_whenUpdateUser_thenUserIsUpdatedOk() throws Exception {
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
    public void givenDeleteUserRequest_whenDeleteUser_thenUserIsDeletedOk() throws Exception {
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
    public void givenAddStoreToUserRequest_whenAddStoreToUser_thenStoreAddedToUserOk() throws Exception {
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
    public void givenRemovedStoreFromUserRequest_whenRemoveStoreFromUser_thenStoreRemovedFromUserOk() throws Exception {
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
}
