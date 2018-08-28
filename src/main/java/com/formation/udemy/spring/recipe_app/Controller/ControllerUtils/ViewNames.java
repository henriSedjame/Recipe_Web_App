package com.formation.udemy.spring.recipe_app.Controller.ControllerUtils;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 14/07/2018
 */
public class ViewNames {

  public static final String ERROR_PAGE_NOT_FOUND = "404";
  public static final String UPLOAD_IMAGE = "recipe/imageForm";
  public static final String REDIRECT = "redirect:/";
  public static final String INDEX_VIEW = "index";
  public static final String RECIPE_DETAIL_VIEW = "recipe/detail";
  public static final String RECIPE_NEW = "recipe/recipeForm";
  public static final String ERROR_PAGE_BAD_REQUEST = "400";

  private ViewNames() {
    throw new IllegalStateException("ViewNames.class");
  }

}
