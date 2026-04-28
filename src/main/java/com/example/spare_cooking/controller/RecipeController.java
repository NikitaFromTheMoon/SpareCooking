package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Recipe;
import com.example.spare_cooking.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("/new")
    public Recipe createNewUser(
            @RequestParam String username,
            @RequestParam String email) {
        var newRecipe = new Recipe();
        newRecipe.setCreatedAt(LocalDateTime.now());
        //newRecipe.setAuthor();
        recipeService.saveRecipe(newRecipe);
        return newRecipe;
    }

    @GetMapping("/update")
    public Recipe updateUser(
            @RequestParam String username,
            @RequestParam String email) {
        var newRecipe = new Recipe();
        newRecipe.setCreatedAt(LocalDateTime.now());
        recipeService.saveRecipe(newRecipe);
        return newRecipe;
    }

    @GetMapping("/all")
    public Page<Recipe> getUsersPage(
            @RequestParam(defaultValue = "0") int page) {
        return recipeService.getPage(page);
    }
}
