package com.levelup.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.levelup.dto.RoleDto;
import com.levelup.model.Role;
import com.levelup.repository.RoleRepo;

@Service
public class RoleService {

    @Autowired
    private RoleRepo roleRepo;

    public List<RoleDto> getRoles(){
        return roleRepo.findAll().stream().map(RoleDto::fromEntity).collect(Collectors.toList());
    }

    public RoleDto createRole(RoleDto dto){
        Role r = RoleDto.toEntity(dto);
        Role saved = roleRepo.save(r);
        return RoleDto.fromEntity(saved);
    }

    public RoleDto findById(Long id){
        return roleRepo.findById(id).map(RoleDto::fromEntity).orElse(null);
    }

    public RoleDto updateRole(Long id, RoleDto dto){
        Role existing = roleRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        existing.setName(dto.getName());
        Role saved = roleRepo.save(existing);
        return RoleDto.fromEntity(saved);
    }

    public void deleteRole(Long id){
        roleRepo.deleteById(id);
    }
}
