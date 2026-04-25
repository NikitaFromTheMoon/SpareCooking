package com.example.spare_cooking.model.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="recipes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content; // собсна, содержимое поста

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(mappedBy="recipe")
    List<IngredientUnit> ingredients;

    @Column(name = "cooking_time")
    private Integer cookingTime;

    @Column(name = "created_date")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @ManyToMany
    @JoinTable(
            name = "recipe_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

}
