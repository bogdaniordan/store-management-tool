package com.store_management.controller;

import com.store_management.entity.Role;
import com.store_management.entity.User;
import com.store_management.repository.UserRepository;
import com.store_management.service.UserService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void test_get_user_by_id() {
        User user = new User(1L, "Cole", "Palmer", "cole.palmer@gmail.com", "123", Role.RoleType.ADMIN, new HashSet<>());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User user1 = userService.getUserById(1L);

        assertNotNull(user1);
    }
}
