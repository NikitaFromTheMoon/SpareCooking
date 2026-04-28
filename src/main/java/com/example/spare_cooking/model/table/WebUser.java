package com.example.spare_cooking.model.table;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keycloak_user_id", unique = true)
    private String keycloakUserId;

    @Column(unique = true, nullable = false)
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    private String avatarUrl;
    private String bio;
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<Recipe> createdRecipes;
    @ManyToMany
    @JoinTable(name = "saved_recipes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Set<Recipe> savedRecipes; // сохранённые рецепты

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
}
