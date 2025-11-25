package com.levelup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.levelup.dto.RoleDto;
import com.levelup.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Roles", description = "Gestión de roles de usuario")
@RestController
@RequestMapping("api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Operation(summary = "Listar todos los roles")
    @ApiResponse(responseCode = "200", description = "Lista de roles obtenida exitosamente")
    @GetMapping
    public List<RoleDto> listRoles(){
        return roleService.getRoles();
    }

    @Operation(summary = "Crear un nuevo rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear el rol")
    })
    @PostMapping
    public ResponseEntity<Map<String,Object>> createRole(@RequestBody RoleDto dto){
        Map<String,Object> resp = new HashMap<>();
        try{
            RoleDto created = roleService.createRole(dto);
            resp.put("message", "Rol generado correctamente");
            resp.put("role", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e){
            resp.put("message", "Error al crear rol");
            resp.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }
    }

    @Operation(summary = "Obtener un rol por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol encontrado"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getRole(@PathVariable Long id){
        RoleDto dto = roleService.findById(id);
        if (dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Rol no encontrado"));
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Actualizar un rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar el rol")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateRole(@PathVariable Long id, @RequestBody RoleDto dto){
        try{
            RoleDto updated = roleService.updateRole(id, dto);
            return ResponseEntity.ok(Map.of("message","Rol actualizado","role", updated));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar rol","error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un rol")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rol no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar el rol")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id){
        try{
            roleService.deleteRole(id);
            return ResponseEntity.ok(Map.of("message","Rol eliminado"));
        } catch (Exception e){
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Rol no encontrado","error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al eliminar rol","error", e.getMessage()));
        }
    }

}
