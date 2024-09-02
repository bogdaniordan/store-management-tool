package com.store_management.service;

import com.store_management.BaseTest;
import com.store_management.entity.User;
import com.store_management.exception.UserAlreadyExistsException;
import com.store_management.repository.StoreRepository;
import com.store_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class UserServiceTest extends BaseTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StoreRepository storeRepository;

    @BeforeEach
    public void before() {
        Mockito.reset(userRepository);
        Mockito.reset(storeRepository);
    }

    @Test
    public void givenValidUserId_whenFindUser_thenReturnUser() {
        //arrange
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));

        //act
        User expectedUser = userService.getUserById(getProduct().getId());

        //assert
        assertNotNull(expectedUser);
        verify(userRepository, times(1)).findById(getUser().getId());
    }

    @Test
    public void givenValidUser_whenCreateUser_thenUserCreated() {
        //arrange
        Mockito.when(userRepository.save(any())).thenReturn(getUser());
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.empty());

        //act
        User expectedUser = userService.createUser(getUser());

        //assert
        assertNotNull(expectedUser);
        verify(userRepository, times(1)).save(getUser());
    }

    @Test
    public void givenInvalidUser_whenCreateUser_thenThrowUserAlreadyExistsException() {
        //arrange
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));

        //act & assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(getUser()));
        verify(userRepository, times(1)).findById(getUser().getId());
    }

    @Test
    public void givenValidUser_whenUpdateUser_thenUserUpdated() {
        //arrange
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        Mockito.when(userRepository.save(any())).thenReturn(getUser());

        //act
        User expected = userService.updateUser(getUser().getId(), getUser());

        //assert
        assertNotNull(expected);
        verify(userRepository, times(1)).findById(getUser().getId());
        verify(userRepository, times(1)).save(getUser());
    }

    @Test
    public void givenValidUserId_whenDeleteUser_thenUserDeleted() {
        //arrange
        Mockito.when(userRepository.existsById(any())).thenReturn(true);

        //act
        userService.deleteUser(getUser().getId());

        //assert
        verify(userRepository, times(1)).existsById(getUser().getId());
        verify(userRepository, times(1)).deleteById(getUser().getId());
    }

    @Test
    public void givenUserAndStore_whenAddStoreToUser_thenStoreAddedToUser() {
        //arrange
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        Mockito.when(storeRepository.findById(any())).thenReturn(Optional.of(getStore()));
        Mockito.when(userRepository.save(any())).thenReturn(getUser());

        //act
        User expected = userService.addStoreToUser(getUser().getId(), getStore().getId());

        //assert
        assertNotNull(expected);
        assertEquals(expected.getStores().size(), 1);
        verify(userRepository, times(1)).save(getUser());
    }

    @Test
    public void givenUserAndStore_whenStoreAddToUser_thenStoreAddedToUserr() {
        //arrange
        User user = getUser();
        user.addStore(getStore());
        
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
        Mockito.when(storeRepository.findById(any())).thenReturn(Optional.of(getStore()));
        Mockito.when(userRepository.save(any())).thenReturn(user);

        //act
        User expected = userService.removeStoreFromUser(getUser().getId(), getStore().getId());

        //assert
        assertNotNull(expected);
        assertEquals(expected.getStores().size(), 0);
        verify(userRepository, times(1)).save(getUser());
    }
}
