package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Model.Category;
import com.formation.udemy.spring.recipe_app.Model.Commands.CategoryCommand;
import com.formation.udemy.spring.recipe_app.Model.Commands.NotesCommand;
import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
import com.formation.udemy.spring.recipe_app.Model.Notes;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanToBeanCommandConverterImplTest {

  //Constantes
  public static final Long ID = 1L;
  public static final String DESCRIPTION = "description";
  public static final String SOURCE = "source";
  public static final String RECIPE_NOTES = "recipe notes";
  public static final Difficulty DIFFICULTY = Difficulty.EASY;
  private static final Integer PREP_TIME = 5;
  private static final Integer COOK_TIME = 10;
  private static final Integer SERVINGS = 4;
  private static final String URL = "url";
  private static final String DIRECTIONS = "directions";
  //
  Recipe recipe;
  @Autowired
  private BeanToBeanCommandConverter beanToBeanCommandConverter;

  @Before
  public void setUp() throws Exception {
    recipe = new Recipe();
    recipe.setId(ID);
    recipe.setPrepTime(PREP_TIME);
    recipe.setCookTime(COOK_TIME);
    recipe.setServings(SERVINGS);
    recipe.setDescription(DESCRIPTION);
    recipe.setSource(SOURCE);
    recipe.setUrl(URL);
    recipe.setDirections(DIRECTIONS);
    recipe.setDifficulty(DIFFICULTY);
    Category category1 = new Category();
    category1.setId(1L);
    category1.setDescription(DESCRIPTION);
    category1.getRecipes().add(recipe);

    Category category2 = Category.builder()
      .id(2L)
      .description(DESCRIPTION)
      .recipes(new HashSet<>())
      .build();
    category2.getRecipes().add(recipe);


    recipe.getCategories().add(category1);
    recipe.getCategories().add(category2);

    Notes notes = new Notes();
    notes.setRecipeNotes(RECIPE_NOTES);
    notes.setRecipe(recipe);
    recipe.setNotes(notes);
  }

  @Test
  public void convert() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    RecipeCommand recipeCommand = (RecipeCommand) beanToBeanCommandConverter.convert(recipe, null, new HashMap<>(), new ArrayList<>());

    //Assertions sur recipCommand
    assertEquals(recipe.getId(), recipeCommand.getId());
    assertTrue(recipe.getPrepTime() == recipeCommand.getPrepTime());
    assertTrue(recipe.getCookTime() == recipeCommand.getCookTime());
    assertTrue(recipe.getServings() == recipeCommand.getServings());
    assertEquals(recipe.getDescription(), recipeCommand.getDescription());
    assertEquals(recipe.getSource(), recipeCommand.getSource());
    assertEquals(recipe.getUrl(), recipeCommand.getUrl());
    assertEquals(recipe.getDirections(), recipeCommand.getDirections());
    assertEquals(recipe.getDifficulty(), recipeCommand.getDifficulty());
    Set<CategoryCommand> categories = recipeCommand.getCategories();
    assertTrue(categories.size() == 2);

    //Assertion sur
    Optional<CategoryCommand> catOpt = categories.stream().findFirst();
    assertTrue(catOpt.isPresent());
    CategoryCommand categoryCommand = catOpt.get();
    assertTrue(categoryCommand.getDescription().equals(DESCRIPTION));

    //
    NotesCommand notes = recipeCommand.getNotes();
    assertTrue(notes != null);
    assertEquals(notes.getRecipeNotes(), recipe.getNotes().getRecipeNotes());
    assertTrue(notes.getRecipe().equals(recipeCommand));
    Set<RecipeCommand> recipes = categoryCommand.getRecipes();
    assertTrue(recipes.size() == 1);


  }
}
