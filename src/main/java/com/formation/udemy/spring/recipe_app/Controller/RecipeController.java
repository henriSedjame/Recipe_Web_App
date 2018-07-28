package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @RequestMapping("recipe/new")
  public String newRecipe(Model model) {
    model.addAttribute("recipe", new RecipeCommand());
    return ViewNames.RECIPE_NEW;
  }

  @PostMapping("recipe")
  public String saveOrUpdate(@ModelAttribute(name = "recipe") RecipeCommand command) {
    RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);

    return ViewNames.REDIRECT + ViewNames.RECIPE_DETAIL_VIEW + "/" + savedCommand.getId();
  }

}
