package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 15/07/2018
 * @Class purposes : .......
 */

@Controller
@Transactional
public class RecipeController {

  RecipeService recipeService;

  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @RequestMapping("/recipe/detail/{id}")
  public String showRecipe(Model model, @PathVariable("id") Long id) {

    model.addAttribute("recipe", recipeService.findRecipeById(id));

    return ViewNames.RECIPE_DETAIL_VIEW;
  }
}
