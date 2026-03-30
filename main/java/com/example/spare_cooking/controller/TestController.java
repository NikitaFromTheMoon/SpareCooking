package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.Ingredient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<Ingredient> getRecipe() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("header-1" )
                .body(new Ingredient("<bold>WOOL\n</bold>BIG BALLS TEST"));
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientByID(@PathVariable int id) {
        return new Ingredient("wool"+id);
    }
}
