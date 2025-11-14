package com.levelup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
 

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
