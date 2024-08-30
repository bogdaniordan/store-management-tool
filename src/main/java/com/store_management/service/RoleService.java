package com.store_management.service;

import com.store_management.entity.Role;
import com.store_management.exception.ResourceNotFoundException;
import com.store_management.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find role with id " + id));
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role role) {
        if (roleRepository.existsById(id)) {
            return roleRepository.save(role);
        }
        throw new ResourceNotFoundException("Could not find role");
    }

    public void deleteRole(Long id) throws Exception {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        }
        throw new ResourceNotFoundException("Role not found");
    }
}
