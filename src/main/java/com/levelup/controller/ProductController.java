package com.levelup.controller;

import com.levelup.dto.ProductDto;
import com.levelup.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Productos", description = "Gestión de productos del ecommerce")
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Listar todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    @GetMapping
    public List<ProductDto> listProducts(){
        return productService.getProducts();
    }

    @Operation(summary = "Crear un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear el producto")
    })
    @PostMapping
    public ResponseEntity<Map<String,Object>> createProduct(@RequestBody ProductDto dto){
        try{
            ProductDto created = productService.createProduct(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Producto creado","product", created));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al crear producto","error", e.getMessage()));
        }
    }

    @Operation(summary = "Crear producto con imagen (multipart/form-data)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado con imagen exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al crear el producto")
    })
    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<Map<String,Object>> createProductWithImage(
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "atributos", required = false) String atributos,
            @RequestParam(value = "precio", required = false) Integer precio,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "plataforma", required = false) java.util.List<String> plataforma,
            @RequestParam(value = "stock", required = false) Integer stock,
            @RequestParam(value = "file", required = false) MultipartFile file
    ){
        try{
            // guardar archivo en uploads/
            String imagenUrl = null;
            if (file != null && !file.isEmpty()){
                java.nio.file.Files.createDirectories(java.nio.file.Path.of("uploads"));
                String original = file.getOriginalFilename();
                String ext = "";
                if (original != null && original.contains(".")) ext = original.substring(original.lastIndexOf('.'));
                String filename = java.util.UUID.randomUUID().toString() + ext;
                java.nio.file.Path target = java.nio.file.Path.of("uploads").resolve(filename);
                java.nio.file.Files.copy(file.getInputStream(), target);
                imagenUrl = "/uploads/" + filename;
            }

        ProductDto dto = ProductDto.builder()
                    .titulo(titulo)
                    .atributos(atributos)
                    .precio(precio)
            .stock(stock)
                    .imagenUrl(imagenUrl)
                    .categoria(categoria)
                    .plataforma(plataforma == null ? null : new java.util.HashSet<>(plataforma))
                    .build();

            ProductDto created = productService.createProduct(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Producto creado","product", created));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al crear producto","error", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener un producto por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Long id){
        ProductDto dto = productService.findById(id);
        if (dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Producto no encontrado"));
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Actualizar un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al actualizar el producto")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto){
        try{
            ProductDto updated = productService.updateProduct(id, dto);
            return ResponseEntity.ok(Map.of("message","Producto actualizado","product", updated));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar producto","error", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "400", description = "Error al eliminar el producto")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id){
        try{
            productService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message","Producto eliminado"));
        } catch (Exception e){
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Producto no encontrado","error", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al eliminar producto","error", e.getMessage()));
        }
    }
}
