package com.levelup.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;
 

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
    private Integer stock;
    private Integer precio;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "category_id")
    private Category categoria;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_platform",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "platform_id"))
    @Builder.Default
    private Set<Platform> plataforma = new HashSet<>();

    private Boolean enOferta;
    private Integer descuento;
    private LocalDate fechaInicioOferta;
    private LocalDate fechaFinOferta;
}
