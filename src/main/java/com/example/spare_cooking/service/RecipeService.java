package com.example.spare_cooking.service;

import com.example.spare_cooking.model.table.Recipe;
import com.example.spare_cooking.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    RecipeRepository repository;

    public RecipeService(RecipeRepository repo) {
        this.repository = repo;
    }

    public Recipe saveRecipe(Recipe recipe) {
        return repository.save(recipe);
    }

    public Page<Recipe> getPage(int page) {

        Sort sort = Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page, 4, sort);

        return repository.findAll(pageable);
    }
}