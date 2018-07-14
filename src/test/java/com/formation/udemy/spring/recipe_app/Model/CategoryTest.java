package com.formation.udemy.spring.recipe_app.Model;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
public class CategoryTest {

  Category category;

  @Before
  public void setUp() throws Exception {
    category = Category.builder()
      .id(4L)
      .description("mexican")
      .recipes(new HashSet<>())
      .build();
  }

  @Test
  public void getId() {
    Long expectedId = 4L;
    assertEquals(expectedId, category.getId());
  }

  @Test
  public void getDescription() {
    String expectedDescription = "mexican";
    assertEquals(expectedDescription, category.getDescription());
  }

  @Test
  public void getRecipes() {
    assertNotNull(category.getRecipes());

    category.getRecipes().add(new Recipe());

    assertEquals(1, category.getRecipes().size());
  }
}
