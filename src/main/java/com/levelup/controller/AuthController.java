package com.levelup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.levelup.dto.AuthRequest;
import com.levelup.dto.AuthResponse;
import com.levelup.model.Role;
import com.levelup.model.User;
import com.levelup.service.AuthService;
import com.levelup.repository.RoleRepo;
import com.levelup.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User userRequest) {
        // Buscar role gestionada: prioriza id, luego name
        Role incoming = userRequest.getRole();
        Role roleToSet = null;

        if (incoming != null) {
            if (incoming.getId() != null) {
                roleToSet = roleRepo.findById(incoming.getId())
                    .orElseThrow(() -> new RuntimeException("Role id no encontrado: " + incoming.getId()));
            } else if (incoming.getName() != null) {
                roleToSet = roleRepo.findByName(incoming.getName())
                    .orElseThrow(() -> new RuntimeException("Role name no encontrado: " + incoming.getName()));
            }
        }

        if (roleToSet == null) {
            // opción: asignar role por defecto o fallar
            roleToSet = roleRepo.findByName("administrador")
                .orElseThrow(() -> new RuntimeException("Role por defecto no existe"));
        }

        userRequest.setRole(roleToSet);

        // guardar contraseña en claro temporalmente para autenticar y generar token
        String rawPassword = userRequest.getPassword();
        userRequest.setPassword(passwordEncoder.encode(rawPassword));
        userRepo.save(userRequest);

        // autenticamos para generar token y devolver AuthResponse con token
        AuthRequest authReq = new AuthRequest(userRequest.getEmail(), rawPassword);
        AuthResponse authResp = service.autenticar(authReq);
        return ResponseEntity.ok(authResp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = service.autenticar(authRequest);
        return ResponseEntity.ok(response);
    }
}
