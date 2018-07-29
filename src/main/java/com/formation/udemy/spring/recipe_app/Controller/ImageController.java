package com.formation.udemy.spring.recipe_app.Controller;

import com.formation.udemy.spring.recipe_app.Controller.ControllerUtils.ViewNames;
import com.formation.udemy.spring.recipe_app.Model.Recipe;
import com.formation.udemy.spring.recipe_app.Service.ImageService;
import com.formation.udemy.spring.recipe_app.Service.RecipeService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Project recipe_app
 * @Author Henri Joel SEDJAME
 * @Date 29/07/2018
 * @Class purposes : .......
 */
@Controller
public class ImageController {

  private final ImageService imageService;
  private final RecipeService recipeService;

  public ImageController(ImageService imageService, RecipeService recipeService) {
    this.imageService = imageService;
    this.recipeService = recipeService;
  }

  @GetMapping("recipe/image/{id}")
  public String showUploadForm(@PathVariable("id") String id, Model model) {
    model.addAttribute("recipe", recipeService.findRecipeById(Long.valueOf(id)));
    return ViewNames.UPLOAD_IMAGE;
  }

  @PostMapping("recipe/image/{id}")
  public String handleImagePost(@PathVariable("id") String id, @RequestParam("imagefile") MultipartFile file) {
    imageService.saveImageFile(Long.valueOf(id), file);
    return ViewNames.REDIRECT + ViewNames.RECIPE_DETAIL_VIEW + "/" + id;
  }

  @GetMapping("recipe/recipeimage/{id}")
  public void renderImageFromDB(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
    Recipe recipe = recipeService.findRecipeById(Long.valueOf(id));

    Byte[] image = recipe.getImage();

    if (image != null) {
      byte[] bytes = new byte[image.length];

      int i = 0;

      for (byte b : image) {
        bytes[i++] = b;
      }

      response.setContentType("image/jpeg");
      InputStream is = new ByteArrayInputStream(bytes);
      IOUtils.copy(is, response.getOutputStream());
    }

  }
}
