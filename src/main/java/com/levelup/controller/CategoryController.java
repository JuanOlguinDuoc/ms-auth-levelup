package com.levelup.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.levelup.dto.CategoryDto;
import com.levelup.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Categorías", description = "Gestión de categorías de productos")
@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Listar todas las categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getCategories();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Obtener una categoría por ID")
    @ApiResponse(responseCode = "200", description = "Categoría encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear la categoría")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody CategoryDto categoryDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CategoryDto created = categoryService.createCategory(categoryDto);
            response.put("message", "Categoria generada correctamente");
            response.put("category", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "Error al crear categoria");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Operation(summary = "Actualizar una categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar la categoría")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        try {
            CategoryDto updated = categoryService.updateCategory(id, categoryDto);
            return ResponseEntity.ok(Map.of("message","Categoria actualizada","category", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar categoria","error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar una categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar la categoría")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long id) {
        try {
            
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Map.of("message","Categoria eliminada correctamente"));
        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Categoria no encontrada"));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al eliminar categoria","error", e.getMessage()));
        }
    }
}
