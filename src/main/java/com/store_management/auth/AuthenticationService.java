package com.store_management.auth;

import com.store_management.dto.AuthenticationRequestDTO;
import com.store_management.dto.AuthenticationResponseDTO;
import com.store_management.dto.RegisterRequestDTO;
import com.store_management.entity.User;
import com.store_management.exception.UserAlreadyExistsException;
import com.store_management.exception.UserDoesNotExistException;
import com.store_management.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public User registerUser(RegisterRequestDTO request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistsException(String.format("User already exists with email: %s", request.getEmail()));
        }
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        User savedUser = userRepository.save(newUser);
        logger.info("User with id {} has been saved.", savedUser.getId());
        return savedUser;
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                    () ->  new UserDoesNotExistException("Could not find user with email " + request.getEmail()));
            logger.info("User with id {} has been authenticated.", user.getId());
            String jwtToken = jwtService.generateToken(user);
            return new AuthenticationResponseDTO(jwtToken);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(String.format("Bad credentials when trying to authenticate user with email: %s.", request.getEmail()));
        }
    }
}
