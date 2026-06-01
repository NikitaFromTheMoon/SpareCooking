package com.example.spare_cooking.controller;

import com.example.spare_cooking.model.table.Ingredient;
import com.example.spare_cooking.model.table.Tag;
import com.example.spare_cooking.model.table.WebUser;
import com.example.spare_cooking.repository.IngredientRepository;
import com.example.spare_cooking.repository.TagRepository;
import com.example.spare_cooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<WebUser> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users/{id}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public void banUser(@PathVariable Long id) {
        WebUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setIsBanned(!user.getIsBanned());
        userRepository.save(user);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tags")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Tag> getTags() {
        return tagRepository.findAll();
    }

    @DeleteMapping("/tags/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(@PathVariable Long id) {
        tagRepository.deleteById(id);
    }

    @PostMapping("/tags")
    @PreAuthorize("hasRole('ADMIN')")
    public Tag addTag(@RequestBody Map<String, String> body) {
        Tag tag = new Tag();
        tag.setName(body.get("name"));
        return tagRepository.save(tag);
    }

    @GetMapping("/ingredients")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Ingredient> getIngredients() {
        return ingredientRepository.findAll();
    }

    @PostMapping("/ingredients")
    @PreAuthorize("hasRole('ADMIN')")
    public Ingredient addIngredient(@RequestBody Ingredient ingredient) {
        if (ingredient.getConversionCoeff() == null) {
            ingredient.setConversionCoeff(1.0);
        }
        return ingredientRepository.save(ingredient);
    }

    @DeleteMapping("/ingredients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteIngredient(@PathVariable Long id) {
        ingredientRepository.deleteById(id);
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Object> getReports() {
        return Collections.emptyList();
    }
}