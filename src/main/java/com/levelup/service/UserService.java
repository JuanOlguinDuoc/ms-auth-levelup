package com.levelup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.levelup.model.User;
import com.levelup.repository.UserRepo;
import com.levelup.dto.UserDto;
import com.levelup.repository.RoleRepo;
import com.levelup.model.Role;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public UserDto createUser(UserDto dto) {
        User entity = UserDto.toEntity(dto);
        // resolve role by name
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("role is required");
        }
        Role role = roleRepo.findByName(dto.getRole()).orElseThrow(() -> new IllegalArgumentException("Role not found: " + dto.getRole()));
        entity.setRole(role);
        User saved = userRepo.save(entity);
        return UserDto.fromEntity(saved);
    }

}
