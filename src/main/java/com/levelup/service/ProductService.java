package com.levelup.service;

import com.levelup.dto.ProductDto;
import com.levelup.model.Product;
import com.levelup.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public List<ProductDto> getProducts(){
        return productRepo.findAll().stream().map(ProductDto::fromEntity).collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductDto dto){
        Product p = ProductDto.toEntity(dto);
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
        existing.setPrecio(dto.getPrecio());
        existing.setCategoria(dto.getCategoria());
    existing.setPlataforma(dto.getPlataforma());
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
