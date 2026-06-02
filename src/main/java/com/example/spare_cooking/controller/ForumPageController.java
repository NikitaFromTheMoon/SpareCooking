package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Ingredient;
import com.example.spare_cooking.model.table.Recipe;
import com.example.spare_cooking.model.table.Tag;
import com.example.spare_cooking.repository.RecipeRepository;
import jakarta.persistence.criteria.Join;
import jakarta.servlet.Filter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/repipes")
public class ForumPageController {

    private final RecipeRepository recipeRepository;

    public ForumPageController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    @GetMapping("/search")
    public Page<Recipe> getRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") int limit,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "createdAt") String sortField,
            @RequestParam String search,
            @RequestParam String tags,
            @RequestParam String ingredients,
            @RequestParam String title
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);

        Specification<Recipe> spec = Specification.allOf((root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("title")), "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("description")), "%" + search.toLowerCase() + "%")
                ));

        if (tags != null && !tags.isBlank()) {
            List<String> tagList = Arrays.asList(tags.split(","));
            spec = spec.and((root, query, cb) -> {
                Join<Recipe, Tag> tagJoin = root.join("tags");
                query.distinct(true);
                return tagJoin.get("name").in(tagList);
            });
        }

        if (ingredients != null && !ingredients.isBlank()) {
            List<String> ingList = Arrays.asList(ingredients.split(","));
            spec = spec.and((root, query, cb) -> {
                Join<Recipe, Ingredient> ingJoin = root.join("ingredients");
                query.distinct(true);
                return ingJoin.get("name").in(ingList);
            });
        }
        Pageable pageable = PageRequest.of(page, limit, sort);
//        if (title != null && !title.isBlank()) {
//            return recipeRepository.findByTitleContainingIgnoreCase(title, pageable);
//        }
//        if (tags != null) {
//            return recipeRepository.findByTagsName(Arrays.stream(tags.split(", ")).toList(), pageable);
//        }

        return recipeRepository.findAll(spec, pageable);
    }
}
