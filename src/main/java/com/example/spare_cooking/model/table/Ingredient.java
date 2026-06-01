package com.example.spare_cooking.model.table;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name="ingredient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private Double conversionCoeff;
    private Boolean isLiquid;

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientUnit> ingredientUnits;
}