package com.levelup.repository;

import com.levelup.model.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepo extends JpaRepository<Platform, Long> {

    Optional<Platform> findByNombre(String nombre);

}
