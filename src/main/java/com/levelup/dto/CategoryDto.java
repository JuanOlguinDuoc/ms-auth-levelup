package com.levelup.dto;

import lombok.*;
import com.levelup.model.Category;;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String nombre;

    public static CategoryDto fromEntity(Category c){
        if (c == null) return null;
        return CategoryDto.builder()
                .id(c.getId())
                .nombre(c.getNombre())
                .build();
    }

    public static Category toEntity(CategoryDto dto){
        if (dto == null) return null;
        return Category.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .build();
    }
}
