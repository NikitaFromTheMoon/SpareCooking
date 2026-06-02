package com.example.spare_cooking.repository;

import com.example.spare_cooking.model.table.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    Page<Recipe> findByTagsName(String name, Pageable pageable);
    Page<Recipe> findByTagsName(List<String> name, Pageable pageable);
    Optional<Recipe> findById(Long id);
    Page<Recipe> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}