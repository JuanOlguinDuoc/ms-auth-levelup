package com.levelup.service;

import com.levelup.dto.ProductDto;
import com.levelup.model.Product;
import com.levelup.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.levelup.repository.PlatformRepo;
import com.levelup.model.Platform;
import com.levelup.repository.CategoryRepo;
import com.levelup.model.Category;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private PlatformRepo platformRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    public List<ProductDto> getProducts(){
        return productRepo.findAll().stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductDto dto){
        Product p = ProductDto.toEntity(dto);
    p.setStock(dto.getStock());
    // resolve plataforma names to entities
    if (dto.getPlataforma() != null) {
        Set<Platform> platforms = dto.getPlataforma().stream()
            .map(nombre -> platformRepo.findByNombre(nombre).orElseGet(() -> platformRepo.save(Platform.builder().nombre(nombre).build())))
            .collect(Collectors.toSet());
        p.setPlataforma(platforms);
    }
    // resolve categoria name to entity
    if (dto.getCategoria() != null) {
        Category cat = categoryRepo.findByNombre(dto.getCategoria()).orElseGet(() -> categoryRepo.save(Category.builder().nombre(dto.getCategoria()).build()));
        p.setCategoria(cat);
    }
        Product saved = productRepo.save(p);
        return ProductDto.fromEntity(saved);
    }

    public ProductDto findById(Long id){
        return productRepo.findById(id).map(ProductDto::fromEntity).orElse(null);
    }

    public ProductDto updateProduct(Long id, ProductDto dto){
        Product existing = productRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existing.setImagenUrl(dto.getImagenUrl());
        existing.setTitulo(dto.getTitulo());
        existing.setAtributos(dto.getAtributos());
    existing.setStock(dto.getStock());
        existing.setPrecio(dto.getPrecio());
    // resolve categoria name to entity
    if (dto.getCategoria() != null) {
        Category cat = categoryRepo.findByNombre(dto.getCategoria()).orElseGet(() -> categoryRepo.save(Category.builder().nombre(dto.getCategoria()).build()));
        existing.setCategoria(cat);
    } else {
        existing.setCategoria(null);
    }
    if (dto.getPlataforma() != null) {
        Set<Platform> platforms = dto.getPlataforma().stream()
            .map(nombre -> platformRepo.findByNombre(nombre).orElseGet(() -> platformRepo.save(Platform.builder().nombre(nombre).build())))
            .collect(Collectors.toSet());
        existing.setPlataforma(platforms);
    }
        existing.setEnOferta(dto.getEnOferta());
        existing.setDescuento(dto.getDescuento());
        existing.setFechaInicioOferta(dto.getFechaInicioOferta());
        existing.setFechaFinOferta(dto.getFechaFinOferta());
        Product saved = productRepo.save(existing);
        return ProductDto.fromEntity(saved);
    }

    public void deleteProduct(Long id){
        if (!productRepo.existsById(id)) throw new IllegalArgumentException("Product not found");
        productRepo.deleteById(id);
    }
}
