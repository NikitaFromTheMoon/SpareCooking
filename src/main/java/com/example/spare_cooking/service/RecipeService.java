package com.example.spare_cooking.service;

import com.example.spare_cooking.model.table.Recipe;
import com.example.spare_cooking.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RecipeService {

    RecipeRepository repository;
    UserService userService;


    public RecipeService(RecipeRepository repo) {
        this.repository = repo;
    }

    public Recipe saveRecipe(Recipe recipe) {
        return repository.save(recipe);
    }

    public Recipe findById(Long id) {

        return repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
//
//    public Pageable getPageable() {
//
//        Sort sort = Sort.by("createdAt").ascending();
//        Pageable pageable = PageRequest.of(page, 4, sort);
//        repository.findAll(pageable);
//        return pageable;
//    }
}