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

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> userList() {
        return service.getUsers();
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        UserDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Usuario no encontrado"));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDto dto){
        try{
            UserDto updated = service.updateUser(id, dto);
            return ResponseEntity.ok(Map.of("message","Usuario actualizado","user", updated));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar usuario","error", e.getMessage()));
        }
    }

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

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchUser(@PathVariable Long id, @RequestBody Map<String, Object> updates){
        try{
            // ignore id, run, role keys if present
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