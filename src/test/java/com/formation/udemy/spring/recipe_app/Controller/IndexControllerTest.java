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

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 * @Class purposes : Classe de test de la classe indexController
 */
public class IndexControllerTest {

  IndexController controller;
  @Mock
  RecipeService recipeService;
  @Mock
  Model model;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    controller = new IndexController(recipeService);
  }

  @Test
  public void getIndexPage() {

    Set<Recipe> recipes = new HashSet<>();
    Recipe recipe1 = Recipe.builder().id("1").build();
    Recipe recipe2 = Recipe.builder().id("2").build();
    recipes.add(recipe1);
    recipes.add(recipe2);

    // La methode when().thenReturn permet d'assigner une valeur de retour à une méthode passée en paramètre de when()
    when(recipeService.getRecipes()).thenReturn(recipes);

    //argumentCaptor permet de capturer une valeur
    ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

    String viewName = controller.getIndexPage(model);
    assertEquals(ViewNames.INDEX_VIEW, viewName);
    verify(recipeService, times(1)).getRecipes();
    // argumentCaptor, grace à sa méthode capture() récupère ici la valeur de l'attribut "recipes" passée au model
    verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());

    //On peut ensuite récupérer la valeur de l'argumentCaptor et vérifier son contenu
    Set<Recipe> setInController = argumentCaptor.getValue();
    assertEquals(2, setInController.size());
  }

  @Test
  public void testMockMVC() throws Exception {
    MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

    mvc.perform(get("/"))
      .andExpect(status().isOk())
      .andExpect(view().name(ViewNames.INDEX_VIEW));
  }
}
