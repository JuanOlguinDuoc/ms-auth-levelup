package com.levelup.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getUsers(){
        return userRepo.findAll();
    }

    public UserDto createUser(UserDto dto) {
        User entity = UserDto.toEntity(dto);
        // resolve role by name
        if (dto.getRole() == null) {
            throw new IllegalArgumentException("role is required");
        }
        // check duplicates email and run
        if (userRepo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        if (dto.getRun() != null && userRepo.existsByRun(dto.getRun())) {
            throw new IllegalArgumentException("El RUN ya está registrado");
        }

        // Encriptar la contraseña antes de guardar
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Role role = roleRepo.findByName(dto.getRole()).orElseThrow(() -> new IllegalArgumentException("Role not found: " + dto.getRole()));
        entity.setRole(role);
        User saved = userRepo.save(entity);
        return UserDto.fromEntity(saved);
    }

    public UserDto findById(Long id){
        return userRepo.findById(id).map(UserDto::fromEntity).orElse(null);
    }

    public UserDto findByEmail(String email){
        return userRepo.findByEmail(email).map(UserDto::fromEntity).orElse(null);
    }

    public UserDto updateUser(Long id, UserDto dto){
        User existing = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        // update fields
        existing.setRun(dto.getRun());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setEmail(dto.getEmail());
        // Encriptar la contraseña solo si se proporciona una nueva
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getRole() != null) {
            Role role = roleRepo.findByName(dto.getRole()).orElseThrow(() -> new IllegalArgumentException("Role not found: " + dto.getRole()));
            existing.setRole(role);
        }
        User saved = userRepo.save(existing);
        return UserDto.fromEntity(saved);
    }

    public void deleteUser(Long id){
        if (!userRepo.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepo.deleteById(id);
    }

    public UserDto patchUser(Long id, Map<String, Object> updates){
        User existing = userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Do not allow updating id, run or role via patch
        if (updates.containsKey("firstName")) existing.setFirstName((String) updates.get("firstName"));
        if (updates.containsKey("lastName")) existing.setLastName((String) updates.get("lastName"));
        if (updates.containsKey("email")) existing.setEmail((String) updates.get("email"));
        // Encriptar la contraseña si se proporciona en el patch
        if (updates.containsKey("password")) {
            String password = (String) updates.get("password");
            if (password != null && !password.isEmpty()) {
                existing.setPassword(passwordEncoder.encode(password));
            }
        }

        User saved = userRepo.save(existing);
        return UserDto.fromEntity(saved);
    }

}
