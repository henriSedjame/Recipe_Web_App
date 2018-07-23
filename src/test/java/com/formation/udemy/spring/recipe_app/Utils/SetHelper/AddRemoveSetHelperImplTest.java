package com.formation.udemy.spring.recipe_app.Utils.SetHelper;

import com.formation.udemy.spring.recipe_app.Model.Category;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class AddRemoveSetHelperImplTest {

  private SetHelper setHelper;
  private Recipe recipe;
  private Category category1;
  private Category category2;

  @Before
  public void setUp() throws Exception {
    setHelper = new AddRemoveSetHelperImpl();
    recipe = Recipe.builder()
            .id(1L)
            .categories(new HashSet<>())
            .build();

    category1 = Category.builder()
            .id(2L)
            .build();

    category2 = Category.builder()
            .id(3L)
            .build();
  }

  @Test
  public void addToSet() {
    this.setHelper.addToSet(recipe, "categories", Arrays.asList(category1, category2));
    assertSame(2, recipe.getCategories().size());
  }

  @Test
  public void removeFromSet() {
    assertTrue(recipe.getCategories().isEmpty());
    this.setHelper.addToSet(recipe, "categories", Arrays.asList(category1, category2));
    this.setHelper.removeFromSet(recipe, "categories", category1);
    assertSame(1, recipe.getCategories().size());
  }
}