package com.formation.udemy.spring.recipe_app.Service;

import com.formation.udemy.spring.recipe_app.Model.Recipe;

import java.util.Set;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
public interface RecipeService {
  Set<Recipe> getRecipes();
}
