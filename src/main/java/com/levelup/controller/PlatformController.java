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

@RestController
@RequestMapping("api/v1/platforms")
public class PlatformController {

    @Autowired
    private PlatformService platformService;

    @GetMapping
    public ResponseEntity<List<PlatformDto>> getAllPlatforms() {
        List<PlatformDto> platforms = platformService.getPlatforms();
        return ResponseEntity.ok(platforms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatformDto> getPlatformById(@PathVariable Long id) {
        PlatformDto platform = platformService.findById(id);
        return ResponseEntity.ok(platform);
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePlatform(@PathVariable Long id, @RequestBody PlatformDto platformDto) {
        try {
            PlatformDto updated = platformService.updatePlatform(id, platformDto);
            return ResponseEntity.ok(Map.of("message","Plataforma actualizada","platform", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar plataforma","error", e.getMessage()));
        }
    }

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

