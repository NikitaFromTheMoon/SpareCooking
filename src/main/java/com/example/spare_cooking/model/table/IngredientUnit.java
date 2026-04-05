package com.example.spare_cooking.model.table;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class IngredientUnit {

    @ManyToOne
    @JoinColumn(name="ingredient_id")
    private Ingredient ingredient;

}
