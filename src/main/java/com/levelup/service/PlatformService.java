package com.levelup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.levelup.dto.PlatformDto;
import com.levelup.model.Platform;
import com.levelup.repository.PlatformRepo;

@Service
public class PlatformService {

    @Autowired
    private PlatformRepo platformRepo;

    public List<PlatformDto> getPlatforms(){
        return platformRepo.findAll().stream().map(PlatformDto::fromEntity).toList();
    }

    public PlatformDto findById(Long id){
        return platformRepo.findById(id).map(PlatformDto::fromEntity).orElse(null);
    }

    public PlatformDto createPlatform(PlatformDto platformDto) {
        Platform p = PlatformDto.toEntity(platformDto);
        Platform saved = platformRepo.save(p);
        return PlatformDto.fromEntity(saved);

    }

    public void deletePlatform(Long id) {
        if (!platformRepo.existsById(id)) {
            throw new IllegalArgumentException("Platform not found");
        }
        platformRepo.deleteById(id);
    }

    public PlatformDto updatePlatform(Long id, PlatformDto dto) {
        Platform existing = platformRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Platform not found"));
        existing.setNombre(dto.getNombre());
        Platform saved = platformRepo.save(existing);
        return PlatformDto.fromEntity(saved);
    }

}
