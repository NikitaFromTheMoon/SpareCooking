package com.example.spare_cooking.model;

import java.util.List;

public record Recipe(
        List<Tag> tags,
        List<Ingredient> ingredients,
        String name,
        int likes
) {
}
