package com.levelup.controller;

import com.levelup.dto.ProductDto;
import com.levelup.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDto> listProducts(){
        return productService.getProducts();
    }

    @PostMapping
    public ResponseEntity<Map<String,Object>> createProduct(@RequestBody ProductDto dto){
        try{
            ProductDto created = productService.createProduct(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Producto creado","product", created));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al crear producto","error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Long id){
        ProductDto dto = productService.findById(id);
        if (dto == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Producto no encontrado"));
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody ProductDto dto){
        try{
            ProductDto updated = productService.updateProduct(id, dto);
            return ResponseEntity.ok(Map.of("message","Producto actualizado","product", updated));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message","Error al actualizar producto","error", e.getMessage()));
        }
    }

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
