package com.levelup.dto;

import com.levelup.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String imagenUrl;
    private String titulo;
    private String atributos;
    private Integer precio;
    private String categoria;
    private String plataforma;
    private Boolean enOferta;
    private Integer descuento;
    private LocalDate fechaInicioOferta;
    private LocalDate fechaFinOferta;

    public static ProductDto fromEntity(Product p){
        if (p == null) return null;
        return ProductDto.builder()
                .id(p.getId())
                .imagenUrl(p.getImagenUrl())
                .titulo(p.getTitulo())
                .atributos(p.getAtributos())
                .precio(p.getPrecio())
                .categoria(p.getCategoria())
                .plataforma(p.getPlataforma())
                .enOferta(p.getEnOferta())
                .descuento(p.getDescuento())
                .fechaInicioOferta(p.getFechaInicioOferta())
                .fechaFinOferta(p.getFechaFinOferta())
                .build();
    }

    public static Product toEntity(ProductDto dto){
        if (dto == null) return null;
        return Product.builder()
                .id(dto.getId())
                .imagenUrl(dto.getImagenUrl())
                .titulo(dto.getTitulo())
                .atributos(dto.getAtributos())
                .precio(dto.getPrecio())
                .categoria(dto.getCategoria())
                .plataforma(dto.getPlataforma())
                .enOferta(dto.getEnOferta())
                .descuento(dto.getDescuento())
                .fechaInicioOferta(dto.getFechaInicioOferta())
                .fechaFinOferta(dto.getFechaFinOferta())
                .build();
    }
}
