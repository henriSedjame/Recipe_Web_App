package com.formation.udemy.spring.recipe_app.Utils.Converters;

import com.formation.udemy.spring.recipe_app.Model.Category;
import com.formation.udemy.spring.recipe_app.Model.Commands.CategoryCommand;
import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Model.Enumerations.Difficulty;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanToBeanCommandConverterImplTest {

    //Constantes
    public static final Long ID = 1L;
    private static final Integer PREP_TIME = 5;
    private static final Integer COOK_TIME = 10;
    private static final Integer SERVINGS = 4;
    public static final String DESCRIPTION = "description";
    public static final String SOURCE = "source";
    private static final String URL = "url";
    private static final String DIRECTIONS ="directions";
    public static final Difficulty DIFFICULTY = Difficulty.EASY;

    //
    Recipe recipe;
    @Autowired
    private BeanToBeanCommandConverter<Recipe, RecipeCommand> beanToBeanCommandConverter;

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
       category1.setDescription(DESCRIPTION);
       //category1.getRecipes().add(recipe);
       recipe.getCategories().add(category1);
    }

    @Test
    public void convert() {
        RecipeCommand recipeCommand = beanToBeanCommandConverter.convert(recipe, null);
        //Assertions
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
        assertTrue(categories.size() == 1);
        Optional<CategoryCommand> catOpt = categories.stream().findFirst();
        assertTrue(catOpt.isPresent());
        assertTrue(catOpt.get().getDescription().equals(DESCRIPTION));


    }
}