package com.formation.udemy.spring.recipe_app.Service.Impl;

import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Repository.RecipeRepository;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import com.formation.udemy.spring.recipe_app.Utils.Converters.BeanToBeanConverter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
public class RecipeServiceImplTest {

  RecipeService recipeService;

  @Mock
  RecipeRepository recipeRepository;

  @Mock
  BeanToBeanConverter beanToBeanConverter;

  @Before
  public void setUp(){
    MockitoAnnotations.initMocks(this);
    recipeService = new RecipeServiceImpl(recipeRepository, beanToBeanConverter);
  }

  @Test
  public void getRecipes() {
    Recipe recipe = new Recipe();
    List<Recipe> recipeData = new ArrayList<>();
    recipeData.add(recipe);

    when(recipeRepository.findAll()).thenReturn(recipeData);

    assertEquals(1, recipeService.getRecipes().size());

    verify(recipeRepository, times(1)).findAll();

  }

  @Test
  public void findRecipeById() {
    Optional<Recipe> recipeOptional = Optional.ofNullable(Recipe.builder().id(1L).build());

    when(recipeRepository.findById(1L)).thenReturn(recipeOptional);

    Recipe recipe = recipeService.findRecipeById(1L);

    verify(recipeRepository, times(1)).findById(1L);
    Long expectedId = 1L;
    assertEquals(expectedId, recipe.getId());
  }

  @Test(expected = RuntimeException.class)
  public void findRecipeByIdThrowException() {
    Optional<Recipe> recipeOptional = Optional.ofNullable(Recipe.builder().id(1L).build());

    when(recipeRepository.findById(1L)).thenReturn(recipeOptional);

    Recipe recipe = recipeService.findRecipeById(2L);

    verify(recipeRepository, times(1)).findById(2L);
    Long expectedId = 1L;
    assertEquals(expectedId, recipe.getId());
  }

  @Test
  public void saveRecipeCommand() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    RecipeCommand command = RecipeCommand.builder().id(1L).build();
    Recipe recipe = Recipe.builder().id(2L).build();

    when(beanToBeanConverter.convert(any(RecipeCommand.class))).thenReturn(recipe);
    when(beanToBeanConverter.convert(any(Recipe.class))).thenReturn(command);
    when(recipeRepository.save(any())).thenReturn(recipe);

    RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

    verify(beanToBeanConverter, times(2)).convert(any());
    verify(recipeRepository, times(1)).save(recipe);

    Long expectedId = 1L;
    assertEquals(expectedId, savedCommand.getId());

  }
}
