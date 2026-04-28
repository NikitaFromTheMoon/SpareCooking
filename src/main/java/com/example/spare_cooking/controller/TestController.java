package com.example.spare_cooking.controller;

import com.example.spare_cooking.SpareCooking;
import com.example.spare_cooking.model.table.Ingredient;
import lombok.AllArgsConstructor;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    //ApplicationContext applicationContext;

    @GetMapping("/a")
    public String getRecipe() {
        return "new Ingredient()";
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientByID(@PathVariable int id) {
        return new Ingredient();
    }
}
