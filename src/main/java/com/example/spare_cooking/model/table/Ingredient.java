package com.example.spare_cooking.model.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ingredient")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private Double conversionCoeff; // Standart units - KG & L, all the others can be converted
    private Boolean isLiquid;

    @OneToMany(mappedBy = "ingredient")
    private List<IngredientUnit> ingredientUnits;
}
