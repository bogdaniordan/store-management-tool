package com.store_management.dto;

import com.store_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RegisterRequestDTO {

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private Role.RoleType role;
}
