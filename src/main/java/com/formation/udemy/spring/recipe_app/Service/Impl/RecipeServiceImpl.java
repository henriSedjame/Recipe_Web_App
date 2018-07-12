package com.formation.udemy.spring.recipe_app.Service.Impl;

import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Repository.RecipeRepository;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Set<Recipe> getRecipes() {
        return ImmutableSet.copyOf(this.recipeRepository.findAll());
    }
}
