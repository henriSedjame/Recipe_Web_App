package com.formation.udemy.spring.recipe_app.Service;

import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Model.Recipe;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
public interface RecipeService {

  Set<Recipe> getRecipes();

  Recipe findRecipeById(Long id);

  RecipeCommand findRecipeCommandByid(Long id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

  Recipe saveRecipe(Recipe recipe);

  RecipeCommand saveRecipeCommand(RecipeCommand command);
}
