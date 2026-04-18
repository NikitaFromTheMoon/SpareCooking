package com.example.spare_cooking.repository;

import com.example.spare_cooking.model.table.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    Page<Recipe> findByTagsName(String name, Pageable pageable);
    Page<Recipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}