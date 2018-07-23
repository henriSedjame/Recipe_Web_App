package com.formation.udemy.spring.recipe_app.Utils.PojoQueryingPredicates;

import com.formation.udemy.spring.recipe_app.Model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PojoQueryingPredicatorTest {

  private PojoQueryingPredicator predicator1;
  private PojoQueryingPredicator predicator2;
  private PojoQueryingPredicator predicator3;

  @Before
  public void setUp() throws Exception {
    predicator1 = new PojoQueryingPredicator(Recipe.class);
    predicator2 = new PojoQueryingPredicator(Ingredient.class);
    predicator3 = new PojoQueryingPredicator(Notes.class);

  }

  @Test
  public void getQClasse() {
    Object qClasse = predicator1.getQClasse();
    Object qClasse1 = predicator2.getQClasse();
    Object qClasse2 = predicator3.getQClasse();

    assertTrue(qClasse instanceof QRecipe);
    assertTrue(qClasse1 instanceof QIngredient);
    assertTrue(qClasse2 instanceof QNotes);

  }
}