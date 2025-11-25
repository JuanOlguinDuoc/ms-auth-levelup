package com.levelup.service;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.levelup.dto.CategoryDto;
import com.levelup.model.Category;
import com.levelup.repository.CategoryRepo;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<CategoryDto> getCategories() {
        return categoryRepo.findAll().stream().map(CategoryDto::fromEntity).toList();
    }

    public CategoryDto findById(Long id) {
        return categoryRepo.findById(id).map(CategoryDto::fromEntity).orElse(null);
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryDto.toEntity(categoryDto);
        Category saved = categoryRepo.save(category);
        return CategoryDto.fromEntity(saved);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepo.deleteById(id);
    }

    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category existing = categoryRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        existing.setNombre(dto.getNombre());
        Category saved = categoryRepo.save(existing);
        return CategoryDto.fromEntity(saved);
    }

}
