package com.levelup.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.levelup.model.User;
import com.levelup.service.UserService;
import com.levelup.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    @GetMapping
    public List<UserDto> userList() {
        return service.getUsers()
                      .stream()
                      .map(UserDto::fromEntity)
                      .collect(Collectors.toList());
    }

    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear el usuario")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody UserDto dto) {
        Map<String, Object> resp = new HashMap<>();
        try {
            UserDto created = service.createUser(dto);
            resp.put("message", "Usuario generado correctamente");
            resp.put("user", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            resp.put("message", "Error al crear usuario");
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
    }

    @Operation(summary = "Buscar usuario por email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @GetMapping("/by-email/{email}")
    public ResponseEntity<Object> getUser(@PathVariable String email, @RequestParam(required = false) String password) {
        // email will be URL-decoded by Spring automatically
        UserDto dto = service.findByEmail(email);
        if (dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Usuario no encontrado"));

        if (password != null) {
            // If password provided as request param, validate it
            if (!password.equals(dto.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Credenciales inválidas"));
            }
        }

        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Iniciar sesión")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "400", description = "Email y password son requeridos"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Email y password son requeridos"));
        }

        UserDto user = service.findByEmail(email);
        if (user == null || !password.equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Credenciales inválidas"));
        }

        String token = "token-" + user.getEmail() + "-" + System.currentTimeMillis();
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("user", user);
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "Actualizar un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar el usuario")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDto dto){
        try{
            UserDto updated = service.updateUser(id, dto);
            return ResponseEntity.ok(Map.of("message","Usuario actualizado","user", updated));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar usuario","error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar el usuario")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id){
        try{
            service.deleteUser(id);
            return ResponseEntity.ok(Map.of("message","Usuario eliminado"));
        } catch (Exception e){
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Usuario no encontrado","error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al eliminar usuario","error", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar parcialmente un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado parcialmente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar el usuario")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        try{
            updates.remove("id");
            updates.remove("run");
            updates.remove("role");
            UserDto patched = service.patchUser(id, updates);
            return ResponseEntity.ok(Map.of("message","Usuario parchado","user", patched));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al parchado usuario","error", e.getMessage()));
        }
    }
    



}