package com.formation.udemy.spring.recipe_app.Service.Impl;

import com.formation.udemy.spring.recipe_app.Exceptions.NotFoundException;
import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Repository.RecipeRepository;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import com.formation.udemy.spring.recipe_app.Utils.Converters.BeanToBeanConverter;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
@Service
@Slf4j
public class RecipeServiceImpl implements RecipeService {
  private final RecipeRepository recipeRepository;
  @Qualifier("beanConverter")
  private final BeanToBeanConverter beanToBeanConverter;

  public RecipeServiceImpl(RecipeRepository recipeRepository, BeanToBeanConverter beanToBeanConverter) {
    this.recipeRepository = recipeRepository;
    this.beanToBeanConverter = beanToBeanConverter;
  }

  @Override
  public Set<Recipe> getRecipes() {
    return ImmutableSet.copyOf(this.recipeRepository.findAll());
  }

  @Override
  public Recipe findRecipeById(String id) {
    return this.recipeRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Recipe with id " + id + " not found"));
  }

  @Override
  public RecipeCommand findRecipeCommandByid(String id) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

    Recipe recipe = findRecipeById(id);
    return (RecipeCommand) beanToBeanConverter.convert(recipe);
  }

  @Override
  public Recipe saveRecipe(Recipe recipe) {
    return recipeRepository.save(recipe);
  }

  @Override
  @Transactional
  public RecipeCommand saveRecipeCommand(RecipeCommand command) {
    Recipe detachedRecipe;
    RecipeCommand savedCommand = null;

    try {
      detachedRecipe = (Recipe) this.beanToBeanConverter.convert(command);
      Recipe savedRecipe = this.recipeRepository.save(detachedRecipe);
      savedCommand = (RecipeCommand) this.beanToBeanConverter.convert(savedRecipe);

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      log.error(e.getMessage(), e.getCause());
    }

    return savedCommand;
  }
}
