package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.Ingredient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecipeController {

    @GetMapping("")
    public String getRecipe() {
        return "BIG BALLS TEST";
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientByID(@PathVariable int id) {
        return new Ingredient("wool"+id);
    }
}
