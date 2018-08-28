package com.formation.udemy.spring.recipe_app.Utils.BidirectionnalSetterHelper;

import com.formation.udemy.spring.recipe_app.Model.Notes;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertSame;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BidirectionnalSetterHelperImplTest {
  Recipe recipe;
  Notes notes;
  @Autowired
  private BidirectionnalSetterHelper bidirectionnalSetterHelper;

  @Before
  public void setUp() throws Exception {
    recipe = Recipe.builder()
            .id("1")
            .build();

    notes = Notes.builder()
            .id("2")
            .build();
  }

  @Test
  public void bidirectionnalSet() {
    this.bidirectionnalSetterHelper.bidirectionnalSet(recipe, notes);
    assertSame(notes, recipe.getNotes());
    assertSame(recipe, notes.getRecipe());
  }

  @Test(expected = Exception.class)
  public void nullParametersTest() {
    this.bidirectionnalSetterHelper.bidirectionnalSet(null, notes);
  }

  @Test(expected = Exception.class)
  public void nullParametersTest2() {
    this.bidirectionnalSetterHelper.bidirectionnalSet(recipe, null);
  }
}