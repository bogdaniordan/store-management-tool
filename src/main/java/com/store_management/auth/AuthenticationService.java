package com.store_management.auth;

import com.store_management.dto.AuthenticationRequestDTO;
import com.store_management.dto.AuthenticationResponseDTO;
import com.store_management.dto.RegisterRequestDTO;
import com.store_management.entity.User;
import com.store_management.exception.UserDoesNotExistException;
import com.store_management.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        User savedUser = userRepository.save(user);
        logger.info("User with id {} has been saved.", user.getId());
        return savedUser;
    }

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.getEmail(),
                        authenticationRequestDTO.getPassword()
                )
        );
        Optional<User> user = userRepository.findByEmail(authenticationRequestDTO.getEmail());
        if (user.isEmpty()) {
            throw new UserDoesNotExistException("Could not find user with email " + authenticationRequestDTO.getEmail());
        }
        logger.info("User has been authenticated.");
        String jwtToken = jwtService.generateToken(user.get());
        logger.info("JWT token has been generated on authentication.");
        return new AuthenticationResponseDTO(jwtToken);
    }
}
