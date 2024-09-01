package com.store_management.dto;

import com.store_management.auth.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}
