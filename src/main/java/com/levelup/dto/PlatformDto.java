package com.levelup.dto;

import com.levelup.model.Platform;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder    
public class PlatformDto {
    private Long id;
    private String nombre;

    public static PlatformDto fromEntity(Platform p){
        if (p == null) return null;
        return PlatformDto.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .build();
    }

    public static Platform toEntity(PlatformDto dto){
        if (dto == null) return null;
        return Platform.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .build();
    }
}
