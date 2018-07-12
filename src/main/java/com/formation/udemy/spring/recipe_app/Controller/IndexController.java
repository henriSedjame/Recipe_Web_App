package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

  private RecipeService recipeService;

  public IndexController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @RequestMapping({"", "/", "index"})
  public String getIndexPage(Model model) {
    model.addAttribute("recipes", this.recipeService.getRecipes());
    return ViewNames.INDEX_VIEW;
  }
}

