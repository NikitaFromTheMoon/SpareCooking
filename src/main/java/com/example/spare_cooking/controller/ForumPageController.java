package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Recipe;
import com.example.spare_cooking.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forum")
public class ForumPageController {

    private final RecipeRepository recipeRepository;

    public ForumPageController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    @GetMapping("/pages")
    public Page<Recipe> getRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String title
    ) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, 9, sort);

        if (title != null && !title.isBlank()) {
            return recipeRepository.findByTitleContainingIgnoreCase(title, pageable);
        }

        if (tag != null) {
            return recipeRepository.findByTagsName(tag, pageable);
        }
        return recipeRepository.findAll(pageable);
    }
}
