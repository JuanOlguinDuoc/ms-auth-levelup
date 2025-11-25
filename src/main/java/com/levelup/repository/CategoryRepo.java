package com.levelup.repository;

import com.levelup.model.Category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    Optional<Category> findByNombre(String nombre);
}
