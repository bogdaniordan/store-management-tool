package com.store_management.dto;

import com.store_management.auth.Role;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}
