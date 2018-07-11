package com.formation.udemy.spring.recipe_app.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private int version;
    private int prepTime;
    private int cookTime;
    private int servings;
    private String source;
    private String url;
    private String directions;
    private Difficulty difficulty;
    private Byte[] image;
    private Notes note;
    private Set<Ingredient> ingredients;
    private Category category;
}
