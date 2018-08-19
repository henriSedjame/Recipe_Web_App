package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Exceptions.NotFoundException;
import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

  MockMvc mvc;
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    controller = new RecipeController(recipeService);
    mvc = MockMvcBuilders.standaloneSetup(controller)
      .setControllerAdvice(new ControllerExceptionHandler())
      .build();
  }

  @Test
  public void showRecipeMockMvc() throws Exception {
    Recipe recipe = Recipe.builder().id(1L).build();

    when(recipeService.findRecipeById(1L)).thenReturn(recipe);

    mvc.perform(get("/recipe/detail/" + 1L))
      .andExpect(status().isOk())
      .andExpect(view().name(ViewNames.RECIPE_DETAIL_VIEW))
      .andExpect(model().attributeExists("recipe"));

  }

  @Test
  public void newRecipeMockMvc() throws Exception {
    mvc.perform(get("/recipe/new"))
      .andExpect(status().isOk())
      .andExpect(view().name(ViewNames.RECIPE_NEW))
      .andExpect(model().attributeExists("recipe"));
  }

  @Test
  public void saveOrUpdateMockMvc() throws Exception {
    RecipeCommand command = RecipeCommand.builder().id(2L).build();

    when(recipeService.saveRecipeCommand(any())).thenReturn(command);

    mvc.perform(post("/recipe")
      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
      .param("id", "")
      .param("description", "some string")
      .param("directions", "directions")
    )
      .andExpect(status().is3xxRedirection())
      .andExpect(view().name(ViewNames.REDIRECT + ViewNames.RECIPE_DETAIL_VIEW + "/" + 2L));
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

  @Test
  public void newRecipe() {
    ArgumentCaptor<RecipeCommand> argumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);

    String viewName = controller.newRecipe(model);

    assertEquals(ViewNames.RECIPE_NEW, viewName);

    verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptor.capture());

    RecipeCommand capturedCommand = argumentCaptor.getValue();

    assertNotNull(capturedCommand);

  }

  @Test
  public void saveOrUpdate() {

  }

  @Test
  public void testGetRecipeNotFound() throws Exception {

    when(recipeService.findRecipeById(anyLong())).thenThrow(NotFoundException.class);

    mvc.perform(get("/recipe/detail/" + 1L))
      .andExpect(status().isNotFound())
      .andExpect(view().name(ViewNames.ERROR_PAGE_NOT_FOUND));
  }

  @Test
  public void testGetNumberFormatException() throws Exception {

    mvc.perform(get("/recipe/detail/kjhh"))
      .andExpect(status().isBadRequest())
      .andExpect(view().name(ViewNames.ERROR_PAGE_BAD_REQUEST));
  }
}
