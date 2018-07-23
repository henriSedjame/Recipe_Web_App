package com.formation.udemy.spring.recipe_app.Utils.QPojoUtils;

import com.formation.udemy.spring.recipe_app.Model.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PojoUtilsTest {

  @Test
  public void getQPojo() {
    Object qPojo = PojoUtils.getQPojo(Recipe.class);
    Object qPojo1 = PojoUtils.getQPojo(Ingredient.class);
    Object qPojo2 = PojoUtils.getQPojo(Notes.class);

    assertTrue(qPojo instanceof QRecipe);
    assertTrue(qPojo1 instanceof QIngredient);
    assertTrue(qPojo2 instanceof QNotes);
  }
}