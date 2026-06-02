package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Ingredient;
import com.example.spare_cooking.model.table.IngredientUnit;
import com.example.spare_cooking.model.table.Recipe;
import com.example.spare_cooking.service.RecipeService;
import com.example.spare_cooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    @PostMapping("/new")
    public Recipe createNewRecipe(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int cookingTime,
            @RequestParam(required = false) String existingImages,
            @RequestParam String ingredients
    ) {
        var newRecipe = new Recipe();
        newRecipe.setAuthor(userService.getCurrentWebUser());
        newRecipe.setCreatedAt(LocalDateTime.now());
        newRecipe.setCookingTime(cookingTime);
        //newRecipe.setIngredients(ingredients);
        newRecipe.setTitle(title);
        newRecipe.setContent(description);


        recipeService.saveRecipe(newRecipe);
        return newRecipe;
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(
            @PathVariable Long id) {
        return recipeService.findById(id);
    }

    @PostMapping("/")
    public Recipe updateRecipe(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam int cookingTime,
            @RequestParam String images,
            @RequestParam String existingImages,
            @RequestParam List<IngredientUnit> ingredients
            ) {
        var newRecipe = new Recipe();
        newRecipe.setCookingTime(cookingTime);
        newRecipe.setIngredients(ingredients);
        newRecipe.setTitle(title);
        newRecipe.setContent(description);

        recipeService.saveRecipe(newRecipe);
        return newRecipe;
    }
}
