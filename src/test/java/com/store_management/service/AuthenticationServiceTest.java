package com.store_management.service;

import com.store_management.BaseTest;
import com.store_management.auth.AuthenticationService;
import com.store_management.auth.JwtService;
import com.store_management.auth.Role;
import com.store_management.dto.AuthenticationRequestDTO;
import com.store_management.dto.AuthenticationResponseDTO;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.store_management.dto.RegisterRequestDTO;
import com.store_management.exception.UserAlreadyExistsException;
import com.store_management.exception.UserDoesNotExistException;
import com.store_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void before() {
        Mockito.reset(userRepository, authenticationManager, passwordEncoder, jwtService);
    }

    @Test
    void givenInvalidUser_whenAuthenticate_throwUserDoesNotExistException() {
        //arrange
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("cole.palmer@gmail.com", "password123");

        //act & assert
        assertThrows(UserDoesNotExistException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void givenValidUser_whenAuthenticate_generateTokenAndReturnResponse() {
        //arrange
        AuthenticationRequestDTO request = new AuthenticationRequestDTO("cole.palmer@gmail.com", "password123");
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(getUser()));
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        Mockito.when(jwtService.generateToken(getUser())).thenReturn(jwtToken);

        //act
        AuthenticationResponseDTO authResponse = authenticationService.authenticate(request);

        //assert
        verify(userRepository, times(1)).findByEmail(any());
        verify(jwtService, times(1)).generateToken(any());
        assertEquals(authResponse.getToken(), jwtToken);
    }


    @Test
    void givenInvalidUser_whenRegister_throwUserAlreadyExistsException() {
        //arrange
        RegisterRequestDTO request = getRegisterRequest();
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(getUser()));
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()))).thenReturn(null);

        //act & assert
        assertThrows(UserAlreadyExistsException.class, () -> authenticationService.registerUser(request));
    }

    @Test
    void givenValidUser_whenRegister_saveUserAndReturnResponse() {
        //arrange
        RegisterRequestDTO request = getRegisterRequest();
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(any())).thenReturn("9122hy39812y39y91d");
        Mockito.when(userRepository.save(any())).thenReturn(getUser());

        //act
        authenticationService.registerUser(request);

        //assert
        verify(userRepository, times(1)).findByEmail(any());
        verify(userRepository, times(1)).save(any());
    }

    private RegisterRequestDTO getRegisterRequest() {
        return RegisterRequestDTO.builder()
                .firstName("Cole")
                .lastName("Palmer")
                .email("colepalmer@gmail.com")
                .role(Role.EMPLOYEE)
                .password("password123")
                .build();
    }
}
