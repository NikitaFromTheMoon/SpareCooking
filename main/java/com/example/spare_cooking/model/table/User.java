package com.example.spare_cooking.model.table;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHashed;

    private String email;
    private String passwordHash;
    private String avatarUrl;
    private String bio;
    private LocalDateTime createdAt;
    //@OneToMany(mappedBy = "author")
    //private List<Recipe> recipes;
//
//    @ManyToMany
//    @JoinTable(name = "saved_recipes",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
//    private Set<String> savedRecipes; // сохранённые рецепты

    //@OneToMany(mappedBy = "user")
    //private List<String> comments;

    //@OneToMany(mappedBy = "user")
    //private List<String> likes;
}
