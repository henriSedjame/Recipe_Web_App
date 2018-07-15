package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 15/07/2018
 * @Class purposes : .......
 */
public class RecipeControllerTest {

  RecipeController controller;
  @Mock
  RecipeService recipeService;
  @Mock
  Model model;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    controller = new RecipeController(recipeService);
  }

  @Test
  public void showRecipeMockMvc() throws Exception {
    MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

    mvc.perform(get("/recipe/detail/" + 1L))
      .andExpect(status().isOk())
      .andExpect(view().name(ViewNames.RECIPE_DETAIL_VIEW));
  }

  @Test
  public void showRecipeDetail() {
    Recipe recipe = Recipe.builder().id(1L).build();

    when(recipeService.findRecipeById(1L)).thenReturn(recipe);

    ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

    String viewName = controller.showRecipe(model, 1L);

    assertEquals(ViewNames.RECIPE_DETAIL_VIEW, viewName);

    verify(recipeService, times(1)).findRecipeById(eq(1L));

    verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());

    Recipe recipeCaptured = argumentCaptor.getValue();

    assertNotNull(recipeCaptured);

    assertTrue(recipeCaptured.getId().equals(1L));
  }
}
