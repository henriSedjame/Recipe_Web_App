package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Model.Commands.RecipeCommand;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 15/07/2018
 * @Class purposes : .......
 */

@Controller
@Transactional
@Slf4j
public class RecipeController {

  RecipeService recipeService;

  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping("/recipe/detail/{id}")
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
  public String saveOrUpdate(@Valid @ModelAttribute(name = "recipe") RecipeCommand command, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      bindingResult.getAllErrors().forEach(objectError -> log.debug(objectError.toString()));
      return ViewNames.RECIPE_NEW;
    }
    RecipeCommand savedCommand = recipeService.saveRecipeCommand(command);
    return ViewNames.REDIRECT + ViewNames.RECIPE_DETAIL_VIEW + "/" + savedCommand.getId();
  }

}
