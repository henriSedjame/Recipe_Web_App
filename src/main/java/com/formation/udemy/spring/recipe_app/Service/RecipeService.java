package com.formation.udemy.spring.recipe_app.Service;

import com.formation.udemy.spring.recipe_app.Model.Recipe;

import java.util.Set;

public interface RecipeService {
    Set<Recipe> getRecipes();
}
