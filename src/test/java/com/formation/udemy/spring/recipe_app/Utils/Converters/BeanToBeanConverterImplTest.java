package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Model.*;
import com.formation.udemy.spring.recipe_app.Model.Commands.*;
import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanToBeanConverterImplTest {

  //Constantes
  private static final Long ID = 1L;
  private static final String DESCRIPTION = "description";
  private static final String SOURCE = "source";
  private static final String RECIPE_NOTES = "recipe notes";
  private static final Difficulty DIFFICULTY = Difficulty.EASY;
  private static final Integer PREP_TIME = 5;
  private static final Integer COOK_TIME = 10;
  private static final Integer SERVINGS = 4;
  private static final String URL = "url";
  private static final String DIRECTIONS = "directions";

  private Recipe recipe;

  @Autowired
  private BeanToBeanConverter beanToBeanConverter;

  @Before
  public void setUp() {
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

    UnitOfMeasure uom = UnitOfMeasure.builder()
      .id(6L)
      .description("uom")
      .build();

    Ingredient ingredient = Ingredient.builder()
      .id(5L)
      .description(DESCRIPTION)
      .unitOfMeasure(uom)
      .amount(BigDecimal.ONE)
      .recipe(recipe)
      .build();

    recipe.getIngredients().add(ingredient);
  }

  @Test
  public void convert() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    RecipeCommand recipeCommand = (RecipeCommand) beanToBeanConverter.convert(recipe);

    //Assertions sur recipCommand
    assertNotNull(recipeCommand);
    assertEquals(recipe.getId(), recipeCommand.getId());
    assertSame(recipe.getPrepTime(), recipeCommand.getPrepTime());
    assertSame(recipe.getCookTime(), recipeCommand.getCookTime());
    assertSame(recipe.getServings(), recipeCommand.getServings());
    assertEquals(recipe.getDescription(), recipeCommand.getDescription());
    assertEquals(recipe.getSource(), recipeCommand.getSource());
    assertEquals(recipe.getUrl(), recipeCommand.getUrl());
    assertEquals(recipe.getDirections(), recipeCommand.getDirections());
    assertEquals(recipe.getDifficulty(), recipeCommand.getDifficulty());
    List<CategoryCommand> categories = recipeCommand.getCategories().stream().collect(Collectors.toList());
    assertEquals(2, categories.size());
    List<IngredientCommand> ingredients = recipeCommand.getIngredients().stream().collect(Collectors.toList());
    assertEquals(1, ingredients.size());

    //Assertion sur les catégories
    Optional<CategoryCommand> catOpt = categories.stream().findFirst();
    assertTrue(catOpt.isPresent());
    CategoryCommand categoryCommand = catOpt.get();
    assertEquals(DESCRIPTION, categoryCommand.getDescription());

    Set<RecipeCommand> recipeCommands = categoryCommand.getRecipes();
    Optional<RecipeCommand> recipOpt = recipeCommands.stream().findFirst();
    assertTrue(recipOpt.isPresent());
    assertEquals(recipOpt.get(), recipeCommand);

    CategoryCommand catCommand2 = categories.get(1);
    assertNotNull(catCommand2);
    RecipeCommand recipeCommand2 = catCommand2.getRecipes().stream().collect(Collectors.toList()).get(0);
    assertSame(recipeCommand, recipeCommand2);

    //Assestion sur les notes
    NotesCommand notes = recipeCommand.getNotes();
    assertNotNull(notes);
    assertEquals(notes.getRecipeNotes(), recipe.getNotes().getRecipeNotes());
    assertEquals(notes.getRecipe(), recipeCommand);
    Set<RecipeCommand> recipes = categoryCommand.getRecipes();
    assertEquals(1, recipes.size());

    //Assetions sur les ingrédients
    IngredientCommand ingredientCommand = ingredients.get(0);
    assertNotNull(ingredientCommand);
    assertSame(5L, ingredientCommand.getId());

    RecipeCommand ingredientCommandRecipe = ingredientCommand.getRecipe();

    assertSame(ingredientCommandRecipe, recipeCommand);

    UnitOfMeasureCommand unitOfMeasureCommand = ingredientCommand.getUnitOfMeasure();

    assertSame(6L, unitOfMeasureCommand.getId());

    Recipe recipeBis = (Recipe) this.beanToBeanConverter.convert(recipeCommand);

    assertEquals(recipe, recipeBis);

  }
}
