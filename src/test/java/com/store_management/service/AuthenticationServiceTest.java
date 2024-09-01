package com.store_management.service;

import com.store_management.BaseTest;
import com.store_management.auth.AuthenticationService;
import com.store_management.auth.JwtService;
import com.store_management.dto.AuthenticationRequestDTO;
import com.store_management.dto.AuthenticationResponseDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.store_management.exception.UserDoesNotExistException;
import com.store_management.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTest extends BaseTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Test
    void authenticate_invalid_user() {
        //arrange
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("cole.palmer@gmail.com", "password123");

        //act & assert
        assertThrows(UserDoesNotExistException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void authenticate_valid_user() {
        //arrange
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("cole.palmer@gmail.com", "password123");
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(getUser()));
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        Mockito.when(jwtService.generateToken(getUser())).thenReturn(jwtToken);

        //act
        AuthenticationResponseDTO authResponse = authenticationService.authenticate(request);

        //assert
        verify(userRepository, times(1)).findByEmail(any());
        assertEquals(authResponse.getToken(), jwtToken);
    }
    //todo add registration test
}
