package com.formation.udemy.spring.recipe_app.Service.Impl;

import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Service.ImageService;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import com.formation.udemy.spring.recipe_app.Utils.FileUtils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 29/07/2018
 * @Class purposes : .......
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

  private FileUtils fileUtils;
  private RecipeService recipeService;

  public ImageServiceImpl(FileUtils fileUtils, RecipeService recipeService) {
    this.fileUtils = fileUtils;
    this.recipeService = recipeService;
  }

  @Override
  public void saveImageFile(Long id, MultipartFile file) {
    try {
      Byte[] bytes = fileUtils.getBytes(file);
      Recipe recipe = recipeService.findRecipeById(id);
      recipe.setImage(bytes);
      recipeService.saveRecipe(recipe);
    } catch (IOException e) {
      log.error(e.getMessage(), e.getCause());
    }
  }
}
