package com.levelup.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.levelup.dto.AuthRequest;
import com.levelup.dto.AuthResponse;
import com.levelup.model.User;
import com.levelup.repository.UserRepo;
import com.levelup.util.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private JwtUtil util;

    @Autowired
    private PasswordEncoder encoder;

    public AuthResponse registro(User user) {

        if (repo.existsByRun(user.getRun())) {
            return new AuthResponse(null, null, "El usuario ya existe");
        }

        if (repo.existsByEmail(user.getEmail())) {
            return new AuthResponse(null, null, "El email ya esta registrado");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = repo.save(user);

        String token = util.generadorToken(savedUser.getEmail());
        return new AuthResponse(token, savedUser.getEmail(), "Registro exitoso");

    }

    public AuthResponse autenticar(AuthRequest authRequest) {
        Optional<User> userOptional = repo.findByEmail(authRequest.getEmail());

        if (userOptional.isEmpty() ||
                !encoder.matches(authRequest.getPassword(), userOptional.get().getPassword())) {
            return new AuthResponse(null, null, "Credenciales inválidas");
        }

        User user = userOptional.get();
        String token = util.generadorToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), "Login exitoso");
    }
}

