package com.formation.udemy.spring.recipe_app.Service.Impl;

import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Repository.RecipeRepository;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Service
public class RecipeServiceImpl implements RecipeService {
  private final RecipeRepository recipeRepository;

  public RecipeServiceImpl(RecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  @Override
  public Set<Recipe> getRecipes() {
    return ImmutableSet.copyOf(this.recipeRepository.findAll());
  }
}
