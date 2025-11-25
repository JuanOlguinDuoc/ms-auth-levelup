package com.levelup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.levelup.dto.PlatformDto;
import com.levelup.service.PlatformService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Plataformas", description = "Gestión de plataformas de juegos")
@RestController
@RequestMapping("api/v1/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @Operation(summary = "Listar todas las plataformas")
    @ApiResponse(responseCode = "200", description = "Lista de plataformas obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<PlatformDto>> getAllPlatforms() {
        List<PlatformDto> platforms = platformService.getPlatforms();
        return ResponseEntity.ok(platforms);
    }

    @Operation(summary = "Obtener una plataforma por ID")
    @ApiResponse(responseCode = "200", description = "Plataforma encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<PlatformDto> getPlatformById(@PathVariable Long id) {
        PlatformDto platform = platformService.findById(id);
        return ResponseEntity.ok(platform);
    }

    @Operation(summary = "Crear una nueva plataforma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Plataforma creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear la plataforma")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlatform(@RequestBody PlatformDto platformDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlatformDto created = platformService.createPlatform(platformDto);
            response.put("message", "Plataforma generada correctamente");
            response.put("platform", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Error al crear plataforma");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Actualizar una plataforma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plataforma actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar la plataforma")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePlatform(@PathVariable Long id, @RequestBody PlatformDto platformDto) {
        try {
            PlatformDto updated = platformService.updatePlatform(id, platformDto);
            return ResponseEntity.ok(Map.of("message","Plataforma actualizada","platform", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar plataforma","error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar una plataforma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plataforma eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Plataforma no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar la plataforma")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePlatform(@PathVariable Long id) {
        try {
            platformService.deletePlatform(id);
            return ResponseEntity.ok(Map.of("message","Plataforma eliminada correctamente"));
        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Plataforma no encontrada"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al eliminar plataforma","error", e.getMessage()));
        }
    }
}

